package edu.curtin.quizflag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import edu.curtin.quizflag.fragment.LayoutSelectorFragment;
import edu.curtin.quizflag.fragment.QuestionAttemptFragment;
import edu.curtin.quizflag.fragment.QuestionSelectorFragment;
import edu.curtin.quizflag.fragment.StatsFragment;
import edu.curtin.quizflag.model.Game;
import edu.curtin.quizflag.model.QuestionSet;

/**
 * Forth or third scree of the app, where a question is selected to answer.
 *
 * @author      Tao Hu
 * Date         20/09/2020
 */
public class QuestionSelectionActivity extends AppCompatActivity implements QuestionSelectorFragment.CallBack
{
    private boolean usingTablet;
    private StatsFragment statsFragment;
    private QuestionSelectorFragment questionSelectorFragment;

    @Override
    protected void onResume() {
        super.onResume();

        Game game = Game.getInstance();

        //special answered then returns to second screen
        if (game.isGameEnded() || game.isSpecialQuestionAnswered())
        {
            finish();
        }
    }

    /**
     * Using the method taught in Big Ranch Guides chapter 17 as the call back method to either
     * open a two pane for tablet or one for phone.
     */
    @Override
    public void onQuestionSelect()
    {
        if (usingTablet)
        {
            QuestionAttemptFragment questionAttemptFragment = new QuestionAttemptFragment();

            questionAttemptFragment.addNewUIUpdaterObserver(statsFragment);
            questionAttemptFragment.addNewUIUpdaterObserver(questionSelectorFragment);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.questionAttemptFragmentTwoPane, questionAttemptFragment)
                    .commit();
        }
        else
        {
            startActivity(new Intent(this, QuestionAttemptActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //R.layout.activity_select_question varies on whether tablet is used or phone is used
        //two pane for tablet and one for phone
        setContentView(R.layout.activity_select_question);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.statsFragment);


        if (fragment == null)
        {
            fragment = new StatsFragment();
            fm.beginTransaction()
                    .add(R.id.statsFragment, fragment)
                    .commit();
        }
        statsFragment = (StatsFragment) fragment;

        statsFragment.enableBackButton();

        fragment = fm.findFragmentById(R.id.layoutSelectorFragment);

        if (fragment == null)
        {
            fragment = new LayoutSelectorFragment();
            fm.beginTransaction()
                    .add(R.id.layoutSelectorFragment, fragment)
                    .commit();
        }

        LayoutSelectorFragment layoutSelectorFragment = (LayoutSelectorFragment) fragment;

        fragment = fm.findFragmentById(R.id.flagSelectorFragment);

        if (fragment == null)
        {
            fragment = new QuestionSelectorFragment();
            fm.beginTransaction()
                    .add(R.id.flagSelectorFragment, fragment)
                    .commit();
        }

        questionSelectorFragment = (QuestionSelectorFragment) fragment;

        layoutSelectorFragment.setLayoutChanger(questionSelectorFragment);

        usingTablet = findViewById(R.id.questionAttemptFragmentTwoPane) != null;

        //if it is using a tablet then the right half of the screen will be the following
        if (usingTablet)
        {
            QuestionSet currentQuestionSet = Game.getInstance().getCurrentQuestionSet();

            if (currentQuestionSet.getCurrentQuestion() == null)
            {
                currentQuestionSet.setCurrentQuestion(0);
            }

            fragment = fm.findFragmentById(R.id.questionAttemptFragmentTwoPane);

            if (fragment == null)
            {
                fragment = new QuestionAttemptFragment();
                fm.beginTransaction()
                        .add(R.id.questionAttemptFragmentTwoPane, fragment)
                        .commit();
            }

            //when a question is attempted, some UI requires update
            QuestionAttemptFragment questionAttemptFragment = (QuestionAttemptFragment) fragment;

            questionAttemptFragment.addNewUIUpdaterObserver(statsFragment);
            questionAttemptFragment.addNewUIUpdaterObserver(questionSelectorFragment);
        }
    }
}