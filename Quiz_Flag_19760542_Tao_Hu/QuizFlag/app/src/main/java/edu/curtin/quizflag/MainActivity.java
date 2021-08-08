package edu.curtin.quizflag;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import edu.curtin.quizflag.model.Game;
import edu.curtin.quizflag.model.Vars;

/**
 * The first screen of the app, the starting point and winning point is calculated.
 *
 * @author      Tao Hu
 * Date         20/09/2020
 */
public class MainActivity extends AppCompatActivity
{
    private static final String WINNING_POINT = "edu.curtin.winningPoint";
    private static final String STARTING_POINT = "edu.curtin.startingPoint";
    private static final String GAME_ENDED = "edu.curtin.gameEnded";
    private static final int REQUEST_CODE_GAME = 0;

    private int winningPoint;
    private int startingPoint;
    private boolean gameEnded = false;
    private TextView tvStartingPoint;
    private TextView tvWinningPoint;
    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findWidget();

        //screen rotations
        if (savedInstanceState != null)
        {
            gameEnded = savedInstanceState.getBoolean(GAME_ENDED);
        }

        if (!gameEnded)
        {
            if (savedInstanceState != null)
            {
                winningPoint = savedInstanceState.getInt(WINNING_POINT);
                startingPoint = savedInstanceState.getInt(STARTING_POINT);
            }
            else
            {
                Random random = new Random();
                startingPoint = random.nextInt(Vars.STARTING_POINT_MAX - Vars.STARTING_POINT_MIN) + 1 + Vars.STARTING_POINT_MIN;
                winningPoint = random.nextInt( Vars.WINNING_POINT_MAX - Vars.WINNING_POINT_MIN) + 1 + Vars.WINNING_POINT_MIN;
            }

            initWidget();
            updateWidget();
        }
        else
        {
            updateWidgetGameEnded();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(WINNING_POINT, winningPoint);
        outState.putInt(STARTING_POINT, startingPoint);
        outState.putBoolean(GAME_ENDED, gameEnded);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)
        {
            if (requestCode == REQUEST_CODE_GAME)
            {
                gameEnded = true;
                updateWidgetGameEnded();

                //check if the game was a win or a lose
                if (GameActivity.getGameOutput(data))
                {
                    Toast.makeText(this,R.string.winMessage,Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this,R.string.loseMessage,Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void findWidget()
    {
        tvStartingPoint = (TextView) findViewById(R.id.startingPoint);
        tvWinningPoint = (TextView) findViewById(R.id.winningPoint);
        btnStart = (Button) findViewById(R.id.start);
    }

    private void initWidget()
    {
        btnStart.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Game.createNewInstance();
                        startActivityForResult(GameActivity.getIntent(MainActivity.this, startingPoint, winningPoint), REQUEST_CODE_GAME);
                    }
                }
        );
    }

    private void updateWidget()
    {
        tvStartingPoint.setText(getString(R.string.startingPoint, startingPoint));
        tvWinningPoint.setText(getString(R.string.winningPoint, winningPoint));
    }

    //this change the views to the state when the game has ended
    private void updateWidgetGameEnded()
    {
        tvStartingPoint.setVisibility(View.INVISIBLE);
        tvWinningPoint.setVisibility(View.INVISIBLE);
        btnStart.setText(getString(R.string.finishGame));
        btnStart.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                }
        );
    }
}