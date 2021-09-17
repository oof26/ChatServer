import java.net.Socket;
import java.util.ArrayList;
/**
* Sets up client to be stored in clientList in ChatServer class
* @author Anonymous?
* @version 1.0
* @release 15/03/2019
* @see ChatServer.java
* 
*/
public class Client {
	boolean active;
	Socket clientSocket;
	/**
	 * Constructor. Sets the field values
	 * @param s
	 * 		Socket of client 
	 */
	public Client(Socket s) {
		this.active = true; 
		this.clientSocket = s;
	}
	/**
	 * Sets active to be false
	 */
	public void setUnactive() {
		this.active=false;
	}
	/**
	* @return clients socket
	*/
	public Socket getSocket() {
		return clientSocket;
	}
	/**
	 * @return Active status of client
	 */
	public boolean getActive() {
		return active;
	}
	

}
