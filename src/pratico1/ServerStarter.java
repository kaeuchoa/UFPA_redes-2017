package pratico1;

import java.io.IOException;
import java.net.SocketException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Description: Main class to initiate the following servers: 
 * 1. HttpServer 
 * 2. UdpDNS
 * Author: kaeuchoa 
 * Date: 05/11/2017
 */
public class ServerStarter {

    public static void main(String[] args) {
        System.out.println("Iniciar:");
        System.out.println("1. Servidor HTTP (TCP).\n2. Servidor DNS (UDP).");
        System.out.print("-> ");
        Scanner input = new Scanner(System.in);
        int option = input.nextInt();
        switch (option) {
            case 1: {
                final HttpServer httpServer = new HttpServer(80);
                try {
                    httpServer.startServer();
                } catch (IOException ex) {
                    Logger.getLogger(ServerStarter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case 2:
                final UdpDNS udpServer = new UdpDNS();
                udpServer.addNameToList("www.facebook.com", "157.240.12.35");
                udpServer.addNameToList("www.google.com", "216.58.218.14");
                udpServer.addNameToList("www.ufpa.br", "200.239.64.85");
                 {
                    try {
                        udpServer.startServer();
                    } catch (SocketException ex) {
                        Logger.getLogger(ServerStarter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            default:
                System.out.println("Opção Inválida.");

        }
    }

}
