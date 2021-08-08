package edu.curtin.citysimulator.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import edu.curtin.citysimulator.R;
import edu.curtin.citysimulator.model.GameData;
import edu.curtin.citysimulator.model.Settings;

/**
 * Allow user to change settings of the game.
 *
 * @author Tao Hu
 * date:    31/10/2020
 */
public class SettingFragment extends Fragment
{
    private static final String SAVED_INPUTS = "edu.curtin.savedinputs";

    private Settings setting;
    private List<SettingValueChanger> settingValueChangers = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.single_recycler_view, container, false);

        setting = GameData.getInstance().getSetting();

        initSettingValueChangers();

        /*
            there are many edit text in this UI. After text has being entered into the edit text,
            on rotation everything gets recreated and the data in the edit text is lost. This
            checks that the data entered into the edit text are restored.
         */
        if (savedInstanceState != null)
        {
            String[] inputs = savedInstanceState.getStringArray(SAVED_INPUTS);

            if (inputs != null)
            {
                for (int x = 0; x < inputs.length; x++)
                {
                    settingValueChangers.get(x).setCurrentInput(inputs[x]);
                }
            }
        }

        RecyclerView rvSetting = (RecyclerView) view.findViewById(R.id.rvRecyclerView);
        LinearLayoutManager llmSetting = new LinearLayoutManager(getActivity());
        rvSetting.setLayoutManager(llmSetting);

        rvSetting.setAdapter(new SettingAdapter(settingValueChangers));

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);
        String[] inputs = new String[settingValueChangers.size()];

        for (int x = 0; x < inputs.length; x++)
        {
            inputs[x] = settingValueChangers.get(x).getCurrentInput();
        }

        outState.putStringArray(SAVED_INPUTS, inputs);
    }

    private abstract static class SettingValueChanger
    {
        private String valueName;
        private String currentInput;
        private SettingValueGetter svg;

        public SettingValueChanger(String valueName, SettingValueGetter svg)
        {
            this.valueName = valueName;
            this.svg = svg;
            this.currentInput = "";
        }

        public String getCurrentInput()
        {
            return currentInput;
        }

        public void setCurrentInput(String currentInput)
        {
            this.currentInput = currentInput;
        }

        public String getValueName()
        {
            return valueName;
        }

        public String getValue()
        {
            return svg.getValue();
        }

        public abstract int getInputType();

        public abstract String changeValue(String newValue);
    }

    private interface SettingValueGetter
    {
        public String getValue();
    }

    private interface SettingIntValueSetter
    {
        public void setValue(int value);
    }

    private interface SettingDoubleValueSetter
    {
        public void setValue(double value);
    }

    private interface SettingStringValueSetter
    {
        public void setValue(String value);
    }

    private class SettingStringValueChanger extends  SettingValueChanger
    {
        private SettingStringValueSetter ssvs;

        public SettingStringValueChanger(String valueName,SettingValueGetter svg, SettingStringValueSetter ssvs)
        {
            super(valueName, svg);
            this.ssvs = ssvs;
        }

        @Override
        public int getInputType()
        {
            return InputType.TYPE_CLASS_TEXT;
        }

        @Override
        public String changeValue(String newValue)
        {
            ssvs.setValue(newValue);
            return null;
        }
    }

    private class SettingIntValueChanger extends SettingValueChanger
    {
        private int minValue;
        private int maxValue;
        private SettingIntValueSetter sivs;

        public SettingIntValueChanger(String valueName, SettingValueGetter svg, SettingIntValueSetter sivs, int minValue, int maxValue)
        {
            super(valueName, svg);
            this.sivs = sivs;
            this.minValue = minValue;
            this.maxValue = maxValue;
        }

        @Override
        public int getInputType() {
            return InputType.TYPE_CLASS_NUMBER;
        }

        @Override
        public String changeValue(String newValue)
        {
            String ret = null;

            try {
                int value = Integer.parseInt(newValue);

                if (value >= minValue && value <= maxValue)
                {
                    sivs.setValue(value);
                }
                else
                {
                    ret = getResources().getString(R.string.errorIntInputInRange, minValue, maxValue);
                }
            }
            catch(NumberFormatException nfe)
            {
                ret = getResources().getString(R.string.errorIntInputInRange, minValue, maxValue);
            }

            return ret;
        }
    }

    private class SettingDoubleValueChanger extends SettingValueChanger
    {
        private double minValue;
        private double maxValue;
        private SettingDoubleValueSetter sdvs;

        public SettingDoubleValueChanger(String valueName, SettingValueGetter svg, SettingDoubleValueSetter sdvs, double minValue, double maxValue)
        {
            super(valueName, svg);
            this.sdvs = sdvs;
            this.minValue = minValue;
            this.maxValue = maxValue;
        }

        @Override
        public int getInputType() {
            return InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
        }

        @Override
        public String changeValue(String newValue)
        {
            String ret = null;

            try {
                double value = Double.parseDouble(newValue);

                if (value >= minValue && value <= maxValue) {
                    sdvs.setValue(value);
                }
                else
                {
                    ret = getResources().getString(R.string.errorDoubleInputInRange, minValue, maxValue);
                }
            }
            catch (NumberFormatException nfe)
            {
                ret = getResources().getString(R.string.errorDoubleInputInRange, minValue, maxValue);
            }

            return ret;
        }
    }

    private void initSettingValueChangers()
    {
        /*
            once the game map has being created, the width and length should not be changed.
            Change width and length should be done through starting new game. The initial money is now
            not useful too
         */
        if (GameData.getInstance().getMap() == null)
        {
            settingValueChangers.add(new SettingIntValueChanger(
                    getString(R.string.mapWidth),
                    new SettingValueGetter()
                    {
                        @Override
                        public String getValue()
                        {
                            return Integer.toString(setting.getMapWidth());
                        }
                    },
                    new SettingIntValueSetter()
                    {
                        @Override
                        public void setValue(int value)
                        {
                            setting.setMapWidth(value);
                        }
                    },
                    1,
                    50

            ));

            settingValueChangers.add(new SettingIntValueChanger(
                    getString(R.string.mapHeight),
                    new SettingValueGetter()
                    {
                        @Override
                        public String getValue()
                        {
                            return Integer.toString(setting.getMapHeight());
                        }
                    },
                    new SettingIntValueSetter()
                    {
                        @Override
                        public void setValue(int value)
                        {
                            setting.setMapHeight(value);
                        }
                    },
                    1,
                    25

            ));

            settingValueChangers.add(new SettingIntValueChanger(
                    getString(R.string.initialMoney),
                    new SettingValueGetter()
                    {
                        @Override
                        public String getValue()
                        {
                            return Integer.toString(setting.getInitialMoney());
                        }
                    },
                    new SettingIntValueSetter()
                    {
                        @Override
                        public void setValue(int value)
                        {
                            setting.setInitialMoney(value);
                            GameData.getInstance().setMoney(value);
                        }
                    },
                    200,
                    1000000

            ));
        }

        settingValueChangers.add(new SettingIntValueChanger(
                getString(R.string.familySize),
                new SettingValueGetter() {
                    @Override
                    public String getValue() {
                        return Integer.toString(setting.getFamilySize());
                    }
                },
                new SettingIntValueSetter() {
                    @Override
                    public void setValue(int value) {
                        setting.setFamilySize(value);
                    }
                },
                1,
                10

        ));

        settingValueChangers.add(new SettingIntValueChanger(
                getString(R.string.shopSize),
                new SettingValueGetter() {
                    @Override
                    public String getValue() {
                        return Integer.toString(setting.getShopSize());
                    }
                },
                new SettingIntValueSetter() {
                    @Override
                    public void setValue(int value) {
                        setting.setShopSize(value);
                    }
                },
                4,
                10

        ));

        settingValueChangers.add(new SettingIntValueChanger(
                getString(R.string.salary),
                new SettingValueGetter() {
                    @Override
                    public String getValue() {
                        return Integer.toString(setting.getSalary());
                    }
                },
                new SettingIntValueSetter() {
                    @Override
                    public void setValue(int value) {
                        setting.setSalary(value);
                    }
                },
                0,
                100

        ));

        settingValueChangers.add(new SettingDoubleValueChanger(
                getString(R.string.taxRate),
                new SettingValueGetter() {
                    @Override
                    public String getValue() {
                        return Double.toString(setting.getTaxRate());
                    }
                },
                new SettingDoubleValueSetter() {
                    @Override
                    public void setValue(double value) {
                        setting.setTaxRate(value);
                    }
                },
                0.0d,
                1.0d

        ));

        settingValueChangers.add(new SettingIntValueChanger(
                getString(R.string.serviceCost),
                new SettingValueGetter() {
                    @Override
                    public String getValue() {
                        return Integer.toString(setting.getServiceCost());
                    }
                },
                new SettingIntValueSetter() {
                    @Override
                    public void setValue(int value) {
                        setting.setServiceCost(value);
                    }
                },
                0,
                Integer.MAX_VALUE

        ));

        settingValueChangers.add(new SettingIntValueChanger(
                getString(R.string.houseBuildingCost),
                new SettingValueGetter() {
                    @Override
                    public String getValue() {
                        return Integer.toString(setting.getHouseBuildingCost());
                    }
                },
                new SettingIntValueSetter() {
                    @Override
                    public void setValue(int value) {
                        setting.setHouseBuildingCost(value);
                    }
                },
                0,
                Integer.MAX_VALUE

        ));

        settingValueChangers.add(new SettingIntValueChanger(
                getString(R.string.commonBuildingCost),
                new SettingValueGetter() {
                    @Override
                    public String getValue() {
                        return Integer.toString(setting.getCommonBuildingCost());
                    }
                },
                new SettingIntValueSetter() {
                    @Override
                    public void setValue(int value) {
                        setting.setCommonBuildingCost(value);
                    }
                },
                0,
                Integer.MAX_VALUE

        ));

        settingValueChangers.add(new SettingIntValueChanger(
                getString(R.string.roadBuildingCost),
                new SettingValueGetter() {
                    @Override
                    public String getValue() {
                        return Integer.toString(setting.getRoadBuildingCost());
                    }
                },
                new SettingIntValueSetter() {
                    @Override
                    public void setValue(int value)
                    {
                        setting.setRoadBuildingCost(value);
                    }
                },
                0,
                Integer.MAX_VALUE
        ));

        settingValueChangers.add(new SettingStringValueChanger(
                getString(R.string.cityName),
                new SettingValueGetter()
                {
                    @Override
                    public String getValue()
                    {
                        return setting.getCityName();
                    }
                },
                new SettingStringValueSetter()
                {
                    @Override
                    public void setValue(String value)
                    {
                        setting.setCityName(value);
                    }
                }
        ));
    }

    private class SettingViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tvVarName;
        private TextView tvVarValue;
        private EditText etVarInput;
        private Button btnVarUpdate;

        private SettingValueChanger settingValueChanger;

        public SettingViewHolder(LayoutInflater inflater, ViewGroup parent)
        {
            super(inflater.inflate(R.layout.setting_entry, parent, false));

            tvVarName = (TextView) itemView.findViewById(R.id.tvVarName);
            tvVarValue = (TextView) itemView.findViewById(R.id.tvVarValue);
            etVarInput = (EditText) itemView.findViewById(R.id.etVarInput);
            btnVarUpdate = (Button) itemView.findViewById(R.id.btnVarUpdate);
            /*
                want the data in memory to update after input are entered into the edit text.
             */
            etVarInput.addTextChangedListener(
                    new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            SettingViewHolder.this.settingValueChanger.setCurrentInput(etVarInput.getText().toString());
                        }
                    }
            );

            btnVarUpdate.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String newValue = etVarInput.getText().toString();
                            if (!newValue.equals(""))
                            {
                                /*
                                    some will introduce error when a invalid input is entered.
                                    For example, changing a integer out of the bound it was set to
                                    be able to change in.
                                 */
                                String message = SettingViewHolder.this.settingValueChanger.changeValue(newValue);

                                if (message == null)
                                {
                                    updateTvVarValue();
                                } else {
                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(getActivity(), R.string.emptyEditTextErrorMessage, Toast.LENGTH_SHORT).show();
                            }

                            etVarInput.setText("");
                            settingValueChanger.setCurrentInput(etVarInput.getText().toString());
                        }
                    }
            );
        }

        public void bind(final SettingValueChanger settingValueChanger)
        {
            this.settingValueChanger = settingValueChanger;

            etVarInput.setText(settingValueChanger.getCurrentInput());
            etVarInput.setInputType(settingValueChanger.getInputType());

            updateTvVarName();
            updateTvVarValue();
        }

        private void updateTvVarName()
        {
            tvVarName.setText(settingValueChanger.getValueName());
        }

        private void updateTvVarValue()
        {
            tvVarValue.setText(settingValueChanger.getValue());
        }
    }

    private class SettingAdapter extends RecyclerView.Adapter<SettingViewHolder>
    {
        private List<SettingValueChanger> settingValueChangers;

        public SettingAdapter(List<SettingValueChanger> settingValueChangers)
        {
            this.settingValueChangers = settingValueChangers;
        }

        @NonNull
        @Override
        public SettingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SettingViewHolder(LayoutInflater.from(getActivity()), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull SettingViewHolder holder, int position) {
            holder.bind(settingValueChangers.get(position));
        }

        @Override
        public int getItemCount() {
            return settingValueChangers.size();
        }
    }
}
