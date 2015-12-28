// This class represents one playing card.
public class Card
{
	//Declaring card suits
	public static final int spades   = 0,
							hearts   = 1,
							clubs    = 2,
							diamonds = 3;

	// Declaring cards that contain a face. The other numbers can be generated.
	public static final int ace      = 1,
							two		 = 2,
							three	 = 3,
							four	 = 4,
							five 	 = 5,
							six 	 = 6,
							seven	 = 7,
							eight	 = 8,
							nine 	 = 9,
							ten		 = 10,
							jack     = 11,
							queen    = 12,
							king     = 13;


	// define fields here
	private int suit;
	private int face;
	private boolean show;
	
	
	// This constructor builds a card with the given suit and face, turned face down.
	public Card(int cardSuit, int cardFace){
		suit = cardSuit;
		face = cardFace;
		this.show = false;
	}

	// This method retrieves the suit (spades, hearts, etc.) of this card.
	public int getSuit(){
		return suit;
	}
	
	// This method retrieves the face (ace through king) of this card.
	public int getFace(){
		return face;
	}
	
	// This method retrieves the numerical value of this card
	// (usually same as card face, except 1 for ace and 10 for jack/queen/king)
	public int getValue(){
		if (face >= 10)
			return 10;
		else
			return face;
	}
	
	//This method flips up the card.
	public void flipUp(){
		this.show = true;
	}
	
	//This method flips the card down.
	public void flipDown(){
		this.show = false;
	}
	
	public String toString(){
        String strFace = null;
        String strSuit = null;
        String sentence;
		
        if (face == 1)
        	strFace = "Ace";
        if (face == 2)
        	strFace = "2";
        if (face == 3)
        	strFace = "3";
        if (face == 4)
        	strFace = "4";
        if (face == 5)
        	strFace = "5";
        if (face == 6)
        	strFace = "6";
        if (face == 7)
        	strFace = "7";
        if (face == 8)
        	strFace = "8";
        if (face == 9)
        	strFace = "9";
        if (face == 10)
        	strFace = "10";
        if (face == 11)
        	strFace = "Jack";
        if (face == 12)
        	strFace = "Queen";
        if (face == 13)
        	strFace = "King";
        
        if (suit == 0)
        	strSuit = "Spades";
        if (suit == 1)
        	strSuit = "Hearts";
        if (suit == 2)
        	strSuit = "Clubs";
        if (suit == 3)
        	strSuit = "Diamonds";
        
        sentence = strFace + " of " + strSuit;
        
        return sentence;
	}
	
}