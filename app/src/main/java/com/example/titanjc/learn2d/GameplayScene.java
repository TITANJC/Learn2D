package com.example.titanjc.learn2d;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * Created by TITANJC on 11/21/2016.
 */

public class GameplayScene implements Scene {
    private Rect r;

    private RectPlayer player;
    private Point playerPoint;
    private BotManager botManager;
    private HighScoreHandler scoreHandler;

    private int currentScore;
    private Paint scorePaint;

    private boolean gameOver;
    private long gameOverTime;

    private OrientationData orientationData;
    private long frameTime;

    public GameplayScene() {
        player = new RectPlayer(new Rect(110, 110, 210, 210));
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, 3*Constants.SCREEN_HEIGHT/4);
        player.update(playerPoint);
        r = new Rect();

        scorePaint = new Paint();

        botManager = new BotManager();
        orientationData = new OrientationData();
        orientationData.register();
        frameTime = System.currentTimeMillis();
        currentScore = botManager.getScore();

        scoreHandler = new HighScoreHandler();
    }

    @Override
    public void update() {
        if (!gameOver) {
            if(frameTime < Constants.INIT_TIME)
                frameTime = Constants.INIT_TIME;
            int elapsedTime = (int) (System.currentTimeMillis() - frameTime);
            frameTime = System.currentTimeMillis();
            if (orientationData.getOrientation() != null && orientationData.getStartOrientation() != null) {
                float pitch = orientationData.getOrientation()[1] - orientationData.getStartOrientation()[1];
                float roll = orientationData.getOrientation()[2] - orientationData.getStartOrientation()[2];

                float xSpeed = 2 * roll * Constants.SCREEN_WIDTH/1500f;
                float ySpeed = pitch * Constants.SCREEN_HEIGHT/1000f;

                playerPoint.x += Math.abs(xSpeed*elapsedTime) > 5 ? xSpeed*elapsedTime : 0;
                playerPoint.y -= Math.abs(ySpeed*elapsedTime) > 5 ? ySpeed*elapsedTime : 0;
            }

            if (playerPoint.x < 0)
                playerPoint.x = 0;
            else if(playerPoint.x > Constants.SCREEN_WIDTH)
                playerPoint.x = Constants.SCREEN_WIDTH;

            if (playerPoint.y < 0)
                playerPoint.y = 0;
            else if(playerPoint.y > Constants.SCREEN_HEIGHT)
                playerPoint.y = Constants.SCREEN_HEIGHT;

            player.update(playerPoint);
            botManager.update();
            currentScore = botManager.getScore();
            if (botManager.playerCollide(player) || botManager.playerShot(player)) {
                gameOver = true;
                gameOverTime = System.currentTimeMillis();
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        Drawable bg = ContextCompat.getDrawable( Constants.CURRENT_CONTEXT, R.drawable.star_background);
        bg.setBounds(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        bg.draw(canvas);

        player.draw(canvas);
        botManager.draw(canvas);

        scorePaint.setTextSize(100);
        scorePaint.setColor(Color.MAGENTA);
        canvas.drawText("" + currentScore, 50, 50 + scorePaint.descent() - scorePaint.ascent(), scorePaint);

        if (gameOver) {
            Paint paint = new Paint();
            paint.setTextSize(100);
            paint.setColor(Color.GREEN);
            drawCenterText(canvas, paint, "Game Over");
        }
    }

    public void reset() {
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, 3*Constants.SCREEN_HEIGHT/4);
        player.update(playerPoint);
        botManager = new BotManager();
        orientationData = new OrientationData();
        orientationData.register();
    }

    @Override
    public void terminate(String scene) {
        SceneManager.changeScene(scene);
    }

    @Override
    public void recieveTouch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if(!gameOver ) {
                    botManager.playerFired(player);
                }

                if (gameOver && System.currentTimeMillis() - gameOverTime >= 2000) {
                    if(scoreHandler.checkHighScores(currentScore)) {
                        //TODO: display screen if score is high enough to make it onto High Score
                    }
                    reset();
                    gameOver = false;
                    orientationData.newGame();
                    terminate("MainMenuScene");
                }
        }
    }

    private void drawCenterText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }
}
