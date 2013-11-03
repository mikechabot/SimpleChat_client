package interwebs;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class SimpleChat {

	private Socket socket;
	private Scanner scanner;
	private String host;
	private int port;
	private static boolean started;
	
	public SimpleChat() {
		scanner = new Scanner(System.in);
	}
		
	public void start() {
		try {
			socket = new Socket(host, port);
			Client client = new Client(socket);
			client.start();
			started = true;
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
		System.out.print("\n");
		System.out.print("What's the hostname of the server? ");
		host = scanner.nextLine();
		try {
			System.out.print("What's the port number? ");
			port = scanner.nextInt();
		} catch (InputMismatchException e) {		
			System.out.print("\n");
			System.out.println(">> Port numbers can't contain non-numeric characters");			
		}
	}
	
	public static void main(String[] args) {
		System.out.println("+------------------------+");
		System.out.println("| Welcome to SimpleChat! |");
		System.out.println("+------------------------+");
		SimpleChat simpleChat;
		while (!started) {
			simpleChat = new SimpleChat();
			simpleChat.getHostInfo();
			simpleChat.start();	
		}
	}
}


