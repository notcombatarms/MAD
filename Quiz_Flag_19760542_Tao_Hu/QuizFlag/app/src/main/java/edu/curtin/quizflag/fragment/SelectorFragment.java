package edu.curtin.quizflag.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.curtin.quizflag.R;

/**
 * Abstract because there are many commonality between FlagSelectorFragment and
 * QuestionSelectorFragment. This class is created to store the common things, which is being a
 * LayoutChanger that can be used by LayoutSelectorFragment to control the displayed item of the
 * RecyclerView the subclass fragment might have.
 *
 * @author      Tao Hu
 * Date         20/09/2020
 */
public abstract class SelectorFragment extends Fragment implements LayoutChanger
{
    private GridLayoutManager gridLayoutManager;
    private LayoutChanger layoutChanger;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flag_selector, container, false);

        RecyclerView questionRecyclerView = (RecyclerView) view.findViewById(R.id.flagRecyclerView);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        questionRecyclerView.setLayoutManager(gridLayoutManager);

        questionRecyclerView.setAdapter(getAdapter());

        layoutChanger = (LayoutChanger) this;

        return view;
    }

    @Override
    public int getSpanCount()
    {
        return gridLayoutManager.getSpanCount();
    }

    @Override
    public boolean isOrientationHorizontal()
    {
        return gridLayoutManager.getOrientation() == GridLayoutManager.HORIZONTAL;
    }

    @Override
    public void setOrientationVertical() {
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
    }

    @Override
    public void setOrientationHorizontal() {
        gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
    }

    @Override
    public void setSpanCount(int amount) {
        gridLayoutManager.setSpanCount(amount);
    }

    public LayoutChanger getLayoutChanger()
    {
        return layoutChanger;
    }

    protected abstract RecyclerView.Adapter getAdapter();
}
