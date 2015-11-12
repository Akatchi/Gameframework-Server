package server;

import models.Client;
import models.commands.*;
import models.responses.OkResponse;
import utils.ConfigFile;
import utils.Log;
import utils.ReadWriteBuffer;
import utils.ResponseMapGenerator;

import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.util.Set;
import java.util.Iterator;
import java.net.InetSocketAddress;

/**
 * Created by Vasco on 30-7-2015.
 */
public class GameServer implements Runnable
{
    private boolean running = false;

    private ConfigFile configFile = new ConfigFile();

    private Selector selector;
    private ServerSocketChannel serverSocket;
    private SelectionKey selectionKey;

    private ClientManager clientManager;
    private CommandHandler commandHandler;
    private ClientInputHandler clientInputHandler;
    private ResponseMapGenerator responseMapGenerator;

    public GameServer() throws IOException
    {
        clientManager = new ClientManager();

        responseMapGenerator = new ResponseMapGenerator();

        commandHandler = new CommandHandler();
        commandHandler.addCommand(new LoginCommand());
        commandHandler.addCommand(new ChallengeCommand());
        commandHandler.addCommand(new GetCommand());
        commandHandler.addCommand(new MoveCommand());

        clientInputHandler = new ClientInputHandler(commandHandler);

        serverSocket = ServerSocketChannel.open();
        serverSocket.configureBlocking(false);
        serverSocket.socket().bind(new InetSocketAddress(Integer.valueOf(configFile.getValue(ConfigFile.HOST_PORT))));

        selector = Selector.open();

        serverSocket.register(selector, SelectionKey.OP_ACCEPT);

        Log.DEBUG("Listening on port: " + configFile.getValue(ConfigFile.HOST_PORT));

        Thread thread = new Thread(this, "Gameserver-Thread");
        running = true;
        thread.start();
    }

    @Override
    public void run()
    {
        Log.DEBUG("Running!");
        while( running )
        {
            try
            {
                int selectedChannels = selector.select();

                if( selectedChannels == 0 )
                {
                    continue;
                }

                Set selectionKeys = selector.selectedKeys();
                Iterator it = selectionKeys.iterator();

                while( it.hasNext() )
                {
                    SelectionKey key = (SelectionKey) it.next();
                    it.remove();

                    if( key.isAcceptable() )
                    {
                        handleAccept(key);
                    }
                    else if( key.isWritable() )
                    {
                        handleWrite(key);
                    }
                    else if( key.isReadable() )
                    {
                        handleRead(key);
                    }
                }
            }
            catch( Exception e )
            {
                e.printStackTrace();
            }
        }
    }

    private void handleAccept(SelectionKey key) throws IOException
    {
        SocketChannel socket = serverSocket.accept();

        socket.configureBlocking(false);

        ReadWriteBuffer rwBuffer = new ReadWriteBuffer(ByteBuffer.allocateDirect(4096), ByteBuffer.allocateDirect(4096));
        Client client = new Client(socket, rwBuffer, clientManager);
        socket.register(selector, 1, client);

        client.writeResponse(new OkResponse(responseMapGenerator.getClientConnectedMap()));
    }

    private void handleWrite(SelectionKey key) throws IOException
    {
        key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);

        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer writeBuffer = ((Client) key.attachment()).getWriteBuffer();

        synchronized( writeBuffer )
        {
            writeBuffer.flip();
            client.write(writeBuffer);

            if( writeBuffer.hasRemaining() )
            {
                key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
            }

            writeBuffer.compact();
        }
    }

    private void handleRead(SelectionKey key) throws IOException
    {
        SocketChannel channel = (SocketChannel) key.channel();
        Client client = (Client) key.attachment();
        ByteBuffer readBuffer = client.getReadBuffer();

        int readSize;
        readSize = channel.read(readBuffer);

        if( readSize < 0 )
        {
            Log.DEBUG("Read -1 bytes, disconnecting client");
            disconnect(key);
            return;
        }

        readBuffer.flip();
        byte[] data = new byte[readBuffer.remaining()];
        readBuffer.get(data);
        readBuffer.clear();

        clientInputHandler.addData(client, data);
    }

    public void setWritable(SocketChannel client)
    {
        SelectionKey key = client.keyFor(selector);

        if( key != null )
        {
            selector.wakeup();
            key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
        }
    }

    public void disconnect(SelectionKey key) throws IOException
    {
        Log.DEBUG("Disconnecting client");

        SocketChannel client = (SocketChannel) key.channel();

        if(client != null)
        {
            //Remove the client from the gameserver since itś discconected
            clientInputHandler.addData((Client) key.attachment(), null);

            Log.DEBUG("Closing client socket connection");
            client.close();
        }

        key.cancel();
        key.attach(null);
    }

    public void disconnect(SocketChannel client) throws IOException
    {
        SelectionKey key = client.keyFor(selector);

        disconnect(key);
    }

    public ClientManager getClientManager()
    {
        return clientManager;
    }
}
