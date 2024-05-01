package lms_lecture;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class OverJLabel extends JLabel {
   


    private double dX, dY;
    private int   w, h;
    private Icon image;  

    OverJLabel(double x, double y, int width, int height, Icon image) {
        this.image = image;  // Initialize the image
        dX = x;
        dY = y;
        w = width;
        h = height;
    }

    private Ellipse2D.Double border = new Ellipse2D.Double();

    @Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);  // Call the superclass method to ensure correct painting
        
    Graphics2D g2d = (Graphics2D) g.create();  // Create a copy of the graphics object
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Set the clipping area to the ellipse
    border.setFrame(0, 0, w, h);
    g2d.setClip(border);

    // Draw the red background
    g2d.setPaint(Color.RED);
    g2d.fillRect(0, 0, w, h);

    // Draw the image
    g2d.drawImage(((ImageIcon) image).getImage(), 0, 0, w, h, this);

    g2d.dispose();  // Dispose of the graphics object
    }
}
