package edu.curtin.citysimulator.model.database;


/**
 * object containing all the data that actually needs to be saved from GameData class object.
 *
 * @author taohu
 * Date:    8/11/2020
 */
public class DataGameData
{
    private int money;
    private int gameTime;
    private int nResidential;
    private int nCommercial;
    private int recentIncome;

    public DataGameData(int money, int gameTime, int nResidential, int nCommercial, int recentIncome)
    {
        this.money = money;
        this.gameTime = gameTime;
        this.nResidential = nResidential;
        this.nCommercial= nCommercial;
        this.recentIncome = recentIncome;
    }

    public int getMoney()
    {
        return money;
    }

    public int getGameTime()
    {
        return gameTime;
    }

    public int getnResidential()
    {
        return nResidential;
    }

    public int getnCommercial()
    {
        return nCommercial;
    }

    public int getRecentIncome()
    {
        return recentIncome;
    }
}
