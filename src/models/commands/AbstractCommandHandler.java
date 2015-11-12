package models.commands;

import models.Client;
import models.responses.ClientCommand;

/**
 * Created by akatchi on 8-8-15.
 */
public abstract class AbstractCommandHandler implements ICommand
{
    private String[] supportedCommands;

    public AbstractCommandHandler(String... supportedCommands)
    {
        this.supportedCommands = supportedCommands;
    }

    @Override
    public boolean isSupported(Client client, ClientCommand command)
    {
        for( String supportedCommand : supportedCommands )
        {
            if( command.getAction().equalsIgnoreCase(supportedCommand) )
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public String getCommandName()
    {
        return supportedCommands[0];
    }
}
