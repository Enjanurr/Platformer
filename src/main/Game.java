package main;

import entities.Player;
import levels.LevelManager;

import java.awt.*;

public class Game implements Runnable {

    // Window and rendering panel
    private GameWindow gameWindow;
    private GamePanel gamePanel;

    // Separate thread running the game loop
    private Thread gameThread;

    // Target frame rate and update rate
    private final int FPS_SET = 120;
    private final int UP_SET = 200;

    // Core game objects
    private Player player;
    private LevelManager levelManager;

    /* =========================
       Game world / screen sizing
       ========================= */

    // Base tile size before scaling
    public static final int TILES_DEFAULT_SIZE = 32;

    // Global scale factor for the entire game
    public static final float SCALE = 1f;

    // Number of tiles visible on screen
    public static final int TILES_IN_WIDTH = 26;
    public static final int TILES_IN_HEIGTH = 14;

    // Final tile size after applying scale
    public static final int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);

    // Final game resolution derived from tile grid
    public static final int GAME_WIDTH  = TILES_SIZE * TILES_IN_WIDTH;
    public static final int GAME_HEIGTH = TILES_SIZE * TILES_IN_HEIGTH;

    public Game(){
        // Initialize all game objects
        initClasses();

        // Create rendering panel and window
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);

        // Ensure keyboard input is received
        gamePanel.requestFocus();

        // Start the main game loop
        startGameLoop();
    }

    // Creates core game objects (player, level, etc.)
    private void initClasses() {
        player = new Player(
                200,
                200,
                (int) (64 * SCALE),
                (int) (40 * SCALE)
        );
        levelManager = new LevelManager(this);
    }

    // Starts the game loop in a separate thread
    public void startGameLoop(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    // Updates all game logic (called UPS times per second)
    public void update(){
        player.update();
        levelManager.update();
    }

    // Renders all game objects to the screen
    public void render(Graphics g){
        player.render(g);
        levelManager.draw(g);
    }

    @Override
    public void run() {

        // Time allowed per frame and per update (nanoseconds)
        double timePerFrame  = 1_000_000_000.0 / FPS_SET;
        double timePerUpdate = 1_000_000_000.0 / UP_SET;

        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        // Main game loop
        while(true){

            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            // Update game logic at fixed UPS
            if(deltaU >= 1){
                update();
                updates++;
                deltaU--;
            }

            // Render frames at target FPS
            if(deltaF >= 1){
                gamePanel.repaint();
                frames++;
                deltaF--;
            }

            // Debug output for FPS and UPS
            if(System.currentTimeMillis() - lastCheck >= 1000){
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
            }
        }
    }

    // Resets player input state when the game window loses focus
    public void windowFocusLost(){
        player.resetDirBooleans();
    }

    // Allows other classes to access the player instance
    public Player getPlayer(){
        return player;
    }
}
