package assignment;

import javax.swing.*;

import assignment.BVH.SplitMethod;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.awt.geom.Point2D;

import java.util.List;


public class GUI {
    public static Visualization visualization = new Visualization();

    public static void main(String[] args) {
        
        // maybe delete later

        // Triangle triangle = new Triangle(new Point2D.Double(20, 30), new Point2D.Double(20, 20), new Point2D.Double(30, 30));
        // Triangle triangle2 = new Triangle(new Point2D.Double(50, 44), new Point2D.Double(50, 34), new Point2D.Double(34, 50));
        
        // Triangle triangle3 = new Triangle(new Point2D.Double(110, 100), new Point2D.Double(100, 100), new Point2D.Double(110, 110));
        // Triangle triangle4 = new Triangle(new Point2D.Double(78, 64), new Point2D.Double(64, 64), new Point2D.Double(78, 78));
        BoundedVolumeHierarchy bvh = new BoundedVolumeHierarchy();
        Triangle triangle = new Triangle(new Point2D.Double(0, 0), new Point2D.Double(1, 0), new Point2D.Double(1, 2));
        Triangle triangle2 = new Triangle(new Point2D.Double(1, 5), new Point2D.Double(2, 3), new Point2D.Double(3, 5));
        Triangle triangle3 = new Triangle(new Point2D.Double(2, 2), new Point2D.Double(3, 1), new Point2D.Double(4, 2));
        Triangle triangle4 = new Triangle(new Point2D.Double(4, 5), new Point2D.Double(5, 4), new Point2D.Double(5, 5));
        Triangle triangle5 = new Triangle(new Point2D.Double(2, 5), new Point2D.Double(5, 4), new Point2D.Double(5, 5));


        List<Shape> shapeList = new ArrayList<>();
        shapeList.add(triangle);
        shapeList.add(triangle2);
        shapeList.add(triangle3);
        shapeList.add(triangle5);
        bvh.buildBVH(shapeList);
        bvh.insert(triangle4);
        bvh.remove(triangle5);
        //System.out.println(bvh.toString());

        Point2D.Double origin = new Point2D.Double(0, 1);
        Vector2D rayVector = new Vector2D(1, 1);
        Shape intersectedShape = bvh.intersectRay(origin, rayVector);
        System.out.println(intersectedShape);
        // System.out.println(intersectedShape.toString());
        // Point2D.Double testFindCollision = new Point2D.Double(2, 2);
        //System.out.println(bvh.findCollision(testFindCollision));

        // Point2D.Double testFindCollision2 = new Point2D.Double(20, 20);
        // System.out.println(bvh.findCollision(testFindCollision2).isEmpty());
        // System.out.println("AFTER:");
        // System.out.println(bvh.toString());
       //visualization.boundingVolumeHierarchy.buildBVH(shapeList);

        JFrame frame = new JFrame("BVH Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        BVHVisualization bvhVisualization = new BVHVisualization();
        frame.add(bvhVisualization);
        frame.setVisible(true);
        frame.setResizable(false);
    }
}

class BVHVisualization extends JPanel {
    public BVHVisualization() {
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                if (GUI.visualization.moveSquare(e.getKeyChar())) {
                    repaint();
                }
            }
        });
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        LinkedList<Point> points = GUI.visualization.drawGUI();


        for (Point point : points) {
            g.fillRect(point.x, point.y, 1, 1);
        }

        g.setColor(Color.BLUE);
        g.fillRect(190, 190, 21, 21);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 400);
    }
}
