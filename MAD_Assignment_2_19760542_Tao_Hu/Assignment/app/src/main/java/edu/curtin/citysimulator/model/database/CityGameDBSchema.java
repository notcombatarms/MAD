package edu.curtin.citysimulator.model.database;

/**
 * Contains the schema of the database used to store the game and setting data.
 *
 * @author taohu
 * Date:    8/11/2020
 */
public class CityGameDBSchema
{
    public static class SettingTable
    {
        public static final String NAME = "setting";

        public static class Cols
        {
            public static final String MAP_WIDTH = "map_width";
            public static final String MAP_HEIGHT = "map_height";
            public static final String INITIAL_MONEY = "initial_money";
            public static final String FAMILY_SIZE = "family_size";
            public static final String SHOP_SIZE = "shop_size";
            public static final String SALARY = "salary";
            public static final String SERVICE_COST = "service_cost";
            public static final String HOUSE_BUILDING_COST = "house_building_cost";
            public static final String COMM_BUILDING_COST = "comm_building_cost";
            public static final String ROAD_BUILDING_COST = "road_building_cost";
            public static final String TAX_RATE = "tax_rate";
            public static final String CITY_NAME = "city_name";
        }
    }

    public static class GameDataTable
    {
        public static final String NAME = "game_data";

        public static class Cols
        {
            public static final String MONEY = "money";
            public static final String GAME_TIME = "game_time";
            public static final String N_RESIDENTIAL = "n_residential";
            public static final String N_COMMERCIAL = "n_commercial";
            public static final String RECENT_INCOME = "recent_income";
        }
    }

    public static class MapDataTable
    {
        public static final String NAME = "map_data";

        public static class Cols
        {
            public static final String ROW = "row";
            public static final String COL = "col";
            public static final String TERRAIN_NORTH_WEST = "terrain_north_west";
            public static final String TERRAIN_SOUTH_WEST = "terrain_south_west";
            public static final String TERRAIN_NORTH_EAST = "terrain_north_east";
            public static final String TERRAIN_SOUTH_EAST = "terrain_south_east";
            public static final String STRUCTURE_ID = "structure_id";
            public static final String IMAGE_BIT_MAP = "image_bit_map";
            public static final String OWNER_NAME = "owner_name";
            public static final String EDITABLE_NAME = "editable_name";
        }
    }
}
