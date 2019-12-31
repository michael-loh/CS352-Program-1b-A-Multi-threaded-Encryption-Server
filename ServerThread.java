import java.io.*;
import java.net.*;
import java.util.*;

class ServerThread extends Thread{
	private Socket clientSock = null;
	private PrintWriter logFile = null;
	private PrintWriter writer = null;
	private BufferedReader reader = null;

	public ServerThread(Socket clientSock, PrintWriter logFile){
		this.clientSock = clientSock;
		this.logFile = logFile;
	}

	public void run(){
		Date date = new Date();
		logFile.println("Got a Connection: " + date.toString() +  "   " + clientSock.getInetAddress() + "   Port: " + clientSock.getPort());
		logFile.flush();

		try {
			reader = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
			writer = new PrintWriter(clientSock.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e2){
			e2.printStackTrace();
		}

		PolyAlphabet pa = new PolyAlphabet(5, 19);

		String disconnect = "ServerThread Excpetion: java.net.SocketException: Connection Reset   Port: " + clientSock.getPort();
		while(!clientSock.isClosed()){
			String inLine;
			try{
				inLine = reader.readLine();
				if(inLine.toLowerCase().equals("quit")){
					writer.println("Disconnected");
					disconnect = "Connection closed. Port: " + clientSock.getPort();
					clientSock.close();
				}
				else{
					writer.println(pa.encrypt(inLine));
				}

			}catch(IOException e){			//if readLine is null, continue looping until something is input
				continue;
			}catch(NullPointerException e2){
				try {
					clientSock.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					break;
				}
			}
		}
		logFile.println(disconnect);
		logFile.flush();
	}

}
