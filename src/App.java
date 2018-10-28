import Model.Vertex;
import raster.ZbufferAlgorithm;
import renderer.RasterizerTriangles;
import transforms.Col;
import transforms.Point3D;
import transforms.Vec2D;
import transforms.Vec3D;

import java.util.function.Function;

public class App {

    //perspektivně korektní interpolace
    class ColorShader implements Function<Vertex, Col>{
        @Override
        public Col apply(Vertex vertex) {
            return vertex.getColor().mul(1/vertex.getOne());
        }
    }

    public static void main(String[] args){
        Vertex v1 = new Vertex( new Point3D(-1,-1,0.5,1),
                                new Col(0xff000),
                                new Vec3D(0,0,-1),
                                new Vec2D(0.7,0.8));

        Vertex v2 = new Vertex( new Point3D(-0.5,-0.5,0.5,1),
                                new Col(0xff000),
                                new Vec3D(0,0,-1),
                                new Vec2D(0.7,0.8));

        Vertex v23= new Vertex( new Point3D(1,-1,0.5,1),
                                new Col(0xff000),
                                new Vec3D(0,0,-1),
                                new Vec2D(0.7,0.8));

        //image z canvasu
        ZbufferAlgorithm zb = new ZbufferAlgorithm(image);
        RasterizerTriangles rt = new RasterizerTriangles(zb, new Function<Vertex, Col>() {
            @Override
            public Col apply(Vertex vertex) {
                return vertex.getColor().mul(1/vertex.getOne());
            }
        });
    }

}
