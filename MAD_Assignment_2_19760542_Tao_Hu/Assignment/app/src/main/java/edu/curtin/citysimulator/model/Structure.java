package edu.curtin.citysimulator.model;

/**
 * Structure, represent the building that can be placed at a block of the map.
 *
 * @author Tao Hu
 * date:    25/10/2020
 */
public abstract class Structure
{
    private int imageId;

    public Structure(int imageId)
    {
        this.imageId = imageId;
    }

    public int getDrawableId()
    {
        return imageId;
    }

    public static String getStructureType(Structure structure)
    {
        String ret = null;

        if (structure instanceof Residential)
        {
            ret = "Residential";
        }
        else if (structure instanceof Commercial)
        {
            ret = "Commercial";
        }
        else if (structure instanceof Road)
        {
            ret = "Road";
        }

        return ret;
    }
}