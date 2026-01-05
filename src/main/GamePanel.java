package main;

/* =========================
   Imports
   ========================= */
import inputs.MouseInputs;
import inputs.KeyBoardInputs;


import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;


public class GamePanel extends JPanel {
    private MouseInputs mouseInputs;
    private Game game;

    public GamePanel(Game game) {

        mouseInputs = new MouseInputs(this);
        this.game = game;

        setPanelSize();
        addKeyListener(new KeyBoardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    private void setPanelSize() {
        setPreferredSize(new Dimension(1280, 800));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.render(g);
    }

    public void updateGame(){

    }
    public Game getGame(){
        return game;
    }
}
