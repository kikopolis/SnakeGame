package com.kikopolis.entity;

import com.kikopolis.movement.Direction;

import static com.kikopolis.config.PanelConfig.GAME_UNITS;

public class Snake {
    private Direction direction;
    private int[] x;
    private int[] y;
    private int bodyPartCount;
    private int applesEaten;
    
    public Snake() {
        super();
        direction = Direction.RIGHT;
        final int gameUnits = GAME_UNITS.getValue();
        x = new int[gameUnits];
        y = new int[gameUnits];
        bodyPartCount = 6;
        applesEaten = 0;
    }
    
    public Direction getDirection() {
        return direction;
    }
    
    public void setDirection(final Direction newDirection) {
        direction = newDirection;
    }
    
    public int[] getX() {
        return x;
    }
    
    public void setX(final int[] newX) {
        x = newX;
    }
    
    public int[] getY() {
        return y;
    }
    
    public void setY(final int[] newY) {
        y = newY;
    }
    
    public int getBodyPartCount() {
        return bodyPartCount;
    }
    
    public void setBodyPartCount(final int newBodyPartCount) {
        bodyPartCount = newBodyPartCount;
    }
    
    public int getApplesEaten() {
        return applesEaten;
    }
    
    public void setApplesEaten(final int newApplesEaten) {
        applesEaten = newApplesEaten;
    }
}
