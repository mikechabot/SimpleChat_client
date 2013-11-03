package interwebs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable {
	
	private Socket socket;
	private Thread thread;
	private Scanner console;
	private Scanner fromServer;
	private PrintWriter toServer;
	private boolean running;
	
	public Client(Socket socket) {
		this.socket = socket;
	}
		
    public void start() { 
    	if (thread == null) { 
    		thread = new Thread(this); 
        	thread.start();
        	running = true;
			System.out.print("\n");
			System.out.println("-- You joined room " + socket.getPort() + " --");
			System.out.print("\n");
        } 
    } 
    
    public void stop() {
    	running = false;
    	console.close();
    	fromServer.close();
    	toServer.close();
    	try {
			socket.close();
		} catch (IOException e) {
			System.out.println("Badness occurred while closing socket");
			e.printStackTrace();
		}
    	thread = null;
    }
	
	@Override
	public void run() {			
		try {
			console = new Scanner(System.in);						// Read data from console
			fromServer = new Scanner(socket.getInputStream());		// Read data from server
			toServer = new PrintWriter(socket.getOutputStream()); 	// Send data to server
			while (running) {
				String input = console.nextLine();	
				toServer.println(input);			
				toServer.flush();
				if(fromServer.hasNext()) {
					System.out.println(fromServer.nextLine());
				}
			}
		} catch (IOException e) {
			System.out.println("Badness occurred while running client thread");
			e.printStackTrace();
		}			
	}
}

