import java.net.*;
import java.io.*;

public class Client {
    // initialize socket and input output streams
    private Socket socket = null;
    private DataInputStream input = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private int check = 0;

    // constructor to put ip address and port
    public Client(String address, int port) {
        // establish a connection
        try {
            socket = new Socket(address, port);
            System.out.println("Connected\n");

            // takes input from terminal
            input = new DataInputStream(System.in);

            // takes input from the server socket
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            // sends output to the socket
            out = new DataOutputStream(socket.getOutputStream());

            Thread rec = new Thread() {
                public void run() {

                    String line = "";

                    // reads message from client until "Over" is sent
                    while (!line.equals("Over")) {

                        try {
                            line = in.readUTF();
                            
                            System.out.println(socket.getInetAddress().toString() + ": " + line);
                            //System.out.print(socket.getLocalAddress().toString() + "(client): ");

                        } catch (IOException i) {
                            System.out.println(i);
                        }
                    }
                    System.out.println("\nClosing connection...Press enter");

                }
            };

            Thread send = new Thread() {
                public void run() {

                    String line = "";

                    // send msg to client until "Over" is sent
                    while (!line.equals("Over")) {
                        // System.out.println("lolawa");

                        try {
                            //System.out.print("->");
                            
                            line = input.readLine();
                            out.writeUTF(line);

                        } catch (IOException i) {
                            System.out.println(i);
                        }
                    }
                    System.out.println("\nClosing connection");

                }
            };

            Thread closing = new Thread() {
                public void run() {
                    while (true) {

                        if (!rec.isAlive()) {
                            send.stop();

                            try {
                                // close connection
                                socket.close();
                                out.close();
                                in.close();
                            } catch (Exception a) {
                                System.out.println(a);
                            }
                            break;
                        } else if (!send.isAlive()) {
                            rec.stop();

                            try {
                                // close connection
                                socket.close();
                                out.close();
                                in.close();
                            } catch (Exception a) {
                                System.out.println(a);
                            }
                            break;
                        }

                    }
                }
            };

            // start receiving thread
            rec.start();

            // start sending thread
            send.start();

            closing.start();

            // try{
            // rec.join();
            // send.join();
            // closing.join();
            // }
            // catch(Exception e)
            // {
            // System.out.println(e);
            // }

        }

        catch (IOException i) {
            System.out.println(i);
        }
    }

    public static void main(String args[]) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // Reading data using readLine
        System.out.print("Enter ip address ");
        String ip = reader.readLine();
        System.out.print("Enter port ");
        int s = Integer.parseInt(reader.readLine());
        Client client = new Client(ip, s); // 192.168.2.9
    }
}