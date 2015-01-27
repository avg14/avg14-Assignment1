// this is a linked list of chars to be used for the DLB tree
public class LL
{
	private LE head;
	int level;

	public LL(int l)
	{
		head = null;
		level = l;
	}

	public LE getHead()
	{
		return head;
	}
	public void setHead(LE l)
	{
		head = l;
	}
	
	// search for character ch in the linked list
	public boolean contains(char ch)
	{
		boolean result = false;
		LE current = head;
		while (current != null)
		{
			if (current.getData() == ch) result = true;
			current = current.getNext();
		}
		return result;
	}
	
	// get TrieElement linking from character ch
	public TrieElement getLink(char ch)
	{
		LE current = head;
		while (current != null)
		{
			if (current.getData() == ch) 
				return current.getLink();
			current = current.getNext();
		}
		return null;
	}
	
	// set new TrieElement linking from character ch
	public void setLink(char ch, TrieElement newLink)
	{
		LE current = head;
		while (current != null)
		{
			if (current.getData() == ch) current.setLink(newLink);
			current = current.getNext();
		}
	}
	
	// adds a new TrieElement below the current one. 
	// ADDS A NEW CHARACTER EVEN IF IT IS ALREADY THERE
	// FIRST NEED TO MAKE SURE THAT CHARACTER IS NOT ALREADY IN THE LL!	

	// I tried sorting the chars in the linked list, but then my program ran slower
	// took more time to sort than you save during searches!
	public void add(String newWord)
	{
		char myChar; // character that we'll want to use for the string s
		TrieElement newElem = new TrieElement(level + 1); // trie element we'll add
		
		// get the character
		if (newWord.length() <= level) myChar = '0'; // 0 char represents null
		else myChar = newWord.charAt(level);
		
		// add in the new character at the front
		head = new LE(myChar, newElem, head);
	}
	
	public void add(char newChar)
	{
		TrieElement newElem = new TrieElement(level + 1);
		head = new LE(newChar, newElem, head);
	}
} 