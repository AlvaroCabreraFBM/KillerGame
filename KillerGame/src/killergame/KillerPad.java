package killergame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class KillerPad implements Runnable {

    // Attributes
    private KillerGame game;
    private KillerClient client;
    private String host;
    private int port;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String type;
    private boolean connected;
    boolean exit;

    // Constructror
    public KillerPad(KillerGame game, String type, Socket socket) {
        this.game = game;
        this.newKillerClient();
        this.type = type;
        this.connected = false;
        this.exit = false;
        this.setSocket(socket);
    }

    // Methods
    public void makeContact() {

        try {
            this.socket = new Socket(host, port);
            if (socket.getInetAddress().isReachable(port)) {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                System.out.println("Connected to [" + this.host + "].");
                connected = true;
            } else {
                System.out.println("Fail to connected to [" + this.host + "].");
                connected = false;
            }
        } catch (IOException e) {
            System.out.println("Fail to connected to [" + this.host + "].");
            connected = false;
        }

        this.sendCommand("CON" + type);

    }

    public void sendCommand(String command) {
        try {
            //System.out.println("Sending comand : " + command + " to [" + this.host + "]...");
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
            this.closeConnection();
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

        // Precess MOV command
        if (!done && command.equals("MOV")) {
            System.out.println("Precesing command MOV from [" + this.host + "]...");
            this.processMove(line.substring(3));
            done = true;
        }

        // Precess STP command
        if (!done && command.equals("STP")) {
            System.out.println("Precesing command STP from [" + this.host + "]...");
            this.procesStop();
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
            this.closeConnection();
            done = true;
        }

        if (!done) {
            //System.out.println("Ignoring command from [" + this.host + "].");
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
            if (!KillerAction.moveUp(this.host, this.game.getObjects())) {
                this.game.getPrevModule().sendCommand("PADMOV" + command + this.host);
                this.game.getNextModule().sendCommand("PADMOV" + command + this.host);

            }
            done = true;
        }

        // Precess UPP command
        if (!done && command.equals("UPL")) {
            System.out.println("Precesing command UPL from [" + this.host + "]...");
            if (!KillerAction.moveUpLeft(this.host, this.game.getObjects())) {
                this.game.getPrevModule().sendCommand("PADMOV" + command + this.host);
                this.game.getNextModule().sendCommand("PADMOV" + command + this.host);
            }
            done = true;
        }

        // Precess UPP command
        if (!done && command.equals("UPR")) {
            System.out.println("Precesing command UPR from [" + this.host + "]...");
            if (!KillerAction.moveUpRight(this.host, this.game.getObjects())) {
                this.game.getPrevModule().sendCommand("PADMOV" + command + this.host);
                this.game.getNextModule().sendCommand("PADMOV" + command + this.host);
            }
            done = true;
        }

        // Precess LEF command
        if (!done && command.equals("LEF")) {
            System.out.println("Precesing command LEF from [" + this.host + "]...");
            if (!KillerAction.moveLeft(this.host, this.game.getObjects())) {
                this.game.getPrevModule().sendCommand("PADMOV" + command + this.host);
                this.game.getNextModule().sendCommand("PADMOV" + command + this.host);
            }
            done = true;
        }

        // Precess RIG command
        if (!done && command.equals("RIG")) {
            System.out.println("Precesing command RIG from [" + this.host + "]...");
            if (!KillerAction.moveRight(this.host, this.game.getObjects())) {
                this.game.getPrevModule().sendCommand("PADMOV" + command + this.host);
                this.game.getNextModule().sendCommand("PADMOV" + command + this.host);
            }
            done = true;
        }

        // Precess DOW command
        if (!done && command.equals("DOW")) {
            System.out.println("Precesing command DOW from [" + this.host + "]...");
            if (!KillerAction.moveDown(this.host, this.game.getObjects())) {
                this.game.getPrevModule().sendCommand("PADMOV" + command + this.host);
                this.game.getNextModule().sendCommand("PADMOV" + command + this.host);
            }
            done = true;
        }

        // Precess DOW command
        if (!done && command.equals("DOL")) {
            System.out.println("Precesing command DOL from [" + this.host + "]...");
            if (!KillerAction.moveDownLeft(this.host, this.game.getObjects())) {
                this.game.getPrevModule().sendCommand("PADMOV" + command + this.host);
                this.game.getNextModule().sendCommand("PADMOV" + command + this.host);
            }
            done = true;
        }

        // Precess DOW command
        if (!done && command.equals("DOR")) {
            System.out.println("Precesing command DOR from [" + this.host + "]...");
            if (!KillerAction.moveDownRight(this.host, this.game.getObjects())) {
                this.game.getPrevModule().sendCommand("PADMOV" + command + this.host);
                this.game.getNextModule().sendCommand("PADMOV" + command + this.host);
            }
            done = true;
        }

        if (!done) {
            System.out.println("The movement direction is uncompatible with the game.");
        }

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
            if (!KillerAction.shootUp(this.host, this.game.getObjects())) {
                this.game.getNextModule().sendCommand("PADSHT" + command + this.host);
            }
            done = true;
        }

        // Precess UPL command
        if (!done && command.equals("UPL")) {
            System.out.println("Precesing command UPL from [" + this.host + "]...");
            if (!KillerAction.shootUpLeft(this.host, this.game.getObjects())) {
                this.game.getNextModule().sendCommand("PADSHT" + command + this.host);
            }
            done = true;
        }

        // Precess UPR command
        if (!done && command.equals("UPR")) {
            System.out.println("Precesing command UPR from [" + this.host + "]...");
            if (!KillerAction.shootUpRight(this.host, this.game.getObjects())) {
                this.game.getNextModule().sendCommand("PADSHT" + command + this.host);
            }
            done = true;
        }

        // Precess LEF command
        if (!done && command.equals("LEF")) {
            System.out.println("Precesing command LEF from [" + this.host + "]...");
            if (!KillerAction.shootLeft(this.host, this.game.getObjects())) {
                this.game.getNextModule().sendCommand("PADSHT" + command + this.host);
            }
            done = true;
        }

        // Precess RIG command
        if (!done && command.equals("RIG")) {
            System.out.println("Precesing command RIG from [" + this.host + "]...");
            if (!KillerAction.shootRight(this.host, this.game.getObjects())) {
                this.game.getNextModule().sendCommand("PADSHT" + command + this.host);
            }
            done = true;
        }

        // Precess DOW command
        if (!done && command.equals("DOW")) {
            System.out.println("Precesing command DOW from [" + this.host + "]...");
            if (!KillerAction.shootDown(this.host, this.game.getObjects())) {
                this.game.getNextModule().sendCommand("PADSHT" + command + this.host);
            }
            done = true;
        }

        // Precess DOL command
        if (!done && command.equals("DOL")) {
            System.out.println("Precesing command DOL from [" + this.host + "]...");
            if (!KillerAction.shootDownLeft(this.host, this.game.getObjects())) {
                this.game.getNextModule().sendCommand("PADSHT" + command + this.host);
            }
            done = true;
        }

        // Precess DOR command
        if (!done && command.equals("DOR")) {
            System.out.println("Precesing command DOR from [" + this.host + "]...");
            if (!KillerAction.shootDownRight(this.host, this.game.getObjects())) {
                this.game.getNextModule().sendCommand("PADSHT" + command + this.host);
            }
            done = true;
        }

        if (!done) {
            System.out.println("The movement direction is uncompatible with the game.");
        }

    }

    private void procesStop() {
        System.out.println("Precesing command STP from [" + this.host + "]...");
        if (!KillerAction.stop(this.host, this.game.getObjects())) {
            this.game.getNextModule().sendCommand("PADSTP" + this.host);
        }
    }

    public void closeConnection() {

        this.sendCommand("BYE");

        try {
            this.socket.close();
            this.socket = null;
            connected = false;
            System.out.println("Disconnected from [" + this.host + "].");
            this.game.getPads().remove(this);
        } catch (IOException | NullPointerException error) {

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

    public void setPort(int port) {
        this.port = port;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
        this.host = socket.getInetAddress().getHostAddress();
        this.port = socket.getPort();
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

        while (!exit) {
            this.processLine(this.getLine());
        }

    }

}
