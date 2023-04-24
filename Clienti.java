public class Clienti {
    public static void main(String args[]) {
        for (int i = 0; i < 2; i++) {
            Client c = new Client("Client_" + (i + 1));
            c.start();
        }
    }
}