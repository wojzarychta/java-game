package inputs;

import main.GameLoop;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseHandler extends MouseAdapter {
    GameLoop game;

    public MouseHandler(GameLoop game){
        this.game = game;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        switch (GameLoop.getGameState()) {
            case PLAY -> {
                game.getPlayState().mousePressed(e);
            }
            case PAUSE -> {
                game.getPauseScreen().mousePressed(e);
            }
            case MAIN_MENU -> {
                game.getMainMenu().mousePressed(e);
            }
            case GAME_OVER -> {
                game.getGameOver().mousePressed(e);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);

        switch (GameLoop.getGameState()) {
            case PLAY -> {
                game.getPlayState().mouseReleased(e);
            }
            case PAUSE -> {
                game.getPauseScreen().mouseReleased(e);
            }
            case MAIN_MENU -> {
                game.getMainMenu().mouseReleased(e);
            }
            case GAME_OVER -> {
                game.getGameOver().mouseReleased(e);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);

        switch (GameLoop.getGameState()) {
            case PLAY -> {
                game.getPlayState().mouseMoved(e);
            }
            case PAUSE -> {
                game.getPauseScreen().mouseMoved(e);
            }
            case MAIN_MENU -> {
                game.getMainMenu().mouseMoved(e);
            }
            case GAME_OVER -> {
                game.getGameOver().mouseMoved(e);
            }
        }
    }
}
