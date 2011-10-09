import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class TestServer {
	
	private ServerSocket listenSocket;
	private List<ClientConnection> clientSockets;
	
	public TestServer(int port) {
		try {
			listenSocket = new ServerSocket(port);
			System.out.println("Server started on port " + port + "!");
			
			clientSockets = new ArrayList<ClientConnection>();
			boolean serverIsRunning = true;
			int threadCount = 1;
			
			while(serverIsRunning) {
				System.out.println("Waiting for connections...");
				Socket clientSocket = listenSocket.accept();
				ClientConnection c = new ClientConnection(threadCount++, clientSocket);
				c.start();
				clientSockets.add(c);
			}
		} catch (BindException e) {
			System.out.println("ERROR: Unable to bind to to port!");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("Server shutting down...");
		}
	}
	
	public static void main(String[] args) {
		new TestServer(7000);
	}

}
