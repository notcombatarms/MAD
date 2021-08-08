package edu.curtin.quizflag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import edu.curtin.quizflag.fragment.FlagSelectorFragment;
import edu.curtin.quizflag.fragment.LayoutChanger;
import edu.curtin.quizflag.fragment.LayoutSelectorFragment;
import edu.curtin.quizflag.fragment.StatsFragment;
import edu.curtin.quizflag.model.Game;
import edu.curtin.quizflag.model.Vars;

/**
 * The second screen of the app, it is where the flag is selected
 *
 * @author      Tao Hu
 * Date         20/09/2020
 */
public class GameActivity extends AppCompatActivity
{
    /**
     * the activity retrieves winning point and starting point from the first screen through intent.
     * When screen is rotate, the onCreate will be ran again, this boolean will make sure it does not
     * retrieve from the intent again and update the Game singleton the game has started.
     * This will be the key to retrieve from onSaveInstance 's bundle.
     */
    private static final String GAME_STARTED = "edu.curtin.gameStarted";
    private static final String STARTING_POINT = "edu.curtin.startingPoint";
    private static final String WINNING_POINT = "edu.curtin.winningPoint";
    /**
     * this is to send result back to the first screen to tell it whether the game was a win or a
     * lose
     */
    private static final String GAME_OUTPUT = "edu.curtin.gameOutput";

    private boolean gameStarted;

    public GameActivity()
    {
        gameStarted = false;
    }

    /**
     * This is the second screen of the app, and points are modified either in the third or forth
     * screen. So checking whether the game is win by checking in onResume works because when
     * the third screen finishes, this function is called and winning is checked.
     */
    @Override
    protected void onResume() {
        super.onResume();

        Game game = Game.getInstance();

        if (game.isGameEnded())
        {
            Intent retData = new Intent();
            retData.putExtra(GAME_OUTPUT, game.getCurrentPoint() >= game.getWinningPoint());
            setResult(RESULT_OK, retData);
            finish();
        }
    }

    /**
     * this makes sure that if the back button provided by android is pressed that intent is correct.
     */
    @Override
    public void onBackPressed() {
        Game game = Game.getInstance();
        Intent retData = new Intent();
        retData.putExtra(GAME_OUTPUT, game.getCurrentPoint() >= game.getWinningPoint());
        setResult(RESULT_OK, retData);
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(GAME_STARTED, gameStarted);
    }

    public static boolean getGameOutput(Intent intent)
    {
        return intent.getBooleanExtra(GAME_OUTPUT, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if (savedInstanceState != null)
        {
            gameStarted = savedInstanceState.getBoolean(GAME_STARTED);
        }

        //only retrieve when the game has not started
        if (!gameStarted)
        {
            gameStarted = true;

            Intent intent = getIntent();
            Game game = Game.getInstance();
            game.setCurrentPoint(intent.getIntExtra(STARTING_POINT, Vars.STARTING_POINT_DEFAULT));
            game.setWinningPoint(intent.getIntExtra(WINNING_POINT, Vars.WINNING_POINT_DEFAULT));
        }

        //third fragment in the screen
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.statsFragment);

        if (fragment == null)
        {
            fragment = new StatsFragment();
            fm.beginTransaction()
                    .add(R.id.statsFragment, fragment)
                    .commit();
        }

        ((StatsFragment) fragment).disableBackButton();

        //first fragment in the screen
        fragment = fm.findFragmentById(R.id.layoutSelectorFragment);

        if (fragment == null)
        {
            fragment = new LayoutSelectorFragment();
            fm.beginTransaction()
                    .add(R.id.layoutSelectorFragment, fragment)
                    .commit();
        }

        LayoutSelectorFragment layoutSelectorFragment = (LayoutSelectorFragment) fragment;

        //middle fragment in the screen
        fragment = fm.findFragmentById(R.id.flagSelectorFragment);

        if (fragment == null)
        {
            fragment = new FlagSelectorFragment();
            fm.beginTransaction()
                    .add(R.id.flagSelectorFragment, fragment)
                    .commit();
        }

        layoutSelectorFragment.setLayoutChanger((LayoutChanger) fragment);
    }

    /**
     * used by MainActivity to send information to GameActivity
     *
     * @param c the caller activity
     * @param startingPoint starting point
     * @param winningPoint winning point
     * @return the intent
     */
    public static Intent getIntent(Context c, int startingPoint, int winningPoint)
    {
        Intent intent = new Intent(c, GameActivity.class);
        intent.putExtra(STARTING_POINT, startingPoint);
        intent.putExtra(WINNING_POINT, winningPoint);

        return intent;
    }
}