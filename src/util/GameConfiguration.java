package util;

import gamestates.Options;
import gamestates.PlayState;

import java.io.*;

public class GameConfiguration implements Serializable {

    private static PlayState playState;
    private static Options options;


    public GameConfiguration(PlayState playState) {
        this.playState = playState;
    }

    public static void serializeGame() {
        try {
            File file = new File("src/config/config.ser");
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(playState);
            out.close();
            fileOut.close();
            System.out.print("Serialized data is saved in config.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static boolean deserializeGame() {
        PlayState ps = null;
        try {
            FileInputStream fileIn = new FileInputStream("src/config/config.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            ps = (PlayState) in.readObject();
            in.close();
            fileIn.close();
            playState.loadObjects(ps);
            return true;
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
        }
        return false;
    }

    public static void saveSettings(Options options) {
        try {
            File file = new File("src/config/settings.ser");
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(options);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in settings.ser");
            GameConfiguration.options = options;
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    private static boolean loadSettingsFile() {
        Options o;
        try {
            FileInputStream fileIn = new FileInputStream("src/config/settings.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            options = (Options) in.readObject();
            in.close();
            fileIn.close();
            return true;
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
        }
        return false;
    }

    public static void loadSettings(Options options){
        if (loadSettingsFile())
            options.loadOptions(GameConfiguration.options);
    }

    public static boolean isFullScreenOn() {
        if (loadSettingsFile())
            return options.isFullScreenOn();
        else
            return false;
    }

    public static void eraseConfig(){
        File file = new File("src/config/config.ser");
        file.delete();
    }


}
