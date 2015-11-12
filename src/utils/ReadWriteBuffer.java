package utils;

import java.nio.ByteBuffer;

/**
 * Created by akatchi on 2-8-15.
 */
public class ReadWriteBuffer
{
    private ByteBuffer readBuffer;
    private ByteBuffer writeBuffer;

    public ReadWriteBuffer(ByteBuffer readBuffer, ByteBuffer writeBuffer)
    {
        this.readBuffer = readBuffer;
        this.writeBuffer = writeBuffer;
    }

    public ByteBuffer getReadBuffer()
    {
        return readBuffer;
    }

    public ByteBuffer getWriteBuffer()
    {
        return writeBuffer;
    }
}
