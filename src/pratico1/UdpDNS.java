package pratico1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Description: Simple name translator using UDP Sockets.
 * Author: kaeuchoa 
 * Date: 05/11/2017
 */
public class UdpDNS {

    private int port;
    // Variable just for reading purporses
    private boolean running;
    // 1024 bytes
    private static final int DATA_SIZE = 1024;
    // REGEX to validate the urls
    private static final String URL_REGEX = "((([A-Za-z]{3,9}:(?:\\/\\/)?)"
            + "(?:[\\-;:&=\\+\\$,\\w]+@)?[A-Za-z0-9\\.\\-]+|"
            + "(?:www\\.|[\\-;:&=\\+\\$,\\w]+@)[A-Za-z0-9\\.\\-]+)"
            + "((?:\\/[\\+~%\\/\\.\\w\\-_]*)"
            + "?\\??(?:[\\-\\+=&;%@\\.\\w_]*)"
            + "#?(?:[\\.\\!\\/\\\\\\w]*))?)";

    private HashMap<String, String> listOfNames;

    public UdpDNS() {
        this.port = 9876;
        this.running = true;
        this.listOfNames = new HashMap<>();
    }

    public UdpDNS(int port) {
        this.port = port;
        this.running = true;
        this.listOfNames = new HashMap<>();
    }

    public void addNameToList(String url, String ip) {
        listOfNames.put(url, ip);
    }

    public void startServer() throws SocketException {
        DatagramSocket socket = new DatagramSocket(port);
        System.out.println("UDP Server listening at port:" + port);
        
        while (running) {
            // 1. Creates arrays to store incoming and outcoming data
            byte[] receivedData = new byte[DATA_SIZE];
            byte[] toSendData = new byte[DATA_SIZE];
            try {
                // Creates a new udp packet. It will translate the bytes to 
                // information relevant for the server
                final DatagramPacket incomingPacket
                        = new DatagramPacket(receivedData, receivedData.length);

                socket.receive(incomingPacket);
                //2. Gets all the relevant data from the packet
                final String clientURL = new String(incomingPacket.getData()).trim();
                System.out.println("URL from client: " + clientURL);
                final InetAddress clientIP = incomingPacket.getAddress();
                final int clientPort = incomingPacket.getPort();

                //3. Parsing the URL
                final Pattern pattern = Pattern.compile(URL_REGEX);
                final Matcher matcher = pattern.matcher(clientURL);
                boolean isURLValid = matcher.find();

                if (isURLValid) {
                    // Process URL
                    if (listOfNames.containsKey(clientURL)) {
                        toSendData = (listOfNames.get(clientURL)).getBytes();
                    } else {
                        toSendData = ("Can't find URL requested.").getBytes();
                    }
                } else {
                    toSendData = ("Invalid URL").getBytes();
                }
                //4. Creates the packet to send back to the client
                final DatagramPacket outcomingPacket
                        = new DatagramPacket(toSendData, toSendData.length,
                                clientIP, clientPort);
                socket.send(outcomingPacket);
                

            } catch (IOException ex) {
                Logger.getLogger(UdpDNS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
