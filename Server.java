import java.net.*; 
import java.io.*; 
import java.util.*;
  
public class Server 
{ 
    //initialize socket and input stream 
    private Socket          socket   = null; 
    private ServerSocket    server   = null; 
    private DataInputStream in       =  null; 
    private DataInputStream input = null;
    private DataOutputStream out = null;
    private int check = 0;
  
    // constructor with port 
    public Server(int port) 
    { 
        // starts server and waits for a connection 
        try
        { 
            server = new ServerSocket(port); 
            System.out.println("Server started"); 
  
            System.out.println("Waiting for a client ..."); 
  
            socket = server.accept(); 
            System.out.println("Client accepted\n"); 
  
            // takes input from the client socket 
            in = new DataInputStream( 
                new BufferedInputStream(socket.getInputStream())); 
            
            // takes input from terminal 
            input  = new DataInputStream(System.in); 
  
            // sends output to the socket 
            out    = new DataOutputStream(socket.getOutputStream()); 
  
           

            Thread rec = new Thread(){
               public void run(){

                    String line = ""; 

                    // reads message from client until "Over" is sent 
                    while (!line.equals("Over")) 
                    { 

                        try
                        { 
                            line = in.readUTF(); 
                            System.out.println(socket.getInetAddress().toString()+ ": " +line); 
                            //System.out.print(socket.getLocalAddress().toString()+ "(server): "); 
        
                        } 
                        catch(IOException i) 
                        { 
                            System.out.println(i); 
                        } 
                    } 
                    
                    System.out.println("\nClosing connection...Press enter"); 

        
                }
            };

            Thread send = new Thread(){
                public void run(){
 
                     String line = ""; 
 
                     // send msg to client until "Over" is sent 
                     while (!line.equals("Over")) 
                     { 
                        try
                        { 
                        //System.out.print(socket.getLocalAddress().toString()+ "(server): "); 
                        line = input.readLine();
                        out.writeUTF(line); 
        
                        } 
                        catch(IOException i) 
                        { 
                        System.out.println(i); 
                        } 
                     } 
                     
                     System.out.println("\nClosing connection"); 
 
                 }
             };

             Thread closing = new Thread(){
                public void run(){
                    while(true){

                        if(!rec.isAlive()){
                            send.stop();

                            try{
                                // close connection 
                                socket.close(); 
                                out.close();
                                in.close();
                            }catch(Exception a){
                                System.out.println(a);
                            }
                            break;
                        }
                        else if(!send.isAlive()){
                            rec.stop();

                            try{
                                // close connection 
                                socket.close(); 
                                out.close();
                                in.close();
                            }catch(Exception a){
                                System.out.println(a);
                            }
                            break;
                        }

                    }
                }
            };


            //start receiving thread
            rec.start();

            //start sending thread
            send.start();

            closing.start();
  
             
        } 
        catch(IOException i) 
        { 
            System.out.println(i); 
        } 
    } 
  
    public static void main(String args[]) 
    { 
        int x;
        System.out.println("enter port number ");
        Scanner sc = new Scanner(System.in);
        x = sc.nextInt();

        Server server = new Server(x); 
    } 
} 