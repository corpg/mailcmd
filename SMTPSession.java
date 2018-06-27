/**
	Classe gérant les différentes connexions au serveur SMTP.
*/

import java.net.Socket;
import java.io.IOException;

public class SMTPSession extends TCPSession
{
	public final String domain;
	
	public SMTPSession(Socket s, String domain)
	{
		super(s);
		super.protocol = "SMTP";
		this.domain = domain;
	}
	
	public void runSession() throws IOException
	{
		System.out.println(this);
		
		// annoncement du serveur
		super.send("220 " + domain + " MailCMD Virtual SMTP Server");
		
		//communication
		String commande;
		while(super.isReady())
			traiteCommande(super.getResponse());
	}
	
	private void traiteCommande(String cmd)
	{
		if(cmd.equals(""))
			return;
		
		String commande = "";
		String param = "";
		
		int d = -1;
		if((d = cmd.indexOf(":")) != -1) 
		{
			commande = cmd.substring(0, d).toLowerCase().replace(" ", "_");
			param = cmd.substring(d + 1);
		}
		else if((d = cmd.indexOf(" ")) != -1)
		{
			commande = cmd.substring(0, d).toLowerCase();
			param = cmd.substring(d + 1);
		}
		else
			commande = cmd.toLowerCase();
		
		try
		{
			SMTPCommand.class.getDeclaredMethod(commande, this.getClass(), param.getClass()).invoke(SMTPCommand.class, this, param);
		}
		catch(Exception e){
			send("500 Unrecognized Command");
			System.out.println(e.toString());
		}
	}	
}
