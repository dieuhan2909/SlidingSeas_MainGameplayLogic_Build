package managers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import data.LevelData;
import screens.AboutScreen;
import screens.GameScreen;
import screens.LevelSelectScreen;
import screens.MainMenuScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScreenManager {
    private final Game game;
    private final LevelManager levelManager;

    public ScreenManager(Game game) {
        this.game = game;
        this.levelManager = new LevelManager(createLevelData());
    }

    // Khởi tạo dữ liệu Level cho tất cả Game Mode
    private Map<String, List<LevelData>> createLevelData() {
        Map<String, List<LevelData>> levels = new HashMap<>();

        for (String mode : MainMenuScreen.modeNames) {
            List<LevelData> modeLevels = new ArrayList<>();

            // Kích thước khởi tạo tạm thời, có thể thay đổi sau
            modeLevels.add(new LevelData(1, 5, 5));
            modeLevels.add(new LevelData(2, 7, 7));
            modeLevels.add(new LevelData(3, 9, 9));

            levels.put(mode, modeLevels);
        }

        return levels;
    }

    // Switch to a new screen
    public void setScreen(Screen newScreen) {
        Screen oldScreen = game.getScreen();

        if (oldScreen == newScreen) return;

        game.setScreen(newScreen);

        if (oldScreen != null) oldScreen.dispose();
    }

    // Main menu
    public void showMainMenuScreen() {
        setScreen(new MainMenuScreen(this));
    }

    // Game Mode menu
    public void showGameModeMenu() {
        MainMenuScreen menu = new MainMenuScreen(this);
        setScreen(menu);
        menu.showGameModeMenu();
    }

    // Level selection
    public void showLevelSelectScreen(String gameMode) {
        setScreen(new LevelSelectScreen(this, levelManager, gameMode));
    }

    // Gameplay
    public void showGameScreen(String selectedMode, LevelData selectedLevel) {
        setScreen(new GameScreen(this, selectedMode, selectedLevel));
    }

    // About
    public void showAboutScreen() {
        setScreen(new AboutScreen(this));
    }

    // Dispose current screen
    public void disposeCurrentScreen() {
        Screen currentScreen = game.getScreen();

        if (currentScreen != null) currentScreen.dispose();
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }
}
