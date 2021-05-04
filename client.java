import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// main client class defiend
public class client extends JFrame implements ActionListener {
    static JTextArea messages; // initialize message
    JTextField inputBox; // initialize inputbox
    JButton btnSend; // initialize button
    static Socket socketConnection; 
    static DataInputStream inFromServer;
    static DataOutputStream outToServer;

    // when client object is created
    public client() throws Exception {
        buildInterface();
    }

    // GUI interface
    public void buildInterface() {
        btnSend = new JButton("Send Message");
        messages = new JTextArea();
        inputBox = new JTextField(40);
        messages.setRows(20);
        messages.setColumns(40);
        messages.setEditable(false);
        JScrollPane scrollPan = new JScrollPane(messages, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(scrollPan, "Center");
        JPanel bp = new JPanel(new FlowLayout());
        bp.add(inputBox);
        bp.add(btnSend);
        add(bp, "South");
        btnSend.addActionListener(this);
        setSize(700, 300);
        setVisible(true);
        pack();
    }

    // when the button Send is clicked this method is called
    public void actionPerformed(ActionEvent e) {
        try {
            // checks whether the user wants to end the chat
            if ((inputBox.getText()).equalsIgnoreCase("end")) {
                outToServer.writeUTF(inputBox.getText());
                System.exit(0);
            }
            
            // shows the messages you've sent and also send it to the server
            outToServer.writeUTF(inputBox.getText());
            messages.append("\n Me:  " + inputBox.getText());
            inputBox.setText(""); // clear the inputbox after a message is sent
        } catch (IOException err) {
            System.out.println(err);
        }
    }

    //main method
    public static void main(String[] arg) throws IOException {
        try {

            String msg = ""; // message is stored
            socketConnection = new Socket("localhost", 11111); // socket connection data with specified ip addreses and the port number
            inFromServer = new DataInputStream(socketConnection.getInputStream());
            outToServer = new DataOutputStream(socketConnection.getOutputStream());
            new client();

            //receives message from the server
            //also adds the message to the GUI interface
            while (true) {
                msg = inFromServer.readUTF();
                System.out.println("- " + msg);
                messages.append("\n " + msg);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

}