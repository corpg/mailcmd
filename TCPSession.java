/**
	Classe gérant les différentes connexions au serveur TCP.
*/

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public abstract class TCPSession implements Runnable
{
	private Socket socket; //socket de la connexion
	private boolean ready; //drapeau indiquant l'état de la session
	private PrintWriter out; //flux de sortie
	private InputStreamReader in; //flux d'entrée
	
	public String protocol = "TCP"; //protocole applicatif utilisé
	
	/* Constantes */
	public static final char LF = 0x0A;
	public static final char CR = 0x0D;
	public static final char EOF = 0x1A;
	public static final char NULL = 0x00;
	/*  *  *  *  */
	
	
	/* *** Construteurs *** */
	/**
		Creation d'un thread pour la session TCP en cours en utilisant le socket s.
		@param s Le socket de la connection établie.
	*/
	public TCPSession(Socket s)
	{
		this.socket = s;
		this.in = getInputStreamReader();
		this.out = getPrintWriter();
		this.ready = true;
	}

	
	
	/* *** Méthodes *** */
	/**
		Méthode permettant d'obtenir un flux sur lequel pourvoir écrire les données à envoyer au client.
		@return Un objet PrintWriter sur lequel efféctuer l'envoie des données.
	*/	
	private PrintWriter getPrintWriter()
	{
		PrintWriter pw = null;
		try
		{
			pw = new PrintWriter(
				new BufferedWriter (
					new OutputStreamWriter (
						socket.getOutputStream())), false);
		}
		catch(IOException e)
		{
			pw = null;
		}
		
		return pw;
	}
	
	/**
		Méthode permettant d'obtenir un flux sur lequel récupérer les données envoyées au serveur.
		@return Un objet InputStreamReader sur lequel lire les données.
	*/
	private InputStreamReader getInputStreamReader()
	{
		InputStreamReader isr = null;
		try
		{
			isr = new InputStreamReader(
					new BufferedInputStream(
						socket.getInputStream()));
		}
		catch(IOException e)
		{
			isr = null;
		}
		
		return isr;
	}
	
	
	/**
		Fermeture de la session à l'initiative du serveur ou à la demande du client.
	*/
	public final void close()
	{
		if(!ready) return;
		
		try{socket.close();}
		catch(IOException e){System.out.println("Failed to close socket !");}
		this.ready = false;
	}
	
	/**
		Retourne le socket sous forme affichable.
		@return Une chaine de caractère représentant le socket (ip:port)
	*/
	public final String getPrintableSocket()
	{
		return socket.getInetAddress().getHostAddress() + ":" +  socket.getPort();
	}
	
	/** 
		Méthode abstraite coeur du thread de la session et réalisant toutes les opérations de traitement des données reçues sur le flux d'entrées et gérant les données à envoyer au client. 
	*/
	public final void run()
	{
		if(!ready)
		{
			System.out.println(this);
			return;
		}
		
		try
		{
			//éxecution du code de la session
			runSession();
		}
		catch(IOException e)
		{
			System.out.println("Connection with " + getPrintableSocket() + " ends prematurely ...");
			//System.out.println(e);
		}
		
		//fin - fermeture du socket
		close();
		System.out.println(this);
	}
	
	/**
		
		@return La chaîne de caractères.
	*/
	public String getResponse() throws IOException
	{
		StringBuilder chaine = new StringBuilder();
		char c = NULL;
		
		do
		{
			chaine.append(c);
			c = (char) in.read();
		}
		while(c != CR & (byte)c != -1);
		
		if((byte)c == -1)
		{
			close();
		}
		
		return chaine.toString().trim();			
	}
	
	/**
		Lire un caractère sur le flux d'entréee.
		@return Le caractère lu.
	*/
	public char read() throws IOException
	{
		char c = NULL;
		int b = in.read();
		if(b == -1)
		{
			close();
			System.out.println(this);
			c = NULL;
		}
		else
		{
			c = (char) b;
		}
		
		return c;			
	}
	
	public void send(String s)
	{
		this.print(s + "\r\n");
	}
	
	public void print(String s)
	{
		out.print(s);
		out.flush();
	}
	
	/**
		Affichage de l'état de la session.
		@return Une chaîne de caractères indiquant l'état de la session (établie, fermée, non initiailisée).
	*/
	public String toString()
	{
		if(socket.isClosed())
			return "Connection closed !";
		else
			return "Connection established with " + getPrintableSocket() + ".";
	}
	
	public boolean isReady()
	{
		return ready;
	}
	
	/**
		Méthode à implémenter dans la classe fille. Cette méthode est permet de gérer la session au niveau applicatif.
	*/		
	public abstract void runSession() throws IOException;
}
