package games;

import util.UtilityClass;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static main.GamePanel.scaling;

public class CoinFlip {
    BufferedImage[] animationFrames;

    private int currentSide = 0;
    private int frameCounter = 0;
    private int currentFrameNum = 0;
    private BufferedImage currentFrame;
    private boolean animationEnd = false;

    public CoinFlip() {
        animationFrames = loadAnimationFrames("res/coin_flip_animation");
    }

    private BufferedImage[] loadAnimationFrames(String path) {
        BufferedImage[] imgs;

        // LOAD TILES
        File folder = new File("src/" + path);

        String[] fileNames = folder.list();
        assert fileNames != null;

        int imgsCount = fileNames.length;

        imgs = new BufferedImage[imgsCount];

        Graphics2D g;
        BufferedImage originalImage;

        for (int i = 0; i < imgsCount; ++i) {
            try {
                originalImage = ImageIO.read(Objects.requireNonNull(UtilityClass.class.getResourceAsStream("/" + path + "/" + fileNames[i])));  // !! bez src/ na początku
                // scaling images
                BufferedImage scaledImage = new BufferedImage((int) (originalImage.getWidth() * scaling/2), (int) (originalImage.getHeight() * scaling/2), BufferedImage.TYPE_INT_ARGB);
                g = scaledImage.createGraphics();
                g.drawImage(originalImage, 0, 0, scaledImage.getWidth(), scaledImage.getHeight(), null);
                imgs[i] = scaledImage;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imgs;
    }

    private BufferedImage getCurrentFrame(HeadOrTails targetSide) { // targetSide being HEADS or TAILS
        int animationFreq = 6;
        final int frames = 7;

        BufferedImage currentFrame = null;

        if (currentFrameNum == frames - 1) {
            currentFrame = animationFrames[targetSide.getValue()];
            animationEnd = true;
        }
        else
            currentFrame = animationFrames[currentFrameNum];

        frameCounter++;

        if (frameCounter > animationFreq) {
            if (currentFrameNum < frames-1)
                currentFrameNum++;
            else
                currentFrameNum = 0;
            frameCounter = 0;
        }

        return currentFrame;
    }

    public void animateCoinFlip(Point pos, Graphics2D g, HeadOrTails targetSide) {
        // pos is position on screen where coin will be located
        g.drawImage(getCurrentFrame(targetSide), pos.x, pos.y, animationFrames[0].getWidth(), animationFrames[0].getHeight(), null);
    }


    public void resetAnimation() {
        frameCounter = 0;
        currentFrameNum = 0;
        animationEnd = false;
    }

    public enum HeadOrTails {
        HEADS(6), TAILS(7);

        private int value;

        HeadOrTails(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public void displayBlankCoin(Point pos, Graphics2D g){
        g.drawImage(animationFrames[0], pos.x, pos.y, animationFrames[0].getWidth(), animationFrames[0].getHeight(), null);
    }

    public void displayFinalFrame(Point pos, Graphics2D g, HeadOrTails targetSide) {
        // pos is position on screen where coin will be located
        g.drawImage(animationFrames[targetSide.getValue()], pos.x, pos.y, animationFrames[0].getWidth(), animationFrames[0].getHeight(), null);
    }

    public Dimension getCoinDimension(){
        return new Dimension(animationFrames[0].getWidth(), animationFrames[0].getHeight());
    }

    public boolean isAnimationEnd() {
        return animationEnd;
    }

    public void setAnimationEnd(boolean animationEnd) {
        this.animationEnd = animationEnd;
    }
}
