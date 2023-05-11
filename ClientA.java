package revision;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientA {
    public static void main(String[] args) throws NumberFormatException, IOException {
        if (args.length != 1) {
            System.out.println("Run java file as follows: java -cp classes revision.ClientA localhost:12345");
            System.exit(0);
        }

        String[] arg = args[0].split(":");
        

        Socket socket = new Socket(arg[0], Integer.parseInt(arg[1]));
        System.out.println("Connected to server...");
        
        OutputStream os = socket.getOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(os);
        DataOutputStream dos = new DataOutputStream(bos);
        
        dos.writeUTF("Client A");
        dos.flush();

        InputStream is = socket.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(is);
        DataInputStream dis = new DataInputStream(bis);
        String message = "";

        Console console = System.console();
        String command = "";

        while(!command.equals("close")) {
            command = console.readLine("Give a command. Type 'help' for command summary.\n").trim().toLowerCase();

            switch (command) {
                case "help":
                    System.out.println("Type 'get-cookie' to request the server to send a cookie");
                    System.out.println("Type 'get-file' to request the server to send a file");
                    System.out.println("Type 'close' to request the server to close the connection");
                    break;
                
                case "get-cookie":
                    // send command to server
                    dos.writeUTF(command);
                    dos.flush();

                    // receive response from server
                    message = dis.readUTF();

                    // get cookie text
                    if (message.equals("cookie-text")) {
                        String cookie = dis.readUTF();
                        System.out.println("Cookie: " + cookie);
                    }

                break;

                case "get-file":
                    // send command to server
                    dos.writeUTF(command);
                    dos.flush();

                    // receive file name from server
                    String fileName = dis.readUTF();
                    
                    // receive file from server
                    byte[] cookieFile = new byte[1024];
                    dis.read(cookieFile);
                    
                    // assign directory for downloads
                    String dir = "C:\\Users\\Lydia\\Documents\\Work Matters\\Visa\\NUS\\SDF\\Project\\day06_workshop\\src\\main\\java\\revision\\downloads";
                    
                    File fileDir = new File(dir);
                    if (!fileDir.exists()) {
                        fileDir.mkdir();
                    }

                    File file = new File(dir + File.separator + fileName);
                    if (!file.exists()) {
                        file.createNewFile();
                    }

                    // download file
                    OutputStream fos = new FileOutputStream(file);
                    BufferedOutputStream bfos = new BufferedOutputStream(fos);
                    bfos.write(cookieFile);
                    bfos.flush();

                    bfos.close();
                    fos.close();

                    System.out.println(fileName + " downloaded from server" );
                break;

                default:
                    dos.writeUTF(command);
                    dos.flush();
                    System.out.println("Message from server: " + dis.readUTF());
                break;
            }
        }

        dis.close();
        bis.close();
        is.close();
        
        dos.close();
        bos.close();
        os.close();

        socket.close();
        
    }
}
