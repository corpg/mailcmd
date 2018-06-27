/**
	Classe principale du programme MailCMD permettant l'éxecution de code à distance par e-mail.
	Contact: etienne.glossi@gmail.com
	Version 0.1 du 26 juillet 2009
   
	@author Etienne Glossi
	@version 0.1
*/


public class MailCmd
{
	/* main */
	public static void main(String[] args)
	{
		// Initialisation du serveur SMTP
		SMTPServer smtp = null;
		try
		{
			smtp = new SMTPServer(); //Démarrage
		}
		catch(ServerException e) // si echec, fin du programme
		{
			System.out.println(e.toString());
			System.out.println("MailCMD will exit !");
			System.exit(0);
		}
		
		Thread server = new Thread(smtp, "SMTPServer"); 
		server.start(); //Démarrage du serveur		
	}
}
