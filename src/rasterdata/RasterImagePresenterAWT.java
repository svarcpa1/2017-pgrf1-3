package rasterdata;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Function;
import java.util.stream.Stream;

/*
        * Implements {@link Presenter} over any {@link RasterImage} using BufferedImage and Graphics
        * in java.awt. The pixel value type is converted to integer using the
        * given function.
        *
        * @param <PixelType>
 *            the pixel value type parameter */


public class RasterImagePresenterAWT<PixelType> implements Presenter<Graphics> {
    private final RasterImage<PixelType> img;
    private final Function<PixelType, Integer> toInteger;


    public RasterImagePresenterAWT(final RasterImage<PixelType> img, final Function<PixelType, Integer> toInteger) {
        this.img = img;
        this.toInteger = toInteger;
    }
    @Override
    public  Graphics present(final Graphics device) {
        final BufferedImage bufImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        Stream.range(0, img.getHeight()).forEach(row -> {
            Stream.range(0, img.getWidth()).forEach(column -> {
                bufImg.setRGB(column, row, toInteger.apply(img.getPixel(column, row).get()));
            });
        });
        device.drawImage(bufImg, 0, 0, null);
        return device;
    }
}
