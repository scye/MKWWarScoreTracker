package ScoreTrack;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.SimpleFileServer;
import java.net.InetSocketAddress;
import java.nio.file.Path;

public class FileServer
{
	private InetSocketAddress address;
	private Path folder;
	private HttpServer server;
	
	public FileServer(String myFolder, int port)
	{
		System.out.println("Creating file server in folder " + myFolder + " at port " + port);
		address = new InetSocketAddress(port);
		folder = Path.of(myFolder);
		server = SimpleFileServer.createFileServer(address, folder, SimpleFileServer.OutputLevel.VERBOSE);
		server.start();
	}
	
	protected void closeServer()
	{
		System.out.println("Closing file server");
		server.stop(0);
	}
}