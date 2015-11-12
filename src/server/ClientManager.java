package server;

import com.sun.istack.internal.NotNull;
import models.Challenge;
import models.Client;
import models.Match;
import models.responses.ChallengeInvitedResponse;
import models.responses.OkResponse;
import models.responses.Response;
import utils.Log;
import utils.ResponseMapGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Vasco on 1-8-2015.
 */
public class ClientManager
{
    private List<Client> clientList;
    private ResponseMapGenerator responseMapGenerator;

    public ClientManager()
    {
        clientList = new ArrayList<Client>();
        responseMapGenerator = new ResponseMapGenerator();
    }

    public boolean login(Client client, String playerName)
    {
        synchronized(clientList)
        {
            for( Client c : clientList )
            {
                if( c.getPlayerName() != null && c.getPlayerName().equals(playerName) )
                {
                    return false;
                }
            }
        }

        //Remove forbidden characters from the name
        playerName = playerName.replace(";", "");

        client.setPlayerName(playerName);
        client.setLoggedIn(true);

        Log.DEBUG("Client logged in with playername: " + playerName);

        return true;
    }

    public void addClient(Client client)
    {
        clientList.add(client);
    }

    public void removeClient(Client client)
    {
        client.setLoggedIn(false);

        if( client.getChallenge() != null )
        {
            cancelChallenge(client.getChallenge(), true);
        }

        Match match = client.getActiveMatch();

        if( match != null && !match.isFinished() )
        {
            match.removePlayer(client);
        }

        synchronized(clientList)
        {
            clientList.remove(client);
        }
    }

    public void createChallenge(Client challenger, Client opponent, String gameType)
    {
        //Cancel previous active challenges since you can only have 1 challenge active.
        if( challenger.getChallenge() != null )
        {
            cancelChallenge(challenger.getChallenge(), true);
        }

        Challenge challenge = new Challenge(challenger, opponent, gameType);
        challenger.setChallenge(challenge);
        opponent.addChallenge(challenge);

        Response response = new ChallengeInvitedResponse(responseMapGenerator.getChallengeCreatedMap(challenge.getChallengeNumber(), challenger.getPlayerName(), opponent.getPlayerName(), gameType));
        challenger.writeResponse(response);
        opponent.writeResponse(response);
    }

    public void acceptChallenge(Challenge challenge)
    {
        cancelChallenge(challenge, false);

        List<Client> players = new ArrayList<Client>();
        players.add(challenge.getChallenger());
        players.add(challenge.getOpponent());
        Collections.shuffle(players);

        Match match = new Match(players.get(0), players.get(1), challenge.getGameType());
        match.start();
    }

    public void cancelChallenge(Challenge challenge, boolean notify)
    {
        challenge.getChallenger().setChallenge(null);
        challenge.getOpponent().removeChallenge(challenge);

        if( notify )
        {
            // TODO change this to challenge canccleed response
            Response response = new OkResponse(responseMapGenerator.getChallengeCancelledMap(challenge.getChallengeNumber()));

            challenge.getChallenger().writeResponse(response);
            challenge.getOpponent().writeResponse(response);
        }
    }

    public void matchFinished(Match match)
    {
        Client playerOne = match.getPlayerOne();
        Client playerTwo = match.getPlayerTwo();

        playerOne.setActiveMatch(null);
        playerTwo.setActiveMatch(null);
    }

    public List<Client> getClientList()
    {
        return clientList;
    }

    public List<Client> getPlayerList()
    {
        List<Client> playerList = new ArrayList<Client>(clientList.size());

        for( Client c : clientList )
        {
            if( c.isLoggedIn() )
            {
                playerList.add(c);
            }
        }

        return playerList;
    }

    public Client getPlayerByName(String playerName)
    {
        for( Client player : getPlayerList() )
        {
            if( player.getPlayerName().equalsIgnoreCase(playerName) )
            {
                return player;
            }
        }

        return null;
    }
}
