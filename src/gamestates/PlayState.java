package gamestates;

import entities.*;
import main.GameLoop;
import main.GamePanel;
import map.Map;
import objects.BigMoneyBag;
import objects.InteractiveObject;
import objects.MoneyBag;
import objects.MoneyCase;
import ui.Overlay;
import ui.Sound;
import util.GameConfiguration;
import venues.VenuesSetter;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.Serializable;

import static gamestates.PlayState.PlayStateType.*;
import static main.GamePanel.tileSize;

public class PlayState implements GameStateMethods, Serializable {

    private transient final Map map = new Map();
    private final Entity[] NPCs = new Entity[4];
    private Player player = new Player(this);
    private transient Overlay overlay = new Overlay(this);
    private VenuesSetter venues;
    private int turn = 1;
    private transient PlayStateType playSubState = RUN;
    private transient int counter = 0;
    private transient float opacityChange = 0.0005f;
    private transient float opacity = 1f;
    private transient InteractiveObject[] objects;


    public PlayState() {
        //player = new Player(this);
        NPCs[0] = new Merchant(new Point(23 * tileSize, 22 * tileSize), this);
        NPCs[1] = new NPC1(this, new Point(31 * tileSize, 21 * tileSize));
        NPCs[2] = new NPC2(this, new Point(2 * tileSize, 24 * tileSize));
        NPCs[3] = new NPC3(this, new Point(1 * tileSize, 9 * tileSize));
        venues = new VenuesSetter(this);
        objects = new InteractiveObject[3];
        objects[0] = new MoneyBag(player);
        objects[1] = new BigMoneyBag(player);
        objects[2] = new MoneyCase(player);
        //objects[0].placeOnMap();
        //overlay = new Overlay(this);
    }

    public static void setGameOver() {
        GameLoop.playSE(Sound.GAME_OVER);
        GameLoop.setGameState(GameLoop.gameStateType.GAME_OVER);
    }

    public void setPlaySubState(PlayStateType playSubState) {
        this.playSubState = playSubState;
        if (playSubState != RUN && playSubState != STATS) player.resetBooleans();
    }

    @Override
    public void update() {

        switch (playSubState) {

            case RUN, STATS -> {
                player.update();
                overlay.update();
                venues.update();
                for (Entity npc : NPCs)
                    npc.update();
                for (InteractiveObject io: objects)
                    io.update(objects);
            }
            case VENUE -> {
                overlay.update();
                venues.update();
            }
            case TRADE -> {
            }
            case TRANSITION -> {
                overlay.update();
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {

        switch (playSubState) {

            case RUN -> {
                map.drawMap(g, player);
                for (InteractiveObject io: objects)
                    io.draw(g);
                venues.drawPrompts(g);
                for (Entity npc : NPCs)
                    npc.paint(g);
                player.paint(g);
                overlay.draw(g);
            }
            case STATS -> {
                map.drawMap(g, player);
                for (InteractiveObject io: objects)
                    io.draw(g);
                venues.drawPrompts(g);
                for (Entity npc : NPCs)
                    npc.paint(g);
                player.paint(g);
                overlay.draw(g);
                player.getStats().showStats(g);
            }
            case VENUE -> {
                map.drawMap(g, player);
                for (InteractiveObject io: objects)
                    io.draw(g);
                for (Entity npc : NPCs)
                    npc.paint(g);
                player.paint(g);
                overlay.draw(g);
                venues.draw(g);
            }
            case TRADE -> {
                map.drawMap(g, player);
                venues.drawPrompts(g);
                player.paint(g);
                overlay.draw(g);
                for (Entity npc : NPCs)
                    npc.paint(g);
            }
            case TRANSITION -> {
                map.drawMap(g, player);
                for (InteractiveObject io: objects)
                    io.draw(g);
                venues.drawPrompts(g);
                for (Entity npc : NPCs)
                    npc.paint(g);
                player.paint(g);
                overlay.draw(g);
                drawTransition(g);
            }
        }

    }

    public void nextTurn() {
        venues.nextTurn(turn);
        player.nextTurn(turn);
        turn++;
        setPlaySubState(TRANSITION);
        for (InteractiveObject io: objects)
            io.nextTurn();
        GameConfiguration.serializeGame();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (playSubState) {
            case RUN -> {
                switch (keyCode) {
                    case KeyEvent.VK_A -> player.setLeft(true);
                    case KeyEvent.VK_D -> player.setRight(true);
                    case KeyEvent.VK_S -> player.setDown(true);
                    case KeyEvent.VK_W -> player.setUp(true);
                    case KeyEvent.VK_ESCAPE -> GameLoop.setGameState(GameLoop.gameStateType.PAUSE);
                    case KeyEvent.VK_TAB -> playSubState = STATS;
                    case KeyEvent.VK_F -> venues.interact();
                    default -> {
                    }
                }
            }
            case STATS -> {
                switch (keyCode) {
                    case KeyEvent.VK_A -> player.setLeft(true);
                    case KeyEvent.VK_D -> player.setRight(true);
                    case KeyEvent.VK_S -> player.setDown(true);
                    case KeyEvent.VK_W -> player.setUp(true);
                    case KeyEvent.VK_ESCAPE -> GameLoop.setGameState(GameLoop.gameStateType.PAUSE);
                    case KeyEvent.VK_F -> venues.interact();
                    default -> {
                    }
                }
            }
            case VENUE -> {
                if (keyCode == KeyEvent.VK_ESCAPE) {
                    GameLoop.setGameState(GameLoop.gameStateType.PAUSE);
                }
            }

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (playSubState) {

            case RUN -> {
                switch (keyCode) {
                    case KeyEvent.VK_A -> player.setLeft(false);
                    case KeyEvent.VK_D -> player.setRight(false);
                    case KeyEvent.VK_S -> player.setDown(false);
                    case KeyEvent.VK_W -> player.setUp(false);
                    default -> {
                    }
                }
            }
            case STATS -> {
                switch (keyCode) {
                    case KeyEvent.VK_A -> player.setLeft(false);
                    case KeyEvent.VK_D -> player.setRight(false);
                    case KeyEvent.VK_S -> player.setDown(false);
                    case KeyEvent.VK_W -> player.setUp(false);
                    case KeyEvent.VK_TAB -> playSubState = RUN;
                    default -> {
                    }
                }
            }
            case VENUE -> {
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch (playSubState) {
            case RUN, STATS -> {
                overlay.mousePressed(e);
            }
            case VENUE -> {
                venues.mousePressed(e);
                overlay.mousePressed(e);
            }
            case TRADE -> {
            }
            case TRANSITION -> {
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        switch (playSubState) {
            case RUN, STATS -> {
                overlay.mouseReleased(e);
            }
            case VENUE -> {
                venues.mouseReleased(e);
                overlay.mouseReleased(e);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

        switch (playSubState) {

            case RUN, STATS -> {
                overlay.mouseMoved(e);
            }
            case VENUE -> {
                venues.mouseMoved(e);
                overlay.mouseMoved(e);
            }
        }
    }

    public int getTurn() {
        return turn;
    }

    public Player getPlayer() {
        return player;
    }

    public Entity[] getNPCs() {
        return NPCs;
    }

    public void restartGame() {
        player.reset();
        ((NPC)NPCs[1]).reset(new Point(31 * tileSize, 21 * tileSize));
        ((NPC)NPCs[2]).reset(new Point(2 * tileSize, 24 * tileSize));
        ((NPC)NPCs[3]).reset(new Point(tileSize, 9 * tileSize));
        venues.reset();
        turn = 1;
        setPlaySubState(RUN);
    }

    private void drawTransition(Graphics2D g) {
        counter++;
//        float opacity;
//        if (counter <= 40){
//            opacity = 1 - counter*0.005f;
//        }
//        else {
//            opacity = 0.8f - (counter-40)*0.02f;
//        }
        opacity -= opacityChange;
        opacityChange += 0.0005f;
        g.setColor(new Color(0, 0, 0, opacity));
        g.fillRect(0, 0, GamePanel.screenWidth, GamePanel.screenHeight);
        if (opacity - opacityChange < 0) {
            setPlaySubState(RUN);
            opacityChange = 0.0005f;
            opacity = 1;
        }
    }

    public enum PlayStateType {RUN, STATS, VENUE, TRADE, TRANSITION}

    public void loadObjects(PlayState ps){
        player.loadObject(ps.player);
        for (int i = 0; i < NPCs.length; i++){
            NPCs[i].loadObject(ps.NPCs[i]);
        }
        venues.loadObjects(ps.venues);
        turn = ps.turn;
    }





}
