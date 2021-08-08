package edu.curtin.citysimulator.model;

/**
 * Contains setting data of the game.
 *
 * @author Tao Hu
 * date:    25/10/2020
 */
public class Settings
{
    private int mapWidth;
    private int mapHeight;
    private int initialMoney;
    private int familySize;
    private int shopSize;
    private int salary;
    private int serviceCost;
    private int houseBuildingCost;
    private int commonBuildingCost;
    private int roadBuildingCost;
    private double taxRate;
    private String cityName;

    public Settings()
    {
        mapWidth = 20;
        mapHeight = 10;
        initialMoney = 1000;
        familySize = 4;
        shopSize = 6;
        salary = 10;
        taxRate = 0.3d;
        serviceCost = 2;
        houseBuildingCost = 100;
        commonBuildingCost = 500;
        roadBuildingCost = 20;
        cityName = "Perth";
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }

    public int getInitialMoney() {
        return initialMoney;
    }

    public void setInitialMoney(int initialMoney) {
        this.initialMoney = initialMoney;
    }

    public int getFamilySize() {
        return familySize;
    }

    public void setFamilySize(int familySize) {
        this.familySize = familySize;
    }

    public int getShopSize() {
        return shopSize;
    }

    public void setShopSize(int shopSize) {
        this.shopSize = shopSize;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public int getServiceCost() {
        return serviceCost;
    }

    public void setServiceCost(int serviceCost) {
        this.serviceCost = serviceCost;
    }

    public int getHouseBuildingCost() {
        return houseBuildingCost;
    }

    public void setHouseBuildingCost(int houseBuildingCost) {
        this.houseBuildingCost = houseBuildingCost;
    }

    public int getCommonBuildingCost() {
        return commonBuildingCost;
    }

    public void setCommonBuildingCost(int commonBuildingCost) {
        this.commonBuildingCost = commonBuildingCost;
    }

    public int getRoadBuildingCost() {
        return roadBuildingCost;
    }

    public void setRoadBuildingCost(int roadBuildingCost) {
        this.roadBuildingCost = roadBuildingCost;
    }

    public String getCityName()
    {
        return cityName;
    }

    public void setCityName(String cityName)
    {
        this.cityName = cityName;
    }
}
