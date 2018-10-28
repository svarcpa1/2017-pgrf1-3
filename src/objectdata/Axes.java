package objectdata;

import transforms.Point3D;

import java.util.ArrayList;
import java.util.List;

/**
 * CLASS HOLDING DATA FOR AXES RENDERING
 */
public class Axes implements Mesh {
    private final List<Point3D> vertices;
    private final List<Integer> indices;

    public Axes() {
        vertices = new ArrayList<>();
        indices = new ArrayList<>();

        vertices.add(new Point3D(0,0,0));
        vertices.add(new Point3D(0,2.0,0));
        vertices.add(new Point3D(2.0,0,0));
        vertices.add(new Point3D(0,0,2.0));

        indices.add(0);
        indices.add(1);
        indices.add(0);
        indices.add(2);
        indices.add(0);
        indices.add(3);
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
