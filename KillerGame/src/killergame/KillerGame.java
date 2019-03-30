package killergame;

import java.awt.GridLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import sun.audio.*;

public class KillerGame extends JFrame {

    // Atributes
    private KillerServer server;
    private boolean createdServer;
    private VisualHandler prevModule;
    private VisualHandler nextModule;
    private ControlPanel panel;
    private ArrayList<KillerPad> pads;
    private ArrayList<VisualObject> objects;
    private Viewer viewer;

    // Constructor
    public KillerGame() {

        // Initzialize Pads ArryList
        this.pads = new ArrayList();

        // Initzialize Pads ArryList
        this.objects = new ArrayList();

        // Ser createdServer to false
        this.createdServer = false;

        // Mostrar ventan
        this.setUpWindow();

        // Añadir viewer
        this.newViewer();

        // Añadir panel
        this.panel = new ControlPanel(this);
        
        // Create Prev and Next module
        this.newPrevModule();
        this.newNextModule();

        // Create server
        this.newKillerServer(8000);

    }

    // Methods
    public boolean checkCollision(VisualObject object) {
        return KillerRules.testCollision(object, this.objects);
    }

    public void setUpWindow() {
        this.setSize(1120, 630);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new GridLayout());
        this.setResizable(false);
        this.setVisible(true);
    }

    public boolean sendObjectToPrevius(VisualObject object) {

        boolean result = false;

        if (this.prevModule.isConnected()) {
            this.prevModule.sendCommand("OBJ" + object.getType() + this.objectToXML(object));
            result = true;
        }

        return result;

    }

    public boolean sendObjectToNext(VisualObject object) {

        boolean result = false;

        if (this.nextModule.isConnected()) {
            this.nextModule.sendCommand("OBJ" + object.getType() + this.objectToXML(object));
            result = true;
        }

        return result;

    }

    // Methods new
    public void newKillerServer(int port) {
        if (!this.createdServer) {
            this.server = new KillerServer(this, port);
            new Thread(this.server).start();
            this.createdServer = true;
        }
    }

    public void newPrevModule() {
        this.prevModule = new VisualHandler(this, "RIG");
        new Thread(this.prevModule).start();
    }

    public void newNextModule() {
        this.nextModule = new VisualHandler(this, "LEF");
        new Thread(this.nextModule).start();
    }

    public void newKillerPad(Socket socket, String params) {
        KillerPad pad = new KillerPad(this, "PAD", socket);
        this.pads.add(pad);
        new Thread(pad).start();
        int red, green, blue;
        try {
            red = Integer.parseInt(params.substring(0, 3));
            green = Integer.parseInt(params.substring(3, 6));
            blue = Integer.parseInt(params.substring(6, 9));
        } catch (NumberFormatException error) {
            red = 255;
            green = 255;
            blue = 255;
        }
        this.newKillerShip(this.viewer.getWidth() / 2, this.viewer.getHeight() / 2, blue, green, red, 0, 0, pad.getHost(), params.substring(9));
    }

    private void newViewer() {
        this.viewer = new Viewer(this);
        this.add(this.viewer, 0, 0);
        new Thread(this.viewer).start();
    }

    public void newKillerShip(int posX, int posY, int red, int green, int blue, int velX, int velY, String id, String name) {
        KillerShip ship = new KillerShip(this, posX, posY, red, green, blue, velX, velY, id, name);
        this.objects.add(ship);
        new Thread(ship).start();
    }

    public void newKillerBullet(int posX, int posY, int red, int green, int blue, int velX, int velY) {
        this.playSoundEfect(0);
        KillerBullet bullet = new KillerBullet(this, posX, posY, red, green, blue, velX, velY);
        this.objects.add(bullet);
        new Thread(bullet).start();
    }

    public String objectToXML(Object object) {
        String result = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
            Marshaller marshaller = jaxbContext.createMarshaller();
            StringWriter StringWriter = new StringWriter();
            marshaller.marshal(object, StringWriter);
            result = StringWriter.toString();
            result = result.substring(55);
        } catch (JAXBException error) {
            System.out.println(error);
        }
        return result;
    }

    public KillerShip xmlToKillerShip(String xml) {
        KillerShip ship = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(KillerShip.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader stringReader = new StringReader(xml);
            ship = (KillerShip) unmarshaller.unmarshal(stringReader);
        } catch (JAXBException error) {
            System.out.println(error);
        }
        return ship;
    }

    public KillerBullet xmlToKillerBullet(String xml) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(KillerBullet.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader stringReader = new StringReader(xml);
            KillerBullet bullet = (KillerBullet) unmarshaller.unmarshal(stringReader);
            return bullet;
        } catch (JAXBException error) {
            System.out.println(error);
        }
        return null;
    }

    // Methods add
    public void addKillerShip(KillerShip ship, String position) {
        if (position.equals("LEF")) {
            ship.setPosX(this.getViewerWidth() - KillerShip.RADIUS);
        }
        if (position.equals("RIG")) {
            ship.setPosX(0 - KillerShip.RADIUS);
        }
        ship.setKillerGame(this);
        this.objects.add(ship);
        new Thread(ship).start();
    }

    public void addKillerBullet(KillerBullet bullet, String position) {
        if (position.equals("LEF")) {
            bullet.setPosX(this.getViewerWidth() - KillerShip.RADIUS);
        }
        if (position.equals("RIG")) {
            bullet.setPosX(0 - KillerShip.RADIUS);
        }
        bullet.setKillerGame(this);
        this.objects.add(bullet);
        new Thread(bullet).start();
    }

    // Methods get
    public KillerServer getServer() {
        return server;
    }

    public ControlPanel getPanel() {
        return this.panel;
    }

    public VisualHandler getPrevModule() {
        return this.prevModule;
    }

    public VisualHandler getNextModule() {
        return this.nextModule;
    }

    public ArrayList<VisualObject> getObjects() {
        return this.objects;
    }

    public int getViewerWidth() {
        return this.viewer.getWidth();
    }

    public int getViewerHeight() {
        return this.viewer.getHeight();
    }

    public ArrayList<KillerPad> getPads() {
        return pads;
    }

    public void playSoundEfect(int type) {

        String path = "src/sfx/shoot.wav";

        switch (type) {

            case 0: // Shoot sound

                path = "src/sfx/shoot.wav";

                break;

            case 1: // Explosion sound

                path = "src/sfx/explosion.wav";

                break;
        }

        try {
            FileInputStream file = new FileInputStream(path);
            AudioStream audio = new AudioStream(file);
            AudioPlayer.player.start(audio);
        } catch (IOException error) {
            System.out.println(error);
        }

    }

    // Main activity
    public static void main(String[] args) {
        KillerGame game = new KillerGame();
    }

}
