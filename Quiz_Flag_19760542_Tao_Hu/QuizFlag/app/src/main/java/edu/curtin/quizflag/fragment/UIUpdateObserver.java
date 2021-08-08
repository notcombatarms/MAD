package edu.curtin.quizflag.fragment;

/**
 * To work with QuestionAttemptFragment as observer pattern. When question is attempted some
 * fragment's UI need to have updates. Communication between these fragments and
 * QuestionAttemptFragment will be through this interface by the observer pattern.
 *
 * @author      Tao Hu
 * Date         20/09/2020
 */
public interface UIUpdateObserver
{
    public void updateUI();
}
