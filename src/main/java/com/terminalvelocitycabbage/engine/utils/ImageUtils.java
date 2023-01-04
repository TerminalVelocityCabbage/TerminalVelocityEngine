package com.terminalvelocitycabbage.engine.utils;

import java.nio.ByteBuffer;

import static java.lang.Math.round;

public class ImageUtils {

    /**
     * multiplies this image by its alpha
     *
     * @param image the image to multiply
     * @param width the width of this image
     * @param height the height of this image
     * @param stride the stride for the color values of the byteBuffer
     */
    public static void premultiplyAlpha(ByteBuffer image, int width, int height, int stride) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int i = y * stride + x * 4;

                float alpha = (image.get(i + 3) & 0xFF) / 255.0f;
                image.put(i + 0, (byte)round(((image.get(i + 0) & 0xFF) * alpha)));
                image.put(i + 1, (byte)round(((image.get(i + 1) & 0xFF) * alpha)));
                image.put(i + 2, (byte)round(((image.get(i + 2) & 0xFF) * alpha)));
            }
        }
    }

    /**
     * Does the inverse of #premultiplyAlpha
     *
     * @param image the image to unpremultiply
     * @param width the width of this image
     * @param height the height of this image
     * @param stride the stride for this ByteBuffer
     */
    public static void unpremultiplyAlpha(ByteBuffer image, int width, int height, int stride) {
        int x, y;

        // Unpremultiply
        for (y = 0; y < height; y++) {
            int row = y * stride;
            for (x = 0; x < width; x++) {
                int r = image.get(row + 0) & 0xFF,
                        g = image.get(row + 1) & 0xFF,
                        b = image.get(row + 2) & 0xFF,
                        a = image.get(row + 3) & 0xFF;

                if (a != 0) {
                    image.put(row + 0, (byte)Math.min(r * 255 / a, 255));
                    image.put(row + 1, (byte)Math.min(g * 255 / a, 255));
                    image.put(row + 2, (byte)Math.min(b * 255 / a, 255));
                }

                row += 4;
            }
        }

        // Defringe
        for (y = 0; y < height; y++) {
            int row = y * stride;
            for (x = 0; x < width; x++) {
                int r = 0, g = 0, b = 0, a = image.get(row + 3) & 0xFF, n = 0;
                if (a == 0) {
                    if (x - 1 > 0 && image.get(row - 1) != 0) {
                        r += image.get(row - 4) & 0xFF;
                        g += image.get(row - 3) & 0xFF;
                        b += image.get(row - 2) & 0xFF;
                        n++;
                    }
                    if (x + 1 < width && image.get(row + 7) != 0) {
                        r += image.get(row + 4) & 0xFF;
                        g += image.get(row + 5) & 0xFF;
                        b += image.get(row + 6) & 0xFF;
                        n++;
                    }
                    if (y - 1 > 0 && image.get(row - stride + 3) != 0) {
                        r += image.get(row - stride) & 0xFF;
                        g += image.get(row - stride + 1) & 0xFF;
                        b += image.get(row - stride + 2) & 0xFF;
                        n++;
                    }
                    if (y + 1 < height && image.get(row + stride + 3) != 0) {
                        r += image.get(row + stride) & 0xFF;
                        g += image.get(row + stride + 1) & 0xFF;
                        b += image.get(row + stride + 2) & 0xFF;
                        n++;
                    }
                    if (n > 0) {
                        image.put(row + 0, (byte)(r / n));
                        image.put(row + 1, (byte)(g / n));
                        image.put(row + 2, (byte)(b / n));
                    }
                }
                row += 4;
            }
        }
    }

    /**
     * Set the alpha of this image
     *
     * @param image the image to modify
     * @param width the width of this image
     * @param height the height of this image
     * @param stride the stride of this bytebuffer
     * @param newAlpha the alpha to change this image to
     */
    public static void setAlpha(ByteBuffer image, int width, int height, int stride, byte newAlpha) {
        int x, y;
        for (y = 0; y < height; y++) {
            int row = y * stride;
            for (x = 0; x < width; x++) {
                image.put(row + x * 4 + 3, newAlpha);
            }
        }
    }

    /**
     * flips this image horizontally
     *
     * @param image the image to flip
     * @param width the width of the image
     * @param height the height of the image
     * @param stride the stride of this bytebuffer
     */
    public static void flipHorizontal(ByteBuffer image, int width, int height, int stride) {
        int i = 0, j = height - 1, k;
        while (i < j) {
            int ri = i * stride;
            int rj = j * stride;
            for (k = 0; k < width * 4; k++) {
                byte t = image.get(ri + k);
                image.put(ri + k, image.get(rj + k));
                image.put(rj + k, t);
            }
            i++;
            j--;
        }
    }

}
