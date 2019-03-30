package killergame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VisualHandler implements Runnable {

    // Attributes
    private KillerGame game;
    private KillerClient client;
    private String host;
    private int port;
    private int portToReconnect;
    private int serverPort;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String type;
    private boolean connected;

    // Constructror
    public VisualHandler(KillerGame game, String type) {
        this.game = game;
        this.newKillerClient();
        this.type = type;
        this.connected = false;
    }

    // Methods
    public void makeContact() {
        
        this.socket = null;

        try {
            
            this.socket = new Socket(host, portToReconnect);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            
            System.out.println("Connected to [" + this.host + "].");
            
            connected = true;
            
            this.sendCommand("CON" + type + this.serverPort);
            
        } catch (IOException e) {
            
            System.out.println("Fail to connected to [" + this.host + "] [" + this.portToReconnect + "].");
            connected = false;
            
        }

    }

    public void sendCommand(String command) {
        try {
            System.out.println("Sending comand : " + command + " to [" + this.host + "]...");
            out.println(command);
        } catch (Exception error) {
        }
    }

    private String getLine() {

        String line;

        try {
            line = this.in.readLine();
            if (!(line == null)) {
                System.out.println("Command from [" + this.host + "] : " + line + ".");
            }
        } catch (IOException error) {
            line = null;
            this.connected = false;
        } catch (NullPointerException error) {
            line = null;
        }

        return line;

    }

    private void processLine(String line) {

        boolean done = false;
        String command = "NUL";

        try {
            command = line.toUpperCase().substring(0, 3);
        } catch (Exception error) {
            done = true;
        }

        // Precess OBJ command
        if (!done && command.equals("OBJ")) {
            System.out.println("Precesing command OBJ from [" + this.host + "]...");
            this.processObject(line.substring(3));
            done = true;
        }

        // Precess PAD command
        if (!done && command.equals("PAD")) {
            System.out.println("Precesing command PAD from [" + this.host + "]...");
            this.processPad(line.substring(3));
            done = true;
        }
        
        //
        if (!done && command.equals("CON")) {
            System.out.println("Precesing command CON from [" + this.host + "]...");
            this.connected = true;
            done = true;
        }

        // Precess BYE command
        if (!done && command.equals("BYE")) {
            //System.out.println("Precesing command BYE from [" + this.host + "]...");
            this.closeConnection();
            done = true;
        }

        if (!done) {
            System.out.println("Ignoring command from [" + this.host + "].");
        }

    }

    private void processObject(String line) {

        boolean done = false;
        String command = "NUL";

        try {
            command = line.toUpperCase().substring(0, 3);
        } catch (Exception error) {
            done = true;
        }

        // Precess SHP command
        if (!done && command.equals("SHP")) {
            System.out.println("Precesing command SHP from [" + this.host + "]...");
            this.game.addKillerShip(this.game.xmlToKillerShip(line.substring(3)), this.type);
            done = true;
        }

        // Precess BUL command
        if (!done && command.equals("BUL")) {
            System.out.println("Precesing command BUL from [" + this.host + "]...");
            this.game.addKillerBullet(this.game.xmlToKillerBullet(line.substring(3)), this.type);
            done = true;
        }

        if (!done) {
            System.out.println("Ignoring command from [" + this.host + "].");
        }

    }

    private void processPad(String line) {

        boolean done = false;
        String command = "NUL";

        try {
            command = line.toUpperCase().substring(0, 3);
        } catch (Exception error) {
            System.out.println("Ignoring command from [" + this.host + "].");
            done = true;
        }

        // Precess MOV command
        if (!done && command.equals("MOV")) {
            System.out.println("Precesing command MOV from [" + this.host + "]...");
            this.processMove(line.substring(3));
            done = true;
        }

        // Precess STP command
        if (!done && command.equals("STP")) {
            System.out.println("Precesing command STP from [" + this.host + "]...");
            this.procesStop(line.substring(3));
            done = true;
        }

        // Precess SHT command
        if (!done && command.equals("SHT")) {
            System.out.println("Precesing command SHT from [" + this.host + "]...");
            this.processShoot(line.substring(3));
            done = true;
        }

        // Precess BYE command
        if (!done && command.equals("BYE")) {

            System.out.println("Precesing command BYE from [" + this.host + "]...");
            processPadBye(line);
            done = true;
        }

        if (!done) {
            System.out.println("Ignoring command from [" + this.host + "].");
        }

    }

    private void processMove(String line) {

        boolean done = false;
        String command = "NUL";

        try {
            command = line.toUpperCase().substring(0, 3);
        } catch (Exception error) {
            System.out.println("The movement direction is uncompatible with the game.");
            done = true;
        }

        // Precess UPP command
        if (!done && command.equals("UPP")) {
            System.out.println("Precesing command UPP from [" + this.host + "]...");
            if (!KillerAction.moveUp(line.substring(3), this.game.getObjects())) {
                this.sendCommand("PADSHT" + command + line.substring(3));
            }
            done = true;
        }

        // Precess UPP command
        if (!done && command.equals("UPL")) {
            System.out.println("Precesing command UPL from [" + this.host + "]...");
            if (!KillerAction.moveUpLeft(line.substring(3), this.game.getObjects())) {
                this.sendCommand("PADSHT" + command + line.substring(3));
            }
            done = true;
        }

        // Precess UPP command
        if (!done && command.equals("UPR")) {
            System.out.println("Precesing command UPR from [" + this.host + "]...");
            if (!KillerAction.moveUpRight(line.substring(3), this.game.getObjects())) {
                this.sendCommand("PADSHT" + command + line.substring(3));
            }
            done = true;
        }

        // Precess LEF command
        if (!done && command.equals("LEF")) {
            System.out.println("Precesing command LEF from [" + this.host + "]...");
            if (!KillerAction.moveLeft(line.substring(3), this.game.getObjects())) {
                this.sendCommand("PADSHT" + command + line.substring(3));
            }
            done = true;
        }

        // Precess RIG command
        if (!done && command.equals("RIG")) {
            System.out.println("Precesing command RIG from [" + this.host + "]...");
            if (!KillerAction.moveRight(line.substring(3), this.game.getObjects())) {
                this.sendCommand("PADSHT" + command + line.substring(3));
            }
            done = true;
        }

        // Precess DOW command
        if (!done && command.equals("DOW")) {
            System.out.println("Precesing command DOW from [" + this.host + "]...");
            if (!KillerAction.moveDown(line.substring(3), this.game.getObjects())) {
                this.sendCommand("PADSHT" + command + line.substring(3));
            }
            done = true;
        }

        // Precess DOW command
        if (!done && command.equals("DOL")) {
            System.out.println("Precesing command DOL from [" + this.host + "]...");
            if (!KillerAction.moveDownLeft(line.substring(3), this.game.getObjects())) {
                this.sendCommand("PADSHT" + command + line.substring(3));
            }
            done = true;
        }

        // Precess DOW command
        if (!done && command.equals("DOR")) {
            System.out.println("Precesing command DOR from [" + this.host + "]...");
            if (!KillerAction.moveDownRight(line.substring(3), this.game.getObjects())) {
                this.sendCommand("PADSHT" + command + line.substring(3));
            }
            done = true;
        }

        if (!done) {
            System.out.println("The movement direction is uncompatible with the game.");
        }

        /*
        
        HAY QUE PONER EL IF DE QUE SI LA NAVE NO ESTA EN ESTE VISUAL PASARLO AL SIGUIENTE
        
         */
    }

    private void processShoot(String line) {

        boolean done = false;
        String command = "NUL";

        try {
            command = line.toUpperCase().substring(0, 3);
        } catch (Exception error) {
            System.out.println("The shoot direction is uncompatible with the game.");
            done = true;
        }

        // Precess UPP command
        if (!done && command.equals("UPP")) {
            System.out.println("Precesing command UPP from [" + this.host + "]...");
            if (!KillerAction.shootUp(line.substring(3), this.game.getObjects())) {
                this.sendCommand("PADSHT" + command + line.substring(3));
            }
            done = true;
        }

        // Precess UPL command
        if (!done && command.equals("UPL")) {
            System.out.println("Precesing command UPL from [" + this.host + "]...");
            if (!KillerAction.shootUpLeft(line.substring(3), this.game.getObjects())) {
                this.sendCommand("PADSHT" + command + line.substring(3));
            }
            done = true;
        }

        // Precess UPR command
        if (!done && command.equals("UPR")) {
            System.out.println("Precesing command UPR from [" + this.host + "]...");
            if (!KillerAction.shootUpRight(line.substring(3), this.game.getObjects())) {
                this.sendCommand("PADSHT" + command + line.substring(3));
            }
            done = true;
        }

        // Precess LEF command
        if (!done && command.equals("LEF")) {
            System.out.println("Precesing command LEF from [" + this.host + "]...");
            if (!KillerAction.shootLeft(line.substring(3), this.game.getObjects())) {
                this.sendCommand("PADSHT" + command + line.substring(3));
            }
            done = true;
        }

        // Precess RIG command
        if (!done && command.equals("RIG")) {
            System.out.println("Precesing command RIG from [" + this.host + "]...");
            if (!KillerAction.shootRight(line.substring(3), this.game.getObjects())) {
                this.sendCommand("PADSHT" + command + line.substring(3));
            }
            done = true;
        }

        // Precess DOW command
        if (!done && command.equals("DOW")) {
            System.out.println("Precesing command DOW from [" + this.host + "]...");
            if (!KillerAction.shootDown(line.substring(3), this.game.getObjects())) {
                this.sendCommand("PADSHT" + command + line.substring(3));
            }
            done = true;
        }

        // Precess DOL command
        if (!done && command.equals("DOL")) {
            System.out.println("Precesing command DOL from [" + this.host + "]...");
            if (!KillerAction.shootDownLeft(line.substring(3), this.game.getObjects())) {
                this.sendCommand("PADSHT" + command + line.substring(3));
            }
            done = true;
        }

        // Precess DOR command
        if (!done && command.equals("DOR")) {
            System.out.println("Precesing command DOR from [" + this.host + "]...");
            if (!KillerAction.shootDownRight(line.substring(3), this.game.getObjects())) {
                this.sendCommand("PADSHT" + command + line.substring(3));
            }
            done = true;
        }

        if (!done) {
            System.out.println("The movement direction is uncompatible with the game.");
        }

    }

    private void procesStop(String line) {
        System.out.println("Precesing command STP from [" + this.host + "]...");
        System.out.println(line);
        if (!KillerAction.stop(line, this.game.getObjects())) {
            this.sendCommand("PADSTP" + line.substring(3));
        }
    }

    private void processPadBye(String line) {
        boolean succes = false;
        for (int inc = 0; inc < this.game.getPads().size(); inc++) {
            KillerPad pad = this.game.getPads().get(inc);
            if (pad.getHost().equals(line.substring(3))) {
                pad.closeConnection();
                succes = true;
            }
        }
        if (!succes) {
            this.sendCommand("PADBYE" + line.substring(3));
        }
    }

    public void closeConnection() {

        if (this.isConnected()) {

            this.sendCommand("BYE");

            try {
                this.socket.close();
                this.socket = null;
                connected = false;
                System.out.println("Disconnected from [" + this.host + "].");
            } catch (IOException | NullPointerException error) {

            }

        }

    }

    // Methods new
    public void newKillerClient() {
        this.client = new KillerClient(this, 2000);
        new Thread(this.client).start();
    }

    // Methods get
    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return port;
    }

    public boolean isConnected() {
        return this.connected;
    }

    // Methods set 
    public void setHost(String host) {
        this.host = host;
    }

    public void setPortToReconnect(int portToReconnect) {
        this.portToReconnect = portToReconnect;
    }
    
    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public void setSocket(Socket socket, int portToReconnect) {
        
        this.closeConnection();
        
        this.socket = socket;
        this.host = socket.getInetAddress().getHostAddress();
        this.port = socket.getPort();
        this.portToReconnect = portToReconnect;
        
        System.out.println(this.port + " " + this.portToReconnect + " " + this.serverPort);

        try {
            
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Connected to [" + this.host + "].");
            connected = true;
            
        } catch (IOException e) {
            
            System.out.println("Fail to connected to [" + this.host + "].");
            connected = false;
            
        }

    }

    // Main activity
    @Override
    public void run() {

        while (true) {
            this.processLine(this.getLine());
        }

    }

}
