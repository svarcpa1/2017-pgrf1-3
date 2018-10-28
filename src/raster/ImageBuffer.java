package raster;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;

public class ImageBuffer implements Raster<Integer>{

	private int width;
	private int height;
	private BufferedImage image;
	
	public ImageBuffer(BufferedImage image, int width, int height) {
		this.image=image;
		this.width=image.getWidth();
		this.height=image.getHeight();
	}

	public ImageBuffer(BufferedImage image) {
		this.image=image;
		this.width=image.getWidth();
		this.height=image.getHeight();
	}
	
	@Override
	public Optional<Integer> getPixel(int x, int y) {
		Integer i = new Integer(image.getRGB(x, y));
		if(isInside(x,y)) {
			return Optional.of(i);
		}else {
			return Optional.empty();
		}
	}

	@Override
	public void setPixel(int x, int y, Integer value) {
		if(isInside(x,y)) {
			image.setRGB(x, y, value);
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
	public void clear(Integer value) {
		Graphics gr = image.getGraphics();
		gr.setColor(new Color(value.intValue()));
		gr.fillRect(0,0,width,height);
	}

	private boolean isInside(int x, int y){
		return ((x>=0 && x<=width)&&(y>=0 && y<=height));
	}
}
