package edu.curtin.citysimulator.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import edu.curtin.citysimulator.R;
import edu.curtin.citysimulator.fragments.BuildingSelectorFragment;
import edu.curtin.citysimulator.fragments.InfoFragment;
import edu.curtin.citysimulator.fragments.MapFragment;
import edu.curtin.citysimulator.model.GameData;
import edu.curtin.citysimulator.model.Structure;

/**
 * The second screen of the app. The map is displayed, interact each block by click on it. Consists
 * of 3 fragments, the map is the second one. The first fragment is to display all the game stats
 * and provide a updateTime button to update the game (game is driven by the time since start). The
 * third fragment provides all structures that can be placed on a block on the map.
 *
 * @author Tao Hu
 * date:    31/10/2020
 */
public class MapActivity extends AppCompatActivity implements MapFragment.OnBlockClickListener
{
    private BuildingSelectorFragment buildingSelectorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.flBuildingSelector);

        if (fragment == null)
        {
            fragment = new BuildingSelectorFragment();
            fm.beginTransaction()
                    .add(R.id.flBuildingSelector, fragment)
                    .commit();
        }

        buildingSelectorFragment = (BuildingSelectorFragment) fragment;

        fragment = fm.findFragmentById(R.id.flInfo);

        if (fragment == null)
        {
            fragment = new InfoFragment();
            fm.beginTransaction()
                    .add(R.id.flInfo, fragment)
                    .commit();
        }

        final InfoFragment infoFragment = (InfoFragment) fragment;

        fragment = fm.findFragmentById(R.id.flMap);

        if (fragment == null)
        {
            fragment = new MapFragment();
            fm.beginTransaction()
                    .add(R.id.flMap, fragment)
                    .commit();
        }

        MapFragment mapFragment = (MapFragment) fragment;

        mapFragment.clearOnStructureChangeObservers();

        /*
            after a structure is bought or added to a map element in map UI. The money and some stats
            of the game has being changed. This observer will notify infoFragment to update its UI
            when map fragment wants.
         */
        mapFragment.addOnStructureChangeObserver(new MapFragment.OnStructuresChangeObserver()
        {
            @Override
            public void execute()
            {
                infoFragment.updateInfoAfterStructureChange();
            }
        });
    }

    @Override
    public Structure getSelectedStructure()
    {
        return buildingSelectorFragment.getSelectedStructure();
    }

    @Override
    public boolean isDemolishOn()
    {
        return buildingSelectorFragment.getDemolish();
    }

    @Override
    public boolean isDetailOn()
    {
        return buildingSelectorFragment.getDetail();
    }
}