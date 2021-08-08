package edu.curtin.citysimulator.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Random;

import edu.curtin.citysimulator.R;
import edu.curtin.citysimulator.model.database.CityGameDBSchema.*;
import edu.curtin.citysimulator.model.database.CityGameDbHelper;
import edu.curtin.citysimulator.model.database.DataGameData;
import edu.curtin.citysimulator.model.database.DataMapData;
import edu.curtin.citysimulator.model.database.DataMapDataCursor;
import edu.curtin.citysimulator.model.database.DataGameDataCursor;
import edu.curtin.citysimulator.model.database.SettingsCursor;

/**
 * Contains all data about the city simulation game.
 *
 * @author Tao Hu
 * date:    25/10/2020
 */
public class GameData
{
    private static final int NO_STRUCTURE = -1;

    private static GameData instance;

    private Settings settings;
    private int money;
    private int gameTime;
    private int nResidential;
    private int nCommercial;
    private int recentIncome;
    private int population;
    private int employmentRate;
    private int temperature;

    private boolean started;

    private SQLiteDatabase db;

    private MapElement[][] map;

    private static final int[] GRASSES = {R.drawable.ic_grass1, R.drawable.ic_grass2, R.drawable.ic_grass3, R.drawable.ic_grass4};

    private GameData()
    {
        settings = new Settings();
        money = settings.getInitialMoney();
        gameTime = 0;
        nResidential = 0;
        nCommercial= 0;
        recentIncome = 0;
        population = 0;
        employmentRate = 0;
        temperature = -1;
    }

    public static GameData getInstance()
    {
        if (instance == null)
        {
            instance = new GameData();
        }

        return instance;
    }

    public void generateNewMap()
    {
        Random random = new Random();
        map = new MapElement[settings.getMapHeight()][settings.getMapWidth()];

        for (int i = 0; i < settings.getMapHeight(); i++)
        {
            for (int j = 0; j < settings.getMapWidth(); j++)
            {
                map[i][j] = new MapElement(
                    GRASSES[random.nextInt(GRASSES.length)],
                    GRASSES[random.nextInt(GRASSES.length)],
                    GRASSES[random.nextInt(GRASSES.length)],
                    GRASSES[random.nextInt(GRASSES.length)],
                    null);
            }
        }
    }

    public void openDatabase(Context context)
    {
        if (db == null)
        {
            db = new CityGameDbHelper(context.getApplicationContext()).getWritableDatabase();
        }
    }

    public boolean isDatabaseEmpty(Context context)
    {
        boolean ret;

        openDatabase(context);

        SettingsCursor settingsCursor = new SettingsCursor(
                db.query(SettingTable.NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null)
        );

        try
        {
            ret = !settingsCursor.moveToFirst();
        }
        finally
        {
            settingsCursor.close();
        }

        return ret;
    }

    public void loadFromDatabase(Context context)
    {
        openDatabase(context);

        //settings
        SettingsCursor settingsCursor = new SettingsCursor(
                db.query(SettingTable.NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null)
        );

        try
        {
            settingsCursor.moveToFirst();
            settings = settingsCursor.getSetting();
        }
        finally
        {
            settingsCursor.close();
        }

        //game data
        DataGameDataCursor dataGameDataCursor = new DataGameDataCursor(
                db.query(GameDataTable.NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null)
        );

        try
        {
            dataGameDataCursor.moveToFirst();
            DataGameData dataGameData = dataGameDataCursor.getDataGameData();
            this.money = dataGameData.getMoney();
            this.gameTime = dataGameData.getGameTime();
            this.nResidential = dataGameData.getnResidential();
            this.nCommercial = dataGameData.getnCommercial();
            this.recentIncome = dataGameData.getRecentIncome();
        }
        finally
        {
            dataGameDataCursor.close();
        }

        //map data
        DataMapDataCursor dataMapDataCursor = new DataMapDataCursor(
                db.query(MapDataTable.NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null)
        );

        try
        {
            StructureData structureData = StructureData.getInstance();
            map = new MapElement[settings.getMapHeight()][settings.getMapWidth()];

            dataMapDataCursor.moveToFirst();

            while(!dataMapDataCursor.isAfterLast())
            {
                DataMapData dataMapData = dataMapDataCursor.getDataMapData();

                map[dataMapData.getRow()][dataMapData.getCol()]
                        = new MapElement
                        (
                                dataMapData.getTerrainNW(),
                                dataMapData.getTerrainSW(),
                                dataMapData.getTerrainNE(),
                                dataMapData.getTerrainSE(),
                                structureData.getStructureByImageId(dataMapData.getStructureID()),
                                getImage(dataMapData.getImageBitMap()),
                                dataMapData.getOwnerName(),
                                dataMapData.getEditableName()
                        );

                dataMapDataCursor.moveToNext();
            }

            if (map[settings.getMapHeight() - 1][settings.getMapWidth() - 1] == null)
            {
                Toast.makeText(context, context.getString(R.string.corruptedDataMsg), Toast.LENGTH_SHORT).show();
                generateNewMap();
            }
        }
        finally
        {
        dataMapDataCursor.close();
        }
    }

    public void saveToDatabase(Context context)
    {
        openDatabase(context);

        //saving setting data
        db.delete(SettingTable.NAME, null, null);

        ContentValues cv = new ContentValues();

        cv.put(SettingTable.Cols.MAP_WIDTH, settings.getMapWidth());
        cv.put(SettingTable.Cols.MAP_HEIGHT, settings.getMapHeight());
        cv.put(SettingTable.Cols.INITIAL_MONEY, settings.getInitialMoney());
        cv.put(SettingTable.Cols.FAMILY_SIZE, settings.getFamilySize());
        cv.put(SettingTable.Cols.SHOP_SIZE, settings.getShopSize());
        cv.put(SettingTable.Cols.SALARY, settings.getSalary());
        cv.put(SettingTable.Cols.SERVICE_COST, settings.getServiceCost() );
        cv.put(SettingTable.Cols.HOUSE_BUILDING_COST, settings.getHouseBuildingCost());
        cv.put(SettingTable.Cols.COMM_BUILDING_COST, settings.getCommonBuildingCost());
        cv.put(SettingTable.Cols.ROAD_BUILDING_COST, settings.getRoadBuildingCost());
        cv.put(SettingTable.Cols.TAX_RATE, settings.getTaxRate());
        cv.put(SettingTable.Cols.CITY_NAME, settings.getCityName());

        db.insert(SettingTable.NAME, null, cv);

        //saving game data
        db.delete(GameDataTable.NAME, null, null);

        cv = new ContentValues();

        cv.put(GameDataTable.Cols.MONEY, money);
        cv.put(GameDataTable.Cols.GAME_TIME, gameTime);
        cv.put(GameDataTable.Cols.N_RESIDENTIAL, nResidential);
        cv.put(GameDataTable.Cols.N_COMMERCIAL, nCommercial);
        cv.put(GameDataTable.Cols.RECENT_INCOME, recentIncome);

        db.insert(GameDataTable.NAME, null, cv);

        //saving map data
        db.delete(MapDataTable.NAME, null, null);

        MapElement mapElement;

        int x = 0;

        for (int i = 0; i < settings.getMapHeight(); i++)
        {
            for (int j = 0; j < settings.getMapWidth(); j++)
            {
                mapElement = map[i][j];
                cv = new ContentValues();

                cv.put(MapDataTable.Cols.ROW, i);
                cv.put(MapDataTable.Cols.COL, j);
                cv.put(MapDataTable.Cols.TERRAIN_NORTH_WEST, mapElement.getNorthWest());
                cv.put(MapDataTable.Cols.TERRAIN_SOUTH_WEST, mapElement.getSouthWest());
                cv.put(MapDataTable.Cols.TERRAIN_NORTH_EAST, mapElement.getNorthEast());
                cv.put(MapDataTable.Cols.TERRAIN_SOUTH_EAST, mapElement.getSouthEast());

                int structureID = NO_STRUCTURE;

                if (mapElement.getStructure() != null)
                {
                    structureID = mapElement.getStructure().getDrawableId();
                }

                cv.put(MapDataTable.Cols.STRUCTURE_ID, structureID);

                byte[] imageBytes = null;

                if (mapElement.getImage() != null)
                {
                    imageBytes = getBytes(mapElement.getImage());
                }

                cv.put(MapDataTable.Cols.IMAGE_BIT_MAP, imageBytes);
                cv.put(MapDataTable.Cols.OWNER_NAME, mapElement.getOwnerName());
                cv.put(MapDataTable.Cols.EDITABLE_NAME, mapElement.getEditableName());

                db.insert(MapDataTable.NAME, null, cv);
            }
        }

    }

    public MapElement getMapElement(int i, int j)
    {
        return map[i][j];
    }

    public Settings getSetting()
    {
        return settings;
    }

    public int getGridCount()
    {
        return settings.getMapWidth() * settings.getMapHeight();
    }

    public int getMoney()
    {
        return money;
    }

    public void setMoney(int money)
    {
        this.money = money;
    }

    public int getGameTime()
    {
        return gameTime;
    }

    public void setGameTime(int gameTime)
    {
        this.gameTime = gameTime;
    }

    public int getnResidential()
    {
        return nResidential;
    }

    public void setnResidential(int nResidential)
    {
        this.nResidential = nResidential;
    }

    public int getnCommercial()
    {
        return nCommercial;
    }

    public void setnCommercial(int nCommercial)
    {
        this.nCommercial = nCommercial;
    }

    public int getRecentIncome()
    {
        return recentIncome;
    }

    public void setRecentIncome(int recentIncome)
    {
        this.recentIncome = recentIncome;
    }

    public int getPopulation()
    {
        return population;
    }

    public void setPopulation(int population)
    {
        this.population = population;
    }

    public int getEmploymentRate()
    {
        return employmentRate;
    }

    public void setEmploymentRate(int employmentRate)
    {
        this.employmentRate = employmentRate;
    }

    public int getTemperature()
    {
        return temperature;
    }

    public void setTemperature(int temperature)
    {
        this.temperature = temperature;
    }

    public MapElement[][] getMap()
    {
        return map;
    }

    public boolean isStarted()
    {
        return started;
    }

    public void setStarted(boolean started)
    {
        this.started = started;
    }

    public boolean isCoordinateValid(int row, int col)
    {
        return row > 0 && row < settings.getMapHeight() && col > 0 && col < settings.getMapWidth();
    }

    /**
     * To compress a bitmap into array of bytes for storing.
     *
     * Retrieved from https://stackoverflow.com/questions/11790104/how-to-storebitmap-image-and-retrieve-image-from-sqlite-database-in-android
     *
     * @param bitmap the bit map image to be compressed to byes
     * @return byte array representation of bitmap
     */
    private static byte[] getBytes(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public static Bitmap getImage(byte[] image)
    {
       Bitmap bitmap = null;

       if (image != null)
       {
           bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
       }

       return bitmap;
    }
}
