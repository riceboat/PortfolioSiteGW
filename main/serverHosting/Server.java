package serverHosting;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server {
	private HttpServer server;
	static String indexPageURI;
	ArrayList<Thread> threads;

	public Server() {
		threads = new ArrayList<Thread>();
		indexPageURI = "/";
		try {
			server = HttpServer.create(new InetSocketAddress(8000), 0);
			server.createContext(indexPageURI, new MyHandler());
			server.setExecutor(null); // creates a default executor
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void startServer() {
		server.start();
	}

	public void stopServer() {
		server.stop(0);
	}

	class MyHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange httpExchange) throws IOException {
			ServerEventHandler eventHandler = new ServerEventHandler(httpExchange);
			Thread t1 = new Thread(eventHandler);
			t1.start();
		}
	}

}
