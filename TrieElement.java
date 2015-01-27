public class TrieElement
{
	private int level;
	private LL links; // store links to the next lower elements if you're not at a least
	
	public TrieElement(int lv)
	{
		links = new LL(lv);
		level = lv;
	}
	
	public LL getLinks()
	{
		return links;
	}
	public int getLevel()
	{
		return level;
	}
	public void setLinks(LL l)
	{
		links = l;
	}
	public void setLevel(int v)
	{
		level = v;
	}
}