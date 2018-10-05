package network.giantpay.api.trello;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import network.giantpay.dto.trello.Board;
import network.giantpay.dto.trello.Card;
import network.giantpay.dto.trello.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;

@Component
public class TrelloApi {

    private final static Logger logger = LoggerFactory.getLogger(TrelloApi.class);
    private final static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private Environment env;
    @Value("${trello.api.key}")
    private String key;
    @Value("${trello.api.token}")
    private String token;
    @Value("${trello.board_id}")
    private String boardId;

    public Board getBoard() {
        try {
            Board board = new Board();
            JsonNode boardResponse = mapper.readTree(getBoardUrl());
            if (boardResponse.has("id")) {
                board.setId(boardResponse.get("id").asText());
                board.setName(boardResponse.get("name").asText());
                board.setUrl(boardResponse.get("url").asText());
                board.setLists(Sets.newHashSet());

                JsonNode listsResponse = mapper.readTree(getListsUrl());
                if (listsResponse.isArray()) {
                    listsResponse.forEach(listJson -> {
                        try {
                            List list = new List();
                            list.setId(listJson.get("id").asText());
                            list.setName(listJson.get("name").asText());
                            list.setCards(Sets.newHashSet());
                            board.getLists().add(list);

                            JsonNode cardsResponse = mapper.readTree(getCardsUrl(list.getId()));
                            cardsResponse.forEach(cardJson -> {
                                Card card = new Card();
                                card.setId(cardJson.get("id").asText());
                                card.setName(cardJson.get("name").asText());
                                card.setUrl(cardJson.get("url").asText());
                                list.getCards().add(card);
                            });

                            Thread.sleep(500);
                        } catch (Exception e) {
                            if (logger.isErrorEnabled()) {
                                logger.error(e.getMessage(), e);
                            }
                        }
                    });
                }
            }
            return board;
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
            return null;
        }
    }

    private URL getCardsUrl(String listId) throws MalformedURLException {
        return new URL("https://api.trello.com/1/lists/" + listId + "/cards?fields=id,name,url&key=" + key + "&token=" + token);
    }

    private URL getListsUrl() throws MalformedURLException {
        return new URL("https://api.trello.com/1/boards/" + boardId + "/lists?fields=id,name&key=" + key + "&token=" + token);
    }

    private URL getBoardUrl() throws MalformedURLException {
        return new URL("https://api.trello.com/1/boards/" + boardId + "?fields=id,name,url&key=" + key + "&token=" + token);
    }
}
