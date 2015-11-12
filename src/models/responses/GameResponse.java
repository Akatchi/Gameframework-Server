package models.responses;

import java.util.Map;

/**
 * Created by akatchi on 10-8-15.
 */
public class GameResponse<K, V> extends Response
{
    public GameResponse(Map<K, V> payload)
    {
        super(Status.GAME, payload);
    };
}
