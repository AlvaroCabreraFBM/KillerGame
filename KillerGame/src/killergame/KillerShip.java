package killergame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "ship")
@XmlType(propOrder = {"id", "name"})
public class KillerShip extends Controllable {

    // Attributes
    private String id;
    public static final int RADIUS = 20;
    public static final int SHOOTCOOLDOWN = 50;
    private int shootTimer;
    private boolean sended;
    private String name;

    // Constructors
    public KillerShip(KillerGame game, int posX, int posY, int red, int green, int blue, int velX, int velY, String id, String name) {
        super(game, posX, posY, "SHP", red, green, blue, velX, velY);
        this.id = id;
        this.name = name;
        this.shootTimer = 0;
        this.setShape(this.createShape());
        sended = false;

    }

    public KillerShip() {
        this.setShape(this.createShape());
        sended = false;
    }

    // Methods
    public synchronized void moveUp() {
        this.setVelX(0);
        this.setVelY(-2);
    }

    public synchronized void moveUpLeft() {
        this.setVelX(-1);
        this.setVelY(-1);
    }

    public synchronized void moveUpRight() {
        this.setVelX(1);
        this.setVelY(-1);
    }

    public synchronized void moveLeft() {
        this.setVelX(-2);
        this.setVelY(0);
    }

    public synchronized void moveRight() {
        this.setVelX(2);
        this.setVelY(0);
    }

    public synchronized void moveDown() {
        this.setVelX(0);
        this.setVelY(2);
    }

    public synchronized void moveDownLeft() {
        this.setVelX(-1);
        this.setVelY(1);
    }

    public synchronized void moveDownRight() {
        this.setVelX(1);
        this.setVelY(1);
    }

    public synchronized void stop() {
        this.setVelX(0);
        this.setVelY(0);
    }

    public synchronized void shootUp() {
        if (this.shootTimer == 0) {
            this.getGame().newKillerBullet(this.getPosX(), this.getPosY() - (KillerShip.RADIUS + KillerBullet.RADIUS), this.getRed(), this.getGreen(), this.getBlue(), 0, -3);
            this.shootTimer = SHOOTCOOLDOWN;
        }
    }

    public synchronized void shootUpLeft() {
        if (this.shootTimer == 0) {
            this.getGame().newKillerBullet(this.getPosX() - (KillerShip.RADIUS + KillerBullet.RADIUS), this.getPosY() - (KillerShip.RADIUS + KillerBullet.RADIUS), this.getRed(), this.getGreen(), this.getBlue(), -3, -3);
            this.shootTimer = SHOOTCOOLDOWN;
        }
    }

    public synchronized void shootUpRight() {
        if (this.shootTimer == 0) {
            this.getGame().newKillerBullet(this.getPosX() + (KillerShip.RADIUS + KillerBullet.RADIUS), this.getPosY() - (KillerShip.RADIUS + KillerBullet.RADIUS), this.getRed(), this.getGreen(), this.getBlue(), 3, -3);
            this.shootTimer = SHOOTCOOLDOWN;
        }
    }

    public synchronized void shootLeft() {
        if (this.shootTimer == 0) {
            this.getGame().newKillerBullet(this.getPosX() - (KillerShip.RADIUS + KillerBullet.RADIUS), this.getPosY(), this.getRed(), this.getGreen(), this.getBlue(), -3, 0);
            this.shootTimer = SHOOTCOOLDOWN;
        }
    }

    public synchronized void shootRight() {
        if (this.shootTimer == 0) {
            this.getGame().newKillerBullet(this.getPosX() + (KillerShip.RADIUS + KillerBullet.RADIUS + 15), this.getPosY(), this.getRed(), this.getGreen(), this.getBlue(), 3, 0);
            this.shootTimer = SHOOTCOOLDOWN;
        }
    }

    public synchronized void shootDown() {
        if (this.shootTimer == 0) {
            this.getGame().newKillerBullet(this.getPosX(), this.getPosY() + (KillerShip.RADIUS + KillerBullet.RADIUS), this.getRed(), this.getGreen(), this.getBlue(), 0, 3);
            this.shootTimer = SHOOTCOOLDOWN;
        }
    }

    public synchronized void shootDownLeft() {
        if (this.shootTimer == 0) {
            this.getGame().newKillerBullet(this.getPosX() - (KillerShip.RADIUS + KillerBullet.RADIUS), this.getPosY() + (KillerShip.RADIUS + KillerBullet.RADIUS), this.getRed(), this.getGreen(), this.getBlue(), -3, 3);
            this.shootTimer = SHOOTCOOLDOWN;
        }
    }

    public synchronized void shootDownRight() {
        if (this.shootTimer == 0) {
            this.getGame().newKillerBullet(this.getPosX() + (KillerShip.RADIUS + KillerBullet.RADIUS), this.getPosY() + (KillerShip.RADIUS + KillerBullet.RADIUS), this.getRed(), this.getGreen(), this.getBlue(), 3, 3);
            this.shootTimer = SHOOTCOOLDOWN;
        }
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
        g.setColor(Color.WHITE);
        g.drawString(name, this.getPosX() - (name.length() / 2) * 5, this.getPosY() - (RADIUS + 10));
    }

    @Override
    public void move() {

        // Detectores horizontales
        if (this.getPosX() - RADIUS <= 0 && this.getVelX() < 0) {
            if (sended == false) {
                if (!this.getGame().sendObjectToPrevius(this)) {
                    this.stop();
                } else {
                    sended = true;
                }
            }
        }

        if (this.getPosX() + RADIUS >= this.getGame().getViewerWidth() && this.getVelX() > 0) {
            if (!sended) {
                if (!this.getGame().sendObjectToNext(this)) {
                    this.stop();
                } else {
                    sended = true;
                }
            }
        }

        // Detectores verticales
        if (this.getPosY() - RADIUS <= 0 && this.getVelY() < 0) {
            this.stop();
        }

        if (this.getPosY() + RADIUS >= this.getGame().getViewerHeight() && this.getVelY() > 0) {
            this.stop();
        }

        // Matar thread
        if (this.getPosX() + RADIUS <= 0 && this.getVelX() < 0) {
            this.getGame().getObjects().remove(this);
        }

        if (this.getPosX() - RADIUS >= this.getGame().getViewerWidth() && this.getVelX() > 0) {
            this.getGame().getObjects().remove(this);
        }

        super.move();
        this.setShape(this.createShape());
    }

    @Override
    public void collision() {

        super.collision();

        this.getGame().playSoundEfect(1);

        boolean succes = false;
        for (int inc = 0; inc < this.getGame().getPads().size(); inc++) {
            KillerPad pad = this.getGame().getPads().get(inc);
            if (pad.getHost().equals(id)) {
                pad.closeConnection();
                succes = true;
            }
        }

        if (!succes) {
            this.getGame().getNextModule().sendCommand("PADBYE" + this.id);
            this.getGame().getPrevModule().sendCommand("PADBYE" + this.id);
        }

    }

    // Methods get
    public String getId() {
        return id;
    }

    public String getName() {
        return this.name;
    }

    public boolean isSended() {
        return sended;
    }

    // Methods set
    public void setId(String ID) {
        this.id = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Main activity
    @Override
    public void run() {

        while (this.isAlive()) {

            this.move();

            if (this.getGame().checkCollision(this)) {
                this.collision();
            }

            if (this.shootTimer > 0) {
                this.shootTimer--;
            }

            try {
                Thread.sleep((long) (0.016683 * 1000));
            } catch (InterruptedException ex) {

            }

        }

    }

}
