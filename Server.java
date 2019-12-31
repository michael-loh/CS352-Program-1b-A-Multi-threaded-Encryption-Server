import java.io.*;
import java.net.*;

public class Server {

	public static void main(String[] args) {
		Server server = new Server();
		server.run();

	}

	public static void run(){
		int portNum = 5520;
		ServerSocket servSock = null;
		try {
			servSock = new ServerSocket(portNum);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		PrintWriter logFile = null;
		try {
			logFile = new PrintWriter(new FileOutputStream("prog1b.log", true));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block

		}


		while(true){
			Socket sock = null;
			try {
				sock = servSock.accept();
				ServerThread servThread = new ServerThread(sock, logFile);
				servThread.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
