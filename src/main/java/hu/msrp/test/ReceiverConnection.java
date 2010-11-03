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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

public class ReceiverConnection extends Observable implements Runnable {

	private int sumOfReadedByte = 0;
	private int countOfRead = 0;
	
	private String saveBuffer = new String();
	
	private ByteBuffer buff = null;
	
	private Connection parent;
	
	private InetAddress localHostAddress;
	private int localPort;
	
	private ServerSocketChannel serverSocketChannel = null;
	private Selector selector = null;
	
	public ReceiverConnection(InetAddress localHostAddress, int localPort, Connection parent) throws IOException {
		this.localHostAddress = localHostAddress;
		this.localPort = localPort;
		this.parent = parent;
		this.selector = initSelector();
		buff = ByteBuffer.allocate(1000000);
		buff.clear();
		this.addObserver(parent);
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
		  this.setChanged();
		    // For an accept to be pending the channel must be a server socket channel.
		    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

		    // Accept the connection and make it non-blocking
		    SocketChannel socketChannel = serverSocketChannel.accept();
		    socketChannel.configureBlocking(false);

		    // Register the new SocketChannel with our Selector, indicating
		    // we'd like to be notified when there's data waiting to be read
		    socketChannel.register(this.selector, SelectionKey.OP_READ);
		    this.notifyObservers("Accept");
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
		    
		    List<String> messages = preParse(data);
		    int i = 0;
		    for (String m : messages) {
		    	i++;
		    	//System.out.println("Preparser után az adat " + i + ":\n" + m + " x");
		    	MessageUtil.createMessage(m);
		    }
		    //System.out.println("-----------------------------------------------------------");
		    //System.out.println("-----------------------------------------------------------");
		    //System.out.println("data[" + numRead + "]: " + new String(data));
		    //FileUtil.printToFile("/home/csaba/temp/recvData", data);
		    //System.out.println(countOfRead + " sum byte: " + sumOfReadedByte);
		    
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
	
	private List<String> preParse(byte[] msg) {
		List<String> messages = new ArrayList<String>();
		int succesProcessedByteCount = 0;
		String m = new String(msg);
		 //System.out.println(m);
		if (!saveBuffer.isEmpty()) {
			m = saveBuffer + m;
			saveBuffer = "";
		}
		
		//System.out.println("preparser üzenet: " + m);
		
		ByteBuffer data = ByteBuffer.wrap(m.getBytes(), 0, m.length());
		//System.out.println("preparser data length: " + data.capacity());
		data.limit(data.capacity());
		//System.out.println("data limit: " + data.limit());
		
		int state = 0;
		int byteCounter = 0;
		int messageCounter = 0;
		while (data.hasRemaining()) {
			byte b = data.get();			
			byteCounter++;
			switch(state) {
				case 0 : 
						if (b == '\r') state = 1;
				
						break;
				case 1 :
						if (b == '\n') state = 2;
						else state = 0;
				
						break;
				case 2 :
						if (b == '-')	state = 3;
						else state = 0;

				 		break;
				case 3 :
						if (b == '-')	state = 4;
						else state = 0;
				
		 		 		break;
				case 4 :
						if (b == '-')	state = 5;
				 		else state = 0;

 		 		 		break;
				case 5 :
						if (b == '-')	state = 6;
				 		else state = 0;
				
 		 		 		break;
				case 6 : 
						if (b == '-') state = 7;
				 		 else state = 0;
 		 		 		 
						 break;
				case 7 : 
						if (b == '-')	state = 8;
				 		 else state = 0;
 		 		 
						 break;
				case 8 : 
						if (b == '-')	state = 9;
				 		 else state = 0;
 		 		 
						 break;
				case 9 :
						//System.out.println("case 9, b: " + (char)b);
						//System.out.println(data.remaining());
						if (b == '$' || b == '#' || b == '+') state = 10;
						
				case 10 : 
						if (state == 10) {
							//System.out.println("case 10");
							byte[] temp = new byte[byteCounter];
							data.position(data.position() - byteCounter);
							//System.out.println("Lekérjük a databól a " + data.position() + ". bytetól " + byteCounter + " byteot");
							data.get(temp, 0, byteCounter);
							String chunk = new String(temp);
							messages.add(chunk);
							state = 0;
							messageCounter++;
							succesProcessedByteCount += byteCounter;
							byteCounter = 0;
						}
			    }			
		}
		
		//System.out.println("success processed byte: " + succesProcessedByteCount);
		
		if (byteCounter != 0) {
			byte[] save = new byte[byteCounter];
			data.position(data.position() - byteCounter);
			//System.out.println("Lekérjük a databól a " + data.position() + ". bytetól " + byteCounter + " byteot");
			data.get(save, 0, byteCounter);
			saveBuffer = new String(save);
			//System.out.println("Maradék adat: " + saveBuffer);
		}
		return messages;
	}
	
	public void start() {
		System.out.println("ReceiverConnection start!");
	    new Thread(this).start();
	}

}
