package src.view.gui.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

// see https://stackoverflow.com/questions/299495/how-to-add-an-image-to-a-jpanel
public class ImagePanel extends JPanel {
    private static final Logger logger = LogManager.getLogger("view");

    private BufferedImage image;

    public ImagePanel(String fileName) {
        try {
            image = ImageIO.read(getClass().getResource(fileName));
        } catch (IOException ex) {
            logger.error(ex);
            logger.error("Cannot load " + fileName + " image");
            image = null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(image!=null) {
            Graphics2D g2d = (Graphics2D) g;
            Dimension dimension = getSize();
            g2d.setPaint(new TexturePaint(image, new Rectangle(dimension.width, dimension.height)));
            g2d.fill(new Rectangle2D.Double(0, 0, dimension.width, dimension.height));
        }
    }

}