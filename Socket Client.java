import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.*;
import javax.swing.JTextField;
import java.awt.Insets;
import java.awt.event.*;
import java.net.*;
import java.io.*;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;

public class Connect extends JFrame implements ActionListener {
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextArea textArea;

	private Socket s = null;
	private BufferedReader readSock = null;
	private PrintWriter writeSock = null;
	private DataInputStream in = null;

	private JButton btnConnect;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Connect frame = new Connect();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Connect() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 436, 614);
		getContentPane().setLayout(null);

		JLabel lblIpAddress = new JLabel("IP Address");
		lblIpAddress.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblIpAddress.setBounds(12, 13, 94, 16);
		getContentPane().add(lblIpAddress);

		JLabel lblPort = new JLabel("Port");
		lblPort.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblPort.setBounds(12, 60, 76, 16);
		getContentPane().add(lblPort);

		textField = new JTextField();
		textField.setBounds(112, 12, 294, 22);
		getContentPane().add(textField);
		textField.setColumns(10);
		textField.setText("localhost");

		textField_1 = new JTextField();
		textField_1.setBounds(112, 59, 116, 22);
		getContentPane().add(textField_1);
		textField_1.setColumns(10);
		textField_1.setText("5520");

		//btnConnect
		btnConnect = new JButton("Connect");
		btnConnect.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnConnect.setBounds(260, 58, 146, 25);
		btnConnect.addActionListener(this);
		btnConnect.setActionCommand("Connect");
		getContentPane().add(btnConnect);

		JLabel lblMessageToServer = new JLabel("Message To Server");
		lblMessageToServer.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblMessageToServer.setBounds(12, 109, 177, 16);
		getContentPane().add(lblMessageToServer);

		textField_2 = new JTextField();
		textField_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		textField_2.setBounds(12, 138, 270, 22);
		getContentPane().add(textField_2);
		textField_2.setColumns(10);

		//btnSend
		JButton btnSend = new JButton("Send");
		btnSend.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnSend.setBounds(309, 137, 97, 25);
		btnSend.addActionListener(this);
		btnSend.setActionCommand("Send");
		getContentPane().add(btnSend);

		JLabel lblNewLabel = new JLabel("Client/Server Communication");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel.setBounds(12, 179, 244, 16);
		getContentPane().add(lblNewLabel);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 208, 394, 331);
		getContentPane().add(scrollPane);

		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setVisible(true);
		textArea.setEditable(false);
	}

	public void actionPerformed(ActionEvent ae) {
        String action = ae.getActionCommand();
        //connect button
        if (action.equals("Connect")) {
        	try{
                int port = Integer.parseInt(textField_1.getText());
        		s = new Socket(textField.getText(), port);

        		writeSock = new PrintWriter(s.getOutputStream(), true);
        		readSock = new BufferedReader( new InputStreamReader(s.getInputStream() ) );

        		btnConnect.setText("Disconnect");
        		btnConnect.setActionCommand("Disconnect");
        		textField.setEditable(false);
        		textField_1.setEditable(false);


                textArea.append("Connected to Server\n");
            }
            catch(UnknownHostException u) {
                textArea.append("Error: Unkown Host\n");
            }
            catch(IOException i){
                textArea.append("Error: Connection Timed Out\n");
            }
        	catch(NumberFormatException e){
        		textArea.append("Error: Invalid Port Value\n");
        	}
        }
        else if(action.equals("Disconnect")){
        	try {
				s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        	btnConnect.setText("Connect");
    		btnConnect.setActionCommand("Connect");
    		textField.setText("localhost");
    		textField_1.setText("5520");
    		textField.setEditable(true);
    		textField_1.setEditable(true);

            textArea.append("Disconnected\n");
        }
        else if(action.equals("Send")){
        	//if the user is not connected to the server
        	if(s == null || s.isClosed()){
        		textArea.append("Error: Cannot send message. You are not connected to server\n");
        	}

        	//if the user is connected to the server
        	else{

        		String outLine = textField_2.getText();
        		textArea.append( "Client: " + outLine + "\n");

        		try {
        			writeSock.println( outLine );
        			String inLine = readSock.readLine();
        			textArea.append( "Server: " + inLine + "\n");
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			if(!outLine.toLowerCase().equals("quit")){
        				textArea.append("Error: Connection timed out\n");
        			}
        			try {
        				s.close();
        			} catch (IOException e2) {
        				// TODO Auto-generated catch block
        				e2.printStackTrace();
        			}

                	btnConnect.setText("Connect");
            		btnConnect.setActionCommand("Connect");
            		textField.setText("localhost");
            		textField_1.setText("5520");
            		textField.setEditable(true);
            		textField_1.setEditable(true);

                    textArea.append("Disconnected\n");
        		} catch (Exception e){
        			if(!outLine.toLowerCase().equals("quit")){
        				textArea.append("Error: Connection timed out\n");
        			}
        		}



        		//if the user types quit, it does what the same thing as the disconnect button
        		if(outLine.toLowerCase().equals("quit")){
        			try {
        				s.close();
        			} catch (IOException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}

                	btnConnect.setText("Connect");
            		btnConnect.setActionCommand("Connect");
            		textField.setText("localhost");
            		textField_1.setText("5520");
            		textField.setEditable(true);
            		textField_1.setEditable(true);

                    textArea.append("Disconnected\n");
        		}

        	}
        	textField_2.setText("");

        }

    }
}
