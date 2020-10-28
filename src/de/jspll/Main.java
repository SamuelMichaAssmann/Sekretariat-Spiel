package de.jspll;

import de.jspll.data.objects.GameObject;
import de.jspll.data.objects.GameTrie;
import de.jspll.data.objects.examples.Counter;
import de.jspll.data.objects.examples.DisplayMover;
import de.jspll.data.objects.examples.MouseFollower;
import de.jspll.data.objects.game.ui.PaperList;
import de.jspll.dev.EditorHandler;
import de.jspll.frames.FrameHandler;

import java.awt.*;
import java.util.ArrayList;

public class Main {

    //handles Frame Drawing and game logic
    private static FrameHandler frameHandler = new FrameHandler("Sekreteriat Spiel", new Dimension(1920, 1080));

    /**
     * Main method, gets called, when the application is started.
     * Starts the game.
     * @param args NOT USED command line arguments
     */
    public static void main(String[] args) {

        //Debug objects
        ArrayList<GameObject> objects = new ArrayList<>();
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 5; y++) {
                objects.add(new GameObject(x + "_" + y, "g.dflt.GameObject", x * 16, y * 32 + (16 * (x % 2)),new Dimension(16,16)));
            }
        }

        //Debug version of the task list
        objects.add(new PaperList("test",new Dimension(600,600),new Point(0,0)));
        //Debug tool to show mose position
        objects.add(new MouseFollower("test1"));
        //Debug tool to move all display objects
        objects.add(new DisplayMover("test1"));
        //Debug time counter
        objects.add(new Counter("test"));
        //Load Debug objects into the Object handler
        frameHandler.getGameObjectHandler().loadObjects(objects);

        EditorHandler test = new EditorHandler("dev1","devtools",new Dimension(200,700));
        frameHandler.getGameObjectHandler().loadObject(test);

        //start the game
        frameHandler.run();
    }
}
