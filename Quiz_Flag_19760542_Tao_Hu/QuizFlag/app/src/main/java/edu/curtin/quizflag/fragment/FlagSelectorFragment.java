package edu.curtin.quizflag.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import edu.curtin.quizflag.QuestionSelectionActivity;
import edu.curtin.quizflag.R;
import edu.curtin.quizflag.model.Game;
import edu.curtin.quizflag.model.Question;
import edu.curtin.quizflag.model.QuestionSet;
import edu.curtin.quizflag.model.Vars;

/**
 * Fragment in the second screen, select flag within this fragment
 *
 * @author      Tao Hu
 * Date         20/09/2020
 */
public class FlagSelectorFragment extends SelectorFragment
{
    private FlagAdapter flagAdapter;
    private Game game;

    public FlagSelectorFragment()
    {
        game = Game.getInstance();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        flagAdapter = new FlagAdapter();

        return flagAdapter;
    }

    /**
     * When the third screen exits, there may be some points change, therefore the view is to be
     * updated.
     */
    @Override
    public void onResume() {
        super.onResume();

        flagAdapter.notifyDataSetChanged();
    }

    /**
     * Represent a flag
     */
    private class FlagViewHolder extends RecyclerView.ViewHolder
    {
        private int flagId;
        private ImageView flagImage;
        private TextView tvScore;

        public FlagViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.flag_cell, parent, false));

            flagImage = (ImageView) itemView.findViewById(R.id.flagImage);
            tvScore = (TextView) itemView.findViewById(R.id.flagScore);

            flagImage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*
                            clicking this button or flag has two outcomes, one is increasing all the
                            question's points of this flag by a amount or to open third screen to
                            select question to answer of this flag.
                         */
                        if (game.isSpecialQuestionAnswered())
                        {
                            //special question was answered
                            game.setSpecialQuestionAnswered(false);

                            Toast.makeText(
                                    getActivity(),
                                    getString(
                                            R.string.specialQuestionPointIncrease
                                            , Vars.SPECIAL_QUESTION_POINTS_INCREASE),
                                    Toast.LENGTH_SHORT).show();

                            //for this selected flag increase all its questions points by the amount
                            for (Question question : game.getQuestionSet(flagId).getQuestions())
                            {
                                question.setPoints(question.getPoints() + Vars.SPECIAL_QUESTION_POINTS_INCREASE);
                            }
                        }
                        else {
                            //prevent from click multiple time to open multiple third screen
                            flagImage.setEnabled(false);
                            game.setCurrentQuestionSet(flagId);
                            startActivity(new Intent(getActivity(), QuestionSelectionActivity.class));
                        }
                    }
                }
            );
        }

        public void bind(int flagId)
        {
            this.flagId = flagId;
            flagImage.setImageDrawable(ResourcesCompat.getDrawable(getResources(), flagId, null));

            QuestionSet qs = game.getQuestionSet(flagId);

            if (qs.isAllAttempted())
            {
                flagImage.setEnabled(false);
                flagImage.setAlpha(0.4f);
                tvScore.setVisibility(View.VISIBLE);
                tvScore.setText(getString(R.string.questionSetScore, qs.getCorrectQuestionAnswered(), qs.getNumOfQuestions()));
                tvScore.setTextColor(getScoreTextColor(qs.getCorrectQuestionAnswered(), qs.getNumOfQuestions()));
            }
            else if (!flagImage.isEnabled())
            {
                flagImage.setEnabled(true);
                flagImage.setAlpha(1.0f);
                tvScore.setVisibility(View.INVISIBLE);
            }
        }
    }

    private class FlagAdapter extends RecyclerView.Adapter<FlagViewHolder>
    {
        private List<Integer> flagIdList;

        public FlagAdapter()
        {
            flagIdList = game.getFlagIdList();
        }

        @NonNull
        @Override
        public FlagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new FlagViewHolder(LayoutInflater.from(getActivity()), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull FlagViewHolder holder, int position) {
            holder.bind(flagIdList.get(position));
        }

        @Override
        public int getItemCount() {
            return flagIdList.size();
        }
    }

    private int getScoreTextColor(int score, int totalScore)
    {
        int color = Vars.GOOD_SCORE_COLOR;

        if (totalScore != 0) {
            double fraction = (double) score / (double) totalScore;

            if (fraction <= Vars.MEDIUM_SCORE && fraction > Vars.BAD_SCORE)
            {
                color = Vars.MEDIUM_SCORE_COLOR;
            }
            else if (fraction <= Vars.BAD_SCORE)
            {
                color = Vars.BAD_SCORE_COLOR;
            }
        }

        return color;
    }

}
