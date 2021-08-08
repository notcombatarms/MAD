package edu.curtin.citysimulator.model.database;

/**
 * represent each map element of the map saved in the database.
 *
 * @author taohu
 * Date:    8/11/2020
 */
public class DataMapData
{
    private int row;
    private int col;
    private int terrainNW;
    private int terrainSW;
    private int terrainNE;
    private int terrainSE;
    private int structureID;
    private byte[] imageBitMap;
    private String ownerName;
    private String editableName;

    public DataMapData(int row, int col, int terrainNW, int terrainSW, int terrainNE, int terrainSE, int structureID, byte[] imageBitMap, String ownerName, String editableName)
    {
        this.row = row;
        this.col = col;
        this.terrainNW = terrainNW;
        this.terrainSW = terrainSW;
        this.terrainNE = terrainNE;
        this.terrainSE = terrainSE;
        this.structureID = structureID;
        this.imageBitMap = imageBitMap;
        this.ownerName = ownerName;
        this.editableName = editableName;
    }

    public int getRow()
    {
        return row;
    }

    public int getCol()
    {
        return col;
    }

    public int getTerrainNW()
    {
        return terrainNW;
    }

    public int getTerrainSW()
    {
        return terrainSW;
    }

    public int getTerrainNE()
    {
        return terrainNE;
    }

    public int getTerrainSE()
    {
        return terrainSE;
    }

    public int getStructureID()
    {
        return structureID;
    }

    public byte[] getImageBitMap()
    {
        return imageBitMap;
    }

    public String getOwnerName()
    {
        return ownerName;
    }

    public String getEditableName()
    {
        return editableName;
    }
}
