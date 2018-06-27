/**
	Serveur TCP permettant d'initialiser une connection et de retourner les données reçues.
	@author Etienne Glossi
*/

import java.util.StringTokenizer;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.io.IOException;

public abstract class TCPServer implements Runnable
{
	/* *** Attributs *** */
	private ServerSocket srvSock;
	private String ip;
	private int port;

	
	
	/* *** Constructeurs *** */
	/**
		Creation d'un serveur TCP sur le socket ip:port.
		@param ip L'IP sur laquelle le serveur sera en écoute.
		@param port Le port du serveur.
		@param localhost Serveur en écoute sur l'adresse locale ou non.
		@throw ServerException Le constructeur renvoit une telle exception en cas d'echec lors de la création du serveur tcp.
	*/
	private TCPServer(int port, String ip, boolean localhost) throws ServerException
	{
		if(localhost) ip="127.0.0.1";
		
		if(!this.checkSocket(port, ip)) //si mauvais format d'IP (IPv4 uniquement)
		{
			ServerException se = new ServerException(0, "Wrong IP address and port" + ip + ":" + port, null);
			throw se;
		}
		
		InetSocketAddress srvAddress = new InetSocketAddress(ip, port);
		try
		{
			this.srvSock = new ServerSocket();
			this.srvSock.bind(srvAddress);
		}
		catch(IOException e)
		{
			ServerException se = new ServerException(1, "Error when listen on " + ip + ":" + port, e);
			throw se;
		}
		
		this.ip = ip;
		this.port = port;
				
		System.out.println("Server correctly initialized on " + ip + ":" + port + ".");
	}
	
	/**
		Constructeur de base permettant d'initialiser un serveur TCP sur l'adresse local, et sur le port fournit en paramètre.
		@param port Port sur lequel le serveur sera en écoute.
		@param localhost Booleen indiquant si le serveur doit-être en écoute sur l'adresse local 127.0.0.1 ("true") ou sur toutes les interfaces ("false").
		@throw ServerException Le constructeur renvoit une telle exception en cas d'échec lors de la création du serveur tcp.
	*/
	public TCPServer(int port, boolean localhost) throws ServerException
	{
		this(port, "0.0.0.0", localhost);	
	}
	
	/**
		Constructeur de base permettant d'initialiser un serveur TCP sur le socket ip:port.
		@param port Port sur lequel le serveur sera en écoute.
		@param ip Adresse IP sur laquelle le serveur sera en écoute.
		@throw ServerException Le constructeur renvoit une telle exception en cas d'échec lors de la création du serveur tcp.
	*/
	public TCPServer(int port, String ip) throws ServerException
	{
		this(port, ip, false);	
	}
	
	
	
	/* *** Methodes *** */
	
	public void run()
	{
		Socket s = null;
		while(true)
		{
			try{s = srvSock.accept();}
			catch(IOException e){continue;}
			
			TCPSession tcps;
			
			if((tcps = connect(s)) != null)
			{
				new Thread(tcps, tcps.protocol + " session from " + tcps.getPrintableSocket() + ".").start();
			}
			else
			{
				try{s.close();}
				catch(IOException e){}
			}
			s = null;
		}
	}
	
	public abstract TCPSession connect(Socket s);
	
	/** 
		Vérifie que le socket ip:port est un socket IPv4 existant.
		@param port Le port du socket à vérifier.
		@param ip L'adresse IP du socket à vérifier.
		@return True si le socket est bon.	
	*/
	public static boolean checkSocket(int port, String ip)
	{
		if(port < 0 | port > 65535) return false;
		
		StringTokenizer tokenizer = new StringTokenizer(ip, ".");
		for(int i=0;i<4;i++)
		{
			int b = 0;
			if(!tokenizer.hasMoreTokens()) return false;
			try
			{
				b = Integer.parseInt(tokenizer.nextToken());
			}
			catch(NumberFormatException e){return false;}
			
			if(b<0 || b>256) return false;
		}
		if(tokenizer.hasMoreTokens()) return false;
		
		//Tout est ok :)
		return true;
	}
}
