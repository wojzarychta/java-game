package util;

import main.GamePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

import static main.GamePanel.tileSize;

public class UtilityClass {

    public static void loadFile() {

    }

    public static BufferedImage[] loadImages(String path) {   // provide path inside src folder
        BufferedImage[] imgs;

        // LOAD TILES
        File folder = new File("src/" + path);

        String[] fileNames = folder.list();
        assert fileNames != null;
        Arrays.sort(fileNames, new Comparator<String>() {   // sort array so that e.g. tile1000 is not before tile101
            public int compare(String o1, String o2) {
                return extractInt(o1) - extractInt(o2);
            }

            int extractInt(String s) {
                String num = s.replaceAll("\\D", "");
                // return 0 if no digits found
                return num.isEmpty() ? 0 : Integer.parseInt(num);
            }
        });

        try {
            System.out.println(folder.getCanonicalPath());
            System.out.println(folder.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }


        int imgsCount = fileNames.length;

        imgs = new BufferedImage[imgsCount];

        Graphics2D g;
        BufferedImage originalImage;

        for (int i = 0; i < imgsCount; ++i) {
            //System.out.println("res/tiles/" + tileFolder.getName() + "/" + tileNames[i]);
            try {
                originalImage = ImageIO.read(Objects.requireNonNull(UtilityClass.class.getResourceAsStream("/" + path + "/" + fileNames[i])));  // !! bez src/ na początku
                // scaling images
                BufferedImage scaledImage = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
                g = scaledImage.createGraphics();
                g.drawImage(originalImage, 0, 0, tileSize, tileSize, null);
                imgs[i] = scaledImage;

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return imgs;
    }

    public static int[][] loadTextFile(String path, int width, int height) {       // returns matrix of numbers of tiles used for map creation
        int[][] tileMap = new int[width][height];
        InputStream is = UtilityClass.class.getResourceAsStream(path);
        assert is != null;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        try {
            for (int i = 0; i < width; i++) {

                String line = br.readLine();
                String[] numbers = line.split("\t");

                for (int j = 0; j < height; j++) {
                    int num = Integer.parseInt(numbers[j]);
                    tileMap[j][i] = num;
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tileMap;
    }

    public static BufferedImage loadSpriteAtlas(String path) {
        BufferedImage img = null;
        InputStream is = UtilityClass.class.getResourceAsStream(path);
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

    public static BufferedImage loadPromptKeyImg(String key) {
        int promptSize = tileSize / 2;
        BufferedImage promptImg = new BufferedImage(promptSize, promptSize, BufferedImage.TYPE_INT_ARGB);
        BufferedImage atlas = loadSpriteAtlas("/res/prompts.png");
        BufferedImage img;
        Graphics2D g = promptImg.createGraphics();
        switch (key) {
            case "F" -> {
                img = atlas.getSubimage(21 * GamePanel.defaultTileSize, 3 * GamePanel.defaultTileSize, GamePanel.defaultTileSize, GamePanel.defaultTileSize);
            }
            default -> img = null;
        }
        g.drawImage(img, 0, 0, promptSize, promptSize, null);

        return promptImg;
    }

    public static Point centerText(Graphics2D g, String s, Point p){
        FontMetrics fm = g.getFontMetrics();
        return new Point(p.x-fm.stringWidth(s)/2, p.y);
    }
    public static Point rightAlign(Graphics2D g, String s, Point p){
        FontMetrics fm = g.getFontMetrics();
        return new Point(p.x-fm.stringWidth(s), p.y);
    }

    public static void drawWrappedText(Graphics g, String text, int x, int y, int w, int h) {
        JTextArea ta = new JTextArea(text);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setBounds(0, 0, w, h);
        ta.setForeground(g.getColor());
        ta.setBackground(new Color(0,0,0,0));
        ta.setFont(g.getFont());
        Graphics g2 = g.create(x, y, w, h); // Use new graphics to leave original graphics state unchanged
        ta.paint(g2);
    }

    public static void registerFont(String path){
        try {
            GraphicsEnvironment ge =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
//            String [] fonts1 = ge.getAvailableFontFamilyNames();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(path)));
//            String [] fonts = ge.getAvailableFontFamilyNames();
//            int i = 0;
//            int index = 0;
//            boolean b = false;
////            for (String s: fonts1)
////                System.out.println(s);
//            for (String s: fonts) {
//                for (String s1 : fonts1)
//                    if (s.equals(s1))
//                        b=true;
//                if (!b)
//                    System.out.println(s);
//                b=false;
//            }
        } catch (IOException|FontFormatException e) {
            e.printStackTrace();
        }
    }

    public static String formatMoney(int money){
        String s = "";
        if (money >= 1e9)
            s = String.format("%.3f", money / 1e9) + "B";
        else if (money >= 1e6)
            s = String.format("%.3f", money / 1e6) + "M";
        else if (money >= 1e3)
            s = String.format("%.3f", money / 1e3) + "K";
        else
            s = String.valueOf(money);
        return s;
    }

    public static BufferedImage loadImage(String path){
        Graphics2D g;
        BufferedImage originalImage;
        try {
            originalImage = ImageIO.read(Objects.requireNonNull(UtilityClass.class.getResourceAsStream(path)));
            // scaling images
            BufferedImage scaledImage = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
            g = scaledImage.createGraphics();
            g.drawImage(originalImage, 0, 0, tileSize, tileSize, null);
            return scaledImage;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage scaleImage(BufferedImage img){
        Graphics2D g;
        // scaling images
        BufferedImage scaledImage = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
        g = scaledImage.createGraphics();
        g.drawImage(img, 0, 0, tileSize, tileSize, null);
        return scaledImage;
    }
}
