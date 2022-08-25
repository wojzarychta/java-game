package map;

import entities.Player;
import main.GamePanel;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Map {

    //WORLD SETTINGS
    static public final int worldSizeHor = 36;   //zparametryzować !!!
    static public final int worldSizeVer = 27;
    static public final int worldWidth = worldSizeHor * GamePanel.tileSize;     // world width in pixels
    static public final int worldHeight = worldSizeVer * GamePanel.tileSize;    // world height in pixels
    public static List<int[][]> tileLayerMap = new ArrayList<>();
    static public Tile[] tiles;

    public Map() {

        tiles = TileManager.getTiles();

        loadMap("/res/map/map_layer1.txt");  //layer 1
        loadMap("/res/map/map_layer2.txt");  //layer 2
    }

    public void loadMap(String mapPath) {       // returns matrix of numbers of tiles used for map creation
        int[][] tileMap = new int[worldSizeHor][worldSizeVer];
        InputStream is = getClass().getResourceAsStream(mapPath);
        assert is != null;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        try {
            for (int i = 0; i < worldSizeVer; i++) {

                String line = br.readLine();
                String[] numbers = line.split("\t");

                for (int j = 0; j < worldSizeHor; j++) {
                    int num = Integer.parseInt(numbers[j]);
                    tileMap[j][i] = num;
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tileLayerMap.add(tileMap);
    }

    public void drawMap(Graphics2D g, Player p) {
        int tileNum = 0;

        Point worldPos = new Point();
        Point screenPos = new Point();

        int screenWidth, screenHeight;
        screenWidth = (int) (Math.ceil((double) GamePanel.screenWidth/GamePanel.tileSize) * GamePanel.tileSize);
        screenHeight = (int) (Math.ceil((double)GamePanel.screenHeight/GamePanel.tileSize) * GamePanel.tileSize);

        for (int i = 0; i < worldSizeVer; i++) {
            for (int j = 0; j < worldSizeHor; j++) {

                worldPos.x = j * GamePanel.tileSize;
                worldPos.y = i * GamePanel.tileSize;


                if (worldPos.x > p.getWorldPosition().x - screenWidth && worldPos.x < p.getWorldPosition().x + screenWidth &&
                        worldPos.y > p.getWorldPosition().y - screenHeight && worldPos.y < p.getWorldPosition().y + screenHeight) {
                    screenPos.x = worldPos.x - p.getWorldPosition().x + p.getScreenPosition().x;
                    screenPos.y = worldPos.y - p.getWorldPosition().y + p.getScreenPosition().y;

                    // handling corners of the map
                    if (p.getWorldPosition().x < GamePanel.screenCenter.x)
                        screenPos.x = worldPos.x;
                    if (p.getWorldPosition().y < GamePanel.screenCenter.y)
                        screenPos.y = worldPos.y;
                    if (GamePanel.screenWidth - GamePanel.screenCenter.x > worldWidth - p.getWorldPosition().x)
                        screenPos.x = GamePanel.screenWidth - (worldWidth - worldPos.x);
                    if (GamePanel.screenHeight - GamePanel.screenCenter.y > worldHeight - p.getWorldPosition().y)
                        screenPos.y = GamePanel.screenHeight - (worldHeight - worldPos.y);

                    for (int[][] ints : tileLayerMap) {
                        tileNum = ints[j][i];
                        if (tileNum != -1)
                            g.drawImage(tiles[tileNum].image, screenPos.x, screenPos.y, null);
                    }

                }
            }
        }

    }


}
