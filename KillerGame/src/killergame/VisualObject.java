package killergame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "visualObject")
@XmlType(propOrder = {"posX", "posY", "type", "red", "blue", "green"})
public class VisualObject {

    // Attributes
    private KillerGame killerGame;

    private int posX;
    private int posY;

    private String type;

    private int red;
    private int green;
    private int blue;

    private Shape shape;

    // Constructors
    public VisualObject(KillerGame game, int posX, int posY, String type, int red, int green, int blue) {
        this.killerGame = game;
        this.posX = posX;
        this.posY = posY;
        this.type = type;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public VisualObject() {

    }

    // Methods
    public void paint(BufferedImage image) {

        Graphics g = image.getGraphics();
        g.setColor(this.getColor());

    }

    public void collision() {

    }

    // Methods get
    public KillerGame getGame() {
        return this.killerGame;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public String getType() {
        return type;
    }
    
    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }
    
    public Color getColor() {
        return new Color(blue, green, red);
    }

    public Area getArea() {
        return new Area(shape);
    }

    // Methods set
    public void setKillerGame(KillerGame game) {
        this.killerGame = game;
    }
    
    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public void setRed(int red) {
        this.red = red;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

}
