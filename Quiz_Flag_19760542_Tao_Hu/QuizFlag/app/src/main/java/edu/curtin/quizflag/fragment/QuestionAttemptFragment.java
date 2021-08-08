package edu.curtin.quizflag.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import java.util.ArrayList;
import java.util.List;

import edu.curtin.quizflag.R;
import edu.curtin.quizflag.model.Game;
import edu.curtin.quizflag.model.Question;

/**
 * Third or forth screen, where the question is displayed in gui and then answer is inputted.
 *
 * @author      Tao Hu
 * Date         20/09/2020
 */
public class QuestionAttemptFragment extends Fragment
{
    private TextView questionStr;
    private Button[] buttons;
    private Question question;
    private List<UIUpdateObserver> uiUpdaters = new ArrayList<>();

    /**
     * add new observer
     *
     * @param observer who's gui will need update when an question is answered
     */
    public void addNewUIUpdaterObserver(UIUpdateObserver observer)
    {
        if (observer == null)
        {
            throw new IllegalArgumentException("Null UIUpdateObserver");
        }

        uiUpdaters.add(observer);
    }

    /**
     * call all observer's update gui method
     */
    public void notifyAllObservers()
    {
        for (UIUpdateObserver observer : uiUpdaters)
        {
            observer.updateUI();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_attempt, container, false);

        buttons = new Button[4];
        question = Game.getInstance().getCurrentQuestionSet().getCurrentQuestion();

        findView(view);
        initView();

        return view;
    }

    private void findView(View view)
    {
        questionStr = (TextView) view.findViewById(R.id.questionStr);
        buttons[0] = (Button) view.findViewById(R.id.qaButton1);
        buttons[1] = (Button) view.findViewById(R.id.qaButton2);
        buttons[2] = (Button) view.findViewById(R.id.qaButton3);
        buttons[3] = (Button) view.findViewById(R.id.qaButton4);
    }

    private void initView()
    {
        questionStr.setText(question.getQuestionStr());

        String[] choices = question.getChoices();

        int nOfValidBtn = 0;
        boolean attempted = question.isAttempted();

        /*
            the number of button is always 4, but there can be 4 or 3 or 2 choices, so the amount
            of active button is dependent on the number of choices.
         */
        for (; nOfValidBtn < choices.length; nOfValidBtn++)
        {
            Button button = buttons[nOfValidBtn];

            button.setText(choices[nOfValidBtn]);

            if (!attempted)
            {
                button.setOnClickListener(
                        new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                Button currentButton = (Button) view;
                                question.setSelectedAnswer(currentButton.getText().toString());
                                Game game = Game.getInstance();
                                game.getCurrentQuestionSet().incrementQuestionAttempted();

                                /*
                                    after attempt, if the selected button is incorrect then it is
                                    coloured red if correct then green.
                                 */
                                updateButtonsAfterAttempt();

                                if (question.isAttemptCorrect())
                                {
                                    //update singleton
                                    game.setCurrentPoint(game.getCurrentPoint() + question.getPoints());
                                    game.getCurrentQuestionSet().incrementCorrectQuestionAnswered();

                                    if (question.isSpecial())
                                    {
                                        game.setSpecialQuestionAnswered(true);
                                        Toast.makeText(getActivity(), R.string.answerCorrectSpecial, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), R.string.answerCorrect, Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getActivity(), R.string.answerIncorrect, Toast.LENGTH_SHORT).show();
                                    game.setCurrentPoint(game.getCurrentPoint() - question.getPenalty());
                                }

                                //update gui
                                notifyAllObservers();

                                if (game.isGameEnded())
                                {
                                    /*Doesn't really work well
                                    long currentTime = SystemClock.elapsedRealtime();

                                    while (SystemClock.elapsedRealtime() - currentTime < 3000)
                                    {
                                    }*/

                                    getActivity().finish();
                                }
                            }
                        }
                );
            }
            else
            {
                /*
                    this is done when the screen is rotated, the buttons are restored to default
                    state. However wrong choice button are coloured red and correct one are coloured
                    green, so this makes sure that when screen is rotated, the correct colour are
                    displayed for button. Also, once attempted all button are disabled, this makes
                    sure that they are disabled.
                 */
                updateSingleButtonAfterAttempt(button);
            }
        }

        for (; nOfValidBtn < buttons.length; nOfValidBtn++)
        {
            Button button = buttons[nOfValidBtn];

            button.setVisibility(View.GONE);
            button.setEnabled(false);
        }
    }

    private void updateButtonsAfterAttempt()
    {
        for (int x = 0; x < question.getChoices().length; x++)
        {
            updateSingleButtonAfterAttempt(buttons[x]);
        }
    }

    /**
     * if button is the clicked one and it is incorrect then colour it red, if it is clicked one
     * and it is correct then colour it green, else nothing happen. And all button are disabled,
     * since the question is attempted.
     *
     * @param button the button to check against
     */
    private void updateSingleButtonAfterAttempt(Button button)
    {
        String buttonText = button.getText().toString();

        button.setEnabled(false);

        if (question.getCorrectAnswer().equals(buttonText))
        {
            button.setBackgroundColor(Color.GREEN);
        }
        else if (question.getSelectedAnswer().equals(buttonText))
        {
            button.setBackgroundColor(Color.RED);
        }
    }
}
