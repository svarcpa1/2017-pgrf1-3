package objectdata;

import transforms.Point3D;

import java.util.List;

public interface Mesh {
    List<Point3D> getVertices();
    List<Integer> getIndices();
}
