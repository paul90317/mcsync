package com.paul90317.mcsync.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class NetStream {
    public final InputStream in;
    public final OutputStream out;
    public final Socket socket;
    public NetStream(Socket client)throws IOException{
        in = client.getInputStream();
        out = client.getOutputStream();
        socket = client;
    }
    public void writeSized(byte[] data)throws Exception{
        out.write(data.length>>24);
        out.write(data.length>>16);
        out.write(data.length>>8);
        out.write(data.length);
        out.write(data);
    }
    public byte[] readSized()throws Exception{
        byte[] data = in.readNBytes(4);
        int len = (int) data[0] << 24 | (int) data[1] << 16 | (int) data[2] << 8 | (int) data[3];
        return in.readNBytes(len);
    }
    public void close()throws Exception{
        in.close();
        out.close();
        socket.close();
    }
}
