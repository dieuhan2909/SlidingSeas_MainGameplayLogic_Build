package io.SlidingSeasCore;

import com.badlogic.gdx.Game;
import managers.ScreenManager;

public class Main extends Game {
    private ScreenManager screenManager;

    @Override
    public void create() {
        // Initialize screen manager
        screenManager = new ScreenManager(this);

        // Show the first screen
        screenManager.showMainMenuScreen();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public ScreenManager getScreenManager() {
        return screenManager;
    }
}
