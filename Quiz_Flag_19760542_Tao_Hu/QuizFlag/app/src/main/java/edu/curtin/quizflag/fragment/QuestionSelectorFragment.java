package edu.curtin.quizflag.fragment;

import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import edu.curtin.quizflag.R;
import edu.curtin.quizflag.model.Game;
import edu.curtin.quizflag.model.Question;
import edu.curtin.quizflag.model.QuestionSet;

/**
 * Part of the third screen, displays all the questions of a selected flag available to select.
 *
 * @author      Tao Hu
 * Date         20/09/2020
 */
public class QuestionSelectorFragment extends SelectorFragment implements UIUpdateObserver
{
    private QuestionAdapter questionAdapter;
    private Game game;
    private CallBack callBack;

    /*
        Using the method learnt from Big Ranch Guides chapter 17 for two pane
     */
    public interface CallBack
    {
        public void onQuestionSelect();
    }

    public QuestionSelectorFragment()
    {
        game = Game.getInstance();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //context is the parent activity and it is also the callback
        callBack = (CallBack) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callBack = null;
    }

    @Override
    public void updateUI()
    {
        questionAdapter.notifyDataSetChanged();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        questionAdapter = new QuestionAdapter();

        return questionAdapter;
    }

    @Override
    public void onResume() {
        super.onResume();
        questionAdapter.notifyDataSetChanged();
    }

    private class QuestionViewHolder extends RecyclerView.ViewHolder
    {
        private Question question;
        private TextView tvQuestionName;
        private TextView tvQuestionPoints;
        private TextView tvQuestionPenalty;
        private Button btnSelectQuestion;

        public QuestionViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.question_cell, parent, false));
            tvQuestionName = (TextView) itemView.findViewById(R.id.questionName);
            tvQuestionPoints = (TextView) itemView.findViewById(R.id.questionPoints);
            tvQuestionPenalty = (TextView) itemView.findViewById(R.id.questionPenalty);
            btnSelectQuestion = (Button) itemView.findViewById(R.id.questionButton);
            btnSelectQuestion.setOnClickListener(
                    new View.OnClickListener() {
                        private long mLastClickTime;

                        @Override
                        public void onClick(View view) {
                            //Stopping frequent clicks, every click makes a new Fragment
                            //Initially it was to disable the button, since a new activity is started when
                            //the new activity is destroyed all buttons in this fragment wil undergo rebind due to onResume
                            //which enables the one that was disabled. However, when tablet is used no new
                            //activity is created and onResume is not called, so timer is used instead.
                            //learnt from https://stackoverflow.com/questions/5608720/android-preventing-double-click-on-a-button

                            //btnSelectQuestion.setEnabled(false)
                            if (SystemClock.elapsedRealtime() - mLastClickTime >= 1000) {
                                mLastClickTime = SystemClock.elapsedRealtime();
                                game.getCurrentQuestionSet().setCurrentQuestion(question);
                                callBack.onQuestionSelect();
                            }
                        }
                    }
            );
        }

        public void bind(Question question, int position)
        {
            this.question = question;
            tvQuestionName.setText(question.isSpecial() ? getString(R.string.questionNameSpecial, (position + 1)) : getString(R.string.questionName, (position + 1)));
            tvQuestionPoints.setText(getString(R.string.questionPoints, question.getPoints()));
            tvQuestionPenalty.setText(getString(R.string.questionPenalty, question.getPenalty()));

            //attempted change the color displayed, red for incorrect and green for correct
            if (question.isAttempted())
            {
                btnSelectQuestion.setEnabled(false);
                itemView.setBackground(
                        ResourcesCompat.getDrawable(getResources(),
                        (question.isAttemptCorrect() ? R.drawable.square_border_filled_green : R.drawable.square_border_filled_red),
                        null));
            }
            else if (!btnSelectQuestion.isEnabled())
            {
                /*
                    if not attempted and the button of this view holder is disabled then enable it,
                    because if it is disabled but not attempted then it means the bind between view
                    holder and the question has changed. This view holder was previous holding an
                    already attempted question, but gui change and now it binds to another question
                    that has not being attempted
                 */
                btnSelectQuestion.setEnabled(true);
                itemView.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.square_border_filled_light_blue, null));
            }
        }
    }

    private class QuestionAdapter extends RecyclerView.Adapter<QuestionViewHolder>
    {
        private QuestionSet questionSet;

        public QuestionAdapter()
        {
           questionSet = game.getCurrentQuestionSet();
        }

        @NonNull
        @Override
        public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new QuestionViewHolder(LayoutInflater.from(getActivity()), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
            holder.bind(questionSet.getQuestion(position), position);
        }

        @Override
        public int getItemCount() {
            return questionSet.getNumOfQuestions();
        }
    }
}
