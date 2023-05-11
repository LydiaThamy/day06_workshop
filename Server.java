package revision;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) throws NumberFormatException, IOException {
        if (args.length != 2) {
            System.out.println("Run java file as follows: java -cp classes revision.Server 12345 cookie_file.txt");
            System.exit(0);
        }

        // start server
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        ServerSocket server = new ServerSocket(Integer.parseInt(args[0]));
        System.out.println("Server waiting for connection...");

        // accept connections
        while (true) {
            Socket socket = server.accept();

            // pass connection to client handler
            CookieClientHandler worker = new CookieClientHandler(socket, args[1]);
            threadPool.submit(worker);
            // System.out.println("New client connected to the server");
        }

    }
}
