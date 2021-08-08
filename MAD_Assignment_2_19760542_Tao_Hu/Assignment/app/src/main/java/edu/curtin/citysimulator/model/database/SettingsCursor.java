package edu.curtin.citysimulator.model.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import edu.curtin.citysimulator.model.Settings;
import edu.curtin.citysimulator.model.database.CityGameDBSchema.SettingTable;

/**
 * get setting object from database.
 *
 * @author taohu
 * Date:    8/11/2020
 */
public class SettingsCursor extends CursorWrapper
{
    public SettingsCursor(Cursor cursor)
    {
        super(cursor);
    }

    public Settings getSetting()
    {
        Settings settings = new Settings();

        settings.setMapWidth(getInt(getColumnIndex(SettingTable.Cols.MAP_WIDTH)));
        settings.setMapHeight(getInt(getColumnIndex(SettingTable.Cols.MAP_HEIGHT)));
        settings.setInitialMoney(getInt(getColumnIndex(SettingTable.Cols.INITIAL_MONEY)));
        settings.setFamilySize(getInt(getColumnIndex(SettingTable.Cols.FAMILY_SIZE)));
        settings.setShopSize(getInt(getColumnIndex(SettingTable.Cols.SHOP_SIZE)));
        settings.setSalary(getInt(getColumnIndex(SettingTable.Cols.SALARY)));
        settings.setServiceCost(getInt(getColumnIndex(SettingTable.Cols.SERVICE_COST)));
        settings.setHouseBuildingCost(getInt(getColumnIndex(SettingTable.Cols.HOUSE_BUILDING_COST)));
        settings.setCommonBuildingCost(getInt(getColumnIndex(SettingTable.Cols.COMM_BUILDING_COST)));
        settings.setRoadBuildingCost(getInt(getColumnIndex(SettingTable.Cols.ROAD_BUILDING_COST)));
        settings.setTaxRate(getDouble(getColumnIndex(SettingTable.Cols.TAX_RATE)));
        settings.setCityName(getString(getColumnIndex(SettingTable.Cols.CITY_NAME)));

        return settings;
    }
}
