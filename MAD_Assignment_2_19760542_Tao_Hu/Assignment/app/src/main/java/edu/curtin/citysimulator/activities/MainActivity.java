package edu.curtin.citysimulator.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.curtin.citysimulator.R;
import edu.curtin.citysimulator.model.GameData;
import edu.curtin.citysimulator.model.MapElement;

/**
 * The first screen of the app. Provide button to access setting screen and the map screen.
 *
 * @author Tao Hu
 * date:    31/10/2020
 */
public class MainActivity extends AppCompatActivity
{

    private TextView authorName;
    private Button btnSetting;
    private Button btnMap;

    private GameData gameData;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameData = GameData.getInstance();


        findView();
        initView();
    }


    @Override
    protected void onResume()
    {
        enableAllButtons();

        super.onResume();
    }

    @Override
    protected void onDestroy()
    {
        /*
        if (gameData.getMap() != null)
        {
            GameData.getInstance().saveToDatabase(this);
        }*/

        super.onDestroy();
    }

    @Override
    protected void onPause()
    {
        if (gameData.getMap() != null)
        {
            GameData.getInstance().saveToDatabase(this);
        }

        super.onPause();
    }

    private void findView()
    {
        authorName = (TextView) findViewById(R.id.authorName);
        btnSetting = (Button) findViewById(R.id.btnSetting);
        btnMap = (Button) findViewById(R.id.btnMap);
    }

    private void initView()
    {
        Resources res = getResources();
        authorName.setText(res.getString(R.string.stringColonString, res.getString(R.string.author), res.getString(R.string.authorName)));

        /*
            disable all buttons. If press setting and map quick enough, then two activity will be
            opened. If setting is pressed first then map, after exiting map activity user can change
            the width and height of the map, which will mess up calculations crash the app.
         */
        btnSetting.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        if (!gameData.isStarted())
                        {
                            if (!gameData.isDatabaseEmpty(MainActivity.this))
                            {
                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i)
                                    {
                                        if (i == DialogInterface.BUTTON_POSITIVE)
                                        {
                                            gameData.loadFromDatabase(MainActivity.this);
                                        }

                                        gameData.setStarted(true);
                                        startActivity(new Intent(MainActivity.this, SettingActivity.class));
                                    }
                                };

                                AlertDialog.Builder builderLoadGame = new AlertDialog.Builder(MainActivity.this);
                                builderLoadGame.setMessage("Continue previous saved game?").setPositiveButton("Yes", dialogClickListener)
                                        .setNegativeButton("No", dialogClickListener).show();
                            }
                            else
                            {
                                disableAllButtons();
                                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                            }
                        }
                        else
                        {
                            disableAllButtons();
                            startActivity(new Intent(MainActivity.this, SettingActivity.class));
                        }
                    }
                }
        );

        btnMap.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!gameData.isStarted())
                        {
                            if (!gameData.isDatabaseEmpty(MainActivity.this))
                            {
                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i)
                                    {
                                        if (i == DialogInterface.BUTTON_POSITIVE)
                                        {
                                            gameData.loadFromDatabase(MainActivity.this);
                                        }
                                        else if (i == DialogInterface.BUTTON_NEGATIVE)
                                        {
                                            gameData.generateNewMap();
                                        }

                                        gameData.setStarted(true);
                                        startActivity(new Intent(MainActivity.this, MapActivity.class));
                                    }
                                };

                                AlertDialog.Builder builderLoadGame = new AlertDialog.Builder(MainActivity.this);
                                builderLoadGame.setMessage("Continue previous saved game?").setPositiveButton("Yes", dialogClickListener)
                                        .setNegativeButton("No", dialogClickListener).show();
                            }
                            else
                            {
                                if (gameData.getMap() == null)
                                {
                                    gameData.generateNewMap();
                                }

                                disableAllButtons();
                                startActivity(new Intent(MainActivity.this, MapActivity.class));
                            }
                        }
                        else
                        {
                            if (gameData.getMap() == null)
                            {
                                gameData.generateNewMap();
                            }

                            disableAllButtons();
                            startActivity(new Intent(MainActivity.this, MapActivity.class));
                        }
                    }
                }
        );
    }


    private void disableAllButtons()
    {
        btnMap.setEnabled(false);
        btnSetting.setEnabled(false);
    }

    private void enableAllButtons()
    {
        btnMap.setEnabled(true);
        btnSetting.setEnabled(true);
    }
}