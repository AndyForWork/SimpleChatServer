package org.example;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.EventListener;

public class TCPConnection {

    private final Socket socket;
    private final BufferedReader inBuffer;
    private final BufferedWriter outBuffer;
    private final  TCPConnectionListener listener;

    private final Thread rThread;

    public TCPConnection(TCPConnectionListener listener, String ipAddr, int port) throws IOException {
        this(listener,new Socket(ipAddr,port));
    }

    public TCPConnection(final TCPConnectionListener listener, final Socket socket) throws IOException {
        this.socket = socket;
        inBuffer = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
        outBuffer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),Charset.forName("UTF-8")));
        this.listener = listener;
        rThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    listener.onConnectionListener(TCPConnection.this);
                    while (!rThread.isInterrupted()) {
                        String msg = inBuffer.readLine();
                        listener.onReceiveListener(TCPConnection.this, msg);
                    }
                } catch (IOException e) {
                    System.out.println("read exception");
                    listener.onExceptionListener(TCPConnection.this,e);
                }
                finally {
                    listener.onDisconnectListener(TCPConnection.this);
                }
            }
        });
        rThread.start();
    }


    public synchronized void sendString(String val){
        try{
            outBuffer.write(val + "\r\n");
            outBuffer.flush();
        } catch (IOException e) {
            System.out.println("Send string exception");
            listener.onExceptionListener(TCPConnection.this,e);
            disconnect();
        }
    }

    public synchronized void disconnect(){
        rThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            listener.onExceptionListener(TCPConnection.this,e);
        }
    }

    @Override
    public String toString() {
        return "TCPConnection: " + socket.getInetAddress() + ": "+ socket.getPort();
    }
}
