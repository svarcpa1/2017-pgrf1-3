package raster;

import java.awt.image.BufferedImage;
import java.util.Optional;

public class DepthBuffer implements Raster<Double>{

	private double[][] depth;
    private BufferedImage image;
    private int width;
	private int height;

    public DepthBuffer(int width, int height){
        this.width=width;
        this.height=height;
    }
    public DepthBuffer(BufferedImage image){
        this.width=image.getWidth();
        this.height=image.getHeight();
    }

	@Override
	public Optional<Double> getPixel(int x, int y) {
        if (isInside(x,y)){
            return Optional.of(new Double(depth[x][y]));
        }else {
            return Optional.empty();
        }
	}

	@Override
	public void setPixel(int x, int y, Double value) {
        if (isInside(x,y)){
            depth[x][y]=value;
        }
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

    @Override
    public void clear(Double value) {
        for(int x =0; x<width; x++){
            for(int y =0; y<height; y++){
                depth[x][y]= value;
            }
        }
    }

    private boolean isInside(int x, int y){
        return ((x>=0 && x<=width)&&(y>=0 && y<=height));
    }
}
