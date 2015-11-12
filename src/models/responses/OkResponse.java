package models.responses;

import java.util.Map;

/**
 * Created by akatchi on 8-8-15.
 */
public class OkResponse<K, V> extends Response
{
    public OkResponse(Map<K, V> payload)
    {
        super(Status.OK, payload);
    }
}
