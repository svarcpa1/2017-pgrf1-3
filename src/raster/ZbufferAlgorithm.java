package raster;

import transforms.Col;

import java.awt.image.BufferedImage;
import java.util.Optional;

public class ZbufferAlgorithm {
    ImageBuffer imageBuffer;
    DepthBuffer depthBuffer;

    public ZbufferAlgorithm(BufferedImage image){
        this.depthBuffer = new DepthBuffer(image);
        this.imageBuffer = new ImageBuffer(image);
    }

    public void test(int x, int y, double z, Col color){
        Optional<Double> zValue = depthBuffer.getPixel(x,y);
        if(z>=0 && zValue.isPresent() && zValue.get()>z){
            imageBuffer.setPixel(x,y,color.getRGB());
            depthBuffer.setPixel(x,y,z);
        }
    }

    public int getWidth(){
        return imageBuffer.getWidth();
    }

    public int getHeight(){
        return imageBuffer.getHeight();
    }

    public void clear(Col color){
        depthBuffer.clear(1.0);
        imageBuffer.clear(color.getRGB());
    }
}
