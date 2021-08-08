package edu.curtin.quizflag.model;

import android.graphics.Color;

public class Vars
{
    /**
     * Game generated starting point and winning point bounds
     */
    public static final int STARTING_POINT_MIN = 10;
    public static final int STARTING_POINT_MAX = 30;
    public static final int WINNING_POINT_MIN = 80;
    public static final int WINNING_POINT_MAX = 100;
    public static final int STARTING_POINT_DEFAULT = 20;
    public static final int WINNING_POINT_DEFAULT = 90;

    public static final int SPECIAL_QUESTION_POINTS_INCREASE = 10;

    /**
     * Color values, used in indicating score of a country
     * */
    public static final int GOOD_SCORE_COLOR = Color.GREEN;
    public static final int MEDIUM_SCORE_COLOR = Color.rgb(255, 165, 0);
    public static final int BAD_SCORE_COLOR = Color.RED;

    public static final double MEDIUM_SCORE = 0.7d;
    public static final double BAD_SCORE = 0.5d;
}
