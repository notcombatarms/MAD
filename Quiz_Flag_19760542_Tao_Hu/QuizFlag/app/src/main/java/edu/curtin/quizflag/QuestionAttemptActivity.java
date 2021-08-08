package edu.curtin.quizflag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import edu.curtin.quizflag.fragment.QuestionAttemptFragment;
import edu.curtin.quizflag.fragment.StatsFragment;

/**
 * Forth or third scree of the app, where answer is selected on a question.
 *
 * @author      Tao Hu
 * Date         20/09/2020
 */
public class QuestionAttemptActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_attempt);

        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.statsFragment);

        StatsFragment statsFragment = null;

        if (fragment == null)
        {
            fragment = new StatsFragment();
            fm.beginTransaction()
                    .add(R.id.statsFragment, fragment)
                    .commit();
        }

        statsFragment = (StatsFragment) fragment;
        statsFragment.enableBackButton();

        fragment = fm.findFragmentById(R.id.questionAttemptFragment);

        if (fragment == null)
        {
            fragment = new QuestionAttemptFragment();
            fm.beginTransaction()
                    .add(R.id.questionAttemptFragment, fragment)
                    .commit();
        }

        ((QuestionAttemptFragment) fragment).addNewUIUpdaterObserver(statsFragment);
    }
}