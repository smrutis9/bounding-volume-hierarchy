package assignment;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.List;

public class BoundingBox extends BVHNode {
    
    private Rectangle boundingBox = new Rectangle();
    private BVHNode left = null;
    private BVHNode right = null;



    public BoundingBox(BVHNode t1, BVHNode t2) {
        
    }

    public BoundingBox(Point2D.Double[] ends) {
        this.boundingBox.maxPos = ends[1];
        this.boundingBox.minPos = ends[0];
    }

    @Override
    BVHNode getLeft() {
        return this.left;
    }

    @Override
    BVHNode getRight() {
        return this.right;
    }

    @Override
    void setLeft(BVHNode node) {
        this.left = node;
    }

    @Override
    void setRight(BVHNode node) {
        this.right = node;
    }

    @Override
    boolean leaf() {
        return false;
    }

    @Override
    boolean containsPoint(Point2D.Double point) {
        return this.boundingBox.containsPoint(point);
    }

    @Override
    public Shape getShape() {
        return this.boundingBox;
    }

    @Override 
    void recalculate() {
        Shape leftShape = this.getLeft().getShape();
        Shape rightShape = this.getRight().getShape();

        Point2D.Double leftMin = leftShape.getMinSurroundingPoint();
        Point2D.Double leftMax = leftShape.getMaxSurroundingPoint();
        Point2D.Double rightMin = rightShape.getMinSurroundingPoint();
        Point2D.Double rightMax = rightShape.getMaxSurroundingPoint();

        Point2D.Double newMin = new Point2D.Double();
        Point2D.Double newMax = new Point2D.Double();

        newMin.x = Math.min(leftMin.x, rightMin.x);
        newMin.y = Math.min(leftMin.y, rightMin.y);
        newMax.x = Math.max(leftMax.x, rightMax.x);
        newMax.y = Math.max(leftMax.y, rightMax.y);

        this.boundingBox.minPos = newMin;
        this.boundingBox.maxPos = newMax;
    }


    @Override
    double area(Shape shape) {
        double area = (this.boundingBox.maxPos.x - this.boundingBox.minPos.x) * (this.boundingBox.maxPos.y - this.boundingBox.minPos.y);

        Point2D.Double minPosTemp = this.boundingBox.minPos;
        Point2D.Double maxPosTemp = this.boundingBox.maxPos;
        
        Point2D.Double minShape = shape.getMinSurroundingPoint();
        Point2D.Double maxShape = shape.getMaxSurroundingPoint();
        
        Point2D.Double newMin = new Point2D.Double();
        Point2D.Double newMax = new Point2D.Double();
        newMin.x = Math.min(minShape.x, minPosTemp.x);
        newMin.y = Math.min(minShape.y, minPosTemp.y);
        newMax.x = Math.min(maxShape.x, maxPosTemp.x);
        newMax.y = Math.min(maxShape.y, maxPosTemp.y);

        return (newMax.x - newMin.x) * (newMax.y - newMin.y) - area;
    }

    @Override
    public void recalculateHeight(int diff) {
        this.height += diff;
    }

    @Override
    public void recalculateHeightRec (BVHNode node, int diff) {
        node.height += diff;
        if (node.leaf()) {
            return;
        }
        recalculateHeightRec(node.getLeft(), diff);
        recalculateHeightRec(node.getRight(), diff);
    }

    @Override
    public void recalculateDepth() {

        if (this.getLeft().leaf()) {
            this.depthOnLeft = 1;
        } else {
            this.depthOnLeft = Math.max(this.getLeft().depthOnLeft, this.getLeft().depthOnRight) + 1;
        }

        if (this.getRight().leaf()) {
            this.depthOnRight = 1;
        } else {
            this.depthOnRight = Math.max(this.getRight().depthOnLeft, this.getRight().depthOnRight) + 1;
        }
    }

    @Override
    public String toString() {

        Point2D.Double maxPos = this.boundingBox.maxPos;
        Point2D.Double minPos = this.boundingBox.minPos;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=0; i< height; i++) {
            stringBuilder.append("\t");
        }
        stringBuilder.append("[(").append(minPos.x).append(", ").append(minPos.y).append("), (").append(maxPos.x).append(", ").append(maxPos.y).append(")]\n");
        
        return stringBuilder.toString();

    }

}