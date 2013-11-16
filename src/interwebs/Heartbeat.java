package interwebs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Heartbeat implements Runnable{
	
	private static final String PING_CLNT = "**PING_FROM_CLIENT**";
	
	private Client client;
	private Thread thread;
	private Socket socket;
	private Scanner fromServer;
	private PrintWriter toServer;
	private boolean running;
	
	public Heartbeat(Client client) {
		this.client = client;
		this.socket = client.getSocket();
	}
		
    public void start() { 
    	if (thread == null) { 
    		thread = new Thread(this); 
        	thread.start();
        	running = true;
        } 
    }
    
    public boolean isRunning() {
    	return running;
    }
    
    public void stop() {
    	running = false;
    	fromServer.close();
    	toServer.close();
    	try {
			socket.close();
		} catch (IOException e) {
			System.out.println("\n>> Badness occurred while closing socket");
			e.printStackTrace();
		}
    	thread = null;
    }
    	
	@Override
	public void run() {			
		try {						
			fromServer = new Scanner(socket.getInputStream());		// Read text data from socket input stream
			toServer = new PrintWriter(socket.getOutputStream());	// Send text data to server
			while (true) {
				toServer.println(PING_CLNT);
				if(toServer.checkError()) {
					System.out.println(">> Disconnected from chat, press Enter to continue...");
					client.stop();
					stop();
					break;
				}
				toServer.flush();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					System.out.println("\n>> Thread sleep interrupted unexpectedly");
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			System.out.println("\n>> Badness occurred while running client thread");
			e.printStackTrace();
		}			
	}
	
}
