package edu.curtin.citysimulator.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import edu.curtin.citysimulator.R;
import edu.curtin.citysimulator.fragments.SettingFragment;

/**
 * Second screen. UI that allows user to change the settings of the game.
 *
 * @author Tao Hu
 * date:    31/10/2020
 */
public class SettingActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.flSettingChanger);

        if (fragment == null)
        {
            fm.beginTransaction()
                    .add(R.id.flSettingChanger, new SettingFragment())
                    .commit();
        }
    }
}