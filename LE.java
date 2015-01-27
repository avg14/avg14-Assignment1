// This is one element of a linked list of chars used for the DLB trie
public class LE
{
  private char data;
  private TrieElement link;
  private LE next; 

  public LE(char ch, TrieElement li, LE ne)
  {
    setData( ch );
    setLink( li );
    setNext( ne );
  }

  public char getData()
  {
    return data;
  }
  public LE getNext()
  {
    return next;
  }  
  public TrieElement getLink()
  {
	  return link;
  }
  public void setData(char data)
  {
     this.data = data;
  }
  public void setNext(LE next)
  {
    this.next = next;
  }  
  public void setLink(TrieElement link)
  {
	  this.link = link;
  }
} 