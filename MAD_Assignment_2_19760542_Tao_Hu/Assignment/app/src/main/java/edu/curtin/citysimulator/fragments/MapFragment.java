package edu.curtin.citysimulator.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.curtin.citysimulator.R;
import edu.curtin.citysimulator.activities.DetailActivity;
import edu.curtin.citysimulator.model.Commercial;
import edu.curtin.citysimulator.model.GameData;
import edu.curtin.citysimulator.model.MapElement;
import edu.curtin.citysimulator.model.Residential;
import edu.curtin.citysimulator.model.Road;
import edu.curtin.citysimulator.model.Structure;

/**
 * This fragment is the UI where the game map will be displayed and where the user interacts with
 * the map.
 *
 * @author Tao Hu
 * date:    31/10/2020
 */
public class MapFragment extends Fragment
{
    //for detail activity
    private static final int DETAIL_CODE = 1;

    private GameData gameData;
    private RecyclerView mapRecyclerView;
    private GridLayoutManager mapGridLayoutManager;
    //how large the view holders should be displayed in dp. Varying map width and height varies this
    private int viewHolderSize;
    private MapAdapter mapAdapter;
    //call back used to communicate with building selector fragment, to know what structure is
    //selected
    private OnBlockClickListener callBack;
    private List<OnStructuresChangeObserver> onStructuresChangeObservers = new ArrayList<>();

    /*
        after detail activity is started. User may take pictures to replace the structure drawable
        to display a bitmap taken from the camera. This remembers the view holder's position, so
        that this single view holder can get rebind to display the bitmap.
     */
    private int currentViewHolderPosition = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.single_recycler_view, container, false);

        gameData = GameData.getInstance();

        mapRecyclerView = (RecyclerView) view.findViewById(R.id.rvRecyclerView);

        mapGridLayoutManager = new GridLayoutManager(getActivity(), gameData.getSetting().getMapHeight(), GridLayoutManager.HORIZONTAL, false);

        mapRecyclerView.setLayoutManager(mapGridLayoutManager);
        mapAdapter = new MapAdapter(gameData, gameData.getSetting().getMapHeight(), gameData.getSetting().getMapWidth());
        mapRecyclerView.setAdapter(mapAdapter);

        mapRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout()
            {
                mapRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int mapHeight = gameData.getSetting().getMapHeight();
                int mapWidth = gameData.getSetting().getMapWidth();

                int sizeByHeight = mapRecyclerView.getHeight() / mapHeight;
                //the size of viewHolder should be even, because grid cell has 4 terrain grass blocks
                //and each terrain block gets half of viewHolderSize as their length (terrain is
                //square shaped. And viewHolderSize is the dp which is the smallest unit possible
                //when rendering. Therefore, if the viewHolderSize is not even, then each terrain
                //will render with a width of viewHolderSize / 2, which should be a float value with
                //a .5 to give symmetric terrain widths. However, dp is smallest unit and it rounds
                //to int so the .5 is gone and the 4 terrain is not symmetrical. Then, there will be
                //some small gaps(1dp) between few of the terrains.
                sizeByHeight = sizeByHeight - (sizeByHeight % 2);
                int sizeByHeightGap = mapRecyclerView.getWidth() - (sizeByHeight * mapWidth);

                int sizeByWidth = mapRecyclerView.getWidth() / mapWidth;
                sizeByWidth = sizeByWidth - (sizeByWidth % 2);
                int sizeByWidthGap = mapRecyclerView.getHeight() - (sizeByWidth * mapHeight);

                if (sizeByHeightGap < sizeByWidthGap)
                {
                    viewHolderSize = sizeByHeight;

                    //change the recycler's height to just fit the map's height.
                    //if this is not done, then there may be some gaps between rows of blocks.
                    ViewGroup.LayoutParams layoutParams = mapRecyclerView.getLayoutParams();
                    layoutParams.height = viewHolderSize * gameData.getSetting().getMapHeight();
                    mapRecyclerView.setLayoutParams(layoutParams);

                    //although the recyclerview got resized so there wont be gaps between
                    //view holders, but the recyclerview is displayed from left to right or top
                    //to bottom. So right side or bottom side wil have some gap between other
                    //fragments. Translate by gap / 2 will make sure that the map is displayed in
                    //middle of the map fragment.
                    mapRecyclerView.setTranslationY((mapRecyclerView.getHeight() - viewHolderSize * mapHeight) / 2.0f);

                    mapGridLayoutManager.setSpanCount(gameData.getSetting().getMapHeight());
                    mapGridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
                }
                else
                {
                    viewHolderSize = sizeByWidth;

                    ViewGroup.LayoutParams layoutParams = mapRecyclerView.getLayoutParams();
                    layoutParams.width = viewHolderSize * gameData.getSetting().getMapWidth();
                    mapRecyclerView.setLayoutParams(layoutParams);

                    mapRecyclerView.setTranslationX((mapRecyclerView.getWidth() - viewHolderSize * mapWidth) / 2.0f);

                    mapGridLayoutManager.setSpanCount(gameData.getSetting().getMapWidth());
                    mapGridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
                }

                //somehow viewHolder gets created before the height of the recyclerview is even
                //calculated. And since width length of map can change at runtime, then the size
                //of viewHolder are to be calculated at run time too. Because viewHolders are already
                //created with wrong size, the viewHolders need to be updated with the new size.
                mapAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    public static interface OnStructuresChangeObserver
    {
        public void execute();
    }

    public void clearOnStructureChangeObservers()
    {
        onStructuresChangeObservers = new ArrayList<>();
    }

    public void addOnStructureChangeObserver(OnStructuresChangeObserver observer)
    {
        onStructuresChangeObservers.add(observer);
    }

    public void notifyOnStructuresChangeObservers()
    {
        for (OnStructuresChangeObserver observer : onStructuresChangeObservers)
        {
            observer.execute();
        }
    }

    public static interface OnBlockClickListener
    {
        public boolean isDetailOn();
        public boolean isDemolishOn();
        public Structure getSelectedStructure();
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        callBack = (OnBlockClickListener) context;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK)
        {
            //if the screen was rotated in detail activity. Then this activity and fragments would
            //also be recreated. Then currentViewHolderPosition would be -1 by default, not saved.
            //All view holders are recreated, hence no need to notifyItemChanged.
            if (requestCode == DETAIL_CODE && currentViewHolderPosition > -1)
            {
                mapAdapter.notifyItemChanged(currentViewHolderPosition);
            }
        }
    }

    /**
     * Check around the position provided in coordinate if there are any adjacent structures that
     * are road.
     *
     * @param row the row of the block of the map.
     * @param col the column of the block of the map.
     * @return whether there are any road adjacent to this coordinate provided.
     */
    private boolean isCoordinateAdjacentToRoad(int row, int col)
    {

        /*
            check the top, bottom, left and right of this coordinate.
         */
        boolean ret = gameData.isCoordinateValid(row - 1, col) && gameData.getMapElement(row - 1, col).getStructure() instanceof Road;
        ret = ret || (gameData.isCoordinateValid(row + 1, col) && gameData.getMapElement(row + 1, col).getStructure() instanceof Road);
        ret = ret || (gameData.isCoordinateValid(row, col - 1) && gameData.getMapElement(row, col - 1).getStructure() instanceof Road);
        ret = ret || (gameData.isCoordinateValid(row, col + 1) && gameData.getMapElement(row, col + 1).getStructure() instanceof Road);

        return ret;
    }

    private class MapViewHolder extends RecyclerView.ViewHolder
    {
        private MapElement mapElement;
        /*
           5 image views that make up this block. 4 small square image view is the terrain or grass.
           The 5th image view is the structure image view which covers the whole square block.
         */
        private ImageView[] images;
        private ImageView ivCover;
        private int row;
        private int col;
        private int position;

        public MapViewHolder(LayoutInflater inflater, ViewGroup parent)
        {
            super(inflater.inflate(R.layout.grid_cell, parent, false));

            images = new ImageView[5];
            images[0] = (ImageView) itemView.findViewById(R.id.terrian1);
            images[1] = (ImageView) itemView.findViewById(R.id.terrian2);
            images[2] = (ImageView) itemView.findViewById(R.id.terrian3);
            images[3] = (ImageView) itemView.findViewById(R.id.terrian4);
            ivCover = (ImageView) itemView.findViewById(R.id.coverImage);
            View clView = (View) itemView.findViewById(R.id.clMapGridCell);

            clView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (callBack.isDetailOn())
                    {
                        //start new activity to view detail of the block

                        startActivityForResult(DetailActivity.getIntent(MapViewHolder.this.row, MapViewHolder.this.col, MapFragment.this.getActivity()), DETAIL_CODE);
                        currentViewHolderPosition = MapViewHolder.this.position;
                    }
                    else if (callBack.isDemolishOn())
                    {
                        //demolishing this block

                        Structure structure = mapElement.getStructure();

                        if (structure != null)
                        {
                            mapElement.setStructure(null);
                            mapElement.setOwnerDefault();
                            ivCover.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.transparent, null));

                            if (structure instanceof Residential)
                            {
                                gameData.setnResidential(gameData.getnResidential() - 1);
                            } else if (structure instanceof Commercial)
                            {
                                gameData.setnCommercial(gameData.getnCommercial() - 1);
                            }

                            notifyOnStructuresChangeObservers();
                        }
                    }
                    else
                    {
                        //placing structure

                        Structure structure = callBack.getSelectedStructure();

                        if (structure != null)
                        {
                            //block has no structure
                            if (mapElement.getStructure() == null)
                            {
                                if ((structure instanceof Road) )
                                {
                                    if ((gameData.getMoney() >= gameData.getSetting().getRoadBuildingCost()))
                                    {
                                        mapElement.setStructure(structure);
                                        mapElement.setOwnerPlayer();
                                        ivCover.setImageDrawable(ResourcesCompat.getDrawable(getResources(), mapElement.getStructure().getDrawableId(), null));
                                        gameData.setMoney(gameData.getMoney() - gameData.getSetting().getRoadBuildingCost());
                                    }
                                    else
                                    {
                                        Toast.makeText(getActivity(), getString(R.string.cannotAfford), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    if (isCoordinateAdjacentToRoad(row, col))
                                    {
                                        if ((structure instanceof Residential))
                                        {
                                            if ((gameData.getMoney() >= gameData.getSetting().getHouseBuildingCost()))
                                            {
                                                mapElement.setStructure(structure);
                                                mapElement.setOwnerPlayer();
                                                ivCover.setImageDrawable(ResourcesCompat.getDrawable(getResources(), mapElement.getStructure().getDrawableId(), null));
                                                gameData.setnResidential(gameData.getnResidential() + 1);
                                                gameData.setMoney(gameData.getMoney() - gameData.getSetting().getHouseBuildingCost());
                                            }
                                            else
                                            {
                                                Toast.makeText(getActivity(), getString(R.string.cannotAfford), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else if (structure instanceof Commercial)
                                        {
                                            if ((gameData.getMoney() >= gameData.getSetting().getCommonBuildingCost()))
                                            {
                                                mapElement.setStructure(structure);
                                                mapElement.setOwnerPlayer();
                                                ivCover.setImageDrawable(ResourcesCompat.getDrawable(getResources(), mapElement.getStructure().getDrawableId(), null));
                                                gameData.setnCommercial(gameData.getnCommercial() + 1);
                                                gameData.setMoney(gameData.getMoney() - gameData.getSetting().getCommonBuildingCost());
                                            }
                                            else
                                            {
                                                Toast.makeText(getActivity(), getString(R.string.cannotAfford), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(getActivity(), R.string.noAdjacentRoad, Toast.LENGTH_SHORT).show();
                                    }
                                }

                                notifyOnStructuresChangeObservers();
                            }
                            else
                            {
                                //structure already exist in this block

                                Toast.makeText(getActivity(), R.string.alreadyHaveBuilding, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
        }

        public void bind(MapElement mapElement, int row, int col, int position)
        {
            /*
                each view holder represent one square area on the map. The size of side of the
                square is determined runtime by the map height and map width.
                the size was already calculated when this fragment is created and each view holder
                gets their size changed.
             */
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            layoutParams.height = viewHolderSize;
            layoutParams.width = viewHolderSize;
            itemView.setLayoutParams(layoutParams);

            this.mapElement = mapElement;
            this.row = row;
            this.col = col;
            this.position = position;

            images[0].setImageDrawable(ResourcesCompat.getDrawable(getResources(), mapElement.getNorthWest(), null));
            images[1].setImageDrawable(ResourcesCompat.getDrawable(getResources(), mapElement.getNorthEast(), null));
            images[2].setImageDrawable(ResourcesCompat.getDrawable(getResources(), mapElement.getSouthWest(), null));
            images[3].setImageDrawable(ResourcesCompat.getDrawable(getResources(), mapElement.getSouthEast(), null));


            if (mapElement.getStructure() != null)
            {
                //bit map exists then display bitmap instead of structure drawable.
                if (mapElement.getImage() != null)
                {
                    ivCover.setImageBitmap(mapElement.getImage());
                }
                else
                {
                    ivCover.setImageDrawable(ResourcesCompat.getDrawable(getResources(), mapElement.getStructure().getDrawableId(), null));
                }
            }
            else
            {
                ivCover.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.transparent, null));
            }
        }
    }

    private class MapAdapter extends RecyclerView.Adapter<MapViewHolder>
    {
        private GameData gameData;
        private int height;
        private int width;

        public MapAdapter(GameData gameData, int height, int width)
        {
            this.gameData = gameData;
            this.height = height;
            this.width = width;
        }

        @NonNull
        @Override
        public MapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MapViewHolder(LayoutInflater.from(getActivity()), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull MapViewHolder holder, int position)
        {
            /*
                The position bind to each view holder changes when the recycler view's scrolling
                direction is changed.

                When scrolling in vertical direction respect to the map height:
                0  1  2  3  4  5  6  7
                8  9  10 11 12 13 14 15
                16 17 18 19 20 21 22 23

                when scrolling in horizontal respect to the map height:
                0  3  6  9 12 15 18 21
                1  4  7 10 13 16 19 22
                2  5  8 11 14 17 20 23

                Want to access the same map element from the game data so 8 in vertical should
                be accessing the same map element as 1 in horizontal
             */
            if (mapGridLayoutManager.getOrientation() == GridLayoutManager.VERTICAL)
            {
                int row = position / width;
                int col = position % width;

                holder.bind(gameData.getMapElement(row, col), row, col, position);
            }
            else
            {
                int row = position % height;
                int col = position / height;

                holder.bind(gameData.getMapElement(row, col), row, col, position);
            }
        }

        @Override
        public int getItemCount() {
            return gameData.getGridCount();
        }
    }
}
