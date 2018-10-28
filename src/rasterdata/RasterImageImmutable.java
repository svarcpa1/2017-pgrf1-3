package rasterdata;

import io.vavr.collection.Vector;

import java.util.Optional;

/*
 * {@link RasterImage} implementation using {@link io.vavr.collection.Vector}
 *
 * @param <P> the pixel value type parameter
 */

public class RasterImageImmutable<P> implements RasterImage<P> {
    private final Vector<P> data;
    private final int width, height;

/*
     * Creates a new immutable raster image with the given width and height and filled with the given pixel value
     *
     * @param width  number of columns
     * @param height number of rows
     * @param pixel  value to fill the image with
*/

    public RasterImageImmutable(final int width, final int height, final P pixel) {
        this.width = width;
        this.height = height;
        data = Vector.fill(width * height, () -> pixel);
    }

    private RasterImageImmutable(final int width, final int height, final Vector<P> data) {
        this.width = width;
        this.height = height;
        this.data = data;
    }


    @Override
    public Optional<P> getPixel(final int column, final int row) {
        if (column >= 0 && row >= 0 && column < getWidth() && row < getHeight()) {
            return Optional.of(data.get(row * width + column));
        }
        return Optional.empty();
    }


    @Override
    public RasterImage<P> withPixel(final int column, final int row, final P pixel) {
        if (column >= 0 && row >= 0 && column < getWidth() && row < getHeight()) {
            return new RasterImageImmutable<>(width, height, data.update(row * width + column, pixel));
        }
        return this;
    }

    @Override
    public RasterImage<P> cleared(final P pixel) {
        return new RasterImageImmutable<>(width, height, pixel);
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }
}
