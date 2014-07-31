package edu.illinois.seclab.extDroid.bm;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * 
 * @author http://stackoverflow.com/questions/8028094/java-datainputstream-replacement-for-endianness
 *
 */
public class Utils {
	public static byte[] byteBuffer = new byte[8];
	

	/**
     * Reads short type stored in little endian (see readShort() for big endian)
     * @return short value translated from little endian
     * @throws IOException if an IO error occurs
     */    
    public static short readLittleShort(DataInputStream dataInStream) throws IOException {
    dataInStream.readFully(byteBuffer, 0, 2);
    return (short)((byteBuffer[1] & 0xff) << 8 | (byteBuffer[0] & 0xff));
    }
    
    /**
     * Reads unsigned short type stored in little endian (see readUnsignedShort() for big endian)
     * @return integer value representing unsigned short value translated from little endian
     * @throws IOException if an IO error occurs
     */        
    public static int readLittleUnsignedShort(DataInputStream dataInStream) throws IOException {
        dataInStream.readFully(byteBuffer, 0, 2);
        return ((byteBuffer[1] & 0xff) << 8 | (byteBuffer[0] & 0xff));
    }

    /**
     * Reads char (16-bits) type stored in little endian (see readChar() for big endian)
     * @return char value translated from little endian
     * @throws IOException if an IO error occurs
     */    
    public static char readLittleChar(DataInputStream dataInStream) throws IOException {
        dataInStream.readFully(byteBuffer, 0, 2);
        return (char)((byteBuffer[1] & 0xff) << 8 | (byteBuffer[0] & 0xff));
    }    

    /**
     * Reads integer type stored in little endian (see readInt() for big endian)
     * @return integer value translated from little endian
     * @throws IOException if an IO error occurs
     */        
    public static int readLittleInt(DataInputStream dataInStream) throws IOException {
        dataInStream.readFully(byteBuffer, 0, 4);
        return (byteBuffer[3]) << 24 | (byteBuffer[2] & 0xff) << 16 |
            (byteBuffer[1] & 0xff) << 8 | (byteBuffer[0] & 0xff);
    }

    /**
     * Reads long type stored in little endian (see readLong() for big endian)
     * @return long value translated from little endian
     * @throws IOException if an IO error occurs
     */        
    public static long readLittleLong(DataInputStream dataInStream) throws IOException {
        dataInStream.readFully(byteBuffer, 0, 8);
        return (long)(byteBuffer[7]) << 56 | (long)(byteBuffer[6]&0xff) << 48 |
            (long)(byteBuffer[5] & 0xff) << 40 | (long)(byteBuffer[4] & 0xff) << 32 |
            (long)(byteBuffer[3] & 0xff) << 24 | (long)(byteBuffer[2] & 0xff) << 16 |
            (long)(byteBuffer[1] & 0xff) <<  8 | (long)(byteBuffer[0] & 0xff);
    }
}

