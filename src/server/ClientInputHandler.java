package server;

import models.Client;
import models.commands.CommandHandler;
import models.commands.ICommand;
import models.responses.ClientCommand;
import server.Application;
import utils.ByteUtils;
import utils.KeyValuePair;
import utils.Log;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by akatchi on 8-8-15.
 */
public class ClientInputHandler implements Runnable
{
    private boolean running = false;

    private BlockingQueue<KeyValuePair<Client, byte[]>> commandQueue;
    private Map<Client, ByteBuffer> clientInputBufferMap;

    private CommandHandler commandHandler;
    private Thread thread;

    public ClientInputHandler(CommandHandler commandHandler)
    {
        this.commandHandler = commandHandler;

        commandQueue = new LinkedBlockingQueue<KeyValuePair<Client, byte[]>>();
        clientInputBufferMap = new HashMap<Client, ByteBuffer>();

        thread = new Thread(this, "ClientInputHandler-Thread");
        running = true;
        thread.start();
    }

    @Override
    public void run()
    {
        while( running )
        {
            try
            {
                //Wait for client input
                KeyValuePair<Client, byte[]> entry = commandQueue.take();

                Client client = entry.getKey();
                byte[] data = entry.getValue();

                if( data == null )
                {
                    Log.DEBUG("Client disconnected!");
                    Application.getInstance().getGameServer().getClientManager().removeClient(client);
                    continue;
                }

                ByteBuffer inputBuffer = getInputBuffer(client);
                inputBuffer.put(data);
                inputBuffer.flip();

                List<ClientCommand> clientCommands = readCommands(inputBuffer);
                inputBuffer.compact();

                for( ClientCommand clientCommand : clientCommands )
                {
                    ICommand command = commandHandler.getCommand(client, clientCommand);

                    Log.DEBUG("Handling command: " + command.getDescription());

                    command.handleCommand(client, clientCommand);
                }

            }
            catch( InterruptedException e )
            {
                e.printStackTrace();
            }
        }
    }

    public void exit()
    {
        running = false;
        thread.interrupt();
    }

    public void addData(Client client, byte[] data)
    {
        if( commandQueue != null )
        {
            commandQueue.add(new KeyValuePair<Client, byte[]>(client, data));
        }
    }

    private ByteBuffer getInputBuffer(Client client)
    {
        ByteBuffer inputBuffer = clientInputBufferMap.get(client);

        if(inputBuffer == null) {
            inputBuffer = ByteBuffer.allocate(16384);
            clientInputBufferMap.put(client, inputBuffer);
        }

        return inputBuffer;
    }


    private List<ClientCommand> readCommands(ByteBuffer buffer)
    {
        List<ClientCommand> commandList = new ArrayList<ClientCommand>();

        String line;

        while( (line = ByteUtils.readLine(buffer)) != null )
        {
            commandList.add(new ClientCommand(line));
        }

        return commandList;
    }


}
