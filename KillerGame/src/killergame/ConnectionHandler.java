package killergame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionHandler implements Runnable {

    // Atributes
    private KillerGame game;
    private Socket client;
    private String address;
    private PrintWriter out;
    private BufferedReader in;
    private boolean success;

    // Constructors
    public ConnectionHandler(KillerGame game, Socket client) {

        this.game = game;
        this.client = client;
        this.address = this.client.getInetAddress().getHostAddress();
        this.success = false;

    }

    // Methods
    private void makeContact() {

        try {
            this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
            this.out = new PrintWriter(this.client.getOutputStream(), true);
            System.out.println("Connected to [" + this.address + "].");
        } catch (IOException error) {
            System.out.println("Fail to connected to [" + this.address + "].");
        }

    }

    private String getLine() {

        String line = null;

        try {
            line = this.in.readLine();
            if (line != null) {
                System.out.println("Command from [" + this.address + "] : " + line + ".");
                this.success = true;
            }
        } catch (IOException error) {
            System.out.println("Fail to read line from [" + this.address + "].");
        }

        return line;

    }

    private boolean processLine(String line) {

        boolean done = false;
        String command = "NUL";

        try {
            command = line.toUpperCase().substring(0, 3);
        } catch (Exception error) {
            System.out.println("Ignoring command from [" + this.address + "].");
        }

        // Precess CON command
        if (!done && command.equals("CON")) {
            System.out.println("Procesing command CON Ofrom [" + this.address + "]...");
            done = processConnection(line.substring(3));
        }

        if (!done) {
            System.out.println("Ignoring command from [" + this.address + "].");
        }
        
        return done;

    }

    private boolean processConnection(String line) {

        boolean done = false;
        String command = "NUL";

        try {
            command = line.toUpperCase().substring(0, 3);
        } catch (Exception error) {
            System.out.println("Ignoring command from [" + this.address + "].");
        }

        if (command.equals("LEF") && !done) {
            System.out.println("Procesing command LEF from [" + this.address + "]...");
            this.game.getPrevModule().setSocket(this.client, Integer.parseInt(line.substring(3)));
            done = true;
        }

        if (command.equals("RIG") && !done) {
            System.out.println("Procesing command RIG from [" + this.address + "]...");
            this.game.getNextModule().setSocket(this.client, Integer.parseInt(line.substring(3)));
            done = true;
        }

        if (command.equals("PAD") && !done) {
            System.out.println("Procesing command PAD from [" + this.address + "]...");
            this.game.newKillerPad(client, line.substring(3));
            done = true;
        }

        if (!done) {
            System.out.println("The connection type is uncompatible with the game.");
        }
        
        return done;

    }

    public void sendCommand(String command) {
        try {
            System.out.println("Sending comand : " + command + " to [" + this.address + "]...");
            out.println(command);
        } catch (Exception error) {

        }
    }

    public void closeConnection() {
        try {
            this.sendCommand("BYE");
            this.client.close();
            System.out.println("Disconnected from [" + this.address + "].");
        } catch (IOException error) {

        }
    }

    // Main activity
    @Override
    public void run() {

        this.makeContact();
        
        String line = "";

        while (!this.success) {
            line = this.getLine();
        }
        
        if (!this.processLine(line)) {
            this.closeConnection();
        }

    }

}
