import java.io.*;
import java.net.*;
import java.util.ArrayList;

class ProcesatorCerereClient extends Thread {
    Socket clientSocket = null;
    PrintWriter out = null;
    BufferedReader in = null;

    static int numarCurentClienti = 0;
    static final int numarMaximClienti = 5;
    static Object obiectSincronizare = new Object();

    static StringBuilder log = new StringBuilder();

    public ProcesatorCerereClient(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        boolean nuSePoateProcesaCererea = false;
        try {
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            // numarClientiConectati();
            String numeUtilizator = in.readLine();
            synchronized (obiectSincronizare) {
                if (numarCurentClienti == numarMaximClienti) {
                    System.out.println("NumarCurentClienti nu mai poate fi crescut pentru " + numeUtilizator);
                    nuSePoateProcesaCererea = true;
                } else {
                    numarCurentClienti++;
                }
            }

            if (nuSePoateProcesaCererea == true) {
                out.println("Server supraincarcat incercati mai tarziu!");
            } else {
                System.out.println("Utilizator conectat: " + numeUtilizator);
                String comanda = in.readLine();
                // while (comanda.equals("exit") == false) {
                    if (comanda.equals("sendMessage")) {
                        trimiteMesaj(numeUtilizator);
                    } else if (comanda.equals("getMessages")) {
                        out.println(primesteMesaje());
                    // }
                }
                // procesareCerere();
                // out.println("CerereProcesata pentru " + numeUtilizator + "!");
                // System.out.println("ServerCerereProcesata pt. " + numeUtilizator + "!");
                synchronized (obiectSincronizare) {
                    numarCurentClienti--;
                }
            }
        } catch (IOException e) {
            System.err.println("Comunicare esuata cu aplicatia client!");
        } finally {
            try {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            } catch (IOException e) {
            }
            if (clientSocket != null)
                try {
                    clientSocket.close();
                } catch (IOException e) {
                }
        }
    }

    public void procesareCerere() {
        try {
            sleep(2000);
        } catch (InterruptedException ex) {
        }
    }

    public void trimiteMesaj(String numeUtilizator) {
        StringBuilder logLine;
        String destinatar;
        String mesaj;
        logLine = new StringBuilder();
        try {
            destinatar = in.readLine();
            mesaj = in.readLine();
            logLine.append(numeUtilizator)
                    .append(" ")
                    .append(destinatar)
                    .append(" ")
                    .append(mesaj);
            log.append(logLine)
                    .append(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String primesteMesaje() {
        String logString = log.toString();
        String[] logLines = logString.split(System.lineSeparator());
        String[] logLinesSplit;
        String destinatar;
        StringBuilder mesaj = new StringBuilder();
        StringBuilder mesaje = new StringBuilder();
        for (int i = 0; i < logLines.length; i++) {
            logLinesSplit = logLines[i].split("\\s+");
            destinatar = logLinesSplit[1];
            mesaj.append(logLinesSplit[0])
                    .append(":");
            for (int j = 2; j < logLinesSplit.length; j++) {
                mesaj.append(logLinesSplit[j])
                        .append(" ");
            }
            mesaje.append(mesaj)
                    .append(System.lineSeparator());
        }
        return mesaje.toString();
    }

    // static void numarClientiConectati() throws IOException {
    // BufferedReader inputStream = null;
    // PrintWriter outputStream = null;
    // try {
    // inputStream = new BufferedReader(new
    // java.io.FileReader("informatiiServer.txt"));
    // outputStream = new PrintWriter(new
    // java.io.FileWriter("informatiiServer.txt"));
    // String l;
    // while ((l = inputStream.readLine()) != null) {
    // outputStream.println(l);
    // }
    // } finally {
    // if (inputStream != null) {
    // inputStream.close();
    // }
    // if (outputStream != null) {
    // outputStream.close();
    // }
    // }
    // }
}