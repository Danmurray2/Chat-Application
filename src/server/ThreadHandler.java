package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ThreadHandler implements Runnable {
	private Socket socket;
	private BufferedReader userInput;
	private BufferedReader recieveFromClient;
	private PrintWriter pwrite;
	private int clientNumber;

	/*
	 * Class Constructor taking in Socket and clientNumber as parameters Initialises
	 * the class variables
	 */
	public ThreadHandler(Socket sock, int clientNumber) throws IOException {
		this.socket = sock;
		this.clientNumber = clientNumber;
		System.out.println("Connection made with client: " + clientNumber);
		// initialising objects
	    userInput = new BufferedReader(new InputStreamReader(System.in));
		pwrite = new PrintWriter(sock.getOutputStream(), true);
		recieveFromClient = new BufferedReader(new InputStreamReader(sock.getInputStream()));
	}

	/*
	 * Responsible for reading, sending, and outputting messages. Allows for
	 * multiple threaded clients to chat with the server
	 */
	public void run() {

		String receiveMessage, sendMessage;
		boolean keepRunning = true;
		try {
			while (keepRunning) {
				
				// reads and checks for any unexpected disconnection (!=null). Prints received
				// message from client to console
				if ((receiveMessage = recieveFromClient.readLine()) != null) {
					System.out.println("Client " + clientNumber + ": " + receiveMessage);
					System.out.print("Server > ");

					if ("quit".equalsIgnoreCase(receiveMessage)) {
						// checks if client has sent escape char.
						System.out.println("Client " + clientNumber + " has left the chat");
					}

				}
				// assigns input to sendMessage
				sendMessage = userInput.readLine();

				if ("quit".equalsIgnoreCase(sendMessage)) { // checking for escape characters
					System.out.println("Server left chat...");
					pwrite.println("Server Disconnected");
					keepRunning = false;
				}
				// sends message back to client
				System.out.print("Server > ");
				pwrite.println("Server: " + sendMessage);
				pwrite.flush();
			}

			socket.close(); // terminates socket connection

		}

		catch (Exception e) { // if client disconnects suddenly
			System.out.println("Client: " + this.clientNumber + " has disconnected unexpectedly");
		}

		System.exit(0);

	}
}
