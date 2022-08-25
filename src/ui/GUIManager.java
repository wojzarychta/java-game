package ui;

import main.GamePanel;
import map.Tile;
import map.TileManager;
import util.UtilityClass;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class GUIManager {

//    private BufferedImage [] guiTiles;

    public GUIManager(){
//        guiTiles = UtilityClass.loadImages("res/gui");
    }

//    public BufferedImage[] getGuiTiles() {
//        return guiTiles;
//    }

    //    public void loadGUI(){
//
//        // LOAD TILES
//        File folder = new File("src/res/gui");
//
//        String[] tileNames = folder.list();
//        assert tileNames != null;
//        Arrays.sort(tileNames, new Comparator<String>() {   // sort array so that e.g. tile1000 is not before tile101
//            public int compare(String o1, String o2) {
//                return extractInt(o1) - extractInt(o2);
//            }
//
//            int extractInt(String s) {
//                String num = s.replaceAll("\\D", "");
//                // return 0 if no digits found
//                return num.isEmpty() ? 0 : Integer.parseInt(num);
//            }
//        });
//
//        int tileCount = tileNames.length;
//
//        guiTiles = new BufferedImage[tileCount];
//
//        Graphics2D g;
//        BufferedImage originalImage;
//
//        for (int i = 0; i < tileCount; ++i) {
//            //System.out.println("res/tiles/" + tileFolder.getName() + "/" + tileNames[i]);
//            try {
//                originalImage = ImageIO.read(Objects.requireNonNull(TileManager.class.getResourceAsStream("/res/gui/" + tileNames[i])));
//                // scaling images
//                BufferedImage scaledImage = new BufferedImage(GamePanel.tileSize, GamePanel.tileSize, BufferedImage.TYPE_INT_ARGB);
//                g = scaledImage.createGraphics();
//                g.drawImage(originalImage, 0, 0, GamePanel.tileSize, GamePanel.tileSize, null);
//                guiTiles[i] = scaledImage;
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }
//
//    public BufferedImage[] getGuiTiles(){
//        return guiTiles;
//    }
}
