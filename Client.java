import java.io.*;
import java.net.*;

public class Client extends Thread {
    Socket socket = null;
    PrintWriter out = null;
    BufferedReader in = null;
    static Object obiectSincronizareClient = new Object();

    Client(String nume) {
        super(nume);
    }

    public void run() {
        // System.out.println("Thread [" + getName() + "] incep executia ...");
        try {
            socket = new Socket("127.0.0.1", 4444);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Nu pot realiza conexiunea la 127.0.0.1 !");
            return;
        } catch (IOException e) {
            System.err.println("Nu pot realiza conexiuni I/O cu 127.0.0.1 !");
            return;
        }

        try {
            synchronized (obiectSincronizareClient) {
                out.println(getName());
                Console c = System.console();
                if (c == null) {
                    System.err.println("No console.");
                    System.exit(1);
                }

                String comanda = c.readLine("Thread[" + getName() + "] Comanda:");
                // while (comanda.equals("exit") == false) {
                    if (comanda.equals("sendMessage")) {
                        out.println(comanda);
                        String destinatar = c.readLine("Thread[" + getName() + "] Destinatar:");
                        out.println(destinatar);
                        String mesaj = c.readLine("Thread[" + getName() + "] Trimiteti mesajul:");
                        out.println(mesaj);
                    } else if (comanda.equals("getMessages")) {
                        out.println(comanda);
                        String linieDeChat;
                        while ((linieDeChat = in.readLine()) != null) {
                            System.out.println(linieDeChat);
                        }
                    }
                //     comanda = c.readLine("Thread[" + getName() + "] Comanda:");
                // }

                // String utilizator = c.readLine("Thread[" + getName() + "] Nume utilizator:
                // ");
                // char[] parola = c.readPassword("Thread[" + getName() + "] Parola: ");
                // c.format("Thread[" + getName() + "] Ati introdus utilizator/parola: %s / %s
                // %n", utilizator, parola);
            }

            // String mesajServer = in.readLine();
            // System.out.println("Thread[" + getName() + "] mesaj server:" + mesajServer);
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("Nu pot realiza conexiuni I/O cu 127.0.0.1 !");
        }
        // System.out.println("Thread [" + getName() + "] inchei executia!");
    }
}
