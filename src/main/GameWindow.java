package main;

import util.UtilityClass;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Currency;
import java.util.Objects;

public class GameWindow extends JFrame {

    public static final String gameName = "Tycoon RPG";
    private Cursor cursor;
    private boolean fullScreenOn = true;

    GameWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle(gameName);

        ImageIcon icon = new ImageIcon("src/res/icons/bag.png");
        setIconImage(icon.getImage());

        UtilityClass.registerFont("src/res/Kenney Pixel.ttf");

        GamePanel gamePanel = new GamePanel(this);
        add(gamePanel);

        //load config
        //

        //if (!fullScreenOn)
        pack();

        setCursorImage();

        setLocationRelativeTo(null);

        setVisible(true);
    }

    private void setCursorImage(){
        BufferedImage image;
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/cursor/glove3.png")));
            cursor = Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(0, 0), "customCursor");
            setCursor(cursor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

