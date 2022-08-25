package venues;

import entities.Merchant;
import gamestates.GameStateMethods;
import gamestates.PlayState;
import gamestates.PlayState.PlayStateType;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.Serializable;

import static main.GamePanel.tileSize;

public class VenuesSetter implements GameStateMethods, Serializable {

    private final PlayState playState; // ??
    private Venue[] venues;
    private transient Venue currentVenue = null;

    public VenuesSetter(PlayState ps) {
        playState = ps;
        createVenues();
    }

    private void createVenues() {
        venues = new Venue[10];
        venues[0] = new Casino(new Point((int) (3.5 * tileSize), 9 * tileSize), playState.getPlayer());
        venues[1] = new Shop(playState.getNPCs()[0].getWorldPos(), playState, (Merchant) playState.getNPCs()[0]);
        venues[2] = new Labor(new Point((int) (7.5 * tileSize), 24 * tileSize), playState.getPlayer());
        venues[3] = new Brokerage(new Point((int) (16.5 * tileSize), (int) (8 * tileSize)), playState.getPlayer());
        venues[4] = new Bank(new Point((int) (10.5 * tileSize), 8 * tileSize), playState);
        venues[5] = new Market(new Point(27 * tileSize, 8 * tileSize), playState.getPlayer());
        venues[6] = new Market(new Point(18 * tileSize, 22 * tileSize), playState.getPlayer());
        venues[7] = new Market(new Point(28 * tileSize, 22 * tileSize), playState.getPlayer());
    }

    public void drawPrompts(Graphics2D g) {
        for (Venue v : venues) {
            if (v != null) {
                v.drawPrompt(g);
            }
        }
    }

    public void draw(Graphics2D g) {
        if (currentVenue != null)
            currentVenue.draw(g);
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        currentVenue.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        currentVenue.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        currentVenue.mouseMoved(e);
    }

    public void update() {
        for (Venue v : venues) {
            if (v != null) {
                v.update();
            }
        }
        if (currentVenue != null && currentVenue.isOver) {
            playState.setPlaySubState(PlayStateType.RUN);
            currentVenue.setOver(false);
            currentVenue = null;
        }
    }

    public void interact() {
        for (Venue v : venues) {
            if (v != null) {
                if (v.getIsClose()) {
                    v.enter();
                    currentVenue = v;
                    playState.setPlaySubState(PlayStateType.VENUE);

                    break;
                }
            }
        }
    }

    public void nextTurn(int turn) {
        for (Venue v : venues) {
            if (v != null)
                v.nextTurn(turn);
        }
    }

    public void reset() {
        for (Venue v : venues)
            if (v != null)
                v.reset();
    }

    public void loadObjects(VenuesSetter vs){
        for (int i = 0; i < venues.length; i++){
            if (venues[i] != null)
                venues[i].loadObject(vs.getVenues()[i]);
        }
    }

    public Venue[] getVenues() {
        return venues;
    }
}
