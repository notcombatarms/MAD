package edu.curtin.quizflag.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.curtin.quizflag.R;
import edu.curtin.quizflag.model.Game;

/**
 * Used in second, third and forth screen. This is to just display the current point and provide a
 * optional back button that returns to the previous screen. Implemented UIUpdateObserver, because
 * this fragment displays the current points and the ui need update when an question is attempted.
 *
 * @author      Tao Hu
 * Date         20/09/2020
 */
public class StatsFragment extends Fragment implements UIUpdateObserver
{
    private boolean enableButton;
    private TextView tvCurrentPoint;
    private Button backButton;

    @Override
    public void onResume() {
        super.onResume();

        updateTvCurrentPoint();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        tvCurrentPoint = (TextView) view.findViewById(R.id.currentPoint);
        backButton = (Button) view.findViewById(R.id.backButton);

        if (enableButton)
        {
            enableBackButton();
        }
        else
        {
            disableBackButton();
        }

        updateTvCurrentPoint();
        backButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity().finish();
                    }
                }
        );

        return view;
    }

    public void disableBackButton()
    {
        if (backButton != null) {
            backButton.setEnabled(false);
            backButton.setVisibility(View.INVISIBLE);
        }
        enableButton = false;
    }

    public void enableBackButton()
    {
        if (backButton != null) {
            backButton.setEnabled(true);
            backButton.setVisibility(View.VISIBLE);
        }
        enableButton = true;
    }

    public void updateTvCurrentPoint()
    {
        tvCurrentPoint.setText(getString(R.string.currentPoint, Game.getInstance().getCurrentPoint()));
    }

    @Override
    public void updateUI()
    {
        updateTvCurrentPoint();
    }
}
