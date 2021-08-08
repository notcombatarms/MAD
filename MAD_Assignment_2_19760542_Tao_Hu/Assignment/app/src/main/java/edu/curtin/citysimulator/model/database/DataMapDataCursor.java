package edu.curtin.citysimulator.model.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import edu.curtin.citysimulator.model.database.CityGameDBSchema.MapDataTable;

/**
 * get data map data from database.
 *
 * @author taohu
 * Date:    8/11/2020
 */
public class DataMapDataCursor extends CursorWrapper
{
    public DataMapDataCursor(Cursor cursor)
    {
        super(cursor);
    }

    public DataMapData getDataMapData()
    {
        int row = getInt(getColumnIndex(MapDataTable.Cols.ROW));
        int col = getInt(getColumnIndex(MapDataTable.Cols.COL));
        int terrainNW = getInt(getColumnIndex(MapDataTable.Cols.TERRAIN_NORTH_WEST));
        int terrainSW = getInt(getColumnIndex(MapDataTable.Cols.TERRAIN_SOUTH_WEST));
        int terrainNE = getInt(getColumnIndex(MapDataTable.Cols.TERRAIN_NORTH_EAST));
        int terrainSE = getInt(getColumnIndex(MapDataTable.Cols.TERRAIN_SOUTH_EAST));
        int structureID = getInt(getColumnIndex(MapDataTable.Cols.STRUCTURE_ID));
        byte[] imageBitMap = getBlob(getColumnIndex(MapDataTable.Cols.IMAGE_BIT_MAP));
        String ownerName = getString(getColumnIndex(MapDataTable.Cols.OWNER_NAME));
        String editableName = getString(getColumnIndex(MapDataTable.Cols.EDITABLE_NAME));

        return new DataMapData(row, col, terrainNW, terrainSW, terrainNE, terrainSE, structureID, imageBitMap, ownerName, editableName);
    }
}
