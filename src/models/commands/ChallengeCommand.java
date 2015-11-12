package models.commands;

import models.Challenge;
import models.Client;
import models.responses.*;
import server.Application;
import utils.Log;
import utils.ResponseMapGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akatchi on 10-8-15.
 */
public class ChallengeCommand extends AbstractCommandHandler
{
    private ResponseMapGenerator responseMapGenerator;

    public ChallengeCommand()
    {
        super("challenge");
        responseMapGenerator = new ResponseMapGenerator();
    }

    @Override
    public void handleCommand(Client client, ClientCommand command)
    {
        if( !client.isLoggedIn() )
        {
            client.writeResponse(new ErrorResponse(responseMapGenerator.getNotLoggedInMap()));
        }

        try
        {
            if(command.getArgument().contains("accept"))
            {
                handleAccept(client, command);
            }
            else
            {
                handleCreate(client, command);
            }
        }
        catch( Exception e )
        {
            e.printStackTrace();
            client.writeResponse(new ErrorResponse(responseMapGenerator.getInvalidArgumentsMap()));
        }
    }

    private void handleAccept(Client client, ClientCommand command) throws Exception
    {
        int challengeNumber = Integer.parseInt(command.getArgument().split(" ")[1]);

        Challenge challenge = client.getChallengeWithId(challengeNumber);

        if( challenge == null )
        {
            client.writeResponse(new ErrorResponse(responseMapGenerator.getNoChallengeFoundWithIdMap(challengeNumber)));
            return;
        }

        if( client.getActiveMatch() != null )
        {
            client.writeResponse(new ErrorResponse(responseMapGenerator.getAlreadyInMatchMap()));
            return;
        }

        if( challenge.getChallenger().getActiveMatch() != null )
        {
            client.writeResponse(new ErrorResponse(responseMapGenerator.getChallengerAlreadyInMatchMap()));
            return;
        }

        client.getClientManager().acceptChallenge(challenge);

        client.writeResponse(new ChallengeAcceptedResponse(responseMapGenerator.getChallengeAcceptedMap()));
    }

    private void handleCreate(Client client, ClientCommand command) throws Exception
    {
        String[] splitArguments = command.getArgument().split(" ", 2);
        String opponentName = splitArguments[0];
        String gameType = splitArguments[1];

        if( opponentName.equals("") || gameType.equals("") )
        {
            client.writeResponse(new ErrorResponse(responseMapGenerator.getEmptyOpponentOrGameTypeMap()));
        }

        Client opponent = client.getClientManager().getPlayerByName(opponentName);

        if( opponent == null )
        {
            client.writeResponse(new ErrorResponse(responseMapGenerator.getNoOpponentWithNameFoundMap(opponentName)));
            return;
        }

        if( !Application.getInstance().getGameLoader().getGameTypeList().contains(gameType) )
        {
            client.writeResponse(new ErrorResponse(responseMapGenerator.getUnkownGametypeMap(gameType)));
            return;
        }

        if( client.getPlayerName().equals(opponentName) )
        {
            client.writeResponse(new ErrorResponse(responseMapGenerator.getCantChallengeSelfMap()));
            return;
        }

        client.getClientManager().createChallenge(client, opponent, gameType);
        client.writeResponse(new OkResponse(responseMapGenerator.getChallengeCreatedMap()));
    }

    @Override
    public String getDescription()
    {
        return "Command to handle challenges.";
    }

    @Override
    public List<String> getUsage()
    {
        ArrayList<String> usageList = new ArrayList<String>();

        usageList.add("usage: challenge [accept] [args]");
        usageList.add("");
        usageList.add("Valid options and arguments:");
        usageList.add("  \"<player>\" \"<game>\"          : Challenge player for game");
        usageList.add("  accept <challenge number>    : Accept challenge");

        return usageList;
    }
}
