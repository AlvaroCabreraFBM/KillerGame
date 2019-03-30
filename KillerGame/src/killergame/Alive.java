package killergame;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "alive")
@XmlType(propOrder = {"velX", "velY"})
public class Alive extends VisualObject implements Runnable{

    // Attributes
    private int velX;
    private int velY;
    private boolean alive;
    
    // Constructors
    public Alive(KillerGame game, int posX, int posY, String type, int red, int green, int blue, int velX, int velY) {
        super(game, posX, posY, type, red, green, blue);
        this.velX = velX;
        this.velY = velY;
        alive = true;
    }

    public Alive() {
        alive = true;
    }
    
    // Methods
    public void move() {
        this.setPosX(this.getPosX() + velX);
        this.setPosY(this.getPosY() + velY);
    }
    
    @Override
    public void collision() {
        setDead();
        this.getGame().getObjects().remove(this);
    }
    
    // Methods get
    public int getVelX() {
        return velX;
    }

    public int getVelY() {
        return velY;
    }
    
    public boolean isAlive() {
        return this.alive;
    }
    
    // Methods set
    public void setVelX(int velX) {
        this.velX = velX;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }
    
    public void setDead() {
        alive = false;
    }
    
    // Main activity
    @Override
    public void run() {

        while (alive) {

            this.move();

            if (this.getGame().checkCollision(this)) {
                this.collision();
            }

            try {
                Thread.sleep((long) (0.016683 * 1000));
            } catch (InterruptedException ex) {

            }

        }

    }

}
