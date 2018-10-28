package Model;

import transforms.Col;
import transforms.Point3D;
import transforms.Vec2D;
import transforms.Vec3D;

public class Vertex {
    private final Point3D position;
    private final Col color;
    private final Vec3D normal;
    private final Vec2D texUV;
    private final double one;

    public Vertex(Point3D position, Col color, Vec3D normal, Vec2D texUV) {
        this.position = position;
        this.color = color;
        this.normal = normal;
        this.texUV = texUV;
        one =1;
    }

    public Vertex(Point3D position, Col color, Vec3D normal, Vec2D texUV, double one) {
        this.position = position;
        this.color = color;
        this.normal = normal;
        this.texUV = texUV;
        this.one = one;
    }

    public Point3D getPosition() {
        return position;
    }

    public Col getColor() {
        return color;
    }

    public Vec3D getNormal() {
        return normal;
    }

    public Vec2D getTexUV() {
        return texUV;
    }

    public Double getOne() {
        return one;
    }

    public Vertex mul(double t){
        return new Vertex(getPosition().mul(t),
                getColor() == null ? null : getColor().mul(t),
                getNormal().mul(t),
                getTexUV().mul(t),
                one*t);
    }

    public Vertex add(Vertex v){
        return new Vertex(getPosition().add(v.getPosition()),
                getColor() == null || v.getColor() == null ? null : getColor().add(v.getColor()),
                getNormal().add(v.normal),
                getTexUV().add(v.getTexUV()),
                one*v.getOne());
    }
}

