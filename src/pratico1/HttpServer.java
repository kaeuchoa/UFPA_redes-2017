package pratico1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * 
 * Description: Simple HTTP server using TCP sockets.
 * @author kaeuchoa
 * date: 05/11/2017
 */
public class HttpServer {
    
    private int port;
    // Variable just for reading purporses
    private boolean running;
    
    
    public HttpServer(){
        this.port = 8080;
        this.running = true;
    }   
    
    public HttpServer(int port){
        this.port = port;
        this.running = true;
    }   
    
    public int getPort(){
        return this.port;
    }
    
    public void startServer() throws IOException{
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server listening at http://localhost:"+ port);
        
        while(running){
            final Socket connSocket = serverSocket.accept();
            
            // 1. Read HTTP request from the client socket
            // Reader to read incoming messages
            BufferedReader inputReader = new BufferedReader(
                    new InputStreamReader(connSocket.getInputStream()));
            // Reads the first line of the request
            String line = inputReader.readLine();
            System.out.println("Incoming request: ");
            System.out.println("################## BEGIN ###########################");
            while(!line.isEmpty()){
                System.out.println(line);
                line = inputReader.readLine();
            }
            System.out.println("################### END ############################");            
            // 2. Prepare an HTTP response
            Date today = new Date();
            final String responseHeader = "HTTP/1.1 200 OK\r\n\r\n";
            final String htmlResponse = "HTTP Server is working fine.\r\n"+
                                    "Date: " + today ;  
            // 3. Send HTTP response to the client
            final OutputStream outputWriter = connSocket.getOutputStream();
            outputWriter.write((responseHeader + htmlResponse).getBytes("UTF-8"));
            // 4. Close the socket, inputs and outputs
            inputReader.close();
            outputWriter.close();
            connSocket.close();         
            
        }
    }
    
}
