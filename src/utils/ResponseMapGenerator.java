package utils;

import models.Client;
import server.Application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by akatchi on 10-8-15.
 */
public class ResponseMapGenerator
{
    public ResponseMapGenerator(){}

    public Map<String, String> getClientConnectedMap()
    {
        return getMapWithMessage("Connected");
    }

    public Map<String, String> getNoLoginNameEnteredMap()
    {
        return getMapWithMessage("No login name entered");
    }

    public Map<String, String> getAlreadyLoggedInMap()
    {
        return getMapWithMessage("You are already logged in");
    }

    public Map<String, String> getLoginSuccessMap()
    {
        return getMapWithMessage("Successfully logged in");
    }

    public Map<String, String> getNameAlreadyInUseMap()
    {
        return getMapWithMessage("That name already in use");
    }

    public Map<String, String> getNoMoveEnteredMap()
    {
        return getMapWithMessage("No move entered!");
    }

    public Map<String, String> getNotInActiveMatchMap()
    {
        return getMapWithMessage("Not in an active match!");
    }

    public Map<String, String> getMoveMadeMap()
    {
        return getMapWithMessage("Move made");
    }

    public Map<String, String> getUnsupportedCommandMap(String command)
    {
        return getMapWithMessage(String.format("Command: %s not supported", command));
    }

    public Map<String, String> getNotLoggedInMap()
    {
        return getMapWithMessage("Not logged in");
    }

    public Map<String, String> getInvalidArgumentsMap()
    {
        return getMapWithMessage("Invalid arguments for the command");
    }

    public Map<String, String> getNoChallengeFoundWithIdMap(int id)
    {
        return getMapWithMessage(String.format("No challenge found with the id: %d", id));
    }

    public Map<String, String> getChallengeCancelledMap(int id)
    {
        return getMapWithMessage(String.format("Challenge %d has been cancelled", id));
    }

    public Map<String, String> getAlreadyInMatchMap()
    {
        return getMapWithMessage("You are already in a match");
    }

    public Map<String, String> getChallengerAlreadyInMatchMap()
    {
        return getMapWithMessage("The challenger is already in a match");
    }

    public Map<String, String> getChallengeAcceptedMap()
    {
        return getMapWithMessage("Challenge accepted");
    }

    public Map<String, String> getEmptyOpponentOrGameTypeMap()
    {
        return getMapWithMessage("No opponent name or gametype entered");
    }

    public Map<String, String> getNoOpponentWithNameFoundMap(String opponentName)
    {
        return getMapWithMessage(String.format("No opponent with the name: %s", opponentName));
    }

    public Map<String, String> getUnkownGametypeMap(String gameType)
    {
        return getMapWithMessage(String.format("No gametype found with the name: %s", gameType));
    }

    public Map<String, String> getCantChallengeSelfMap()
    {
        return getMapWithMessage("You can´t challenge yourself");
    }

    public Map<String, String> getChallengeCreatedMap()
    {
        return getMapWithMessage("Challenge created");
    }

    public Map<String, String> getErrorMap(Exception e)
    {
        return getMapWithMessage(e.toString());
    }

    public Map<String, List<String>> getPlayerListMap()
    {
        Map<String, List<String>> map = new HashMap<String, List<String>>();

        List<String> playerNames = new ArrayList<String>();

        for( Client player : Application.getInstance().getGameServer().getClientManager().getPlayerList() )
        {
            playerNames.add(player.getPlayerName());
        }

        map.put("PLAYERS", playerNames);

        return map;
    }

    public Map<String, String> getMatchStartedMap(String gameType, String playerToMove, String opponent)
    {
        Map<String, String> map = new HashMap<String, String>();

        map.put("GAMETYPE", gameType);
        map.put("PLAYERTOMOVE", playerToMove);
        map.put("OPPONENT", opponent);

        return map;
    }

    public Map<String, String> getTurnMessage(String turnMessage)
    {
        Map<String, String> map = new HashMap<String, String>();

        map.put("YOURTURN", "true");
        map.put("TURNMESSAGE", turnMessage);

        return map;
    }

    public Map<String, String> getMatchMoveMadeMap(String playerName, String madeMove, String moveDetails)
    {
        Map<String, String> map = new HashMap<String, String>();

        map.put("PLAYER", playerName);
        map.put("MOVE", madeMove);
        map.put("MOVEDETAILS", moveDetails);

        return map;
    }

    public Map<String, String> getGameResultsMap(String gameState, String matchComment, int playerOneScore, int playerTwoScore)
    {
        Map<String, String> map = new HashMap<String, String>();

        map.put("GAMESTATE", gameState);
        map.put("MATCHCOMMENT", matchComment);
        map.put("PLAYERONESCORE", String.valueOf(playerOneScore));
        map.put("PLAYERTWOSCORE", String.valueOf(playerTwoScore));

        return map;
    }

    public Map<String, String> getChallengeCreatedMap(int challengeNumber, String challenger, String opponent, String gameType)
    {
        Map<String, String> map = new HashMap<String, String>();

        map.put("CHALLENGENUMBER", String.valueOf(challengeNumber));
        map.put("CHALLENGER", challenger);
        map.put("OPPONENT", opponent);
        map.put("GAMETYPE", gameType);

        return map;
    }

    private Map<String, String> getMapWithMessage(String message)
    {
        Map<String, String> map = new HashMap<String, String>();

        map.put("MESSAGE", message);

        return map;
    }
}
