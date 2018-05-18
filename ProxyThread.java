import java.io.*;
import java.net.*;
import java.nio.charset.*;

public class ProxyThread extends Thread {
	
	// The server socket responsible for taking information from the proxy.
	private Socket server;
	
	// The client socket responsible for taking information from the proxy.
    private Socket client;
    
    private Cache cache;
    
	private InputStream in;
    
    private OutputStream out;
    
    // The host name being requested.
    private String hostName = "";
    
    // A string representation storing data from the client.
    private String inputString;
    
    /**
     * The constructor
     * @param client
     * @param port
     * @throws IOException
     */
    public ProxyThread(Socket client) throws IOException {
    	this.client = client;
    	in = client.getInputStream();
    	cache = new Cache();
    }
    
	
	/**
	 * @return the server
	 */
	public Socket getServer() {
		return server;
	}

	/**
	 * @param server the server to set
	 */
	public void setServer(Socket server) {
		this.server = server;
	}

	/**
	 * @return the client
	 */
	public Socket getClient() {
		return client;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(Socket client) {
		this.client = client;
	}

	/**
	 * @return the in
	 */
	public InputStream getIn() {
		return in;
	}

	/**
	 * @param in the in to set
	 */
	public void setIn(InputStream in) {
		this.in = in;
	}

	/**
	 * @return the out
	 */
	public OutputStream getOut() {
		return out;
	}

	/**
	 * @param out the out to set
	 */
	public void setOut(OutputStream out) {
		this.out = out;
	}

	/**
	 * @return the hostName
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * @param hostName the hostName to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * @return the inputString
	 */
	public String getInputString() {
		return inputString;
	}

	/**
	 * @param inputString the inputString to set
	 */
	public void setInputString(String inputString) {
		this.inputString = inputString;
	}

	/**
	 * A helper method to process the input stream before it is written to the server.
	 */
	public void processData() {
    	String url = "";
    	String relativeURL = "";
    	String[] split = inputString.split(" ");
    	// The second chunk of text separated by spaces represents the URL.
    	url = split[1];
    	// Uses the URL to find the relative URL and replaces the URL with the relative URL in the input string.
    	try {
			URL hostURL = new URL(url);
			String urlString = hostURL.getHost();
			setHostName(urlString);
			setHostName(cache.getHost(getHostName()));
			relativeURL = url.replace("http://" + getHostName(), "");
			inputString = inputString.replaceAll(url, relativeURL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
    	// Alters the connection header to change keep-alive to close.
    	inputString = inputString.replaceAll("Proxy-Connection: Keep-Alive", "Connection: Close");
    	inputString = inputString.replaceAll("Connection: Keep-Alive", "Connection: Close");
    }
	
	/**
	 * The main run method.
	 */
    public void run() {
    	// Reads the data from the input stream into a buffer array and then into a String.
		try {
			byte[] b = new byte[2048];
			BufferedInputStream clientReader = new BufferedInputStream(client.getInputStream());
			clientReader.read(b, 0, 2048);
			setInputString(new String(b, StandardCharsets.UTF_8));
			// Processes the data by changing the URL and connection status.
			processData();
			// Creates a new socket on the server at port 80 (the default for HTTP).
			if (!getHostName().equals("")) {
					InetAddress address = InetAddress.getByName(getHostName());
					cache.add(getHostName(), address.getHostAddress());
					setServer(new Socket(address, 80));
					setOut(server.getOutputStream());
			}
			// Writes the altered request to the server.
			DataOutputStream outWriter = new DataOutputStream(server.getOutputStream());
			byte[] bytes = getInputString().getBytes(StandardCharsets.UTF_8);
			outWriter.write(bytes);
			outWriter.flush();
			// Handles the response from the server.
			DataInputStream inFromServer = new DataInputStream(server.getInputStream());
			DataOutputStream outToClient = new DataOutputStream(client.getOutputStream());
			int input2;
			while ((input2 = inFromServer.read()) != -1) {
				outToClient.write(input2);
				outToClient.flush();
			}
			// Closes all resources.
			outWriter.flush();
			inFromServer.close();
			outToClient.close();
			outWriter.close();
			server.close();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

}
