package edu.curtin.citysimulator.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.curtin.citysimulator.R;

/**
 * Contains all available structures that can be chosen by the user. Structures are stored in to 3
 * list of different types of structure, road, commercial and residential.
 *
 * @author Tao Hu
 * date:    25/10/2020
 */
public class StructureData
{
    private static StructureData instance;

    private Map<Integer, Structure> structures;

    private List<Commercial> commercials;
            /*
            = Arrays.asList(new Commercial[] {
            new Commercial(R.drawable.ic_building5),
            new Commercial(R.drawable.ic_building6),
            new Commercial(R.drawable.ic_building7),
            new Commercial(R.drawable.ic_building8)
    });*/

    private List<Residential> residentials;
    /*
    = Arrays.asList(new Residential[] {
            new Residential(R.drawable.ic_building1),
            new Residential(R.drawable.ic_building2),
            new Residential(R.drawable.ic_building3),
            new Residential(R.drawable.ic_building4)
    });*/

    private List<Road> roads;
        /*
            = Arrays.asList(new Road[]{
            new Road(R.drawable.ic_road_ns),
            new Road(R.drawable.ic_road_ew),
            new Road(R.drawable.ic_road_nsew),
            new Road(R.drawable.ic_road_ne),
            new Road(R.drawable.ic_road_nw),
            new Road(R.drawable.ic_road_se),
            new Road(R.drawable.ic_road_sw),
            new Road(R.drawable.ic_road_n),
            new Road(R.drawable.ic_road_e),
            new Road(R.drawable.ic_road_s),
            new Road(R.drawable.ic_road_w),
            new Road(R.drawable.ic_road_nse),
            new Road(R.drawable.ic_road_nsw),
            new Road(R.drawable.ic_road_new),
            new Road(R.drawable.ic_road_sew),
    });*/

    private StructureData()
    {
        structures = new HashMap<>();
        //commercial
        structures.put(R.drawable.ic_building5, new Commercial(R.drawable.ic_building5));
        structures.put(R.drawable.ic_building6, new Commercial(R.drawable.ic_building6));
        structures.put(R.drawable.ic_building7, new Commercial(R.drawable.ic_building7));
        structures.put(R.drawable.ic_building8, new Commercial(R.drawable.ic_building8));
        //residential
        structures.put(R.drawable.ic_building1, new Residential(R.drawable.ic_building1));
        structures.put(R.drawable.ic_building2, new Residential(R.drawable.ic_building2));
        structures.put(R.drawable.ic_building3, new Residential(R.drawable.ic_building3));
        structures.put(R.drawable.ic_building4, new Residential(R.drawable.ic_building4));
        //road
        structures.put(R.drawable.ic_road_ns, new Road(R.drawable.ic_road_ns));
        structures.put(R.drawable.ic_road_ew, new Road(R.drawable.ic_road_ew));
        structures.put(R.drawable.ic_road_nsew, new Road(R.drawable.ic_road_nsew));
        structures.put(R.drawable.ic_road_ne, new Road(R.drawable.ic_road_ne));
        structures.put(R.drawable.ic_road_nw, new Road(R.drawable.ic_road_nw));
        structures.put(R.drawable.ic_road_se, new Road(R.drawable.ic_road_se));
        structures.put(R.drawable.ic_road_sw, new Road(R.drawable.ic_road_sw));
        structures.put(R.drawable.ic_road_n, new Road(R.drawable.ic_road_n));
        structures.put(R.drawable.ic_road_e, new Road(R.drawable.ic_road_e));
        structures.put(R.drawable.ic_road_s, new Road(R.drawable.ic_road_s));
        structures.put(R.drawable.ic_road_w, new Road(R.drawable.ic_road_w));
        structures.put(R.drawable.ic_road_nse, new Road(R.drawable.ic_road_nse));
        structures.put(R.drawable.ic_road_nsw, new Road(R.drawable.ic_road_nsw));
        structures.put(R.drawable.ic_road_new, new Road(R.drawable.ic_road_new));
        structures.put(R.drawable.ic_road_sew, new Road(R.drawable.ic_road_sew));

        commercials = new ArrayList<>();
        commercials.add((Commercial) structures.get(R.drawable.ic_building5));
        commercials.add((Commercial) structures.get(R.drawable.ic_building6));
        commercials.add((Commercial) structures.get(R.drawable.ic_building7));
        commercials.add((Commercial) structures.get(R.drawable.ic_building8));

        residentials = new ArrayList<>();
        residentials.add((Residential) structures.get(R.drawable.ic_building1));
        residentials.add((Residential) structures.get(R.drawable.ic_building2));
        residentials.add((Residential) structures.get(R.drawable.ic_building3));
        residentials.add((Residential) structures.get(R.drawable.ic_building4));

        roads = new ArrayList<>();
        roads.add((Road) structures.get(R.drawable.ic_road_ns));
        roads.add((Road) structures.get(R.drawable.ic_road_ew));
        roads.add((Road) structures.get(R.drawable.ic_road_nsew));
        roads.add((Road) structures.get(R.drawable.ic_road_ne));
        roads.add((Road) structures.get(R.drawable.ic_road_nw));
        roads.add((Road) structures.get(R.drawable.ic_road_se));
        roads.add((Road) structures.get(R.drawable.ic_road_sw));
        roads.add((Road) structures.get(R.drawable.ic_road_n));
        roads.add((Road) structures.get(R.drawable.ic_road_e));
        roads.add((Road) structures.get(R.drawable.ic_road_s));
        roads.add((Road) structures.get(R.drawable.ic_road_w));
        roads.add((Road) structures.get(R.drawable.ic_road_nse));
        roads.add((Road) structures.get(R.drawable.ic_road_nsw));
        roads.add((Road) structures.get(R.drawable.ic_road_new));
        roads.add((Road) structures.get(R.drawable.ic_road_sew));
    }

    public List<Commercial> getCommercials()
    {
        return commercials;
    }

    public List<Residential> getResidentials()
    {
        return residentials;
    }

    public List<Road> getRoads()
    {
        return roads;
    }

    /**
     *
     * @param imageId the drawable image id of this structure, since it is unique, it is used as
     *                key to the structure object it self. This allows every block's structure from
     *                the map to refer to the same object in memory. That if this method is not used
     *                , then when loading game from database, map elements which have structure with
     *                the same image id refers to different structure objects in memory.
     * @return the structure with this image id.
     */
    public Structure getStructureByImageId(int imageId)
    {
        return structures.get(imageId);
    }

    public static StructureData getInstance()
    {
        if (instance == null)
        {
            instance = new StructureData();
        }

        return instance;
    }
}
