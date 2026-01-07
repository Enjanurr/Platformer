package utils;

import main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class LoadSave {
    public static final String PLAYER_ATLAS = "player_sprites.png";
    public static final String LEVEL_ATLAS = "outside_sprites.png";
    public static final String LEVEL_ONE_DATA = "level_one_data.png";

    // a functions that handles the imports of images
    public static BufferedImage GetSpriteAtlas(String fileName){
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
        try {
              img = ImageIO.read(is);


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }

    // this is for the level , normally we make a 2d array represented by numbers like 1 is wall ,
    // 0 is water and so on but we do it using color
    public static int[][] GetLevelData(){
        int[][] levelData = new int[Game.TILES_IN_HEIGTH][Game.TILES_IN_WIDTH];
        BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);

        for(int j = 0 ; j < img.getHeight();j++){
            for(int i = 0 ; i < img.getWidth();i++) {
                Color color = new Color(img.getRGB(i, j));
                int value  =  color.getRed();
                if (value>= 48){
                    value = 0;
                }
                levelData[j][i] = value;
            }
        }
        return levelData;
    }
}
