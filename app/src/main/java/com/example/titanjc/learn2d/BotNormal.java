package com.example.titanjc.learn2d;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by TITANJC on 11/23/2016.
 */

public class BotNormal extends Enemy {
    private int MAX_HITS = 1;
    private int MIN_FIRE_RATE = 3;
    private int MAX_FIRE_RATE = 10;
    private int SCORE_VALUE = 50;
    private Random r = new Random();
    private int firingRate;
    private long lastTime;
    private long currentTime;

    private int ID;
    public int getID() {return ID;}
    public void setID(int ID) {this.ID = ID;}
    public int getMinFireRate() {return MIN_FIRE_RATE;}
    public int getMaxFireRate() {return MAX_FIRE_RATE;}
    public Rect getRectangle() {return super.rectangle;}

    public BotNormal(Bitmap idleImg, Bitmap moveR, Bitmap moveL, Rect rectangle) {
        super(idleImg, moveR, moveL, rectangle);
        super.hits = 0;
        super.scoreValue = SCORE_VALUE;
        this.firingRate = (r.nextInt(this.MAX_FIRE_RATE - this.MIN_FIRE_RATE)) + this.MIN_FIRE_RATE;
        this.lastTime = System.currentTimeMillis();
        this.currentTime = System.currentTimeMillis();
    }

    @Override
    public void move() {
        super.move();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void update(Point point) {
        super.update(point);
    }

    @Override
    public BotLaser fire() {
        return super.fire();
        //TODO: Spawn flash animation for firing as well as laser object starting from bottom of rectangle
    }

    @Override
    public boolean shouldFire() {
        this.currentTime = System.currentTimeMillis();
        boolean fire;
        if ((int) (currentTime/1000) - (int) (lastTime/1000) >= firingRate && super.rectangle.bottom >= Constants.MOVE_FLOOR) {
            fire = true;
            this.lastTime = System.currentTimeMillis();
        } else {
            fire = false;
        }
        return fire;
    }

    @Override
    public boolean playerCollide(RectPlayer player) {
        return super.playerCollide(player);
    }

    @Override
    public void laserHit() {
        super.hits++;
    }

    public boolean isDead() {
        return (hits >= MAX_HITS);
    }
}
