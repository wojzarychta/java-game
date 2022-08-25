package inputs;

import main.GameLoop;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyHandler extends KeyAdapter {

    GameLoop game;

    public KeyHandler(GameLoop game){
        this.game = game;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);

        switch (GameLoop.getGameState()) {
            case PLAY -> {
                game.getPlayState().keyPressed(e);
            }
            case PAUSE -> {
                game.getPauseScreen().keyPressed(e);
            }
            case MAIN_MENU -> {
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);

        switch (GameLoop.getGameState()) {
            case PLAY -> {
                game.getPlayState().keyReleased(e);
            }
            case PAUSE -> {
            }
        }
    }

}
