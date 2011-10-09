import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;


public class ClientConnection extends Thread {
	
	private int threadId;
	private Socket clientConnection;
	private boolean clientIsConnected;
	
	public ClientConnection(int threadId, Socket clientConnection) {
		this.threadId = threadId;
		this.clientConnection = clientConnection;
		this.clientIsConnected = true;
	}
	
	public void run() {
		System.out.println("Client connected! Thread ID: " + threadId);
		
		String clientSentence;
		String capitalizedSentence;
		
		try {
			BufferedReader inFromClient;
			DataOutputStream outToClient;
			
			outToClient = new DataOutputStream(clientConnection.getOutputStream());
			Scanner scanner = new Scanner(new FileReader(new File("welcome_message.txt")));
			
			try {
				while(scanner.hasNextLine()) {
					outToClient.writeBytes(scanner.nextLine() + '\r' + '\n');
				}
			} finally {
				scanner.close();
			}
			
			while(clientIsConnected) {
				// Is the socket disconnected?
				if(clientConnection.isOutputShutdown()) {
					clientIsConnected = false;
				} else {
					inFromClient = new BufferedReader(new InputStreamReader(clientConnection.getInputStream()));
					
					clientSentence = inFromClient.readLine();
					System.out.println("Thread ID: " + threadId + ", received message: " + clientSentence);
					
					if(clientSentence.equals("EXIT")) {
						System.out.println("Thread ID: " + threadId + ", client disconnected!");
						clientIsConnected = false;
					} else {
						outToClient = new DataOutputStream(clientConnection.getOutputStream());
						
						capitalizedSentence = clientSentence.toUpperCase() + '\n';
						outToClient.writeBytes(capitalizedSentence);
					}
				}
			}
		} catch (SocketException e) {
			System.out.println("Thread ID: " + threadId + ", socket closed unexpectedly!");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("Thread ID: " + threadId + ", finished!");
		}
	}
	
}
