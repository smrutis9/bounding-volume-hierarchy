package assignment;

import java.awt.geom.Point2D;

public class TriNode extends BVHNode {
    private Triangle triangle;

    public TriNode (Shape shape) {
        if (!(shape instanceof Triangle)) {
            throw new IllegalArgumentException();
        }

        this.triangle = (Triangle) shape;
    }

    @Override
    BVHNode getLeft() {
        return null;
    }

    @Override
    BVHNode getRight() {
        return null;
    }

    @Override
    void setLeft(BVHNode node) {
        return;
    }

    @Override
    void setRight(BVHNode node) {
        return;
    }

    @Override
    boolean leaf() {
        return true;
    }

    @Override
    boolean containsPoint(Point2D.Double point) {
        return this.triangle.containsPoint(point);
    }

    @Override
    public Shape getShape() {
        return this.triangle;
    }

    @Override
    double area(Shape shape) {
        throw new UnsupportedOperationException();
    }

    @Override 
    void recalculate() {
        throw new IllegalArgumentException();
    }

    @Override
    void recalculateDepth() {
        throw new IllegalArgumentException();
    }

    @Override
    void recalculateHeight(int diff) {
        this.height += diff;
    }

    @Override
    void recalculateHeightRec (BVHNode node, int diff) {
        
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i< height; i++){
            sb.append("\t");
        }

        sb.append(triangle.toString());

        return sb.toString();
    }
}