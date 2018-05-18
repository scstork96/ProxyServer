import java.net.*;
import java.io.*;

/**
 * Project 1: Proxy
 * @author Shannon Stork
 *
 */
public class Proxyd {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("Proxy initiated");
		ServerSocket serverSocket = null;
		int port = 5040;
		try {
			if (args[1] != null)
				port = Integer.parseInt(args[1]);
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Port not specified. Listening on port 5040.");
		}
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                new ProxyThread(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("Bad port: " + port);
        }
       
	}

}
