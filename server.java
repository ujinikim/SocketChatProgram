import java.io.*;
import java.net.*;

//inner class to run threads for each clients
class handleClient extends Thread {
    DataInputStream infromMe; // input stream to read from the client connected to a socket
    DataOutputStream outToMe; // output stream to send message to myself
    DataOutputStream outToClient; // output stream to relay messge to friend client
    Socket mySocket; // my socket connection data stored
    Socket friendSocket; // friend's socket connection data stored

    // counstructor for handlClient class
    handleClient(Socket s1, Socket s2) throws IOException {
        mySocket = s1;
        friendSocket = s2;
        infromMe = new DataInputStream(mySocket.getInputStream()); // defines the input stream
        outToMe = new DataOutputStream(mySocket.getOutputStream()); // defines the output stream for myself
        outToClient = new DataOutputStream(s2.getOutputStream()); // defines the output stream for friend client
    }

    // this part is ran when handClient object is successfully created and executed
    public void run() {
        String msg = new String(); // stores the message sent from clients
        try {
            outToMe.writeUTF("Connected to the server");
            outToClient.writeUTF("Friend has entered the chat!");
            while (true) {
                msg = infromMe.readUTF();
                // checks the message if a client wants to end the chat
                if (msg.equalsIgnoreCase("end")) {
                    outToClient.writeUTF("Friend has left the chat");
                    break;
                }
                // relay the messages to the friend clients that are received by the server
                System.out.println("Friend: " + msg);
                outToClient.writeUTF("Friend: " + msg);
            }

            // closes the socket after done using
            mySocket.close();
            friendSocket.close();
        } catch (IOException ex) {

        }
    }
}

//main class defined
public class server {

    // main method
    public static void main(String args[]) throws IOException, InterruptedException {

        ServerSocket ss = new ServerSocket(11111); // serversocket created with specified port number
        System.out.println("Server is waiting for clients to join");
        Socket s1 = ss.accept(); // accepts the clients request to join the server
        System.out.println("Client 1 connected");
        Socket s2 = ss.accept(); // accepts the clients request to join the server
        System.out.println("Client 2 connected");
        handleClient t1 = new handleClient(s1, s2); // creates new handClient object
        handleClient t2 = new handleClient(s2, s1); // creates new handClient object
        t1.start(); // starts the thread
        t2.start(); // starts the thread
        System.out.println("Now ready to chat");
        
        Thread.sleep(3000);

    }
}
