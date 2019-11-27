package com.stanleyj.android.snake_game;

import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.stanleyj.android.snake_game.engine.GameEngine;
import com.stanleyj.android.snake_game.enums.Direction;
import com.stanleyj.android.snake_game.enums.GameState;
import com.stanleyj.android.snake_game.views.SnakeView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    private GameEngine gameEngine;
    private SnakeView snakeView;
    private final Handler handler = new Handler();
    int score;
    private long updatedelay = 200;
    TextView tv, tv2;
    private float prevX, prevY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameEngine = new GameEngine();
        gameEngine.intitGame();
        snakeView = (SnakeView) findViewById(R.id.sanke_view);
        tv = (TextView) findViewById(R.id.show_score);
        snakeView.setOnTouchListener(this);
        startUpdateHander();


    }

    public void startUpdateHander() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                score = GameEngine.score[0];
//                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                int lev = sharedPref.getInt("level", 150);
//                System.out.println(lev +" thos");
                tv.setText("Score: " + score);
                gameEngine.Update();
                if (gameEngine.getCurrentGameState() == GameState.Running) {
                    handler.postDelayed(this, updatedelay);
                }
                if (gameEngine.getCurrentGameState() == GameState.Lost) {
                    onGameLost();
                }
                snakeView.setSnakeViewMap(gameEngine.getMap());
                snakeView.invalidate();
            }
        });

    }

    private void onGameLost() {
        score = GameEngine.score[0];
        System.out.println(score + " this is your score");
        Toast.makeText(this, "You Lost Score " + score, Toast.LENGTH_LONG).show();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int HS = sharedPref.getInt("High_score", 0);
        tv.setText("Score: " + score);
        System.out.println(HS + " thos");
        if (score > HS) {
            SharedPreferences Pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor piss = Pref.edit();
            piss.putInt("High_score", score);
            piss.commit();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.speed, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.Decre) {
            updatedelay = 200;
            updatedelay = updatedelay - 50;
//            piss.putInt("level", 500);
//            piss.commit();
            return true;
        } else if (id == R.id.Incre) {
            updatedelay = 200;
            updatedelay = updatedelay + 50;
//            piss.putInt("level", 150);
//            piss.commit();
            return true;
        } else if (id == R.id.Reset) {
            GameEngine.score[0] = 0;
            updatedelay = 200;
            gameEngine = new GameEngine();
            gameEngine.intitGame();
            startUpdateHander();
            return true;
        } else if (id == R.id.Exit) {
            System.exit(1);
            return true;
        } else if (id == R.id.pause) {
            updatedelay = 5200;
            return true;
        } else if (id == R.id.Hscore) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            int HS = sharedPref.getInt("High_score", 0);
            System.out.println(HS + " thos");
            Toast.makeText(this, "High Score " + HS, Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onTouch(View view, MotionEvent mEvent) {
        updatedelay = 200;
        switch (mEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                prevX = mEvent.getX();
                prevY = mEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                float newX = mEvent.getX();
                float newY = mEvent.getY();
                // calculating where swaped L-R-U-D
                if (Math.abs(newX - prevX) > Math.abs(newY - prevY)) {
                    // Left - Right
                    if (newX > prevX) {
//                  right
                        gameEngine.UpdateDirection(Direction.East);
                    } else {
                        //left
                        gameEngine.UpdateDirection(Direction.West);
                    }
                } else {
                    //Up - Down
                    if (newY > prevY) {
                        //Down
                        gameEngine.UpdateDirection(Direction.South);
                    } else {
                        //UP
                        gameEngine.UpdateDirection(Direction.North);
                    }

                }
                break;


        }
        return true;
    }
}