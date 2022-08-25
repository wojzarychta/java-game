package map;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class TileManager {

    public static Tile[] getTiles(){  // returns array of all tiles used in map creation

        Tile[] tiles;

        // LOAD TILES
        File tileFolder = new File("src/res/tiles/modern_city_pack");

        String[] tileNames = tileFolder.list();
        assert tileNames != null;
        Arrays.sort(tileNames, new Comparator<String>() {   // sort array so that e.g. tile1000 is not before tile101
            public int compare(String o1, String o2) {
                return extractInt(o1) - extractInt(o2);
            }

            int extractInt(String s) {
                String num = s.replaceAll("\\D", "");
                // return 0 if no digits found
                return num.isEmpty() ? 0 : Integer.parseInt(num);
            }
        });

        int tileCount = tileNames.length;

        tiles = new Tile[tileCount];

        Graphics2D g;
        BufferedImage originalImage;

        for (int i = 0; i < tileCount; ++i) {
            //System.out.println("res/tiles/" + tileFolder.getName() + "/" + tileNames[i]);
            tiles[i] = new Tile();
            try {
                originalImage = ImageIO.read(Objects.requireNonNull(TileManager.class.getResourceAsStream("/res/tiles/" + tileFolder.getName() + "/" + tileNames[i])));
                // scaling images
                BufferedImage scaledImage = new BufferedImage(GamePanel.tileSize, GamePanel.tileSize, BufferedImage.TYPE_INT_ARGB);
                g = scaledImage.createGraphics();
                g.drawImage(originalImage, 0, 0, GamePanel.tileSize, GamePanel.tileSize, null);
                tiles[i].image = scaledImage;

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        //SET COLLISION                // można zmienić format tego pliku na 'pionowy' !!!
        InputStream is = TileManager.class.getResourceAsStream("/res/tiles/collision.txt");
        assert is != null;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line;
        try {
            line = br.readLine();
            String[] collisionTiles = line.split(" ");
            for (String s: collisionTiles){
                tiles[Integer.parseInt(s)].collision = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tiles;
    }

}
