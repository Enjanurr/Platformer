package levels;

import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LevelManager {

    // Reference to the main Game class (for constants like tile size)
    private Game game;

    // Holds all individual tile sprites sliced from the level atlas
    private BufferedImage[] levelSprite;

    // Represents the current level data (tile layout)
    private Level levelOne;

    public LevelManager(Game game){
        this.game = game;

        // Load and slice the level tile sprites from the atlas
        importOutsideSprites();

        // Load level layout data (2D array based on color values)
        levelOne = new Level(LoadSave.GetLevelData());
    }

    // Loads the level sprite atlas and cuts it into individual 32x32 tiles
    private void importOutsideSprites() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS); // get sprites from Loadsave

        // Total number of tiles in the atlas (4 rows × 12 columns)
        levelSprite = new BufferedImage[48];

        for (int j = 0 ; j < 4 ; j++){
            for(int i = 0 ; i < 12 ; i++){
                int index = j * 12 + i;

                // Extract a single tile from the atlas
                levelSprite[index] = img.getSubimage(
                        i * 32,
                        j * 32,
                        32,
                        32
                );
            }
        }
    }

    // Draws the level by mapping tile indices to sprites and rendering them on screen
    public void draw(Graphics g){
        for(int j = 0 ; j < Game.TILES_IN_HEIGTH; j++){
            for(int i = 0 ; i < Game.TILES_IN_WIDTH; i++ ){

                // Get which tile sprite should be drawn at this grid position
                int index = levelOne.getSpriteIndex(i, j);

                // Draw the tile at the correct scaled world position
                g.drawImage(
                        levelSprite[index],
                        Game.TILES_SIZE * i,
                        Game.TILES_SIZE * j,
                        Game.TILES_SIZE,
                        Game.TILES_SIZE,
                        null
                );
            }
        }
    }

    // Update logic for the level (unused for now, added for future extensions)
    public void update(){
    }
}
