package hu.msrp.test.gui;

import hu.msrp.test.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Receiver extends JFrame implements WindowListener, Runnable {

	private static final long serialVersionUID = 2447095394523943452L;

	private MSRPStack msrpStack = new MSRPStack();
	
	@Override
	public void run() {
		createGUI();
	}
	
	private JFrame createGUI() {
		JFrame win = new JFrame();
		win.setLayout(new BorderLayout());
		win.setTitle("Receiver");
		win.setLocation(0, 210);
		
		win.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				
		JButton inviteButton = new JButton("--> Invite");
		inviteButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {								
				System.out.println("Invite érkezett! Küldjünk erre 200 OK-t");				
			}			
		});
		
		JButton okButton = new JButton("200 OK -->");
		okButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					//9092 a helyi port
					getMsrpStack().createConnection(InetAddress.getLocalHost(), 9092, InetAddress.getLocalHost(), 9090);
					getMsrpStack().getConnection().getReceiverConn().start();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("200 OK elküldve!");
			}			
		});
		
		JButton ackButton = new JButton("--> Ack");
		ackButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				getMsrpStack().getConnection().getSenderConn().start();
				System.out.println("ACK érkezett!");
			}			
		});
		
		JPanel buttonPanel = new JPanel(new GridLayout(0,1));
		
		buttonPanel.add(inviteButton);
		buttonPanel.add(okButton);
		buttonPanel.add(ackButton);
		win.add(buttonPanel, BorderLayout.NORTH);
		win.setPreferredSize(new Dimension(200, 200));
		
		win.pack();
		win.setVisible(true);
		return null;
	}
	
	public MSRPStack getMsrpStack() {
		return msrpStack;
	}

	@Override
	public void windowActivated(WindowEvent arg0) {	}

	@Override
	public void windowClosed(WindowEvent arg0) { }

	@Override
	public void windowClosing(WindowEvent arg0) { }

	@Override
	public void windowDeactivated(WindowEvent arg0) { }

	@Override
	public void windowDeiconified(WindowEvent arg0) { }

	@Override
	public void windowIconified(WindowEvent arg0) { }

	@Override
	public void windowOpened(WindowEvent arg0) { }

	public static void main(String[] args) {
		Receiver receiver = new Receiver();
		receiver.createGUI();
	}
}
