package transformops;

import rasterdata.RasterImage;
import rasterops.LineRasterizerDDA;
import transforms.Mat4;
import transforms.Point3D;
import transforms.Vec2D;
import transforms.Vec3D;

import java.util.List;

/**
 * Class which represents wireframe model - view chain
 * @param <T> value of pixel
 */
public class RendererWireframe<T> implements Renderer<T> {
    private final LineRasterizerDDA<T> liner;
    public RendererWireframe(final LineRasterizerDDA<T> liner) {
        this.liner = liner;
    }

    @Override
    public RasterImage<T> render(
            RasterImage<T> background,
            List<Point3D> vertices,
            List<Integer> indices,
            T value, Mat4 mat) {
        RasterImage<T> result = background;

        for (int i = 0; i < indices.size(); i += 2) { //line list

            final Point3D p1 = vertices.get(indices.get(i)).mul(mat);
            final Point3D p2 = vertices.get(indices.get(i + 1)).mul(mat);

            if (p1.getW() <= 0 || p2.getW() <= 0)
                continue;
            final Vec3D v1 = p1.dehomog().get();
            final Vec3D v2 = p2.dehomog().get();
            final Vec2D lv1 = v1
                    .ignoreZ()
                    .add(new Vec2D(1,1))
                    .mul(0.5);
            final Vec2D lv2 = v2
                    .ignoreZ()
                    .add(new Vec2D(1,1))
                    .mul(0.5);

            //fast custting
            if  (   (v1.getX() > 1 || v1.getX()<-1) ||
                    (v1.getY() > 1 || v1.getY()<-1) ||
                    (v1.getZ() > 1 || v1.getZ()<-1) ||
                    (v2.getX() > 1 || v2.getX()<-1) ||
                    (v2.getZ() > 1 || v2.getZ()<-1) ||
                    (v2.getY() > 1 || v2.getY()<-1)
                ) return result;

            result = liner.rasterizeLine(result,
                    lv1.getX(), lv1.getY(),
                    lv2.getX(), lv2.getY(),
                    value);
        }
        return result;
    }

    @Override
    public RasterImage<T> renderTriangle(RasterImage<T> background,
                                         List<Point3D> vertices,
                                         List<Integer> indices,
                                         T value, Mat4 mat) {
        RasterImage<T> result = background;

        for (int i = 0; i < indices.size(); i += 3) { //line list

            final Point3D p1 = vertices.get(indices.get(i)).mul(mat);
            final Point3D p2 = vertices.get(indices.get(i + 1)).mul(mat);
            final Point3D p3 = vertices.get(indices.get(i + 2)).mul(mat);

            if (p1.getW() <= 0 || p2.getW() <= 0 || p3.getW() <= 0)
                continue;
            final Vec3D v1 = p1.dehomog().get();
            final Vec3D v2 = p2.dehomog().get();
            final Vec3D v3 = p3.dehomog().get();

            final Vec2D lv1 = v1
                    .ignoreZ()
                    .add(new Vec2D(1,1))
                    .mul(0.5);
            final Vec2D lv2 = v2
                    .ignoreZ()
                    .add(new Vec2D(1,1))
                    .mul(0.5);
            final Vec2D lv3 = v3
                    .ignoreZ()
                    .add(new Vec2D(1,1))
                    .mul(0.5);

            //fast custting
            if  (   (v1.getX() > 1 || v1.getX()<-1) ||
                    (v1.getY() > 1 || v1.getY()<-1) ||
                    (v1.getZ() > 1 || v1.getZ()<-1) ||
                    (v2.getX() > 1 || v2.getX()<-1) ||
                    (v2.getZ() > 1 || v2.getZ()<-1) ||
                    (v2.getY() > 1 || v2.getY()<-1) ||
                    (v3.getX() > 1 || v3.getX()<-1) ||
                    (v3.getZ() > 1 || v3.getZ()<-1) ||
                    (v3.getY() > 1 || v3.getY()<-1)
                    ) return result;

            result = liner.rasterizeLine(result,
                    lv1.getX(), lv1.getY(),
                    lv2.getX(), lv2.getY(),
                    value);
            result = liner.rasterizeLine(result,
                    lv2.getX(), lv2.getY(),
                    lv3.getX(), lv3.getY(),
                    value);
            result = liner.rasterizeLine(result,
                    lv3.getX(), lv3.getY(),
                    lv1.getX(), lv1.getY(),
                    value);
        }
        return result;
    }
}
