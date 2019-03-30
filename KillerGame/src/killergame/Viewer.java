package killergame;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Viewer extends Canvas implements Runnable {

    // Attributes
    private final double REFRESHTIME = 0.016683 * 1000;
    private KillerGame game;
    private BufferedImage image;
    private BufferedImage imageBase;

    // Constructor
    public Viewer(KillerGame game) {
        this.game = game;
    }

    // Methods
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(this.game.getWidth() - 1, this.game.getHeight() - 1);
    }

    // Main activity
    @Override
    public void run() {
        
        try {
            this.imageBase = ImageIO.read(new File("src/img/background.png"));
        } catch (IOException error) {
            this.imageBase = new BufferedImage(this.game.getWidth() - 1, this.game.getHeight() - 1, 1);
        }

        while (true) {

            
            this.image = new BufferedImage(this.game.getWidth() - 1, this.game.getHeight() - 1, 2);
            Graphics g = image.getGraphics();
            g.drawImage(imageBase, 0, 0, this);
            
            for (int inc = 0; inc < this.game.getObjects().size(); inc++) {
                this.game.getObjects().get(inc).paint(this.image);
            }

            this.getGraphics().drawImage(this.image, 0, 0, this);

            try {
                Thread.sleep((long) (this.REFRESHTIME));
            } catch (InterruptedException ex) {
                Logger.getLogger(Viewer.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
