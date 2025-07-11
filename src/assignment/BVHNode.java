package assignment;

import java.awt.geom.Point2D;

public abstract class BVHNode {
    
    abstract BVHNode getLeft();
    abstract BVHNode getRight();
    abstract void setLeft(BVHNode node);
    abstract void setRight(BVHNode node);
    abstract double area(Shape shape);
    abstract void recalculate();
    abstract void recalculateDepth();
    abstract void recalculateHeight(int diff);
    abstract void recalculateHeightRec (BVHNode node, int diff);

    abstract boolean leaf();

    abstract boolean containsPoint(Point2D.Double point);

    abstract Shape getShape();

    public int height;

    public int depthOnLeft;
    public int depthOnRight;

    public int getBalance() {
        return depthOnLeft - depthOnRight;
    }
}