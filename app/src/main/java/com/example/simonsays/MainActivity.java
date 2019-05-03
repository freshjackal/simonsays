package com.example.simonsays;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView gameInfo, scoreInfo;
    Random rand;
    ArrayList<Animator> animList;
    Button[] btnArray;
    Button clickedBtn;
    private boolean isGameStarted;
    private int random;
    private int round;
    private int counter;
    private int actScore;
    private int highScore;

    public MainActivity() {
        this.setIsGameStarted(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameInfo = findViewById(R.id.gameInfo);
        scoreInfo = findViewById(R.id.scoreInfo);

        rand = new Random();
        animList = new ArrayList<>();


        fillBtnArray();
        btnSetOnClickListener();
        btnSetOnTouchListener();
        startGame();
    }

    @Override
    public void onClick(View view) {
        clickedBtn = (Button)view;

        if(this.getIsGameStarted()) {
            Animator animator = animList.get(this.getCounter());
            ObjectAnimator checkAnimator = (ObjectAnimator)animator;
            Button correctBtn = (Button)checkAnimator.getTarget();

            if(clickedBtn == correctBtn) {
                gameInfo.setText("Nice!");
                this.setActScore(this.getActScore() + 1);
                scoreInfo.setText(String.valueOf(this.getActScore()));

                if(this.getCounter() + 1 == this.getRound()) {
                    animateBtn();
                    this.setRound(this.getRound() + 1);
                    this.setCounter(0); // reset counter
                }
                else {
                    this.setCounter(this.getCounter() + 1);
                }
            }
            else {
                newHighscore();
                printHighscore();
                resetGame();
            }
        }
        else {
            showToast();
        }
    }

    public void fillBtnArray() {
        btnArray = new Button[4];
        btnArray[0] = findViewById(R.id.redBtn);
        btnArray[1] = findViewById(R.id.blueBtn);
        btnArray[2] = findViewById(R.id.greenBtn);
        btnArray[3] = findViewById(R.id.yellowBtn);
    }

    public void btnSetOnClickListener() {
        for(int i = 0; i < btnArray.length; i++) {
            btnArray[i].setOnClickListener(this);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void btnSetOnTouchListener() {
        for(int i = 0; i < btnArray.length; i++) {
            btnArray[i].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //view.getBackground().setColorFilter(getResources().getColor(R.color.background),PorterDuff.Mode.SRC_ATOP);
                            view.getBackground().setAlpha(128);
                            //view.invalidate();
                            break;
                        case MotionEvent.ACTION_UP:
                            //view.getBackground().clearColorFilter();
                            view.getBackground().setAlpha(255);
                            view.performClick();
                            //view.invalidate();
                            break;
                    }
                    return true;
                }
            });
        }
    }


    public void configureAnimation() {
        ObjectAnimator animation = ObjectAnimator.ofFloat(btnArray[this.getRandom()], "alpha", 0.3f);
        animation.setRepeatCount(1);
        animation.setRepeatMode(ValueAnimator.REVERSE);
        animation.setDuration(300);
        animList.add(animation);
    }

    public  void showToast() {
        Toast toast = Toast.makeText(this, "Press Start", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER | Gravity.TOP, 0, 50);
        toast.show();
    }

    public void startGame() {
        Button start = findViewById(R.id.startBtn);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGame();
                animateBtn();
                round = round + 1;
                isGameStarted = true;
            }
        });
    }

    public void animateBtn() {
        this.setRandom(rand.nextInt(4));
        configureAnimation();

        AnimatorSet animSet = new AnimatorSet();
        animSet.playSequentially(animList);

        // addListener to avoid clicking buttons as long animation is running
        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                for(int i = 0; i < btnArray.length; i++) {
                    btnArray[i].setEnabled(false);
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                for(int i = 0; i < btnArray.length; i++) {
                    btnArray[i].setEnabled(true);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animSet.start();
    }

    public void newHighscore() {
        if(this.getActScore() >= this.getHighScore()) {
            Toast toast = Toast.makeText(this, "New Best!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER | Gravity.TOP, 0, 50);
            toast.show();
            this.setHighScore(this.getActScore());
        }
    }

    public void printHighscore() {
        gameInfo.setText(String.format("%s %s", "Your Best", String.valueOf(this.getHighScore())));
    }

    public void resetGame() {
        this.setCounter(0);
        this.setRound(0);
        this.setActScore(0);
        this.setIsGameStarted(false);
        animList.clear();
    }

    public void setIsGameStarted(boolean isGameStarted) {
        this.isGameStarted = isGameStarted;
    }

    public boolean getIsGameStarted() {
        return isGameStarted;
    }

    public void setRandom(int random) {
        this.random = random;
    }

    public int getRandom() {
        return random;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getRound() {
        return round;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getCounter() {
        return counter;
    }

    public void setActScore(int actScore) {
        this.actScore = actScore;
    }

    public int getActScore() {
        return actScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public int getHighScore() {
        return highScore;
    }
}