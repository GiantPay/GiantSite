package network.giantpay.dto.trello;

import java.util.Collection;

public class Board extends TrelloDto {

    private Collection<List> lists;

    public Collection<List> getLists() {
        return lists;
    }

    public void setLists(Collection<List> lists) {
        this.lists = lists;
    }
}
