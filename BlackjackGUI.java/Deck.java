import java.util.Random;

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

	// This method shuffles the deck (randomizes the array of cards).
	// Hint: loop over the cards and swap each one with another card in a random position.
	public void shuffle(){
        for (int i = 51; i > 0; i-- ) {
            int random = (int)(Math.random()*(i+1));
            Card temp = deck[i];
            deck[i] = deck[random];
            deck[random] = temp;
        }
        cardsUsed = 0;
	}
	
	// This method takes the top card off the deck and returns it.
	public Card drawCard(){
		if (cardsUsed == 52){
			shuffle();
			cardsUsed = 0;
			Player.clearCounter();
		}
		
		cardsUsed++;
		return deck[cardsUsed - 1];
	}
	
	// This method returns the number of cards left in the deck.
	public int getSize(){
		return 52 - cardsUsed;
	}
}

