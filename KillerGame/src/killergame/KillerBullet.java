package killergame;

import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "bullet")
@XmlType(propOrder = {})
public class KillerBullet extends Autonomous {

    // Atributtes
    public static final int RADIUS = 5;
    boolean sended;

    // Constructrors
    public KillerBullet(KillerGame game, int posX, int posY, int red, int green, int blue, int velX, int velY) {
        super(game, posX, posY, "BUL", red, green, blue, velX, velY);
        this.setShape(this.createShape());
        sended = false;
    }

    public KillerBullet() {
        sended = false;
    }

    // Methods
    @Override
    public void move() {

        // Detectores horizontales
        if (this.getPosX() - RADIUS <= 0 && this.getVelX() < 0 && !sended) {
            if (!this.getGame().sendObjectToPrevius(this)) {
                this.setVelX(-this.getVelX());
            } else {
                sended = true;
            }
        }

        if (this.getPosX() + RADIUS >= this.getGame().getViewerWidth() && this.getVelX() > 0 && !sended) {
            if (!this.getGame().sendObjectToNext(this)) {
                this.setVelX(-this.getVelX());
            } else {
                sended = true;
            }
        }

        // Detectores verticales
        if (this.getPosY() - RADIUS <= 0 && this.getVelY() < 0) {
            this.setVelY(-this.getVelY());
        }

        if (this.getPosY() + RADIUS >= this.getGame().getViewerHeight() && this.getVelY() > 0) {
            this.setVelY(-this.getVelY());
        }

        // Matar thread
        if (this.getPosX() + RADIUS < 0 && this.getVelX() < 0) {
            this.getGame().getObjects().remove(this);
        }

        if (this.getPosX() - RADIUS > this.getGame().getViewerWidth() && this.getVelX() > 0) {
            this.getGame().getObjects().remove(this);
        }

        super.move();
        this.setShape(this.createShape());

    }

    public Shape createShape() {
        Ellipse2D.Double shape = null;
        shape = new Ellipse2D.Double(this.getPosX() - RADIUS, this.getPosY() - RADIUS, RADIUS * 2, RADIUS * 2);
        return shape;
    }

    @Override
    public void paint(BufferedImage image) {
        Graphics g = image.getGraphics();
        g.setColor(this.getColor());
        g.fillOval(this.getPosX() - RADIUS, this.getPosY() - RADIUS, RADIUS * 2, RADIUS * 2);
    }
    
    @Override
    public void collision() {

        super.collision();

    }

    // Main activity
}
