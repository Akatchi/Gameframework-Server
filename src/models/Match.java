package models;

import models.responses.*;
import nl.hanze.t23i.gamemodule.extern.AbstractGameModule;
import nl.hanze.t23i.gamemodule.extern.IGameModule;
import server.Application;
import utils.ResponseMapGenerator;

/**
 * Created by akatchi on 9-8-15.
 */
public class Match
{
    private static int matchNumbercount = 1;

    private String gameType;
    private int matchNumber;
    private boolean finished;

    private AbstractGameModule gameModule;
    private Client playerOne;
    private Client playerTwo;
    private ResponseMapGenerator responseMapGenerator;

    public Match(Client playerOne, Client playerTwo, String gameType)
    {
        matchNumber = matchNumbercount++;

        responseMapGenerator = new ResponseMapGenerator();
        gameModule = Application.getInstance().getGameLoader().loadGameModule(gameType, playerOne.getPlayerName(), playerTwo.getPlayerName());

        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.gameType = gameType;

        playerOne.setActiveMatch(this);
        playerTwo.setActiveMatch(this);

        //Cancel player initiated matches
        if( playerOne.getChallenge() != null )
        {
            playerOne.getClientManager().cancelChallenge(playerOne.getChallenge(), false);
        }

        if( playerTwo.getChallenge() != null )
        {
            playerTwo.getClientManager().cancelChallenge(playerTwo.getChallenge(), false);
        }
    }

    public void start()
    {
        gameModule.start();

        playerOne.writeResponse(new MatchStartedResponse(responseMapGenerator.getMatchStartedMap(gameType, playerOne.getPlayerName(), playerTwo.getPlayerName())));
        playerTwo.writeResponse(new MatchStartedResponse(responseMapGenerator.getMatchStartedMap(gameType, playerOne.getPlayerName(), playerOne.getPlayerName())));

        if( !playerOne.isLoggedIn() )
        {
            removePlayer(playerOne);
        }
        else if( !playerTwo.isLoggedIn() )
        {
            removePlayer(playerTwo);
        }

        nextTurn();
    }

    public void nextTurn()
    {
        Client nextPlayer = getPlayerToMove();

        nextPlayer.writeResponse(new TurnResponse(responseMapGenerator.getTurnMessage(gameModule.getTurnMessage())));
    }

    public void doPlayerMove(Client client, String move) throws IllegalStateException
    {
        gameModule.doPlayerMove(client.getPlayerName(), move);

        Response moveResponse = new MoveResponse(responseMapGenerator.getMatchMoveMadeMap(client.getPlayerName(), move, gameModule.getMoveDetails()));

        playerOne.writeResponse(moveResponse);
        playerTwo.writeResponse(moveResponse);

        if( !isFinished() )
        {
            nextTurn();
        }
        else
        {
            finished();
        }
    }

    public void removePlayer(Client client)
    {
        finishedAbnormally(client, "Client disconnected");
    }

    private void finishedAbnormally(Client player, String reason)
    {
        int playerOneResult = AbstractGameModule.PLAYER_WIN;
        int playerTwoResult = AbstractGameModule.PLAYER_LOSS;

        // If player two disconnected then we dont have to chance the values because we set this
        // as default values for our variables
        if( player.equals(playerOne) )
        {
            playerOneResult = AbstractGameModule.PLAYER_LOSS;
            playerTwoResult = AbstractGameModule.PLAYER_WIN;
        }

        announceGameResult(playerOneResult, playerTwoResult, 0, 0, reason);

        Application.getInstance().getGameServer().getClientManager().matchFinished(this);
    }

    public void finished()
    {
        announceGameResult();

        Application.getInstance().getGameServer().getClientManager().matchFinished(this);
    }

    private void announceGameResult()
    {
        int playerOneResult = gameModule.getPlayerResult(playerOne.getPlayerName());
        int playerTwoResult = gameModule.getPlayerResult(playerTwo.getPlayerName());
        int playerOneScore = gameModule.getPlayerScore(playerOne.getPlayerName());
        int playerTwoScore = gameModule.getPlayerScore(playerTwo.getPlayerName());

        String matchComment = gameModule.getMatchResultComment();

        announceGameResult(playerOneResult, playerTwoResult, playerOneScore, playerTwoScore, matchComment);
    }

    private void announceGameResult(int playerOneResult, int playerTwoResult,
                                    int playerOneScore, int playerTwoScore, String matchComment)
    {
        playerOne.writeResponse(new GameOverResponse(responseMapGenerator.getGameResultsMap(getPlayerResultString(playerOneResult), matchComment, playerOneScore, playerTwoScore)));
        playerTwo.writeResponse(new GameOverResponse(responseMapGenerator.getGameResultsMap(getPlayerResultString(playerTwoResult), matchComment, playerOneScore, playerTwoScore)));
    }

    public Client getPlayerToMove()
    {
        if( gameModule.getPlayerToMove().equals(playerOne.getPlayerName()) )
        {
            return playerOne;
        }
        else
        {
            return playerTwo;
        }
    }

    public String getGameType()
    {
        return gameType;
    }

    public String getPlayerResultString(int playerResult)
    {
        switch( playerResult )
        {
            case AbstractGameModule.PLAYER_DRAW:
                return "DREW";
            case AbstractGameModule.PLAYER_LOSS:
                return "LOST";
            case AbstractGameModule.PLAYER_WIN:
                return "WON";
            default:
                return null;
        }
    }

    public int getMatchNumber()
    {
        return matchNumber;
    }

    public boolean isFinished()
    {
        return finished || gameModule.getMatchStatus() == AbstractGameModule.MATCH_FINISHED;
    }

    public IGameModule getGameModule()
    {
        return gameModule;
    }

    public Client getPlayerOne()
    {
        return playerOne;
    }

    public Client getPlayerTwo()
    {
        return playerTwo;
    }
}
