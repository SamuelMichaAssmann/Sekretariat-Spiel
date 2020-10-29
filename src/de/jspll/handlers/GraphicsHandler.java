package de.jspll.handlers;

import de.jspll.data.ChannelID;
import de.jspll.data.objects.GameObject;
import de.jspll.frames.SubHandler;
import de.jspll.graphics.*;
import de.jspll.logic.InputHandler;

import java.awt.*;
import java.awt.Dimension;
import java.util.concurrent.atomic.AtomicBoolean;

import static de.jspll.data.ChannelID.*;

/**
 * Created by reclinarka on 05-Oct-20.
 */
public class GraphicsHandler implements SubHandler {

    public GraphicsHandler(String windowTitle, Dimension size, HandlerMode mode){
        slate = new Slate(this);
        this.mode = mode;
        switch (mode){
            case DIALOG:
                dialog = new Secondary_window(windowTitle,slate,size);
                cameras[0] = new Camera(0,0,(int) size.getWidth(),(int) size.getHeight(),2);
                break;
            case MAIN:
                this.window = new de.jspll.graphics.Window(windowTitle,slate,size);
                this.windowTitle = windowTitle;
                cameras[0] = new Camera(0,0,slate.getWidth(),slate.getHeight(),2);
                break;
        }
    }

    private HandlerMode mode;
    private String windowTitle;

    //Main mode
    private Slate slate;
    private de.jspll.graphics.Window window;

    //Dialog mode
    private Secondary_window dialog;

    //time passed since last drawing call
    private float elapsedTime;
    //keeps track if drawing thread is layerSelection
    private AtomicBoolean active = new AtomicBoolean();
    private GameObjectHandler gameObjectHandler;
    private Camera[] cameras = new Camera[10];
    private int selectedCamera = 0;


    //gets called according to fps target;
    // - elapsedTime is the time in seconds that has passed since the finish of last call
    public void execute(float elapsedTime){
        this.elapsedTime = elapsedTime;
        switch (mode){
            case MAIN:
                active.set(true);
                this.window.repaint();
                break;
            case DIALOG:
                active.set(true);
                this.dialog.repaint();
                break;
        }
        while(active.get()){
            //lock thread until drawing has finished
        }
    }



    //actual drawing call, keeps
    public void drawingRoutine(Graphics g){
        if(g == null){
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        //fill background
        g.fillRect(0,0,slate.getWidth(),slate.getHeight());

        if(gameObjectHandler != null) {
            for(int i = FIRST_LAYER.valueOf(); i <= LAST_LAYER.valueOf(); i++){
                for (GameObject object : gameObjectHandler.getChannel(ChannelID.getbyID(i)).allValues()) {
                    try {
                        object.paint(g, elapsedTime, cameras[selectedCamera]);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

        }
        switch (mode){
            case MAIN:
                break;
            case DIALOG:
                break;
        }

        //Everything that needs to be drawn goes here...

        //Signal that frame is finished
        active.set(false);
    }

    public Point getMousePos(){
        if(window == null)
            return null;
        //Point global_mouse = MouseInfo.getPointerInfo().getLocation();
        //return new Point(global_mouse.x - window.getLocation().x,global_mouse.y  - window.getRootPane().getContentPane().getLocation().y);
        return  slate.getMousePosition(true);

    }

    public Camera getSelectedCamera() {
        return cameras[selectedCamera];
    }

    public void setInputListener(InputHandler inputHandler){
        slate.addMouseListener(inputHandler);
        slate.addMouseWheelListener(inputHandler);
        slate.addMouseMotionListener(inputHandler);
        switch (mode){
            case MAIN:
                window.addKeyListener(inputHandler);
                break;
            case DIALOG:
                dialog.addKeyListener(inputHandler);
                break;
        }
    }

    public void setGameObjectHandler(GameObjectHandler gameObjectHandler) {
        this.gameObjectHandler = gameObjectHandler;
    }

    public de.jspll.graphics.Window getWindow() {
        return window;
    }

    public Slate getSlate() {
        return slate;
    }


    public enum HandlerMode{
        MAIN,
        DIALOG
    }

}
