import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

/**
* Server side of a chat program.
*
* @author Anonymous?
* @version 1.0
* @release 15/03/2019
* 
*/

public class ChatServer {
	ServerSocket in;
	boolean running = true;
	ArrayList<Client> clients = new ArrayList<Client>(); //used to send message to all clients
	/**
	 * Constructor. Sets the field values
	 * @param port
	 * 			port number server is connected to
	 */
	public ChatServer(int port) {
		try {
			in = new ServerSocket(port);
		    in.setReuseAddress(true); //used for testing purposes and so that server can be run and terminated repeatedly 
		} catch (IOException e) {
			System.out.println("Server setup failed, try new port parameter");
			System.exit(0);
		}
	}
	public void go() {
		/**
		 * 
		 * This Runnable deals with input to server from the console, namely the "EXIT" command which shuts down the server cleanly
		 *
		 */
		class ServerInput implements Runnable{

			@Override
			public void run() {
				InputStreamReader r = new InputStreamReader(System.in);
				BufferedReader serverIn = new BufferedReader(r);
				while(true) {
					try {
						if(serverIn.readLine().equals("EXIT")) {
							this.shutdownServer();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			private void shutdownServer(){
				running = false;
				try {
					for(Client c : clients) {
							c.getSocket().close();
					}
				} catch (IOException e) {
					System.out.println("Server shutdown failed");
					System.exit(0);
				}
				System.exit(0);
			}
			
		}
		
		Thread serverThread = new Thread(new ServerInput());
		serverThread.start();
		
		try {
			System.out.println("Server listening");
			while(running) {
				Socket s = in.accept();
				Client client = new Client(s);
				clients.add(client); //Adds new client to client list 
				System.out.println("New client connected from: " + s.getPort() + " on port: " + in.getLocalPort());
				/**
				 * 
				 * This Runnable spawns is used to spawn new client connections to the server
				 * Lambda notation used as ServerSocket is used by all clientRunnable threads
				 *
				 */
				class ClientRunnable implements Runnable { 
					public void run() {
						try {
							InputStreamReader r = new InputStreamReader(s.getInputStream()); 
							BufferedReader clientIn = new BufferedReader(r);
							while(true) {
								try {
									if(client.getActive()) {
										String userInput =  clientIn.readLine();		 //Gets input from client
										if(userInput.equals("EXIT")) { //if client input == EXIT then client will be removed from active clients list 
											client.setUnactive();
										}else {//Otherwise output client input to all active clients on server
											userInput = s.getPort() + ":" + userInput;
											
											for(Client c : clients) { //Outputs userInput to all active clients connected to server
												if(c.getActive()) {
													PrintWriter clientOut = new PrintWriter(c.getSocket().getOutputStream(), true);
													clientOut.println(userInput);
												}else {
													c.getSocket().close();
												}
											}
										}
									} 
								}catch(SocketException e) {
									//This exception occurs when a client leaves so should be ignored
								}
							}
						}catch(IOException e) {
							System.out.println("An error occured, please restart the server");
							System.exit(0);
						}
					}
				}
				
				Thread clientThread = new Thread(new ClientRunnable()); //Thread for each client connected to server that runs ClientRunnable
				clientThread.start();
			}
		} catch (IOException e) {
			System.out.println("An error occured, please restart the server");
			System.exit(0);
		}
		finally {
			try {
				in.close();
			} catch (IOException e) {
				//if an error occurs when closing the server cleanly the program should just shut down with a system exit
				System.exit(0);
			}
		}
	}
	/**
	 * Checks if argument if a port parameter flag
	 * @param s
	 * 			arg given at runtime
	 * @return true: if arg is a port param flag
	 */
	private static boolean isParam(String s) {
		if(s.substring(0, 3).equals("-csp")) {
			return true;
		}
			return false;
	}
	public static void main(String[] args) {
		int port = 14001; //Default port 
		for(int i=0; i < args.length-1 ; i++) {
			if(isParam(args[i])){
				port = Integer.valueOf(args[i+1]);
				i=args.length;
			}
		}
		new ChatServer(port).go();
	}
}

