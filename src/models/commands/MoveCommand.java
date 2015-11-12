package models.commands;

import models.Client;
import models.Match;
import models.responses.ClientCommand;
import models.responses.ErrorResponse;
import models.responses.OkResponse;
import utils.ResponseMapGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akatchi on 10-8-15.
 */
public class MoveCommand extends AbstractCommandHandler
{
    private ResponseMapGenerator responseMapGenerator;

    public MoveCommand()
    {
        super("move");
        responseMapGenerator = new ResponseMapGenerator();
    }

    @Override
    public void handleCommand(Client client, ClientCommand command)
    {
        if( !client.isLoggedIn() )
        {
            client.writeResponse(new ErrorResponse(responseMapGenerator.getNotLoggedInMap()));
            return;
        }

        if( command.getArgument().equals("") )
        {
            client.writeResponse(new ErrorResponse(responseMapGenerator.getNoMoveEnteredMap()));
            return;
        }

        Match match = client.getActiveMatch();

        if( match == null || match.isFinished() )
        {
            client.writeResponse(new ErrorResponse(responseMapGenerator.getNotInActiveMatchMap()));
            return;
        }

        try
        {
            match.doPlayerMove(client, command.getArgument());
            client.writeResponse(new OkResponse(responseMapGenerator.getMoveMadeMap()));
        }
        catch( IllegalStateException e )
        {
            e.printStackTrace();
            client.writeResponse(new ErrorResponse(responseMapGenerator.getErrorMap(e)));
        }
    }

    @Override
    public String getDescription()
    {
        return "Make a game move in the current match";
    }

    @Override
    public List<String> getUsage()
    {
        ArrayList<String> usageList = new ArrayList<String>();

        usageList.add("usage: move <game move>");
        usageList.add("");
        usageList.add("Valid options and arguments:");
        usageList.add("  <game move>    : Game-specific move to make");

        return usageList;
    }
}
