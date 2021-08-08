package edu.curtin.citysimulator.model;

import android.graphics.Bitmap;

/**
 * Represent each block of the map simulated in the city simulation game.
 *
 * @author Tao Hu
 * date:    25/10/2020
 */
public class MapElement
{
    private static final String DEFAULT_OWNER_NAME = "System";
    private static final String PLAYER_OWNER_NAME = "Player";

    private final int terrainNorthWest;
    private final int terrainSouthWest;
    private final int terrainNorthEast;
    private final int terrainSouthEast;
    private Structure structure;
    private Bitmap image;
    private String ownerName;
    private String editableName;

    public MapElement(int northWest, int southWest, int northEast, int southEast, Structure structure)
    {
        this.terrainNorthWest = northWest;
        this.terrainSouthWest = southWest;
        this.terrainNorthEast = northEast;
        this.terrainSouthEast = southEast;
        this.structure = structure;
        this.ownerName = DEFAULT_OWNER_NAME;
        this.editableName = null;
    }

    public MapElement(int northWest, int southWest, int northEast, int southEast, Structure structure, Bitmap image, String ownerName, String editableName)
    {
        this.terrainNorthWest = northWest;
        this.terrainSouthWest = southWest;
        this.terrainNorthEast = northEast;
        this.terrainSouthEast = southEast;
        this.structure = structure;
        this.image = image;
        this.ownerName = ownerName;
        this.editableName = editableName;
    }

    public int getNorthWest()
    {
        return terrainNorthWest;
    }

    public int getSouthWest()
    {
        return terrainSouthWest;
    }

    public int getNorthEast()
    {
        return terrainNorthEast;
    }

    public int getSouthEast()
    {
        return terrainSouthEast;
    }

    public Structure getStructure()
    {
        return structure;
    }

    public void setStructure(Structure structure)
    {
        this.structure = structure;
    }

    public String getOwnerName()
    {
        return ownerName;
    }

    public void setOwnerDefault()
    {
        ownerName = DEFAULT_OWNER_NAME;
    }

    public void setOwnerPlayer()
    {
        ownerName = PLAYER_OWNER_NAME;
    }

    public Bitmap getImage()
    {
        return image;
    }

    public void setImage(Bitmap image)
    {
        this.image = image;
    }

    public String getEditableName()
    {
        return editableName;
    }

    public void setEditableName(String editableName)
    {
        this.editableName = editableName;
    }
}
