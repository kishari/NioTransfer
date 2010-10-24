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

public class Sender extends JFrame implements WindowListener, Runnable {

	private static final long serialVersionUID = 2447095394523943452L;
	
	private MSRPStack msrpStack = new MSRPStack();
	
	
	@Override
	public void run() {
		createGUI();
	}
	
	private void createConnection() throws UnknownHostException, IOException {
		getMsrpStack().createConnection(InetAddress.getLocalHost(), 9090, InetAddress.getLocalHost(), 9092);
	}

	private JFrame createGUI() {
		JFrame win = new JFrame();
		win.setLayout(new BorderLayout());
		win.setTitle("Sender");
		win.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				
		JButton inviteButton = new JButton("Invite");
		inviteButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				System.out.println("Invite elküldve!");
			}			
		});
		
		JButton okButton = new JButton("200 OK");
		okButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					createConnection();
					getMsrpStack().getConnection().getReceiverConn().start();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				System.out.println("200 OK érkezett! Connection létrehozva. Küldünk erre ACK-ot!\n" +
								   "(Várunk a távoli gép connect kérelmére)");
			}			
		});
		
		JButton sendButton = new JButton("send");
		sendButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					getMsrpStack().getConnection().getSenderConn().send("Nesze üzenet");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}			
		});
		
		JPanel buttonPanel = new JPanel(new GridLayout(0,1));
		
		buttonPanel.add(inviteButton);
		buttonPanel.add(okButton);
		buttonPanel.add(sendButton);
		win.add(buttonPanel, BorderLayout.NORTH);
		win.setPreferredSize(new Dimension(200, 200));
		
		win.pack();
		win.setVisible(true);
		return null;
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
	
	public MSRPStack getMsrpStack() {
		return msrpStack;
	}

	public static void main(String[] args) {
		new Thread( new Sender() ).start();
					
	}
}
