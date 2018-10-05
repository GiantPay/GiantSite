package network.giantpay.dto.trello;

import java.util.Collection;

public class List extends TrelloDto {

    private Collection<Card> cards;

    public Collection<Card> getCards() {
        return cards;
    }

    public void setCards(Collection<Card> cards) {
        this.cards = cards;
    }
}
