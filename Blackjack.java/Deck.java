// This class represents the deck of cards from which cards are dealt to players.
public class Deck {
	// define fields here
	private Card[] deck; // defines array of 52 cards
	private static int cardsUsed; // amount of cards dealt so far
	
	// This constructor builds a deck of 52 cards.
	public Deck(){
		deck = new Card[52];
		int cardCount = 0;
		for (int suit = 0; suit <= 3; suit++) {
			for (int value = 1; value <= 13; value++) {
				deck[cardCount] = new Card (suit, value);
				cardCount++;
			}
		}
		cardsUsed = 0;
		shuffle();
	}

	
	// This method takes the top card off the deck and returns it.
	public Card deal(){
		if (cardsUsed == 52){
			shuffle();
			cardsUsed = 0;
			Player.clearCounter();
		}
		
		cardsUsed++;
		return deck[cardsUsed - 1];
	}
	
	
	// this method returns true if there are no more cards to deal, false otherwise
	public boolean isEmpty(){
		if (cardsUsed == 52)
			return true;
		else
			return false;
	}
	
	//this method puts the deck int some random order
	public void shuffle(){
        for (int i = 51; i > 0; i-- ) {
            int random = (int)(Math.random()*(i+1));
            Card temp = deck[i];
            deck[i] = deck[random];
            deck[random] = temp;
        }
        cardsUsed = 0;
	}
	
	public Card[] getDeck() {
		return deck;
	}
}

