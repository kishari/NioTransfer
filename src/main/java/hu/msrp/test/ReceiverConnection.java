package hu.msrp.test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

public class ReceiverConnection implements Runnable {

	private int sumOfReadedByte = 0;
	private int countOfRead = 0;
	
	private boolean isServer = false; 
	
	private ByteBuffer buff = null;
	
	private Connection parent;
	
	private InetAddress localHostAddress;
	private int localPort;
	
	private ServerSocketChannel serverSocketChannel = null;
	private Selector selector = null;
	
	public ReceiverConnection(InetAddress localHostAddress, int localPort, Connection parent, boolean server) throws IOException {
		this.localHostAddress = localHostAddress;
		this.localPort = localPort;
		this.parent = parent;
		this.isServer = server;
		this.selector = initSelector();
		buff = ByteBuffer.allocate(1000000);
		buff.clear();
	}

	public void run() {
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
			        if (key.isAcceptable()) {
				        accept(key);
				    }
			        if (key.isReadable()) {
			        	read(key);
			        }
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void accept(SelectionKey key) throws IOException {
		  System.out.println("accept!");
		    // For an accept to be pending the channel must be a server socket channel.
		    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

		    // Accept the connection and make it non-blocking
		    SocketChannel socketChannel = serverSocketChannel.accept();
		    socketChannel.configureBlocking(false);

		    // Register the new SocketChannel with our Selector, indicating
		    // we'd like to be notified when there's data waiting to be read
		    socketChannel.register(this.selector, SelectionKey.OP_READ);
	}
	
	private void read(SelectionKey key) throws IOException {
		//System.out.println("Server read from client!");
	    SocketChannel socketChannel = (SocketChannel) key.channel();
		    
		    // Clear out our read buffer so it's ready for new data
		    
		    // Attempt to read off the channel
		    int numRead;
		    try {
		      buff.clear();
		      numRead = socketChannel.read(buff);
		      sumOfReadedByte += numRead;
		      countOfRead++;
		      //System.out.println("number of readed byte: " + numRead);
		    } catch (IOException e) {
		      // The remote forcibly closed the connection, cancel
		      // the selection key and close the channel.
		      key.cancel();
		      socketChannel.close();
		      return;
		    }

		    if (numRead == -1) {
		      // Remote entity shut the socket down cleanly. Do the
		      // same from our end and cancel the channel.
		      key.channel().close();
		      key.cancel();
		      return;
		    }
		    //System.out.println(readBuffer.position());
		    //this.readBuffer.rewind();
		    
		    byte[] data = new byte[numRead];
		   // System.out.println(readBuffer.position());
		    
		    buff.rewind();
		    //System.out.println(b.toString());
		    buff.get(data, 0, numRead);
		    //System.out.println(b.toString());
		    
		    System.out.println("data[" + numRead + "]: " + new String(data));
		    //FileUtil.printToFile("/home/csaba/temp/recvData", data);
		    System.out.println(countOfRead + " sum byte: " + sumOfReadedByte);
		    
		    if (countOfRead%5 == 0 && isServer) {
		    	String m = new String(data);
		    	m += countOfRead;
		    	parent.getSenderConn().send(m);
		    }

	}
	  
	private Selector initSelector() throws IOException {
	    Selector socketSelector = SelectorProvider.provider().openSelector();

	    // Create a new non-blocking server socket channel
	    this.serverSocketChannel = ServerSocketChannel.open();
	    serverSocketChannel.configureBlocking(false);

	    InetSocketAddress isa = new InetSocketAddress(this.localHostAddress, this.localPort);
	    serverSocketChannel.socket().bind(isa);

	    // Register the server socket channel, indicating an interest in 
	    // accepting new connections
	    serverSocketChannel.register(socketSelector, SelectionKey.OP_ACCEPT);

	    return socketSelector;
	}
	
	public void start() {
		System.out.println("ReceiverConnection start!");
	    new Thread(this).start();
	}

}
