package killergame;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class ControlPanel extends JFrame implements ActionListener {

    // Attributes
    private KillerGame game;

    private String currentPort;
    private Label port;
    private JTextField serverPort;
    private JTextField ipLeft;
    private JTextField portLeft;
    private JTextField ipRight;
    private JTextField portRight;

    // Constructors
    public ControlPanel(KillerGame game) {
        super("Control Panel");
        this.game = game;
        this.setUpWindow();
        this.addComponents();
        this.setVisible(true);
    }

    // Methods
    private void setUpWindow() {
        this.setSize(300, 250);
        this.setLocation(this.game.getX() + this.game.getWidth(), this.game.getY());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new GridBagLayout());
        this.setResizable(false);
    }

    private void addComponents() {

        // Server port
        this.port = new Label("Puerto actual del servidor : " + this.currentPort);
        this.add(this.port, editConstraints(0, 0, 3, 1, 0, 0, 1));
        this.add(new Label("Nuevo puerto : "), editConstraints(0, 1, 1, 1, 0, 0, 1));
        serverPort = new JTextField();
        this.add(serverPort, editConstraints(1, 1, 2, 1, 0, 0, 1));
        addButton("Cambiar puerto del servidor.", editConstraints(0, 2, 3, 1, 0, 0, 1));

        // Prev module ip and port
        this.add(new Label("IP Izquierda : "), editConstraints(0, 3, 1, 1, 0, 0, 1));
        ipLeft = new JTextField();
        this.add(ipLeft, editConstraints(1, 3, 2, 1, 0, 0, 1));
        this.add(new Label("Puerto Izquierdo : "), editConstraints(0, 4, 1, 1, 0, 0, 1));
        portLeft = new JTextField();
        this.add(portLeft, editConstraints(1, 4, 2, 1, 0, 0, 1));
        addButton("Cambiar configuracion del modulo previo.", editConstraints(0, 5, 3, 1, 0, 0, 1));

        // Next module ip and port
        this.add(new Label("IP Derecha : "), editConstraints(0, 6, 1, 1, 0, 0, 1));
        ipRight = new JTextField();
        this.add(ipRight, editConstraints(1, 6, 2, 1, 0, 0, 1));
        this.add(new Label("Puerto derecho : "), editConstraints(0, 7, 1, 1, 0, 0, 1));
        portRight = new JTextField();
        this.add(portRight, editConstraints(1, 7, 2, 1, 0, 0, 1));
        addButton("Cambiar configuracion del modulo siguiente.", editConstraints(0, 8, 3, 1, 0, 0, 1));

    }

    private GridBagConstraints editConstraints(int x, int y, int width, int height, int weightx, int weighty, int fill) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = width;
        constraints.gridheight = height;
        constraints.weightx = weightx;
        constraints.weighty = weighty;
        constraints.fill = fill;
        return constraints;
    }

    private void addButton(String text, GridBagConstraints constraints) {
        JButton button = new JButton(text);
        this.add(button, constraints);
        button.addActionListener(this);
    }

    public void updateServerPort(String port) {
        this.currentPort = port;
        this.remove(this.port);
        this.port = new Label("Puerto actual del servidor : " + this.currentPort);
        this.add(this.port, editConstraints(0, 0, 3, 1, 0, 0, 1));
        this.setVisible(true);
    }

    // Main activity
    @Override
    public void actionPerformed(ActionEvent ae) {

        if (ae.getActionCommand().equals("Cambiar puerto del servidor.")) {
            try {
                this.game.getServer().setPort(Integer.parseInt(this.serverPort.getText()));
            } catch (NumberFormatException error) {
                
            }
        }

        if (ae.getActionCommand().equals("Cambiar configuracion del modulo previo.")) {
            try {
                this.game.getPrevModule().setPortToReconnect(Integer.parseInt(this.portLeft.getText()));
                this.game.getPrevModule().setHost(this.ipLeft.getText());
            } catch (NumberFormatException error) {
                
            }
        }

        if (ae.getActionCommand().equals("Cambiar configuracion del modulo siguiente.")) {
            try {
                this.game.getNextModule().setPortToReconnect(Integer.parseInt(this.portRight.getText()));
                this.game.getNextModule().setHost(this.ipRight.getText());
            } catch (NumberFormatException error) {
                
            }
        }

    }

}
