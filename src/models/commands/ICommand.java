package models.commands;

import models.Client;
import models.responses.ClientCommand;

import java.util.List;

/**
 * Created by akatchi on 8-8-15.
 */
public interface ICommand
{
    public void handleCommand(Client client, ClientCommand command);

    public boolean isSupported(Client client, ClientCommand command);

    public String getCommandName();

    public String getDescription();

    public List<String> getUsage();
}
