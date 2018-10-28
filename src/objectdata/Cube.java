package objectdata;

import transforms.Point3D;

import java.util.ArrayList;
import java.util.List;

/**
 * CLASS HOLDING DATA FOR CUBE RENDERING
 */
public class Cube implements Mesh{
    private final List<Point3D> vertices;
    private final List<Integer> indices;

    public Cube() {
        vertices = new ArrayList<>();
        indices = new ArrayList<>();

        vertices.add(new Point3D(0,0,0));
        vertices.add(new Point3D(1,0,0));
        vertices.add(new Point3D(1,1,0));
        vertices.add(new Point3D(0,1,0));
        vertices.add(new Point3D(0,0,1));
        vertices.add(new Point3D(1,0,1));
        vertices.add(new Point3D(1,1,1));
        vertices.add(new Point3D(0,1,1));

        for (int i = 0; i < 4; i++) {
            indices.add(i);
            indices.add((i + 1) % 4);
            indices.add(i);
            indices.add(i + 4);
            indices.add(i + 4);
            indices.add((i + 1) % 4 + 4);
        }
    }

    @Override
    public List<Point3D> getVertices() {
        return vertices;
    }
    @Override
    public List<Integer> getIndices() {
        return indices;
    }
}
