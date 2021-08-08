package edu.curtin.citysimulator.model.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import edu.curtin.citysimulator.model.database.CityGameDBSchema.GameDataTable;

/**
 * To retrieve data game data from database.
 *
 * @author taohu
 * Date:    8/11/2020
 */
public class DataGameDataCursor extends CursorWrapper
{
    public DataGameDataCursor(Cursor cursor)
    {
        super(cursor);
    }

    public DataGameData getDataGameData()
    {
        int money = getInt(getColumnIndex(GameDataTable.Cols.MONEY));
        int gameTime = getInt(getColumnIndex(GameDataTable.Cols.GAME_TIME));
        int nResidential = getInt(getColumnIndex(GameDataTable.Cols.N_RESIDENTIAL));
        int nCommercial = getInt(getColumnIndex(GameDataTable.Cols.N_COMMERCIAL));
        int recentIncome = getInt(getColumnIndex(GameDataTable.Cols.RECENT_INCOME));

        return new DataGameData(money, gameTime, nResidential, nCommercial, recentIncome);
    }
}
