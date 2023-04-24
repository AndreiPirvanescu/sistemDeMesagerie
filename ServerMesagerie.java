import java.io.*;
import java.net.*;

public class ServerMesagerie {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(4444);
        } catch (IOException e) {
            System.err.println("Nu pot asculta pe port: 4444.");
            System.exit(1);
        }

        while (true) {
            Socket clientSocket = null;

            try {
                System.out.println("Astept cerere conectare de la client ...");
                clientSocket = serverSocket.accept();
                new ProcesatorCerereClient(clientSocket).start();
            } catch (IOException e) {
                System.err.println("Nu pot realiza conexiune cu clientul!");
                
                if (clientSocket != null)
                    try {
                        clientSocket.close();
                    } catch (IOException ex) {
                    }
            }
        }
    }
}