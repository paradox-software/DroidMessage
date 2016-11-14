package net.thenightwolf.dm.desktop.controller.components.card;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.*;
import java.util.Random;

public class DefaultContactImage {

    private static Font roboto;

    public Image getImage(String c){
        try {
            roboto = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/roboto.ttf"));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/view/icons/contact_circle.png"));
            image = colorImage(image, getColor());
            return new Image(new ByteArrayInputStream(overlayText(image, c)));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Color getColor() {
        Random ran = new Random();
        switch (ran.nextInt(6)){
            case 1: return Color.MAGENTA;
            case 2: return Color.cyan;
            case 3: return Color.PINK;
            default: return Color.BLACK;
        }
    }

    private byte[] overlayText(BufferedImage old, String character){
        int w = old.getWidth();
        int h = old.getHeight();
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.drawImage(old, 0, 0, null);
        g2d.setPaint(Color.white);
        g2d.setFont(roboto.deriveFont(80f));
        FontMetrics fm = g2d.getFontMetrics();
        //int x = img.getWidth() / 2 - fm.stringWidth(s) / 2;
        //int y = img.getHeight() / 2 - fm.getHeight() / 2;

        int x = (img.getWidth() - fm.stringWidth(character)) / 2;
        int y = ((img.getHeight() - fm.getHeight()) / 2) + fm.getAscent();

        g2d.drawString(character, x, y);
        g2d.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private BufferedImage colorImage(BufferedImage image, Color color){
        int width = image.getWidth();
        int height = image.getHeight();

        WritableRaster raster = image.getRaster();

        for(int xx = 0; xx < width; xx++){
            for(int yy = 0; yy < height; yy++){
                int[] pixels = raster.getPixel(xx, yy, (int[]) null);
                pixels[0] = color.getRed();
                pixels[1] = color.getGreen();
                pixels[2] = color.getBlue();
                raster.setPixel(xx, yy, pixels);
            }
        }
        return image;
    }
}
