package networking;

import java.io.Closeable;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import bus.Message;
import bus.MessageBus;
import bus.Recipients;
import utility.Logger;

public class NetworkConnector implements Closeable {
	
	private static final int READ_TIMEOUT = 10000;
	
	private Socket connection;
	
	public NetworkConnector(String address, int port) {
		try {
			connection = new Socket(address, port);
			connection.setSoTimeout(READ_TIMEOUT);
		} catch (ConnectException e) {
			Logger.ERROR.log("No service listening on the specified port");
		} catch (SocketException e) {
			e.printStackTrace(Logger.ERROR.getPrintStream());
		} catch (UnknownHostException e) {
			Logger.ERROR.log("Cannot find server: Unkown Host");
		} catch (IOException e) {
			e.printStackTrace(Logger.ERROR.getPrintStream());
		}
	}
	
	public void processMessages() {
		Message message = null;
		while((message = MessageBus.getInstance().getNextMessage(Recipients.NETWORK_CONNECTOR)) != null) {
			final Object[] args = message.getArgs();
			switch(message.getBehaviorID()) {
			
			default: Logger.ERROR.log("Network Controller doesn't recognize this behavior ID: " + message.getBehaviorID());
			}
		}
	}

	public boolean isConnected() {
		return (connection == null) ? false : connection.isConnected();
	}
	
	@Override
	public void close() throws IOException {
		// Announce disconnect to Server 
		// Then close
		connection.close();
	}
	
}
