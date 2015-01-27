// DLB trie dictionary
public class TrieDictionary implements DictionaryInterface
{
	TrieElement root;
	
	public TrieDictionary()
	{
		// just make sure a root exists
		root = new TrieElement(0);
	}
	
	public boolean add(String newWord)
	{
		TrieElement currentPos = root;
		char currentChar;
		int length = newWord.length();
		newWord = newWord.toLowerCase();
		
		for (int currentLevel = 0; currentLevel <= length;currentLevel++)
		{
			if (currentLevel == length) currentChar = '0';
			else currentChar = newWord.charAt(currentLevel);
			
			if (currentPos.getLinks().contains(currentChar))
			{
				currentPos = currentPos.getLinks().getLink(currentChar);
				continue;
			}
			else
			{	
				currentPos.getLinks().add(currentChar);
				currentPos = currentPos.getLinks().getLink(currentChar);
				continue;
			}
		}
		
		return true;
	}
	
	/* Returns 0 if s is not a word or prefix within the DictInterface
	 * Returns 1 if s is a prefix within the DictInterface but not a 
	 *         valid word
	 * Returns 2 if s is a word within the DictInterface but not a
	 *         prefix to other words
	 * Returns 3 if s is both a word within the DictInterface and a
	 *         prefix to other words
	 */
	public int search(StringBuilder s)
	{		
		if (root == null) return 0;
		
		String word = s.toString().toLowerCase(); // looking for String word in lower case
		int length = word.length();
		int currentLevel;
		TrieElement currentPos = root; // what we'll iterate through
		char currentChar;
		
		if (word.length() == 0) return 0;
		
		// try to get up to the parent node of the word given
		for (currentLevel = 0; currentLevel < length; currentLevel++)
		{
			currentChar = word.charAt(currentLevel);
			
			if (!currentPos.getLinks().contains(currentChar)) return 0;
			
			currentPos = currentPos.getLinks().getLink(currentChar);
		}
		
		boolean isWord = currentPos.getLinks().contains('0');
		
		boolean isPrefix = false;
		for (int i = 97; i <= 122; i++)
		{
			if (currentPos.getLinks().contains((char) i)) 
			{
				isPrefix = true;
				break;
			}
		}

		if (isWord && isPrefix) return 3;
		else if (isWord) return 2;
		else if (isPrefix) return 1;
		else return 0;
	}
}