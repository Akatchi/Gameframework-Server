package models.responses;

import java.util.Map;

/**
 * Created by akatchi on 10-8-15.
 */
public class MoveResponse<K, V> extends Response
{
    public MoveResponse(Map<K, V> payload)
    {
        super(Status.MOVE, payload);
    }
}
