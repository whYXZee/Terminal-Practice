package whyxzee.terminalpractice.resources;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

public class ImageCreator {
    public int height;
    public int width;
    public BufferedImage img;
    Graphics2D graphics;

    //
    // Graphics
    //

    public ImageCreator(int width, int height) {
        this.width = width;
        this.height = height;
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        graphics = img.createGraphics();

        // Sets background color
        graphics.setBackground(Color.WHITE);
        graphics.clearRect(0, 0, width, height);
    }

    /**
     * Adds a filled rectangle. The origin is (0,0) on the top left.
     * 
     * @param originX The x-position of where the rectangle starts.
     * @param originY The y-position of where the rectangle starts.
     * @param height  The height of the rectangle.
     * @param width   The width of the rectnangle
     */
    public void filledRectangle(int originX, int originY, int height, int width) {
        graphics.fillRect(originX, originY, width, height);
    }

    /**
     * Adds an empty rectangle. The origin is (0,0) on the top left.
     * 
     * @param originX The x-position of where the rectangle starts.
     * @param originY The y-position of where the rectangle starts.
     * @param height  The height of the rectangle.
     * @param width   The width of the rectnangle
     */
    public void emptyRectangle(int originX, int originY, int height, int width, int lineWidth) {
        graphics.fillRect(originX, originY, width, height);
        graphics.clearRect(originX + lineWidth, originY + lineWidth, width - (2 * lineWidth), height - (2 * lineWidth));
    }

    /**
     * Adds a filled 90-degree triangle. The origin is (0,0) on the top left.
     * 
     * @param originX The x-position of where the triangle starts (the angle
     *                connecting hypotenuse and height).
     * @param originY The y-position of where the triangle starts (the angle
     *                connecting hypotenuse and height).
     * @param height  The height of the triangle.
     * @param base    The base of the triangle.
     */
    public void filled90Triangle(int originX, int originY, int height, int base) {
        Path2D trianglePath = new Path2D.Double();

        trianglePath.moveTo(originX, originY);
        trianglePath.lineTo(originX, originY + height);
        trianglePath.lineTo(originX + base, originY + height);
        trianglePath.closePath();

        graphics.fill(trianglePath);
    }

    /**
     * Adds a rotated rectangle (diamond). The origin is (0,0) on the top left.
     * 
     * @param originX  The x-position of where the triangle starts (the angle
     *                 connecting hypotenuse and height).
     * @param originY  The y-position of where the triangle starts (the angle
     *                 connecting hypotenuse and height).
     * @param height   The height of the triangle.
     * @param width    The base of the triangle.
     * @param rotation rotation of the box in radians.
     */
    public void filledRotatedRectangle(int originX, int originY, int height, int width, double rotation) {
        Path2D rectanglePath = new Path2D.Double();

        int triangleHeight = (int) ((width * Math.sin(rotation)) + .5); // height of triangle when the rectangle is
                                                                        // split into
        // triangles
        int triangleHypotenuse = (int) (Math.sqrt(Math.pow(height, 2) + Math.pow(width, 2)) + .5);

        rectanglePath.moveTo(originX, originY);
        rectanglePath.lineTo(originX + (width * Math.cos(rotation)), originY + triangleHeight);
        rectanglePath.lineTo(originX + triangleHypotenuse, originY);
        rectanglePath.lineTo(originX + (height * Math.sin(rotation)), originY - triangleHeight);
        rectanglePath.closePath();

        graphics.fill(rectanglePath);
    }

    public void text(String input, int originX, int originY, Font font) {
        graphics.setFont(font);
        graphics.drawString(input, originX, originY);
    }

    //
    // Color
    //

    /**
     * Sets the color of any graphic added after this method is called.
     * 
     * @param r red value of a color
     * @param g green value of a color
     * @param b blue value of a color
     */
    public void setColor(int r, int g, int b) {
        graphics.setColor(new Color(r, g, b));
    }
}
