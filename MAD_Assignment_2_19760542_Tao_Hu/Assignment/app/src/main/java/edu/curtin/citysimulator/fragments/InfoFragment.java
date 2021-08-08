package edu.curtin.citysimulator.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.curtin.citysimulator.R;
import edu.curtin.citysimulator.model.GameData;
import edu.curtin.citysimulator.model.Settings;

/**
 * Displays the relevant statistics of the game, and provide the update time button which forwards
 * the game time by 1.
 *
 * @author Tao Hu
 * date:    31/10/2020
 */
public class InfoFragment extends Fragment
{
    private static final String FIRST_TIME_GAME_OVER = "edu.curtin.first_time_game_over";
    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String API_KEY = "3ffe7417ae5b443cef412efabd0e29a5";

    private static final float ABSOLUTE_ZERO = 273.15f;

    private Settings settings;
    private GameData gameData;
    private List<NameValuePair> nameValuePairs;
    private Button btnUpdateTime;

    private InfoAdapter infoAdapter;

    private boolean firstTimeGameOver = false;

    private long previousWeatherTempRequestTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        if (savedInstanceState != null)
        {
            firstTimeGameOver = savedInstanceState.getBoolean(FIRST_TIME_GAME_OVER);
        }

        RecyclerView rvInfo = view.findViewById(R.id.rvInfoItem);
        rvInfo.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        gameData = GameData.getInstance();
        settings = gameData.getSetting();

        /*
            want to display all the information with recycler view. Then each information is
            updated through this list, where each info has a unique index.
            The recycler view then uses this list to display through view holder binding.
         */
        nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new NameValuePair(getString(R.string.gameTime), 0));
        nameValuePairs.add(new NameValuePair(getString(R.string.money), gameData.getMoney()));
        nameValuePairs.add(new NameValuePair(getString(R.string.recentIncome), gameData.getRecentIncome()));
        nameValuePairs.add(new NameValuePair(getString(R.string.population), gameData.getPopulation()));
        nameValuePairs.add(new NameValuePair(getString(R.string.employmentRate), gameData.getEmploymentRate()));
        nameValuePairs.add(new NameValuePair(getString(R.string.temperature), 0));

        infoAdapter = new InfoAdapter(nameValuePairs);
        rvInfo.setAdapter(infoAdapter);

        findView(view);
        initView();

        updateInfo();

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        outState.putBoolean(FIRST_TIME_GAME_OVER, firstTimeGameOver);

        super.onSaveInstanceState(outState);
    }

    private void findView(View view)
    {
        btnUpdateTime = (Button) view.findViewById(R.id.btnUpdateTime);
    }

    private void initView()
    {
        btnUpdateTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                gameData.setGameTime(gameData.getGameTime() + 1);
                updateInfo();
            }
        });
    }

    /**
     * update population and employment rate.
     */
    private void updateGameStats()
    {
        gameData.setPopulation(gameData.getnResidential() * settings.getFamilySize());

        if (gameData.getPopulation() != 0)
        {
            gameData.setEmploymentRate((int) (100 * Math.min(1.0, (double) gameData.getnCommercial() * settings.getShopSize() / gameData.getPopulation())));
        }
        else
        {
            gameData.setEmploymentRate(0);
        }
    }

    /**
     * update money
     */
    private void updateMoney()
    {
        int newMoney = (int) (gameData.getMoney() + (gameData.getPopulation() * ((gameData.getEmploymentRate() / 100.0d ) * settings.getSalary() * settings.getTaxRate() - settings.getServiceCost())));

        if (newMoney > gameData.getMoney())
        {
            gameData.setRecentIncome(newMoney - gameData.getMoney());
        }

        gameData.setMoney(newMoney);
    }

    /**
     * updates the ui but does not do money calculation. This is called by map fragment through
     * observers after placing a structure. The game logic is that, calculation of earned money
     * only happens after user press the update game button or only when the game time is actually
     * forwarded.
     */
    public void updateInfoAfterStructureChange()
    {
        updateGameStats();

        nameValuePairs.get(1).setValue(gameData.getMoney());
        nameValuePairs.get(3).setValue(gameData.getPopulation());
        nameValuePairs.get(4).setValue(gameData.getEmploymentRate());
        nameValuePairs.get(5).setValue(gameData.getTemperature());

        infoAdapter.notifyDataSetChanged();
    }

    /**
     * updates UI. also make call to retrieving the weather temperature of the city.
     */
    private void updateInfo()
    {
        updateGameStats();
        updateMoney();

        nameValuePairs.get(0).setValue(gameData.getGameTime());
        nameValuePairs.get(1).setValue(gameData.getMoney());
        nameValuePairs.get(2).setValue(gameData.getRecentIncome());
        nameValuePairs.get(3).setValue(gameData.getPopulation());
        nameValuePairs.get(4).setValue(gameData.getEmploymentRate());
        nameValuePairs.get(5).setValue(gameData.getTemperature());

        /*
            max one call a second only.
         */
        if (System.currentTimeMillis() - previousWeatherTempRequestTime > 1000)
        {
            previousWeatherTempRequestTime = System.currentTimeMillis();
            updateCityTemperature();
        }

        /*
            game over message displayed only once.
         */
        if (!firstTimeGameOver && gameData.getMoney() < 0)
        {
            firstTimeGameOver = true;
            Toast.makeText(getActivity(), R.string.gameOver, Toast.LENGTH_SHORT).show();
        }

        infoAdapter.notifyDataSetChanged();
    }

    private void updateCityTemperature()
    {
        String urlString =
            Uri.parse(WEATHER_URL)
                .buildUpon()
                .appendQueryParameter("q", settings.getCityName())
                .appendQueryParameter("appid", API_KEY)
                .build().toString();

        try
        {
            new WeatherTempTask(gameData, getActivity()).execute(new URL(urlString));
        }
        catch (MalformedURLException murle)
        {
            System.out.println("URL creation failed: " + murle.getMessage());
        }
    }

    /**
     * retrieving the temperature of the city.
     */
    private static class WeatherTempTask extends AsyncTask<URL, Void, Integer>
    {
        private GameData gameData;
        private Context context;

        public WeatherTempTask(GameData gameData, Context context)
        {
            this.gameData = gameData;
            this.context = context;
        }

        @Override
        protected Integer doInBackground(URL... urls)
        {
            int temperature = -1;

            for (URL url : urls)
            {
                HttpURLConnection conn = null;

                try
                {
                    conn = (HttpURLConnection) url.openConnection();

                    if (conn.getResponseCode() != HttpURLConnection.HTTP_OK)
                    {
                        System.out.println("Connection not http ok, maybe city name error");
                    }
                    else
                    {
                        String data = IOUtils.toString(conn.getInputStream(), "UTF-8");

                        JSONObject jBase = new JSONObject(data);

                        temperature = Math.round((float) jBase.getJSONObject("main").getDouble("temp") - ABSOLUTE_ZERO);
                    }

                }
                catch (IOException ioe)
                {
                    System.out.println("Error while reading data in: " + ioe.getMessage());
                }
                catch (JSONException jsone)
                {
                    System.out.println("Error parsing to JSON format: " + jsone.getMessage());
                }
                finally
                {
                    if (conn != null)
                    {
                        conn.disconnect();
                    }
                }
            }

            return temperature;
        }

        @Override
        protected void onPostExecute(Integer integer)
        {
            gameData.setTemperature(integer);
            super.onPostExecute(integer);
        }
    }

    private static class NameValuePair
    {
        private String name;
        private double value;

        public NameValuePair(String name, double value)
        {
            this.name = name;
            this.value = value;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public double getValue()
        {
            return value;
        }

        public void setValue(double value)
        {
            this.value = value;
        }
    }

    private static class InfoViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tvName;
        private TextView tvValue;

        public InfoViewHolder(LayoutInflater layoutInflater, ViewGroup parent)
        {
            super(layoutInflater.inflate(R.layout.info_cell, parent, false));

            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvValue = (TextView) itemView.findViewById(R.id.tvValue);
        }

        public void bind(NameValuePair nameValuePair)
        {
            tvName.setText(nameValuePair.getName());
            tvValue.setText(Double.toString(nameValuePair.getValue()));
        }
    }

    private class InfoAdapter extends RecyclerView.Adapter<InfoViewHolder>
    {
        private List<NameValuePair> nameValuePairs;

        public InfoAdapter(List<NameValuePair> nameValuePairs)
        {
            this.nameValuePairs = nameValuePairs;
        }

        @NonNull
        @Override
        public InfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            return new InfoViewHolder(LayoutInflater.from(getActivity()), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull InfoViewHolder holder, int position)
        {
            holder.bind(nameValuePairs.get(position));
        }

        @Override
        public int getItemCount()
        {
            return nameValuePairs.size();
        }
    }
}
