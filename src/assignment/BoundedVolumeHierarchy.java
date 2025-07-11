package assignment;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import javax.management.RuntimeErrorException;

public class BoundedVolumeHierarchy implements BVH{

    private SplitMethod splitMethod;

    public enum SplitAxis {
        X,
        Y
    }

    private BVHNode root = null;

    @Override
    public void setSplitMethod(SplitMethod splitMethod) {
        // set split method
        this.splitMethod = splitMethod;
        if(splitMethod != SplitMethod.SPLIT_MIDRANGE || splitMethod != SplitMethod.SPLIT_SURFACE_AREA ||splitMethod != SplitMethod.SPLIT_MEDIAN )
        {
            splitMethod = SPLIT_DEFAULT;
        }
    }

    @Override
    public void buildBVH(List<Shape> shapeList) {
        // base case
        if (shapeList == null) {
            return;
        }

        if (shapeList.size()==0) {
            return;
        }
        else if (shapeList.size()==1) {
            this.root = new TriNode(shapeList.get(0));
        }
        else {
            this.root = buildBVHTree(SplitAxis.X, shapeList, 0);
        }
    }

    private BVHNode buildBVHTree(SplitAxis currAxis, List<Shape> shapes, int height) {

        List<Shape>[] med = medianSplit(shapes, currAxis);
        Point2D.Double[] ends = boundingBoxRecal(shapes);
        BoundingBox box = new BoundingBox(ends);
        SplitAxis axis;

        if (shapes.size()== 1) {
            BVHNode node = new TriNode(shapes.get(0));
            node.height = height;
            return node;
        }

        // determine which axis to split on
        if (currAxis == SplitAxis.X) {
            axis = SplitAxis.Y;
        }
        else {
            axis = SplitAxis.X;
        }

        box.height = height;

        box.setLeft(buildBVHTree(currAxis, med[0], height + 1));
        box.setRight(buildBVHTree(currAxis, med[1], height + 1));

        if (box.getLeft().leaf()) {
            box.depthOnLeft = 1;
        }
        else {
            box.depthOnLeft = Math.max(box.getLeft().depthOnLeft + 1, box.getLeft().depthOnRight + 1);
        }

        if (box.getRight().leaf()) {
            box.depthOnRight = 1;
        }
        else {
            box.depthOnRight = Math.max(box.getRight().depthOnLeft, box.getRight().depthOnRight);
        }

        return box;

    }

    private List<Shape>[] medianSplit(List<Shape> shapes, SplitAxis axis) {
        if (axis == SplitAxis.Y) {
            ArrayList<Shape> upper = new ArrayList<>();
            ArrayList<Shape> lower = new ArrayList<>();
            ArrayList<Shape> extra = new ArrayList<>();
            boolean switcher = true;

            Double[] yVal = new Double[shapes.size()];

            for (int i = 0; i < shapes.size(); i++) {
                yVal[i] = shapes.get(i).getCenter().y;
            }

            Arrays.sort(yVal);
            Double medY = 0.0;

            if (yVal.length % 2 == 0) {
                medY = (yVal[yVal.length/2] + yVal[(yVal.length/2) - 1]) /2;
            }
            else {
                medY = yVal[yVal.length/2];
            }

            for (Shape shape : shapes) {
                if (shape.getCenter().y == medY) {
                    extra.add(shape);
                }
                else if (shape.getCenter().y > medY) {
                    lower.add(shape);
                }
                else if (shape.getCenter().y < medY) {
                    upper.add(shape);
                }
            }

            for (Shape shape: extra) {
                if (!switcher) {
                    upper.add(shape);
                }
                else {
                    lower.add(shape);
                }

                switcher = (!switcher);
            }

            return new List[] {upper, lower};
        }
        else if (axis == SplitAxis.X) {
            ArrayList<Shape> left = new ArrayList<>();
            ArrayList<Shape> right = new ArrayList<>();
            ArrayList<Shape> extra = new ArrayList<>();
            boolean switcher = false;

            Double[] xVal = new Double[shapes.size()];
            for(int i = 0; i < shapes.size(); i++) {
                xVal[i] = shapes.get(i).getCenter().x;
            }

            Arrays.sort(xVal);
            Double medX = 0.0;

            if (xVal.length % 2 == 0) {
                medX = (xVal[xVal.length/2] + xVal[(xVal.length/2) - 1]) / 2;
            }
            else {
                medX = xVal[xVal.length/2];
            }

            for (Shape shape : shapes) {
                if (shape.getCenter().x == medX) {
                    extra.add(shape);
                }
                else if (shape.getCenter().x > medX) {
                    right.add(shape);
                }
                else if (shape.getCenter().x < medX) {
                    left.add(shape);
                }
            }

            for (Shape shape : extra) {
                if (!switcher) {
                    left.add(shape);
                }
                else {
                    right.add(shape);
                }

                switcher = (!switcher);
            }

            return new List[] {left, right};
        }
        return null;
    }

    public Point2D.Double[] boundingBoxRecal(List<Shape> shapes) {
    // base case
        if (shapes.isEmpty()) throw new RuntimeException();

        Double minX = null;
        Double minY = null;
        Double maxX = null;
        Double maxY = null;

        Point2D.Double minP = new Point2D.Double();
        Point2D.Double maxP = new Point2D.Double();

        for (Shape shape : shapes) {
            Point2D.Double minPoint = shape.getMinSurroundingPoint();
            Point2D.Double maxPoint = shape.getMaxSurroundingPoint();

            if (minX == null) {
                minX = minPoint.x;
            }

            if (minY == null) {
                minY = minPoint.y;
            }

            if (maxX == null) {
                maxX = maxPoint.x;
            }

            if (maxY == null) {
                maxY = maxPoint.y;
            }

            if (minPoint.x < minX) {
                minX = minPoint.x;
            }

            if (minPoint.y < minY) {
                minY = minPoint.y;
            }

            if (maxPoint.x > maxX) {
                maxX = maxPoint.x;
            }

            if (maxPoint.y > maxY) {
                maxY = maxPoint.y;
            }
        }

        minP.x = minX;
        minP.y = minY;
        maxP.x = maxX;
        maxP.y = maxY;

        return new Point2D.Double[] {minP, maxP};
    }

    @Override
    public void insert(Shape shape) {
        // base case
        if (this.root == null) {
            this.root = new TriNode(shape);
            return;
        }

        Stack<BVHNode> stack = new Stack<>();
        this.root = insertRec(stack, shape, shape.getCenter(), this.root);
        rebalance(stack);
    }

    public BVHNode insertRec(Stack<BVHNode> stack, Shape shape, Point2D.Double center, BVHNode node) {
        
        if (node.leaf()) {
            Point2D.Double[] dimens = shapeArrayBoundingBox(new Shape[] {shape, node.getShape()});
            BVHNode box = new BoundingBox(dimens);
            box.height = node.height;
            node.height++;

            box.setLeft(node);
            BVHNode triangle = new TriNode(shape);

            triangle.height = node.height;

            box.setRight(triangle);
            box.depthOnLeft = 1;
            box.depthOnRight = 1;

            return box;
        }

        int children = 0;
        if ((!node.getLeft().leaf()) || (!node.getRight().leaf())) {
            children++;
        }

        if (children==0) {
            Point2D.Double centerL = node.getLeft().getShape().getCenter();
            Point2D.Double centerR = node.getRight().getShape().getCenter();

            if (centerL.distance(center) > centerR.distance(center)) {
                stack.add(node);
                node.setRight(insertRec(stack, shape, center, node.getRight()));
                node.recalculateDepth();
            }
            else {
                stack.add(node);
                node.setLeft(insertRec(stack, shape, center, node.getLeft()));
                node.recalculateDepth();
            }
        }
        else if (children == 1) {
            if (node.getLeft().leaf()) {
                    stack.add(node);
                    node.setLeft(insertRec(stack, shape, center, node.getLeft()));
                    node.recalculateDepth();
                }
                else {
                    stack.add(node);
                    node.setRight(insertRec(stack, shape, center, node.getRight()));
                    node.recalculateDepth();
                }
        }
        else if (children == 2) {
            if (Shape.isClose(node.getLeft().area(shape), node.getRight().area(shape))) {
                    if (node.depthOnLeft > node.depthOnRight) {
                        stack.add(node);
                        node.setRight(insertRec(stack, shape, center, node.getRight()));
                        node.recalculateDepth();
                    }
                    else {
                        stack.add(node);
                        node.setLeft(insertRec(stack, shape, center, node.getLeft()));
                        node.recalculateDepth();
                    }
                }
                else if (node.getLeft().area(shape) < node.getRight().area(shape)) {
                    stack.add(node);
                    node.setLeft(insertRec(stack, shape, center, node.getLeft()));
                    node.recalculateDepth();
                }
                else if (node.getLeft().area(shape) > node.getRight().area(shape)) {
                    stack.add(node);
                    node.setRight(insertRec(stack, shape, center, node.getRight()));
                    node.recalculateDepth();
                }
        }
        node.recalculate();
        return node;
    }

    private Point2D.Double[] shapeArrayBoundingBox(Shape[] shapes) {
        Point2D.Double minP = new Point2D.Double();
        Point2D.Double maxP = new Point2D.Double();

        if (shapes.length == 0) {
            throw new RuntimeException();
        }

        Double minX = null;
        Double minY = null;
        Double maxX = null;
        Double maxY = null;

        for (Shape shape : shapes) {
        Point2D.Double minPoint = shape.getMinSurroundingPoint();
        Point2D.Double maxPoint = shape.getMaxSurroundingPoint();

        if (minX == null) {
            minX = minPoint.x;
        }

        if (minY == null) {
            minY = minPoint.y;
        }

        if (maxX == null) {
            maxX = maxPoint.x;
        }

        if (maxY == null) {
            maxY = maxPoint.y;
        }

        if (minPoint.x < minX) {
            minX = minPoint.x;
        }

        if (minPoint.y < minY) {
            minY = minPoint.y;
        }

        if (maxPoint.x > maxX) {
            maxX = maxPoint.x;
        }

        if (maxPoint.y > maxY) {
            maxY = maxPoint.y;
        }
        }

        minP.x = minX;
        minP.y = minY;
        maxP.x = maxX;
        maxP.y = maxY;

        return new Point2D.Double[] {minP, maxP};
    }

    @Override
    public void remove(Shape shape) {
        if (this.root != null) {
            if (this.root.leaf()) {
                if (this.root.getShape().equals(shape)) {
                    this.root = null;
                    return;
                }
            }

            Stack<BVHNode> stack = new Stack<>();

            this.root = removeRec(shape, this.root, stack);
            rebalance(stack);
        }
    }

    private BVHNode removeRec(Shape shape, BVHNode node, Stack<BVHNode> stack) {
        if (!(node instanceof BoundingBox)) {
            throw new RuntimeException(String.valueOf(node.getShape()));
        }

        Point2D.Double center = shape.getCenter();

        if (!node.getShape().containsPoint(center)) {
            return node;
        }

        BVHNode left = node.getLeft();
        BVHNode right = node.getRight();

        int children = 2;
        if (left.leaf() || right.leaf()) {
            children--;
        }

        if (children == 0) {
            if (node.getLeft().getShape().equals(shape)) {
                    BVHNode rightNode = node.getRight();
                    rightNode.recalculateHeight(-1);
                    return rightNode;
                }
                else if (node.getRight().getShape().equals(shape)) {
                    BVHNode leftNode = node.getLeft();
                    leftNode.recalculateHeight(-1);
                    return leftNode;
                }
        }
        else if (children == 1) {
            if (node.getLeft().leaf()) {
                    if (shape.equals(node.getLeft().getShape())) {
                        node.getRight().recalculateHeight(-1);
                        return node.getRight();
                    }
                    stack.push(node);
                    node.setRight(removeRec(shape, node.getRight(), stack));
                }
                else if (node.getRight().leaf()) {
                    if (shape.equals(node.getRight().getShape())) {
                        node.getLeft().recalculateHeight(-1);
                        return node.getLeft();
                    }
                    stack.push(node);
                    node.setLeft(removeRec(shape, node.getLeft(), stack));
                }
        }
        else if (children == 2) {
            stack.push(node);
            node.setRight(removeRec(shape, node.getRight(), stack));
            node.setLeft(removeRec(shape, node.getLeft(), stack));
        }

        node.recalculateDepth();
        node.recalculate();
        return node;
    }


    @Override
    public Set<Shape> findCollision(Point2D.Double point) {
        HashSet<Shape> shapes = new HashSet<>();
        findCollisionRec(point, shapes, this.root);
        return shapes;
    }

    private void findCollisionRec(Point2D.Double point, Set<Shape> shapes, BVHNode root){
        if (root == null) {
            return;
        }

        if (root.leaf()) {
            if (root.containsPoint(point)) {
                shapes.add(root.getShape());
            }
            return;
        }

        if (root.containsPoint(point)) {
            findCollisionRec(point, shapes, root.getLeft());
            findCollisionRec(point, shapes, root.getRight());
        }
    }


    @Override
    public Shape intersectRay(Point2D.Double origin, Vector2D direction) {

        ArrayList<Shape> shapes = new ArrayList<>();
        ArrayList<Point2D.Double> points = new ArrayList<>();

        if (this.root == null) {
            return null;
        }
        else if (this.root.leaf()) {
            if (this.root.getShape().findIntersection(origin, direction)== null) {
                return null;
            }
            else {
                return this.root.getShape();
            }
        }

        else {
            intersectRayRec(origin, direction, this.root, shapes, points);
            if (shapes.size() != points.size()) {
                throw new RuntimeException();
            }
            else if (shapes.isEmpty()) {
                return null;
            }
            else if (shapes.size() == 1) {
                return shapes.get(0);
            }
            else {
                int minIndex = 0;
                Point2D.Double minP = points.get(0);
                double distance = origin.distance(minP);

                for (int i = 1; i < shapes.size(); i++) {
                    if (origin.distance(points.get(i)) < distance) {
                        minIndex = i;
                    }
                }

                return shapes.get(minIndex);
            }
        }
    }

    private void intersectRayRec(Point2D.Double origin, Vector2D direction, BVHNode node, List<Shape> shapeList, List<Point2D.Double> points) {
        if (node.leaf()) {
            Triangle tri = (Triangle) node.getShape();
            Point2D.Double triPoint = tri.findIntersection(origin, direction);

            if (triPoint != null) {
                shapeList.add(tri);
                points.add(triPoint);
            }
            return;
        }
        
        Rectangle rect = (Rectangle) node.getShape();
        if(rect.doesRayIntersect(origin, direction)) {
            intersectRayRec(origin, direction, node.getLeft(), shapeList, points);
            intersectRayRec(origin, direction, node.getRight(), shapeList, points);
        }
    }

    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        if (this.root == null) {
            return "";
        }
        toStringRec(this.root, stringBuilder);
        return stringBuilder.toString();
    }

    private void toStringRec(BVHNode root, StringBuilder stringBuilder) {
        stringBuilder.append(root.toString());
        if (root.leaf()) {
            return;
        }
        toStringRec(root.getLeft(), stringBuilder);
        toStringRec(root.getRight(), stringBuilder);
    }

    private void rebalance (Stack<BVHNode> stack) {
        int stackLength = stack.size();

        int counter = stackLength;
        while (counter > 1) {
            BVHNode node = stack.pop();
            BVHNode parentNode = stack.pop();
            rebalanceRec(node, parentNode);
            stack.push(parentNode);
            counter--;
        }

        rootRebalanceRec(this.root);
    }

    private void rebalanceRec (BVHNode node, BVHNode parent) {
        
        boolean isLeft = false;
        if (parent.getRight() == node) {
            isLeft = false;
        }
        if (parent.getLeft() == node) {
            isLeft = true;
        }

        if (node.depthOnRight == node.depthOnLeft) {
            return;
        }
        else if (node.depthOnLeft > (node.depthOnRight + 1)) {
            BVHNode lChild = node.getLeft();
            BVHNode keepLGrandchild;
            BVHNode swapLGrandchild;

            if (lChild.depthOnRight > lChild.depthOnLeft) {
                swapLGrandchild = lChild.getLeft();
                keepLGrandchild = lChild.getRight();
            }
            else {
                swapLGrandchild = lChild.getRight();
                keepLGrandchild = lChild.getLeft();
            }

            lChild.height--;
            keepLGrandchild.recalculateHeight(-1);
            node.height++;
            node.getRight().recalculateHeight(1);

            lChild.setLeft(keepLGrandchild);
            lChild.setRight(node);
            node.setLeft(node.getRight());
            node.setRight(swapLGrandchild);

            if (!isLeft) {
                parent.setRight(lChild);
            }
            else if (isLeft) {
                parent.setLeft(lChild);
            }
        }
        else if (node.depthOnRight > (node.depthOnLeft + 1)) {
            BVHNode rChild = node.getRight();
            BVHNode keepRGrandchild;
            BVHNode swapRGrandchild;

            // rotating left;
            if (rChild.depthOnRight > rChild.depthOnLeft) {
                swapRGrandchild = rChild.getLeft();
                keepRGrandchild = rChild.getRight();
            }
            else {
                swapRGrandchild = rChild.getRight();
                keepRGrandchild = rChild.getLeft();
            }

            rChild.height--;
            keepRGrandchild.recalculateHeight(-1);
            node.height++;
            node.getLeft().recalculateHeight(1);

            rChild.setRight(keepRGrandchild);
            rChild.setLeft(node);
            node.setRight(node.getLeft());
            node.setLeft(swapRGrandchild);

            node.recalculateDepth();
            rChild.recalculateDepth();
            node.recalculate();
            rChild.recalculate();

            if (!isLeft) {
                parent.setRight(rChild);
            }
            else if (isLeft) {
                parent.setLeft(rChild);
            }

        }
    }

    private void rootRebalanceRec (BVHNode node) {
        if (node.depthOnRight == node.depthOnLeft) {
            return;
        }
        else if (node.depthOnLeft > (node.depthOnRight + 1)) {

            BVHNode lChild = node.getLeft();
            BVHNode keepLGrandchild;
            BVHNode swapLGrandchild;

            if (lChild.depthOnRight > lChild.depthOnLeft) {
                swapLGrandchild = lChild.getLeft();
                keepLGrandchild = lChild.getRight();
            }
            else {
                swapLGrandchild = lChild.getRight();
                keepLGrandchild = lChild.getLeft();
            }

            lChild.height--;
            keepLGrandchild.recalculateHeight(-1);
            node.height++;
            node.getRight().recalculateHeight(1);

            lChild.setLeft(keepLGrandchild);
            lChild.setRight(node);
            node.setLeft(node.getRight());
            node.setRight(swapLGrandchild);

            node.recalculateDepth();
            lChild.recalculateDepth();
            node.recalculate();
            lChild.recalculate();
            
            this.root = lChild;
        }
        else if (node.depthOnRight > (node.depthOnLeft + 1) ) {

            BVHNode rChild = node.getRight();
            BVHNode keepRGrandChild;
            BVHNode swapRGrandChild;

            if (rChild.depthOnRight > rChild.depthOnLeft) {
                swapRGrandChild = rChild.getLeft();
                keepRGrandChild = rChild.getRight();
            }
            else {
                swapRGrandChild = rChild.getRight();
                keepRGrandChild = rChild.getLeft();
            }

            rChild.height--;
            keepRGrandChild.recalculateHeight(-1);
            node.height++;
            node.getLeft().recalculateHeight(1);

            rChild.setRight(keepRGrandChild);
            rChild.setLeft(node);
            node.setRight(node.getLeft());
            node.setLeft(swapRGrandChild);

            node.recalculateDepth();
            rChild.recalculateDepth();
            node.recalculate();
            rChild.recalculate();

            this.root = rChild;
        }
    }
}