package de.jspll.data.objects.game.tasks;

import de.jspll.Main;
import de.jspll.data.*;
import de.jspll.data.objects.Animation;
import de.jspll.data.objects.TexturedObject;
import de.jspll.graphics.Camera;
import de.jspll.util.Vector2D;
import java.awt.*;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * © Sekretariat-Spiel
 * By Jonas Sperling, Laura Schmidt, Lukas Becker, Philipp Polland, Samuel Assmann
 *
 * @author Lukas Becker, Philipp Polland
 *
 * @version 1.0
 */

public class TaskHolder extends TexturedObject {

    private Point playerPos;
    private Dimension playerDim;
    private Point pos;
    private Task task;
    private double radius;
    private boolean inProximity = false;
    private HashMap<String,AtomicBoolean> keyMap;
    private final Animation an;
    private BufferedImage texture;

    public TaskHolder(String ID, String objectID, Point pos, Dimension dimension, Task task, double radius) {
        super(ID, objectID, pos.x, pos.y, dimension);
        this.channels = new ChannelID[]{ ChannelID.LOGIC, ChannelID.OVERLAY};
        this.pos = pos;
        this.task = task;
        this.radius = radius;
        if (task != null)
            task.setHolder(this);
        an = new Animation("/assets/task/task_", 20, new Point(pos.x, pos.y - 30), new Dimension(32,32), this,3F);
    }

    @Override
    public void requestTexture() {
        an.requestTextures(this);
        an.setLooping(true);
        an.startAnimation();
    }

    @Override
    public boolean isTextureLoaded() {
        if (!an.isLoaded()){
            an.loadTextures();
            return false;
        }
        return true;
    }

    @Override
    protected void drawFrame(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {
        an.draw((Graphics2D) g, elapsedTime, camera);
    }

    @Override
    public char update(float elapsedTime) {
        super.update(elapsedTime);

        if(task != null && task.isActive()){
            task.update(elapsedTime);
            return 0;
        }

        if (playerPos == null || playerDim == null) {
            requestPlayerPosAndSize();
        }
        if(keyMap == null && getParent() != null){
            keyMap = getParent().getLogicHandler().getInputHandler().getKeyMap();
        }
        inProximity = isInProximity();

        if(inProximity){
            if(keyMap.get("e").get() && task != null){
                task.activate();
            }
        }
        return 0;
    }

    private boolean isInProximity() {
        if(playerPos == null || playerDim == null)
            return false;
        Vector2D distanceToPlayer = new Vector2D(
                new Point(pos.x + dimension.width / 2, pos.y + dimension.height / 2),
                new Point((playerPos.x + playerDim.width / 2), (playerPos.y + playerDim.height / 2) + 24));

        if (distanceToPlayer.euclideanDistance() < radius) {
            return true;
        }
        return false;
    }

    @Override
    public char call(Object[] input) {
        super.call(input);

        if (input == null || input.length < 1) {
            return 0;
        } else if (input[0] instanceof String) {
            String cmd = (String) input[0];
            if (cmd.contentEquals("playerPos")) {
                playerPos = (Point) input[1];
                playerDim = (Dimension) input[2];
            } else if(cmd.contentEquals("toTask")){
                task.call(input);
                return 0;
            }

            if (((String) input[0]).contentEquals("input")) {
                task.call(input);
            }

        }
        return 0;
    }

    Boolean unlockAnimation = true;

    @Override
    public void paint(Graphics g, float elapsedTime, Camera camera, ChannelID currStage) {
        super.paint(g, elapsedTime, camera, currStage);

        if (task != null) {
            if (currStage == ChannelID.getbyID(ChannelID.OVERLAY.valueOf()) && task.isActive()) {
                task.paint(g, elapsedTime, camera, currStage);
                return;
            }
            boolean inProximity = isInProximity();
            Graphics2D g2d = (Graphics2D) g;
            Stroke s = null;
            if(inProximity) {
                s = g2d.getStroke();
                g2d.setStroke(new BasicStroke(3));
            }
            g2d.setColor(Color.RED);
            g2d.drawRect(camera.applyXTransform(pos.x),
                    camera.applyYTransform(pos.y),
                    camera.applyZoom(dimension.width),
                    camera.applyZoom(dimension.height));
            if(inProximity && s != null)
                g2d.setStroke(s);
        }



        if (Main.DEBUG) {


            if(playerPos != null && playerDim != null) {
                Point pl = new Point((playerPos.x + playerDim.width / 2), (playerPos.y + playerDim.height / 2) + 24);
                Vector2D distanceToPlayer = new Vector2D(
                        pl,
                        new Point(pos.x + dimension.width / 2, pos.y + dimension.height / 2));
                Point point = new Point(pl), destination = new Point(pl);
                distanceToPlayer.updatePos(destination);
                g.setColor(Color.CYAN);
                g.drawLine(camera.applyXTransform(point.x), camera.applyYTransform(point.y),
                        camera.applyXTransform(destination.x), camera.applyYTransform(destination.y));
            }

            if (inProximity) {
                g.setColor(Color.CYAN);
                g.fillRect(camera.applyXTransform(pos.x), camera.applyYTransform(pos.y),
                        camera.applyZoom(dimension.width), camera.applyZoom(dimension.height));
            }
        }
    }
}
