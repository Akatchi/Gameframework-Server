package models.responses;

import java.util.Map;

/**
 * Created by akatchi on 8-8-15.
 */
public class ErrorResponse<K, V> extends Response
{
    public ErrorResponse(Map<K, V> payload)
    {
        super(Status.ERROR, payload);
    }
}
