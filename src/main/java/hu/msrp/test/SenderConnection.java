package hu.msrp.test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

public class SenderConnection implements Runnable {

	private InetAddress remoteHostAddress;
	private int remotePort;
	
	private boolean isServer = false;
	private boolean isFirstSend = true;
	private Connection parent;
	private SocketChannel senderChannel = null;
	private Selector selector = null;
	
	public SenderConnection(InetAddress remoteHostAddress, int remotePort, Connection parent, boolean isServer) throws IOException {
		this.remoteHostAddress = remoteHostAddress;
		this.remotePort = remotePort;
		this.parent = parent;
		this.isServer = isServer;
		this.selector = initSelector();
	}
	
	public void run() {
		try {
			senderChannel = this.initiateConnection();
		} catch (IOException e1) {
			e1.printStackTrace();
		}		
		while (true) {
			try {
				// Wait for an event one of the registered channels
				this.selector.select();

				// Iterate over the set of keys for which events are available
				Iterator<SelectionKey> selectedKeys = this.selector.selectedKeys().iterator();
				while (selectedKeys.hasNext()) {
					SelectionKey key = (SelectionKey) selectedKeys.next();
					selectedKeys.remove();

					if (!key.isValid()) {
						continue;
					}
					// Check what event is available and deal with it
					if (key.isConnectable()) {
						this.finishConnection(key);
					} else if (key.isReadable()) {
						//this.read(key);
					} else if (key.isWritable()) {
						//this.write(key);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private SocketChannel initiateConnection() throws IOException {
		System.out.println("sender initiateConnection");
		// Create a non-blocking socket channel
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);
	
		// Kick off connection establishment
		socketChannel.connect(new InetSocketAddress(this.remoteHostAddress, this.remotePort));
		
		socketChannel.register(this.selector, SelectionKey.OP_CONNECT);
		
		return socketChannel;
	}
	
	private void finishConnection(SelectionKey key) throws IOException {
		System.out.println("sender finishconnection");
		SocketChannel socketChannel = (SocketChannel) key.channel();
	
		try {
			socketChannel.finishConnect();
		} catch (IOException e) {
			// Cancel the channel's registration with our selector
			System.err.println(e);
			key.cancel();
			return;
		}
		
		key.interestOps(SelectionKey.OP_READ);
		
		String data = new String();
		
//		for (int i = 0; i < 512; i++) {
			data += "Hello vazze ";
//		}
		send(data);
		
	}
	
	private Selector initSelector() throws IOException {
		// Create a new selector
		return SelectorProvider.provider().openSelector();
	}

	public void send(String msg) throws IOException {
		//System.out.println("Client send to server");
		byte[] data = msg.getBytes();
		System.out.println(msg.getBytes().length);
		ByteBuffer b = ByteBuffer.allocate(msg.getBytes().length);
		b.clear();
		b = ByteBuffer.wrap(data);
	
		if (isServer) {
			if (isFirstSend) {
				try {
					Thread.sleep(10000);
					isFirstSend = false;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("Server küldi: " + new String(data));
			
			this.senderChannel.write(b);
		}
		else {
			for (int i = 0; i < 15; i++) {
				//FileUtil.printToFile("/home/csaba/temp/sendData", data);
				//System.out.println(i + " .küldés...");
				this.senderChannel.write(b);
				b.rewind();
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}
	
	public void start() {
		new Thread(this).start();
	}
}
