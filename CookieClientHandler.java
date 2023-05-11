package revision;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class CookieClientHandler implements Runnable{
    private final Socket socket;
    String fileName;

    public CookieClientHandler(Socket socket, String fileName) {
        this.socket = socket;
        this.fileName = fileName;
    }

    @Override
    public void run() {

        Cookie cookie = new Cookie(fileName);

        try {
            InputStream is = socket.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);

            String client = dis.readUTF();
            System.out.println(client + " is connected to the server");

            OutputStream os = socket.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os);
            DataOutputStream dos = new DataOutputStream(bos);

            // get message from client
            String command = "";
            
            while (!command.equals("close")) {

                command = dis.readUTF().trim().toLowerCase();
                System.out.println(client + ": " + command);

                switch (command) {
                    case "get-cookie":
                        // send message to client
                        dos.writeUTF("cookie-text");
                        dos.flush();
                    
                        // get cookie text from cookie class    
                        String cookieText = cookie.getCookie();

                        // send cookie to client
                        dos.writeUTF(cookieText);
                        dos.flush();
                        System.out.println("Cookie sent to " + client + ": " + cookieText);
                    break;

                    case "get-file":
                        // send file name to client
                        dos.writeUTF(fileName);
                        dos.flush();

                        // get cookie file
                        byte[] cookieFile = cookie.getFile();

                        // send cookie file to client
                        dos.write(cookieFile);
                        dos.flush();

                        System.out.println(fileName + " file sent to " + client);
                    break;
                    
                        default:
                        dos.writeUTF("Invalid command");
                    break;
                }
            }
            
            // close connection with client
            System.out.println(client + " disconnected");
            dos.close();
            bos.close();
            os.close();

            dis.close();
            bis.close();
            is.close();

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
}
