package assignment;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.Set;

public class Visualization {
    BoundedVolumeHierarchy boundingVolumeHierarchy = new BoundedVolumeHierarchy();
    private Point2D.Double origin;

    /**
     * Initialize state of square to be at (0, 0)
     */
    public Visualization () {
        // create a point at 0,0
        origin = new Point2D.Double(0,0);
    }

    /**
     * @param boundingVolumeHierarchy        the BVH to set the boundedVolumeHierarchy to
     */
    public void setBoundingVolumeHierarchy(BoundedVolumeHierarchy boundingVolumeHierarchy) {
        this.boundingVolumeHierarchy = boundingVolumeHierarchy;
    }

    /**
     * Moves the square within the environment if it's able to move in the given direction
     *
     * @param keyPressed        the character of the keyPressed
     * @return              if the environment should be redrawn or not
     */
    boolean moveSquare (char keyPressed) {
        int x = (int) origin.getX();
        int y = (int) origin.getY();
        keyPressed = Character.toLowerCase(keyPressed);
        
        if (keyPressed== 'w') {
            y--;
        }
        if (keyPressed== 'a') {
            x--;
        }
        if (keyPressed== 's') {
            y++;
        }
        if (keyPressed== 'd') {
            x++;
        }

        boolean collision = false;

        for (int numX = x - 10; numX < x + 11; numX++){
            for (int numY = y - 10; numY < y + 11; numY++) {
                boolean emptyCollision = boundingVolumeHierarchy.findCollision(new Point2D.Double(numX, numY)).isEmpty();
                collision = collision || !emptyCollision;
            }
        }

        if (collision == true) {
            //System.out.println("Key is invalid");
        }
        if (collision==false) {
            //System.out.println("Key pressed");
            origin = new Point2D.Double(x, y);
        }

        return !collision;
    }

    /**
     * Calculates all the pixels visible from the square in the center
     *
     * @return              All points that should be drawn other than the square
     */
    LinkedList<Point> drawGUI() {
        // double for loops
        LinkedList<Point> vis = new LinkedList<>();

        if (boundingVolumeHierarchy == null) {
            return vis;
        }

        //System.out.println(origin);

        int originX = (int) origin.getX();
        int originY = (int) origin.getY();

        for (int x = originX - 200; x < originX + 201; x++) {
            for (int y = originY - 200; y < originY + 201; y++) {
                Set<Shape> collisions = boundingVolumeHierarchy.findCollision(new Point2D.Double(x,y));
                if(!collisions.isEmpty()) {
                    int newX = 200 - originX + x;
                    int newY = 200 - originY + y;
                    vis.add(new Point(newX, newY));
                }
            }
        }
        return vis;
    }
}
