package com.kikopolis.config;

public enum PanelConfig {
    SCREEN_WIDTH(800),
    SCREEN_HEIGHT(800),
    UNIT_SIZE(25),
    GAME_UNITS(SCREEN_WIDTH.getValue() * SCREEN_HEIGHT.getValue() / UNIT_SIZE.getValue());
    
    private final int value;
    
    PanelConfig(final int i) {
        value = i;
    }
    
    public int getValue() {
        return value;
    }
}
