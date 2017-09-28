
public class voteCounting 
// Student Name : 	patrick connaughton
  // Student Id Number : 	C00167985
{
	private String name;
	private String Party;
//	private int [] vote = new String [6] ;
	
	
	public voteCounting()
	{
		setName("");
		setParty("");
	}
	public voteCounting(String name, String Party)
	{
		setName(name);
		setParty(Party);	
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public void setParty (String Party)
	{
		this.Party = Party;
	}	
	public String getName()
	{
		return name;
	}
	public String getParty()
	{
		return Party;
	}
	public String toString()
	{
		String printString = "";
		
		printString += "\n There Name is -> " + name;
		printString += "\n There Party is ->  = " + Party;
		return printString;
	}
}