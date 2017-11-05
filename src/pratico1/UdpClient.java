package pratico1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Description: Auxiliary class to test UdpDNS
 * Author: kaeuchoa 
 * Date: 05/11/2017
 */
public class UdpClient {

    private int port;
    // 1024 bytes
    private static final int DATA_SIZE = 1024;

    public UdpClient(int port) {
        this.port = port;
    }
    
    public static void main(String[] args) {
        UdpClient client = new UdpClient(9876);
        try {
            client.startClient();
        } catch (SocketException ex) {
            Logger.getLogger(UdpClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void startClient() throws SocketException {
        DatagramSocket socket = new DatagramSocket();
        // Creates arrays to store incoming and outcoming data
        byte[] receivedData = new byte[DATA_SIZE];
        byte[] toSendData = new byte[DATA_SIZE];
        
        System.out.println("Entre uma URL para buscar:");
        System.out.print("->");
        final Scanner userInput = new Scanner(System.in);
        String inputURL = "";
        inputURL = userInput.next();
        
        toSendData = inputURL.getBytes();

        try {
            InetAddress IPAddress = InetAddress.getLocalHost();
            // Creates a new udp packet. It will translate the bytes to 
            // information relevant to send for the server
            final DatagramPacket outcomingPacket
                    = new DatagramPacket(toSendData, toSendData.length,
                            IPAddress,port);
            socket.send(outcomingPacket);
            
            // Receives the answer from the server
            final DatagramPacket incomingPacket =
                                new DatagramPacket(receivedData, receivedData.length);
            socket.receive(incomingPacket);
            
            final String serverIP = new String(incomingPacket.getData());
            System.out.println("Resposta:");
            System.out.println("- Name: " + inputURL);
            System.out.println("- Addresss: " + serverIP);
            
            socket.close();
            

        } catch (IOException ex) {
            Logger.getLogger(UdpDNS.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
