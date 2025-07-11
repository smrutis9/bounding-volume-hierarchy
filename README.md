# Bounding Volume Hierarchy (BVH) Implementation

## Overview
A 2D Bounding Volume Hierarchy implementation for efficient spatial searching and collision detection. This project creates a balanced binary tree structure where internal nodes store bounding boxes and leaf nodes store triangles, optimized for ray tracing and collision detection applications.

## Features

### Core BVH Operations
- **Build**: Constructs a balanced BVH using median-split algorithm
- **Insert**: Adds triangles while maintaining AVL-tree balance property
- **Remove**: Removes triangles and rebalances the tree structure
- **Collision Detection**: Finds all triangles intersecting a given point
- **Ray Intersection**: Identifies first triangle hit by a ray from origin

### Visualization Component
- 400x400 pixel GUI with movable 20x20 square
- WASD controls for navigation
- Collision prevention with triangles
- Real-time rendering of visible triangles

## Technical Implementation

### Data Structures
- **BVHNode**: Binary tree nodes storing bounding boxes or triangles
- **BoundingBox**: Manages minimum/maximum coordinates and area calculations
- **Stack-based Rebalancing**: Maintains AVL property during insertions/removals

### Algorithms
- **Median Split**: Alternates between X and Y axes for balanced partitioning
- **AVL Rebalancing**: Ensures height difference ≤ 1 between subtrees
- **Ray-Box Intersection**: Optimizes ray tracing by early bounding box rejection

## Testing Strategy

### White Box Testing
- **buildBVH**: Verified tree structure matches expected output
- **insert/remove**: Confirmed proper rebalancing and node placement
- **toString**: Validated pre-order traversal formatting

### Black Box Testing
- **Visualization**: Manual testing of WASD controls and collision detection
- **Edge Cases**: Empty trees, single nodes, duplicate triangles

### Test Results
All core methods passed testing with the following verified scenarios:
- Empty tree handling
- Single triangle operations
- Multiple triangle configurations
- Collision detection accuracy
- Ray intersection correctness

## Educational Applications
- **Computer Graphics**: Understanding spatial data structures for rendering
- **Game Development**: Learning collision detection fundamentals
- **Visual Learning**: Improving spatial reasoning and attention to detail
- **Algorithm Visualization**: Seeing tree rebalancing in real-time

## Limitations
- **2D Only**: No support for 3D objects or spaces
- **Triangle-Only**: Limited to triangle shapes as leaf nodes
- **Manual Control**: No GUI buttons for insert/remove operations
- **Memory Usage**: Stores redundant bounding box information

## Technical Challenges Overcome
1. **Bounding Box Calculation**: Fixed X/Y coordinate confusion
2. **Tree Rebalancing**: Implemented stack-based approach for proper AVL maintenance
3. **Coordinate System**: Adjusted for bottom-left origin in visualization
4. **Movement Logic**: Corrected square movement direction mapping

## Interesting Discoveries
- Perfect pixel-precise navigation between triangles creates natural gameplay
- Large triangles (e.g., (100,30), (200,20), (30,30)) show visible pixelation
- Square movement fits exactly between triangles without buffer space

## Future Improvements
- 3D object support
- Additional shape types beyond triangles
- GUI controls for dynamic triangle manipulation
- Crofton formula implementation for optimal splitting
- Surface area heuristic for better ray tracing performance

## Performance Characteristics
- **Build Time**: O(n log n) for n triangles
- **Insert/Remove**: O(log n) with rebalancing
- **Ray Intersection**: O(log n) average case
- **Space Complexity**: O(n) for tree storage

## Author
Smruti Sannabhadti