package models.responses;

import java.util.Map;

/**
 * Created by akatchi on 8-8-15.
 */
public class PlayerListReceivedCommand<K, V> extends Response
{
    public PlayerListReceivedCommand(Map<K, V> payload)
    {
        super(Status.PLAYERLIST_RECEIVED, payload);
    }
}
