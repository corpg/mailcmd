/**
	Exceptions générées par le serveur TCP ou SMTP.
*/

import java.util.StringTokenizer;

public class ServerException extends Exception
{
	private int errorCode = -1;
	private String description;
	
	public ServerException(int errorCode, String desc, Throwable e)
	{
		super(e);
		this.description = desc;
		this.errorCode = errorCode;
	}
	
	public String toString()
	{
		StringTokenizer st = new StringTokenizer(super.toString(), ":");
		if(st.countTokens() <= 2) return "Error " + errorCode + " : " + description + ".";
		else
		{
			st.nextToken();
			st.nextToken();
			
			String cause = st.nextToken();
			return "Error " + errorCode + " : " + description + "." + cause + ".";
		}
	}
	
	public int getErrorCode()
	{
		return errorCode;
	}
}
