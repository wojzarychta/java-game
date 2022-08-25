package gamestates;

import main.GameLoop;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class VenueState implements GameStateMethods{

    public enum VenueSubType {CASINO}

    public VenueSubType getVenueSubType() {
        return venueSubType;
    }

    public void setVenueSubType(VenueSubType venueSubType) {
        this.venueSubType = venueSubType;
    }

    private VenueSubType venueSubType;



    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D g) {
        switch (venueSubType) {
            case CASINO -> {
                //playState.draw(g2D);
            }
        }
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

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
