
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Segundo {
    public static void main(String[] args) throws Exception {
        String nombre = "Cliente2";
        Socket socketTCP = new Socket("localhost", 5000);
        PrintWriter out = new PrintWriter(socketTCP.getOutputStream(), true);
        DatagramSocket socketUDP = new DatagramSocket();

        new Thread(() -> {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socketTCP.getInputStream()))) {
                String msg;
                while ((msg = in.readLine()) != null) System.out.println(msg);
            } catch (Exception e) {}
        }).start();

        Scanner sc = new Scanner(System.in);
        while (true) {
            String texto = sc.nextLine();
            if (texto.startsWith("!")) {
                byte[] b = (nombre + ": " + texto).getBytes();
                socketUDP.send(new DatagramPacket(b, b.length, InetAddress.getByName("localhost"), 5001));
            } else {
                out.println(nombre + ": " + texto);
            }
        }
    }
}