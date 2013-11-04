package interwebs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class SimpleChat {

	private Socket socket;
	private Scanner console;
	private String host;
	private int port;
	
	public void start() {		
		PrintWriter toServer;	// Send data to server
		Client client;
		try {
			console = new Scanner(System.in);
			socket = new Socket(host, port);
			client = new Client(socket);	
			toServer = new PrintWriter(socket.getOutputStream());
			client.start();			
			while (true) {
				if(console.hasNextLine()) {
					String input = console.nextLine();
					toServer.println(input);		
					toServer.flush();
				}
			}
		
		} catch (UnknownHostException e) {			
			System.out.println("\n>> Could not locate the host; try another, or check again later (UnknownHostException)");			
		} catch (ConnectException e) {		
			System.out.println("\n>> Could not connect to the socket address; try another port, or start checking for firewall issues (ConnectException)");			
		} catch (IllegalArgumentException e) {			
			System.out.println("\n>> Port out of range; try another (IllegalArgumentException)");
		} catch (IOException e) {
			System.out.println("\n>> Badness while acquiring socket connection (IOException)");
			e.printStackTrace();
		} 
	}
	
	public void getHostInfo() {
		console = new Scanner(System.in);	// Read data from console
		System.out.print("\nWhat's the hostname of the server? ");
		host = console.nextLine();
		try {
			System.out.print("What's the port number? ");
			port = console.nextInt();
		} catch (InputMismatchException e) {		
			System.out.println("\n>> Port numbers can't contain non-numeric characters");			
		}
	}
	
	public static void main(String[] args) {
		System.out.println("+------------------------+");
		System.out.println("| Welcome to SimpleChat! |");
		System.out.println("+------------------------+");
		SimpleChat simpleChat;
		while (true) {
			simpleChat = new SimpleChat();
			simpleChat.getHostInfo();
			simpleChat.start();	
		}
	}
}


