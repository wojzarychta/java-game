package venues;

import entities.Player;
import gamestates.GameStateMethods;
import gamestates.PlayState;
import main.GameLoop;
import main.GamePanel;
import ui.SmallButton;
import ui.Sound;
import util.UtilityClass;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import static main.GamePanel.*;
import static ui.SmallButton.Function.EXIT;
import static ui.SmallButton.Function.QUESTION_MARK;

public abstract class Venue implements GameStateMethods, Serializable {
    protected Point location;
    protected Player player;

    protected transient boolean isClose = false;
    //protected VenueState venueState = new VenueState();
    protected Point windowPos;
    protected int windowWidth;
    protected int windowHeight;
    protected transient boolean isActive = false;

    protected transient SmallButton exitButton, helpButton;
    protected transient boolean isOver;
    protected  transient boolean helpOn;

    public Venue(Point location, Player player) {
        this.location = location;
        this.player = player;
        windowPos = new Point(tileSize * 3 / 2, tileSize * 2);
        windowWidth = GamePanel.screenWidth - 3 * tileSize;
        windowHeight = GamePanel.screenHeight - (int) (2.5f * tileSize);
    }

    public Venue(Point location, PlayState playState) {
        this.location = location;
        this.player = playState.getPlayer();
//        exitButton = new smallButton(new Point(windowPos.x+windowWidth+tileSize/2, windowPos.y+35), EXIT){
//            @Override
//            public void performAction() {
//                exit();
//            }
//        };
    }

    protected abstract void nextTurn(int turn);

    protected abstract void setupButtons();

    protected void setupExitButton(Point pos) {
        exitButton = new SmallButton(pos, EXIT) {
            @Override
            public void performAction() {
                exit();
            }
        };
    }

    protected void setupHelpButton(Point pos) {
        helpButton = new SmallButton(pos, QUESTION_MARK) {
            @Override
            public void performAction() {
                helpOn = !helpOn;
            }
        };
    }

    protected abstract void help(Graphics2D g);

    private void getPrompt(Graphics2D g, String prompt) {
        Point screenPos = calculateScreenPos();
        // display prompt img
        BufferedImage promptImg = UtilityClass.loadPromptKeyImg("F");
        g.drawImage(promptImg, screenPos.x + tileSize / 2 - promptImg.getWidth() / 2, screenPos.y, null);
        //display prompt text
        Font font = new Font("Kenney Pixel", Font.PLAIN, (int) (7.5f*scaling));
        g.setFont(font);
        g.setColor(Color.white);
        Point p = UtilityClass.centerText(g, prompt, new Point(screenPos.x + tileSize / 2, screenPos.y + tileSize / 2 + g.getFontMetrics().getHeight() / 2));
        g.drawString(prompt, p.x, p.y);
    }

    public void displayPrompt(Graphics2D g, String prompt) {
        if (calculateDistance() < (int) 2.5 * tileSize) {
            getPrompt(g, prompt);
        }
    }

    public void draw(Graphics2D g) {
        createWindow(g);
        exitButton.draw(g);
        helpButton.draw(g);

    }

    public abstract void drawPrompt(Graphics2D g);

    public void update() {
        if (!isActive)
            checkIfClose();
        else {
            exitButton.update();
            helpButton.update();
        }

    }

    public abstract void reset();

    protected int calculateDistance() {
        int deltaX = player.getWorldPosition().x - location.x;
        int deltaY = player.getWorldPosition().y - location.y;
        return (int) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
    }

    protected Point calculateScreenPos() {
        Point screenPos = new Point();
        screenPos.x = location.x - player.getWorldPosition().x + player.getScreenPosition().x;
        screenPos.y = location.y - player.getWorldPosition().y + player.getScreenPosition().y;
        //System.out.println(screenPos);
        return screenPos;
    }

    protected void enter() {
        isActive = true;
        GameLoop.playSE(Sound.ENTER_SE);
    }

    public boolean getIsClose() {
        return isClose;
    }

    protected void checkIfClose() {
        isClose = calculateDistance() < 1.5 * tileSize;
    }

    //protected VenueState.VenueSubType setSubState();

    public void createWindow(Graphics2D g) {
        drawWindow(windowPos, windowWidth, windowHeight, g);
    }

    protected void drawWindow(Point pos, int width, int height, Graphics2D g) {
        Color c = new Color(39, 45, 70);
        g.setColor(c);
        g.fillRoundRect(pos.x, pos.y, width, height, (int) (9*scaling), (int) (9*scaling));

        c = new Color(58, 68, 102);
        g.setColor(c);
        g.setStroke(new BasicStroke(5));
        g.drawRoundRect((int) (pos.x + 1.5f*scaling), (int) (pos.y + 1.5f*scaling), (int) (width - 3*scaling), (int) (height - 3*scaling), (int) (6*scaling), (int) (6*scaling));
    }

    public boolean isOver() {
        return isOver;
    }

    public void setOver(boolean over) {
        isOver = over;
    }

    public boolean exit() {
        isOver = true;
        isActive = false;
        helpOn = false;
        helpButton.setMousePressed(false);
        return true;
    }

    public abstract void loadObject(Venue v);
}
