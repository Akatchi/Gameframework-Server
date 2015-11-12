package models.commands;

import models.Client;
import models.responses.ClientCommand;
import models.responses.OkResponse;
import models.responses.PlayerListReceivedCommand;
import server.Application;
import utils.ResponseMapGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akatchi on 8-8-15.
 */
public class GetCommand extends AbstractCommandHandler
{
    private ResponseMapGenerator responseMapGenerator;

    public GetCommand()
    {
        super("get");
        responseMapGenerator = new ResponseMapGenerator();
    }

    @Override
    public void handleCommand(Client client, ClientCommand command)
    {
        String argument = command.getArgument();

        if( argument.equalsIgnoreCase("players") )
        {
            client.writeResponse(new PlayerListReceivedCommand(responseMapGenerator.getPlayerListMap()));
        }
    }

    @Override
    public String getDescription()
    {
        return "Get server data";
    }

    @Override
    public List<String> getUsage()
    {
        ArrayList<String> usageList = new ArrayList<String>();

        usageList.add("usage: get <gamelist | playerlist>");
        usageList.add("");
        usageList.add("Valid options and arguments:");
        usageList.add("  <gamelist>      : Available games");
        usageList.add("  <players>    : Currently logged in players");

        return usageList;
    }
}
