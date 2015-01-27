import java.io.*;
import java.util.*;

public class MyBoggle
{		
	static char[][] boardOne;
	static int total; // number of words total
	
	public static void main(String[] args) throws Exception
	{
		// declare variables
		char[][] board = new char[4][4];
		SimpleDictionary allWords = new SimpleDictionary();
		SimpleDictionary goodWords = new SimpleDictionary();
		SimpleDictionary guessedWords = new SimpleDictionary();
		TrieDictionary allWordsT = new TrieDictionary();
		TrieDictionary goodWordsT = new TrieDictionary();
		TrieDictionary guessedWordsT = new TrieDictionary();
		int numGuessed = 0;
		boolean simple = true;
		String boardFile = null;
		boardOne = new char[4][4]; // the original board you NEVER change
		total = 0;
		
		// deal with input arguments to set some flags of which methods to use
		// -d simple: SimpleDictionary 
		// -d dlb: De La Briandais dictionary I'll write
		// default to SimpleDictionary
		// -b boardFileName
		for (int i = 0; i < args.length - 1; i++)
		{
			if (args[i].equals("-b")) boardFile = args[i+1];
			
			if (args[i].equals("-d"))
			{
				if (args[i+1].equals("dlb")) simple = false;
				else if (args[i+1].equals("simple")) simple = true;
				else
				{
					System.out.println("Bad Argument");
					System.exit(0);
				}
			}
		}
		if (boardFile == null)
		{
			System.out.println("No Board Given");
			System.exit(0);
		}
		
		// import from boardFile into board[][]
		File fi = new File(boardFile);
		Scanner sc = new Scanner(fi);
		String str = sc.nextLine();
		sc.close();
		int k = 0;
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				board[i][j] = str.charAt(k);
				boardOne[i][j] = str.charAt(k);
				k++;
			}
		}
		
		// load dictionary.txt using either SimpleDictionary.java or the complex one I write
		fi = new File("dictionary.txt");
		sc = new Scanner(fi);
		while (sc.hasNextLine())
		{
			String currentWord = sc.nextLine();
			if (currentWord.length() >= 3)
			{
				if (simple) allWords.add(currentWord);
				else allWordsT.add(currentWord);
			}
		}
		sc.close();

		// find all words on the board, inputting them into another SimpleDictionary instance
		for (int xStart = 0; xStart < 4; xStart++)
		{
			for (int yStart = 0; yStart < 4; yStart++)
			{
				if (simple)
					goodWords = wordSearch(board, new StringBuilder(), goodWords, allWords, xStart, yStart);
				else
					goodWordsT = wordSearchT(board, new StringBuilder(), goodWordsT, allWordsT, xStart, yStart);
			}
		}		

		// GAMEPLAY
		
		// Print out the board
		System.out.println();
		System.out.println("Welcome to Boggle");
		System.out.println();
		System.out.println();
		
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				System.out.print(board[i][j]);
				System.out.print(' ');
			}
			System.out.println();
		}
		
		System.out.println();
		System.out.println();
		System.out.println("Enter as many words as you can");
		System.out.println("Enter a '.' to end");
		
		// let user enter words
		String guess = new String();
		Console co = System.console();
		while (true)
		{
			// read a word and convert it to lower case
			System.out.println();
			guess = co.readLine("Enter a word: ");
			guess = guess.toLowerCase();
			
			// exit if you enter '.'
			if (guess.equals(".")) break; 
			
			// check if your word is correct
			int wordstatus;
			if (simple)
				wordstatus = goodWords.search(new StringBuilder(guess));
			else
				wordstatus = goodWordsT.search(new StringBuilder(guess));
			if (wordstatus == 0 || wordstatus == 1)
			{
				System.out.println("You're Wrong");
			}
			else
			{
				System.out.println("You're Right");
				
				// make sure it hasn't already been guessed before you add a point
				int guessstatus;
				if (simple)
					guessstatus = guessedWords.search(new StringBuilder(guess));
				else
					guessstatus = guessedWordsT.search(new StringBuilder(guess)); 
				if (guessstatus == 0 || guessstatus == 1)
				{
					numGuessed++;
					if (simple)
						guessedWords.add(guess);
					else
						guessedWordsT.add(guess);
				}
			}
		}
		
		// END OF GAME STUFF
		
		// print out all possible words in alpha order
		System.out.println();
		System.out.println("All the words were:");
		fi = new File("dictionary.txt");
		Scanner sc2 = new Scanner(fi);
		while (sc2.hasNextLine())
		{
			String currentWord = sc2.nextLine();
			int result;
			if (simple) result = goodWords.search(new StringBuilder(currentWord));
			else result = goodWordsT.search(new StringBuilder(currentWord));
			if ((result == 3) || (result == 2))
				System.out.println(currentWord);
		}
		sc2.close();
		
		// show all words user found
		System.out.println();
		System.out.println("You found:");
		fi = new File("dictionary.txt");
		Scanner sc3 = new Scanner(fi);
		while (sc3.hasNextLine())
		{
			String currentWord = sc3.nextLine();
			int result;
			if (simple) result = guessedWords.search(new StringBuilder(currentWord));
			else result = guessedWordsT.search(new StringBuilder(currentWord));
			if ((result == 3) || (result == 2))
				System.out.println(currentWord);
		}
		sc3.close();
		
		// show number of words user found
		System.out.println();
		System.out.println("You guessed " + numGuessed + " words");
		
		// show total number of words
		System.out.println("There were " + total + " words in total");
		
		// show percentage of all words user found
		float percent = 100 * (float) numGuessed / (float) total;
		System.out.println("You found " + percent + "% of the words");		
	}
	
	public static SimpleDictionary wordSearch(char[][] board, StringBuilder wordpart, SimpleDictionary goodWords, SimpleDictionary allWords, int nextI, int nextJ)
	{
		// separately handle case where you have a wild card
		if (board[nextI][nextJ] == '*')
		{
			for (int i = 97; i < 123; i++) // for every ASCII lowercase letter
			{
				board[nextI][nextJ] = (char) i;
				boardOne[nextI][nextJ] = (char) i;
				goodWords =  wordSearch(board, wordpart, goodWords, allWords, nextI, nextJ);
			}
			board[nextI][nextJ] = '*';
			boardOne[nextI][nextJ] = '*';
			return goodWords;
		}
		
		// add current character to wordpart
		wordpart.append(java.lang.Character.toLowerCase(board[nextI][nextJ]));		
		
		switch (allWords.search(wordpart))
		{
			// stop if it's neither prefix nor word
			case 0: wordpart.deleteCharAt(wordpart.length() - 1);
					return goodWords;
			
			// keep going if it's prefix but not word
			case 1: break;
			
			// add to dictionary and stop it it's word but not prefix >= 3 char
			case 2: if (((goodWords.search(wordpart) == 0) || (goodWords.search(wordpart) == 1)) && (wordpart.length() >= 3))
					{
						goodWords.add(wordpart.toString());
						total++;
					}
					wordpart.deleteCharAt(wordpart.length() - 1);
					return goodWords;
			
			// add to dictionary and continue if it's word and prefix >= 3 char
			case 3: if (((goodWords.search(wordpart) == 0) || (goodWords.search(wordpart) == 1)) && (wordpart.length() >= 3))
					{
						goodWords.add(wordpart.toString());
						total++;
					}
					break;
		}
		
		// put an & in current position to mark as used
		board[nextI][nextJ] = '&';
		
		for (int x = -1; x < 2; x++)
		{
			for (int y = -1; y < 2; y++)
			{
				int nextNextI = nextI + x;
				int nextNextJ = nextJ + y;
				
				// don't go on if you're off the edge of the board, in the same spot, or somewhere already used
				if (nextNextI < 0) continue;
				if (nextNextJ < 0) continue;
				if (nextNextI > 3) continue;
				if (nextNextJ > 3) continue;
				if ((x == 0) && (y == 0)) continue;
				if (board[nextNextI][nextNextJ] == '&') continue;

				// recurse to the next spot
				goodWords =  wordSearch(board, wordpart, goodWords, allWords, nextNextI, nextNextJ);
			}
		}
		
		// remove the & when finished and revert wordpart
		board[nextI][nextJ] = boardOne[nextI][nextJ];
		wordpart.deleteCharAt(wordpart.length() - 1);
		
		return goodWords;
	}

	public static TrieDictionary wordSearchT(char[][] board, StringBuilder wordpart, TrieDictionary goodWordsT, TrieDictionary allWordsT, int nextI, int nextJ)
	{	
		// separately handle case where you have a wild card
		if (board[nextI][nextJ] == '*')
		{
			for (int i = 97; i < 123; i++) // for every ASCII lowercase letter
			{
				board[nextI][nextJ] = (char) i;
				boardOne[nextI][nextJ] = (char) i;
				goodWordsT =  wordSearchT(board, wordpart, goodWordsT, allWordsT, nextI, nextJ);
			}
			board[nextI][nextJ] = '*';
			boardOne[nextI][nextJ] = '*';
			return goodWordsT;
		}
		
		// add current character to wordpart
		wordpart.append(java.lang.Character.toLowerCase(board[nextI][nextJ]));
		
		switch (allWordsT.search(wordpart))
		{
			// stop if it's neither prefix nor word
			// THIS IS THE PRUNING!!!
			case 0: wordpart.deleteCharAt(wordpart.length() - 1);
					return goodWordsT;
			
			// keep going if it's prefix but not word
			case 1: break;
			
			// add to dictionary and stop it it's word but not prefix >= 3 char
			case 2: if (((goodWordsT.search(wordpart) == 0) || (goodWordsT.search(wordpart) == 1)) && (wordpart.length() >= 3))
					{
						goodWordsT.add(wordpart.toString());
						total++;
					}
					wordpart.deleteCharAt(wordpart.length() - 1);
					return goodWordsT;
			
			// add to dictionary and continue if it's word and prefix >= 3 char
			case 3: if (((goodWordsT.search(wordpart) == 0) || (goodWordsT.search(wordpart) == 1)) && (wordpart.length() >= 3))
					{
						goodWordsT.add(wordpart.toString());
						total++;
					}
					break;
		}
		
		// put an & in current position to mark as used
		board[nextI][nextJ] = '&';
		
		for (int x = -1; x < 2; x++)
		{
			for (int y = -1; y < 2; y++)
			{
				int nextNextI = nextI + x;
				int nextNextJ = nextJ + y;
				
				// don't go on if you're off the edge of the board, in the same spot, or somewhere already used
				if (nextNextI < 0) continue;
				if (nextNextJ < 0) continue;
				if (nextNextI > 3) continue;
				if (nextNextJ > 3) continue;
				if ((x == 0) && (y == 0)) continue;
				if (board[nextNextI][nextNextJ] == '&') continue;

				// recurse to the next spot
				goodWordsT =  wordSearchT(board, wordpart, goodWordsT, allWordsT, nextNextI, nextNextJ);
			}
		}
		
		// remove the & when finished and revert wordpart
		board[nextI][nextJ] = boardOne[nextI][nextJ];
		wordpart.deleteCharAt(wordpart.length() - 1);
		
		return goodWordsT;
	}
}