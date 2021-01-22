package server;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
	private ServerSocket server = null;
	private Socket sock = null;
	private ExecutorService pool = null;
	private int clientcount = 0;
	private int portNumber;
	private Scanner s;

	
	public Server() {
		//3 threads will be available for client to server connection
		this.pool = Executors.newFixedThreadPool(3); 
	}

	public static void main(String[] args) throws Exception {
		Server s = new Server();
		Scanner scan = new Scanner(System.in);
		System.out.println("-------Server side set up-------");
		System.out.println("-> Type 'd' for default port: 9999");
		System.out.println("-> Type any other character to set up port via console");
		String user = scan.nextLine();
		if (user.equalsIgnoreCase("d")) {
			s.defaultPort();
		} else {
			s.setUp();
		}

		s.connect();
		scan.close();

	}

	private void defaultPort() {
		this.portNumber = 9999;
	}

	private void setUp() {
		s = new Scanner(System.in);
		System.out.println("Enter available port number");
		portNumber = Integer.valueOf(s.nextLine());
	}

	private void connect() throws IOException {
		server = new ServerSocket(this.portNumber);
		System.out.println("Server listening on port " + server.getLocalPort());
		
		while (true) {
			sock = server.accept();
			//keeps track of clients
			this.clientcount++;
			// new ServerThreader instance created and passed into the execute method which
			// calls the run method.
			ThreadHandler runThread = new ThreadHandler(this.sock, this.clientcount);
			pool.execute(runThread);
		}

	}
}
