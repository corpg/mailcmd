/**
   Fournit un serveur SMTP (IPv4 uniquement) simple et efficace permettant de recevoir des e-mail.
*/

import java.net.Socket;

public class SMTPServer extends TCPServer
{
	/* *** Attributs *** */
	private static final int PORT = 25;
	


	/* *** Constructeurs *** */
	
	/**
		Constructeur par défaut initialisant le serveur sur toutes les interfaces.
		@throw ServerException Le constructeur renvoit une telle exception en cas d'echec lors de la création du serveur tcp.
	*/
	public SMTPServer() throws ServerException
	{
		this(false);		
	}
	
	/**
		Constructeur de base permettant d'initialiser le serveur sur une IP précise.
		@param ip L'IP sur laquelle le serveur sera en écoute.
		@throw ServerException Le constructeur renvoit une telle exception en cas d'echec lors de la création du serveur tcp.
	*/
	public SMTPServer(String ip) throws ServerException
	{
		super(PORT, ip);
	}
	
	/**
		Constructeur de base permettant d'initialiser le serveur sur l'adresse local.
		@param localhost Booleen indiquant si le serveur doit-être en écoute sur l'adresse local 127.0.0.1 ("true") ou sur toutes les interfaces ("false").
		@throw ServerException Le constructeur renvoit une telle exception en cas d'échec lors de la création du serveur tcp.
	*/
	public SMTPServer(boolean localhost) throws ServerException
	{
		super(PORT, localhost);
	}
	
	
	
	/* *** Méthodes *** */
	@Override
	public TCPSession connect(Socket s)
	{
		SMTPSession smtps = new SMTPSession(s, "cxr69-10-88-172-231-23.fbx.proxad.net");
		System.out.println("New connection receive from " + smtps.getPrintableSocket() + ".");
		return smtps;		
	}	
}

