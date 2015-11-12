package models.responses;

import java.util.Map;

/**
 * Created by akatchi on 8-8-15.
 */
public class TurnResponse<K, V> extends Response
{
    public TurnResponse(Map<K, V> payload)
    {
        super(Status.TURN, payload);
    }
}
