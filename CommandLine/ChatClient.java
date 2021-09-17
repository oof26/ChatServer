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
			/**
			 * This runnable deals with client output to server
			 */
			Runnable ClientOut = new Runnable() {
				@Override
				public void run() {
					try {
						BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
						PrintWriter serverOut = new PrintWriter(server.getOutputStream(), true);
						while(running) {
							String userInput = userIn.readLine();
							serverOut.println(userInput);
							if(userInput.equals("EXIT")) {
								running = false; //If user enters exit, client should no longer accept input and should stop accepting input
							}
						}
					}catch(IOException e) {
						System.out.println("An IO error occured, please restart client");
						System.exit(0);
					}
				}
				
			};
			/**
			 * This runnable deals with client input and output to console
			 */
			Runnable ClientIn = new Runnable() {

				@Override
				public void run() {
					try {
						BufferedReader serverIn = new BufferedReader(new InputStreamReader(server.getInputStream()));
						while(running) {
							String serverRes = serverIn.readLine();
							if(serverRes!=null) { //If server response is null, either the server has shut down or the client has an error
								outputMessage(serverRes); 
							}
						}
					}catch(IOException e) {
						System.out.println("An IO error occured, please restart client");
						System.exit(0);
					}
					
				}
			};
			
			Thread output = new Thread(ClientOut);
			Thread input = new Thread(ClientIn);
			output.start();
			input.start();

	}
	private void outputMessage(String msg) {
		System.out.println(msg);
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
		new ChatClient(address , port).go();
	}

}
	