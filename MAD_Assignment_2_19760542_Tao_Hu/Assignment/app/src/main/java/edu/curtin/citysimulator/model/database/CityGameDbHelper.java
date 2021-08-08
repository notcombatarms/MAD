package edu.curtin.citysimulator.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.curtin.citysimulator.model.database.CityGameDBSchema.*;

/**
 * Initiate the database, creating the tables.
 *
 * @author taohu
 * Date:    8/11/2020
 */
public class CityGameDbHelper extends SQLiteOpenHelper
{
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "city_game.db";

    public CityGameDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("CREATE TABLE " + SettingTable.NAME +
                "(" +
                SettingTable.Cols.MAP_WIDTH + " INTEGER, " +
                SettingTable.Cols.MAP_HEIGHT + " INTEGER, " +
                SettingTable.Cols.INITIAL_MONEY + " INTEGER, " +
                SettingTable.Cols.FAMILY_SIZE + " INTEGER, " +
                SettingTable.Cols.SHOP_SIZE + " INTEGER, " +
                SettingTable.Cols.SALARY + " INTEGER, " +
                SettingTable.Cols.SERVICE_COST + " INTEGER, " +
                SettingTable.Cols.HOUSE_BUILDING_COST + " INTEGER, " +
                SettingTable.Cols.COMM_BUILDING_COST + " INTEGER, " +
                SettingTable.Cols.ROAD_BUILDING_COST + " INTEGER, " +
                SettingTable.Cols.TAX_RATE + " FLOAT, " +
                SettingTable.Cols.CITY_NAME + " TEXT" +
                ")");

        sqLiteDatabase.execSQL("CREATE TABLE " + GameDataTable.NAME +
                "(" +
                GameDataTable.Cols.MONEY + " INTEGER, " +
                GameDataTable.Cols.GAME_TIME + " INTEGER, " +
                GameDataTable.Cols.N_RESIDENTIAL + " INTEGER, " +
                GameDataTable.Cols.N_COMMERCIAL + " INTEGER, " +
                GameDataTable.Cols.RECENT_INCOME + " INTERGER" +
                ")");

        sqLiteDatabase.execSQL("CREATE TABLE " + MapDataTable.NAME +
                "(" +
                MapDataTable.Cols.ROW + " INTEGER, " +
                MapDataTable.Cols.COL + " INTEGER, " +
                MapDataTable.Cols.TERRAIN_NORTH_WEST + " INTEGER, " +
                MapDataTable.Cols.TERRAIN_SOUTH_WEST + " INTEGER, " +
                MapDataTable.Cols.TERRAIN_NORTH_EAST + " INTEGER, " +
                MapDataTable.Cols.TERRAIN_SOUTH_EAST + " INTEGER, " +
                MapDataTable.Cols.STRUCTURE_ID + " INTEGER, " +
                MapDataTable.Cols.IMAGE_BIT_MAP + " BLOB, " +
                MapDataTable.Cols.OWNER_NAME + " TEXT, " +
                MapDataTable.Cols.EDITABLE_NAME + " TEXT" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }
}
