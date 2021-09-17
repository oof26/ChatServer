import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
* Client side of a chat program.
*
* @author Anonymous?
* @version 1.0
* @release 15/03/2019
* 
*/

public class ChatClient {
	
	private Socket server;
	private static boolean running=true; //used to stop clients when server shuts down
	private ClientGUI gui = new ClientGUI();
	/**
	 * Constructor. sets field values.
	 * @param address
	 * 			IP address of server client is connecting to
	 * @param port
	 * 			Port number of server client is connecting to 
	 */
	public ChatClient(String address, int port) {
		try {
			server = new Socket(address,port);
		} catch (UnknownHostException e) {
			System.out.println("Server Address not recognized");
			System.exit(0);
		} catch (IOException e) {
			System.out.println("Client could not be run, try new port/address");
			System.exit(0);
		}
	}
	
	public void go() {
		ClientGUI gui = new ClientGUI();
		gui.draw(this);
		try {
			BufferedReader serverIn = new BufferedReader(new InputStreamReader(server.getInputStream()));
			while(running) {
				String serverRes = serverIn.readLine();
				if(serverRes!=null) { //If server response is null, either the server has shut down or the client has an error
					gui.addMessage(serverRes); 
				}
			}
		}catch(IOException e) {
			System.out.println("An IO error occured, please restart client");
			System.exit(0);
		}
		
	}

	/**
	 * Checks if argument is a port param flag
	 * @param s
	 * @return true: if arg is a port param flag
	 */
	private static boolean isPortParam(String s) {
		if(s.substring(0, 3).equals("-ccp")) {
			return true;
		}
			return false;		
	}
	/**
	 * Checks if argument is a IP param flag
	 * @param s
	 * 		Arg passed in at runtime
	 * @return true: if arg is an IP param flag
	 */
	private static boolean isIPParam(String s) {
		if(s.substring(0, 3).equals("-cca")) {
			return true;
		}
			return false;	
	}
	
	public void shutdownClient() {
		running = false;
		System.exit(0);
	}
	public void sendMessage(String msg) {
		try {
			PrintWriter serverOut = new PrintWriter(this.server.getOutputStream(), true);
			serverOut.println(msg);
		} catch (IOException e) {
			System.out.println("IO Error occured please restart Client");
			System.exit(0);
		}

	}
	public static void main(String[] args) {
		int port = 14001; 
		String address = "localhost"; 
		for(int i=0; i < args.length-1 ; i++) {
			if(isPortParam(args[i])){
				port = Integer.valueOf(args[i+1]);
				i=args.length;
			}else if(isIPParam(args[i])){
				address = args[i+1];
			}
		}
		ChatClient client = new ChatClient(address , port);
		client.go();
		
	}

}
	