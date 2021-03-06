package interwebs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable {
	
	private static final String GOODBYE_SRVR = "**GOODBYE_FROM_SERVER**";
	private static final String GOODBYE_CLNT = "**GOODBYE_FROM_CLIENT**";
	
	private Heartbeat heart;
	private Socket socket;
	private Thread thread;
	private Scanner fromServer;
	private PrintWriter toServer;
	private boolean running;
	
	public Client(Socket socket) {
		this.socket = socket;
	}
		
    public void start() { 
    	if (thread == null) { 
    		thread = new Thread(this);
    		heart = new Heartbeat(this);
    		heart.start();
    		thread.start();
        	running = true;
        } 
    }
    
    public boolean isRunning() {
    	return running;
    }
    
    public Socket getSocket() {
    	return socket;
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
    
	public void processServerMessage(String message) {
		if(message.equals(GOODBYE_SRVR)) {
			toServer.println(GOODBYE_CLNT);
			toServer.flush();
			heart.stop();
			stop();
		} else {
			System.out.println(message);
		}
	}
	
	@Override
	public void run() {			
		try {						
			fromServer = new Scanner(socket.getInputStream());		// Read text data from socket input stream
			toServer = new PrintWriter(socket.getOutputStream());	// Send text data to server
			while (running) {
				if(fromServer.hasNext()) {
					processServerMessage(fromServer.nextLine());
				}
			}
		} catch (IOException e) {
			System.out.println("\n>> Badness occurred while running client thread");
			e.printStackTrace();
		}			
	}

}

