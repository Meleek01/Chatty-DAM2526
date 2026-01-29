
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    static List<PrintWriter> clientes = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        new Thread(() -> {
            try (DatagramSocket socketUDP = new DatagramSocket(5001)) {
                byte[] buffer = new byte[1024];
                while (true) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socketUDP.receive(packet);
                    System.out.println("[UDP]: " + new String(packet.getData(), 0, packet.getLength()));
                }
            } catch (Exception e) {}
        }).start();

        ServerSocket servidorTCP = new ServerSocket(5000);
        while (true) {
            Socket socket = servidorTCP.accept();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            clientes.add(out);

            new Thread(() -> {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        for (PrintWriter c : clientes) {
                            if (c != out) { // Aquí filtramos al emisor
                                c.println(msg);
                            }
                        }
                    }
                } catch (Exception e) {}
            }).start();
        }
    }
}