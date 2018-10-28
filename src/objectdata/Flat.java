package objectdata;

import transforms.Bicubic;
import transforms.Cubic;
import transforms.Mat4;
import transforms.Point3D;

import java.util.ArrayList;
import java.util.List;

public class Flat implements Mesh {
    private List<Point3D> vertices;
    private List<Integer> indices;
    private List<Point3D> reCountVertices;
    private Mat4 matRule;
    private Bicubic bc;
    private final int POINTS = 10;

    public Flat(int intTypeFlat){
        vertices = new ArrayList<>();
        indices = new ArrayList<>();
        reCountVertices= new ArrayList<>();

        //rows
        //p00-p03
        vertices.add(new Point3D(0,0,2));
        vertices.add(new Point3D(0,0.2,1.8));
        vertices.add(new Point3D(0,0.4,1.6));
        vertices.add(new Point3D(0,0.6,1.4));
        //p10-p13
        vertices.add(new Point3D(0.3,0.3,0));
        vertices.add(new Point3D(0.3,0,0));
        vertices.add(new Point3D(0.3,0.3,0));
        vertices.add(new Point3D(0.3,0,0));
        //p20-p23
        vertices.add(new Point3D(1.6,0,0));
        vertices.add(new Point3D(1.6,0,0));
        vertices.add(new Point3D(1.6,0,0));
        vertices.add(new Point3D(1.6,0,0));
        //p30-p33
        vertices.add(new Point3D(1.9,0,1.5));
        vertices.add(new Point3D(1.9,0.5,1.6));
        vertices.add(new Point3D(1.9,0,1.7));
        vertices.add(new Point3D(1.9,.5,2));

        bc=new Bicubic(setTypeFlat(intTypeFlat)
                ,vertices.get(0),vertices.get(1),vertices.get(2),vertices.get(3)
                ,vertices.get(4),vertices.get(5),vertices.get(6),vertices.get(7)
                ,vertices.get(8),vertices.get(9),vertices.get(10),vertices.get(11)
                ,vertices.get(12),vertices.get(13),vertices.get(14),vertices.get(15));

        //for cycle for generation points (using compute method) and filling them int reCount lists
        for (int i=0; i<POINTS; i++) {
            for (int j=0; j<POINTS; j++) {
                reCountVertices.add(bc.compute((double) i / (POINTS),(double) j / (POINTS)));
            }
        }

        //for cycle for connecting points
        for (int i=0; i<POINTS; i++) {
            for (int j=0; j<POINTS; j++) {
                indices.add(i*(POINTS+1)+j);
                indices.add(i*(POINTS+1)+(j+1));
                indices.add((i+1)*(POINTS+1)+j);

                indices.add(i*(POINTS+1)+(j+1));
                indices.add((i+1)*(POINTS+1)+j);
                indices.add((i+1)*(POINTS+1)+(j+1));
            }
        }
    }

    /**
     * Method for setting cubic dependent on typeCurve
     * @param intTypeFlat code of type of flat
     * @return Cubic object filled with specific curve cubic
     */
    public Mat4 setTypeFlat(int intTypeFlat){
        if (intTypeFlat==0) matRule = Cubic.FERGUSON;
        if (intTypeFlat==1) matRule = Cubic.BEZIER;
        if (intTypeFlat==2) matRule = Cubic.COONS;
        return matRule;
    }

    @Override
    public List<Point3D> getVertices() {
        return vertices;
    }

    @Override
    public List<Integer> getIndices() {
        return indices;
    }

    public List<Point3D> getReCountVertices() {
        return reCountVertices;
    }
}
