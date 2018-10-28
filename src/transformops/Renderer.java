package transformops;

import rasterdata.RasterImage;
import transforms.Mat4;
import transforms.Point3D;

import java.util.List;

public interface Renderer<T> {
    RasterImage<T> render(
            RasterImage<T> background,
            List<Point3D> vertices,
            List<Integer> indices,
            T value,
            Mat4 mat
    );
    RasterImage<T> renderTriangle(
            RasterImage<T> background,
            List<Point3D> vertices,
            List<Integer> indices,
            T value,
            Mat4 mat
    );
}
