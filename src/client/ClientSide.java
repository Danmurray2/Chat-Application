package client;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientSide {

	private int portNumber;
	private static String hostName;
	private Scanner s;

	private void setPort() {
		s = new Scanner(System.in);
		System.out.println("Enter available port number...Note: must match the server port");
		portNumber = Integer.valueOf(s.nextLine());
	}

	public static void main(String[] args) throws Exception {
		ClientSide cs = new ClientSide();
		Scanner scan = new Scanner(System.in);

		// hostName passed in as a program argument or set to default
		if (args.length > 0) {
			hostName = args[0];
		} else {
			hostName = "localHost";
		}
		System.out.println("-------Client side set up-------");
		System.out.println("-> Host name chosen is: " + hostName);
		System.out.println("-> Type 'd' for default port: 9999");
		System.out.println("-> Type any other character to set up port via console");
		String user = scan.nextLine();

		
		if (user.equalsIgnoreCase("d")) {
			cs.setDefault(); //assigns port to 9999 
		
		} else {
			cs.setPort(); //custom assignment of port
		}

		cs.connect();
		scan.close();
			
		}	

	/*
	 * Creates a new Socket instance. Socket address then created and connection to
	 * server attempted
	 */
	private void connect() {
		Socket sock = new Socket();

		try {// InetSocketAddress sets a new socket address and allows timeout to be set in ms
			sock.connect(new InetSocketAddress(hostName, portNumber), 7000);
			handleSocket(sock);
			

		} catch (Exception e) {
			System.out.println("Failed to connect to server..try again");
			System.out.println("Have you set up server port first?");
			e.printStackTrace();
			
		}
		
	}
	
	/*
	 * Reads and handles input and output and displays to console accordingly
	 */
	private void handleSocket(Socket sock) throws IOException {
		// reading user input from client
		BufferedReader readClientInput = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader receiveRead = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		PrintWriter pwrite = new PrintWriter(sock.getOutputStream(), true);

		System.out.println("Client side initialised on port " + sock.getPort() + "...chat ready!");

		// sending and receiving messages, and outputting to console.
		String receiveMessage, sendMessage;
		while (true) {
			try {
				System.out.print("Client > ");
				if ((sendMessage = readClientInput.readLine()) != null) { // reading the input
					pwrite.println(sendMessage); // sending to server
					pwrite.flush(); // flushes the data
					if ("quit".equalsIgnoreCase(sendMessage)) { // checking for escape characters
						pwrite.println("Client has left the chat");
						break;
					}

					if ((receiveMessage = receiveRead.readLine()) != null) { // receive message from server
						System.out.println(receiveMessage);
					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		sock.close();
	}

	public void setDefault() {
		this.portNumber = 9999;
	
	}

}
