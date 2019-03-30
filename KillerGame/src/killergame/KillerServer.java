package killergame;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KillerServer implements Runnable {

    // Atributes
    private KillerGame game;
    private int port;
    private ServerSocket rendezvous;
    private boolean exit;

    // Constructors
    public KillerServer(KillerGame game, int port) {
        this.game = game;
        this.port = port;
        this.exit = false;
    }

    // Methods
    private void waitForClient() {
        System.out.println("Waiting for client...");
        try {
            Socket client = rendezvous.accept();
            this.newConnectionHandler(client);
        } catch (IOException error) {

        }
    }

    private void openServer() {
        boolean opened = false;
        for (int inc = 0; !opened; inc++) {
            try {
                this.rendezvous = new ServerSocket(this.port);
                System.out.println("The open port is " + this.port + ".");
                this.game.getPanel().updateServerPort(Integer.toString(port));
                this.game.getPrevModule().setServerPort(port);
                this.game.getNextModule().setServerPort(port);
                opened = true;
            } catch (IOException error) {
                System.out.println("It is impossible to open port " + this.port + ".");
                this.port++;
            }
        }
    }

    // Methods new
    public void newConnectionHandler(Socket client) {
        ConnectionHandler handler = new ConnectionHandler(this.game, client);
        new Thread(handler).start();
    }

    // Methods get
    public int getPort() {
        return port;
    }

    // Methods set
    public void setPort(int port) {
        try {
            this.rendezvous.close();
        } catch (IOException ex) {

        }
        this.port = port;
        this.openServer();
    }

    public void exit() {
        this.exit = true;
    }

    // Main activity
    @Override
    public void run() {
        this.openServer();
        while (!this.exit) {
            this.waitForClient();
        }
    }

}
