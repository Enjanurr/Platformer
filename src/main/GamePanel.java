package main;

/* =========================
   Imports
   ========================= */
import inputs.MouseInputs;
import inputs.KeyBoardInputs;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;

import static main.Game.GAME_HEIGTH;
import static main.Game.GAME_WIDTH;

public class GamePanel extends JPanel {

    // Handles all mouse input for this panel
    private MouseInputs mouseInputs;

    // Reference to the main Game class (for rendering and updates)
    private Game game;

    public GamePanel(Game game) {

        this.game = game;

        // Initialize mouse input listener
        mouseInputs = new MouseInputs(this);

        // Set panel size based on game constants
        setPanelSize();

        // Add keyboard and mouse listeners for input
        addKeyListener(new KeyBoardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    // Sets the size of the panel to match the game resolution
    private void setPanelSize() {
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGTH));

        // Debug output to ensure panel size matches game constants
        System.out.println("Size: " + GAME_WIDTH + " " + GAME_HEIGTH);
    }

    // Called automatically by Swing whenever the panel needs to be redrawn
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Delegate rendering to the main game object
        game.render(g);
    }

    // Placeholder for future update logic if needed
    public void updateGame(){

    }

    // Allows other classes (like input handlers) to access the game instance
    public Game getGame(){
        return game;
    }
}
