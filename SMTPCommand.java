/**
	Contient l'ensembles des commandes SMTP gére par le serveur virtuel.
*/

public class SMTPCommand
{
	public static void helo(SMTPSession smtp, String param)
	{
		if(param.equals(""))
			smtp.send("501 Wrong Syntax: HELO hostname");
		else //aucune vérification de l'émetteur !
			smtp.send("250 " + smtp.domain);
	}
	
	public static void mail_from(SMTPSession smtp, String param)
	{
		//aucune vérification de l'émetteur !
		smtp.send("250");
	}
	
	public static void rcpt_to(SMTPSession smtp, String param)
	{
		//aucune vérification du destinataire !
		smtp.send("250");
	}
	
	public static void data(SMTPSession smtp, String param)
	{
		//no param yet
		smtp.send("354");
		
		StringBuilder message = new StringBuilder();
		String m = "";
		do
		{
			message.append(m);
			try{m = smtp.getResponse() + "\n";}
			catch(java.io.IOException e){break;}
		}
		while(!m.equals(".\n"));
		
		System.out.println("Message recu: " + message.toString());
		smtp.send("250");
		smtp.send("QUIT");
	}
	
	public static void quit(SMTPSession smtp, String param)
	{
		//fin de la connection
		smtp.close();
	}
}
