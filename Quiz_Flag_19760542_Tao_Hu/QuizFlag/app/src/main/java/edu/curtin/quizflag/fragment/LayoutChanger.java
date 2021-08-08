package edu.curtin.quizflag.fragment;

/**
 * used by LayoutSelectorFragment to control other fragment's layoutManager's state
 *
 * @author      Tao Hu
 * Date         20/09/2020
 */
public interface LayoutChanger
{
    public boolean isOrientationHorizontal();
    public int getSpanCount();
    public void setOrientationVertical();
    public void setOrientationHorizontal();
    public void setSpanCount(int amount);
}
