package assignment;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import assignment.BVH.SplitMethod;

/* 
 * Any comments and methods here are purely descriptions or suggestions.
 * This is your test file. Feel free to change this as much as you want.
 */

public class BVHTest {

    // This will run ONCE before all other tests. It can be useful to setup up
    // global variables and anything needed for all of the tests.
    @BeforeAll
    static void setupAll() {

    }

    // This will run before EACH test.
    @BeforeEach
    void setupEach() {

    }

    // You can test your BVH here
    @Test
    void testBVH() {

    }

    // You can test your Visualization here
    @Test
    void testVisualization() {
        
    }

    public static void main(String[] args) {
        BoundedVolumeHierarchy bvh = new BoundedVolumeHierarchy();

        // set the split method
        bvh.setSplitMethod(SplitMethod.SPLIT_MEDIAN);
        ArrayList<Shape> shapeList = new ArrayList<>();

        Triangle t1 = new Triangle(new Point2D.Double(20,30), new Point2D.Double(20,20), new Point2D.Double(30,30));
        Triangle t2 = new Triangle(new Point2D.Double(50,44), new Point2D.Double(50,34), new Point2D.Double(34,50));
        //Triangle t3 = new Triangle(new Point2D.Double(2,2), new Point2D.Double(3,1), new Point2D.Double(4,2));
        //Triangle t4 = new Triangle(new Point2D.Double(4,5), new Point2D.Double(5,3), new Point2D.Double(5,5));

        shapeList.add(t1);
        shapeList.add(t2);
        //shapeList.add(t4);
        // build the BVH
        bvh.buildBVH(shapeList);
        System.out.println(bvh);

        // System.out.println("Testing empty tree:");
        // System.out.println(bvh.toString());
        // System.out.println("Testing insert:");
        //bvh.insert(t3);
        // System.out.println(bvh.toString());
        // System.out.println("Why is insert printing");
        
       // bvh.insert(t3);
        System.out.println(bvh);
        // System.out.println(bvh.toString());

        // bvh.remove(t4);
        // System.out.println(bvh.toString());

        GUI.visualization.setBoundingVolumeHierarchy(bvh);
        GUI.main(null);
    }

}
