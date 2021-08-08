package edu.curtin.citysimulator.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.curtin.citysimulator.R;
import edu.curtin.citysimulator.model.GameData;
import edu.curtin.citysimulator.model.Structure;
import edu.curtin.citysimulator.model.StructureData;

/**
 * UI for user to select what operation to be performed on map blocks. This include, selecting
 * structures to be placed on a block of a map, or to view detail of map or to demolish a structure
 * from a block of the map.
 *
 * @author Tao Hu
 * date:    31/10/2020
 */
public class BuildingSelectorFragment extends Fragment
{
    private static final String DEMOLISH = "edu.curtin.demolish";
    private static final String DETAIL = "edu.curtin.detail";
    private static final String CURRENT_STRUCTURE_TYPE = "edu.curtin.currentstructuretype";
    private static final String CURRENT_VIEW_HOLDER_POSITION = "edu.curtin.currentviewholderposition";
    private static final String SELECTED_STRUCTURE_IMAGE_ID = "edu.curtin.selectedstructureimageid";

    private static final String ROAD = "structureType.road";
    private static final String RESIDENTIAL = "structureType.residential";
    private static final String COMMERCIAL = "structureType.commercial";

    private StructureData structureData;
    private SelectorAdapter selectorAdapter;

    private Button btnResidential;
    private Button btnCommercial;
    private Button btnRoad;
    private Button btnDemolish;
    private Button btnDetail;

    private View selectedView = null;

    /*
        demolish represent if the demolish feature is toggled on or off
        detail represent if the detail feature is toggled on or off
        currentStructureType is the currently selected structure type, road etc.
        selectedStructureImageId is the key to which structure/building is currently selected
     */
    private boolean demolish = false;
    private boolean detail = false;
    private String currentStructureType = null;
    private int selectedStructureImageId = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_building_selector, container, false);

        structureData = StructureData.getInstance();

        List<? extends Structure> structures = structureData.getResidentials();

        if (savedInstanceState != null)
        {
            demolish = savedInstanceState.getBoolean(DEMOLISH);
            detail = savedInstanceState.getBoolean(DETAIL);
            currentStructureType = savedInstanceState.getString(CURRENT_STRUCTURE_TYPE);
            selectedStructureImageId = savedInstanceState.getInt(SELECTED_STRUCTURE_IMAGE_ID);

            if (currentStructureType != null)
            {
                if (currentStructureType.equals(ROAD))
                {
                    structures = structureData.getRoads();
                }
                else if (currentStructureType.equals(COMMERCIAL))
                {
                    structures = structureData.getCommercials();
                }
            }
        }

        RecyclerView selectorRecyclerView = view.findViewById(R.id.rvBuildingSelector);
        selectorRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        selectorAdapter = new SelectorAdapter(structures);
        selectorRecyclerView.setAdapter(selectorAdapter);

        findView(view);
        initView();

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        outState.putBoolean(DEMOLISH, demolish);
        outState.putBoolean(DETAIL, detail);
        outState.putString(CURRENT_STRUCTURE_TYPE, currentStructureType);
        outState.putInt(SELECTED_STRUCTURE_IMAGE_ID, selectedStructureImageId);

        super.onSaveInstanceState(outState);
    }

    private void findView(View view)
    {
        btnResidential = view.findViewById(R.id.btnResidential);
        btnCommercial = view.findViewById(R.id.btnCommercial);
        btnRoad = view.findViewById(R.id.btnRoad);
        btnDemolish = view.findViewById(R.id.btnDemolish);
        btnDetail = view.findViewById(R.id.btnDetail);
    }

    private void initView()
    {
        btnResidential.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                selectorAdapter.setStructures(structureData.getResidentials());
                currentStructureType = RESIDENTIAL;
            }
        });

        btnCommercial.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                selectorAdapter.setStructures(structureData.getCommercials());
                currentStructureType = COMMERCIAL;
            }
        });

        btnRoad.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                selectorAdapter.setStructures(structureData.getRoads());
                currentStructureType = ROAD;
            }
        });

        /*
            after rotation, check if any of the button was selected before, if did then change the
            color of the button accordingly.
         */
        btnDemolish.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                demolish = !demolish;

                updateButtonBackGround(btnDemolish, demolish, R.drawable.rectangle_outline_top_bottom_fill_red_dark, R.drawable.rectangle_outline_top_bottom_fill_red_bright);
            }
        });

        updateButtonBackGround(btnDemolish, demolish, R.drawable.rectangle_outline_top_bottom_fill_red_dark, R.drawable.rectangle_outline_top_bottom_fill_red_bright);

        btnDetail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                detail = !detail;

                updateButtonBackGround(btnDetail, detail, R.drawable.rectangle_outline_top_bottom_fill_blue_dark, R.drawable.rectangle_outline_top_bottom_fill_blue_light);
            }
        });

        updateButtonBackGround(btnDetail, detail, R.drawable.rectangle_outline_top_bottom_fill_blue_dark, R.drawable.rectangle_outline_top_bottom_fill_blue_light);
    }

    public boolean getDetail()
    {
        return detail;
    }

    public boolean getDemolish()
    {
        return demolish;
    }

    public Structure getSelectedStructure()
    {
        return structureData.getStructureByImageId(selectedStructureImageId);
    }

    private void updateButtonBackGround(Button button, boolean state, int onStateBackground, int offStateBackGround)
    {
        if (state)
        {
            button.setBackground(ResourcesCompat.getDrawable(getResources(), onStateBackground, null));
        }
        else
        {
            button.setBackground(ResourcesCompat.getDrawable(getResources(), offStateBackGround, null));
        }
    }

    private class SelectorViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView ivItem;
        private Structure structure;
        private int position;

        public SelectorViewHolder(LayoutInflater inflater, ViewGroup parent)
        {
            super(inflater.inflate(R.layout.select_item_cell, parent, false));

            ivItem = (ImageView) itemView.findViewById(R.id.ivItem);

            ivItem.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (selectedStructureImageId != SelectorViewHolder.this.structure.getDrawableId())
                    {
                        /*
                            selecting different building, will change the color of the view holder
                            of the one selected before and the view holder of this selected one to
                            help user get idea of what building is currently selected.
                         */
                        if (selectedView != null)
                        {
                            selectedView.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.rectangle_outline_top_bottom_fill_blue_dark, null));
                        }

                        itemView.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.rectangle_outline_top_bottom_fill_gray, null));
                        selectedView = SelectorViewHolder.this.itemView;
                        selectedStructureImageId = SelectorViewHolder.this.structure.getDrawableId();
                    }
                }
            });
        }

        public void bind(Structure structure, int position)
        {
            this.structure = structure;
            this.position = position;
            ivItem.setImageDrawable(ResourcesCompat.getDrawable(getResources(), structure.getDrawableId(), null));

            if (selectedStructureImageId == structure.getDrawableId())
            {
                selectedView = this.itemView;
                itemView.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.rectangle_outline_top_bottom_fill_gray, null));
            }
            else
            {
                itemView.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.rectangle_outline_top_bottom_fill_blue_dark, null));
            }
        }
    }

    private class SelectorAdapter extends RecyclerView.Adapter<SelectorViewHolder>
    {
        List<? extends Structure> structures;

        public SelectorAdapter(List<? extends Structure> structures)
        {
            this.structures = structures;
        }

        public void setStructures(List<? extends Structure> structures)
        {
            this.structures = structures;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public SelectorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SelectorViewHolder(LayoutInflater.from(getActivity()), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull SelectorViewHolder holder, int position) {
            holder.bind(structures.get(position), position);
        }

        @Override
        public int getItemCount() {
            return structures.size();
        }
    }
}
