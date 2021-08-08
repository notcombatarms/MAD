package edu.curtin.quizflag.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.curtin.quizflag.R;

/**
 * LayoutSelectorFragment is part of the third screen used to control the orientation and display
 * of the middle fragment.
 *
 * @author      Tao Hu
 * Date         20/09/2020
 */
public class LayoutSelectorFragment extends Fragment
{
    /**
     * stores the angle the icon will face
     */
    private static final float ROTATION_ANGLE_CHANGE = 90.0f;

    /**
     * the image that act as the button for changing the middle fragment's orientation
     */
    private ImageView ivSingle;
    private ImageView ivDoubleVertical;
    private ImageView ivTripleVertical;
    private ImageView ivViewChanger;

    /**
     * the middle fragment in LayoutChanger form, to communicate through interface
     */
    private LayoutChanger layoutChanger;

    private boolean isDeviceTablet;
    private float rotationAngle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_selector, container, false);

        rotationAngle = 0.0f;
        isDeviceTablet = getResources().getBoolean(R.bool.isDeviceTablet);

        findViews(view);
        initViews();

        return view;
    }

    public void setLayoutChanger(LayoutChanger layoutChanger)
    {
        this.layoutChanger = layoutChanger;
    }

    private void findViews(View v)
    {
        ivSingle = (ImageView) v.findViewById(R.id.singleView);
        ivDoubleVertical = (ImageView) v.findViewById(R.id.doubleView);
        ivTripleVertical = (ImageView) v.findViewById(R.id.tripleView);
        ivViewChanger = (ImageView) v.findViewById(R.id.viewChanger);
    }

    private void initViews()
    {
        ivViewChanger.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!layoutChanger.isOrientationHorizontal())
                        {
                            /*
                                if the device is a phone and it is in landscape mode and the
                                span count is more than 1 then don't make change to middle fragment
                                as it will mess up, the height of item is too small that it
                                doesn't work.
                             */
                            if (!isDeviceTablet
                                    && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                                    && layoutChanger.getSpanCount() != 1)
                            {
                                Toast.makeText(getActivity(), R.string.horizontalViewWarning, Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                rotateAnimation();
                                layoutChanger.setOrientationHorizontal();
                            }
                        }
                        else
                        {
                            rotateAnimation();
                            layoutChanger.setOrientationVertical();
                        }
                    }

                    private void rotateAnimation()
                    {
                        rotationAngle = ROTATION_ANGLE_CHANGE - rotationAngle;

                        ivSingle.animate().rotation(rotationAngle).start();
                        ivDoubleVertical.animate().rotation(rotationAngle).start();
                        ivTripleVertical.animate().rotation(rotationAngle).start();
                        ivViewChanger.animate().rotation(rotationAngle).start();
                    }
                }
        );

        ivSingle.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        layoutChanger.setSpanCount(1);
                    }
                }
        );

        ivDoubleVertical.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*
                            check that if it is phone and orientation is landscape, then don't change
                            middle fragment, it will mess up that the recyclerview item's height is
                            so small that it doesn't work.
                         */
                        if ((!isDeviceTablet)
                                && (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                                && layoutChanger.isOrientationHorizontal()))
                        {
                            Toast.makeText(getActivity(), R.string.horizontalViewWarning, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            layoutChanger.setSpanCount(2);
                        }
                    }
                }
        );


        ivTripleVertical.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*
                            check that if it is phone and orientation is landscape, then don't change
                            middle fragment, it will mess up that the recyclerview item's height is
                            so small that it doesn't work.
                         */
                        if ((!isDeviceTablet)
                                && (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                                && layoutChanger.isOrientationHorizontal()))
                        {
                            Toast.makeText(getActivity(), R.string.horizontalViewWarning, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            layoutChanger.setSpanCount(3);
                        }
                    }
                }
        );
    }
}
