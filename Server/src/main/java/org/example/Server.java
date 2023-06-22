package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server implements TCPConnectionListener{

    public static void main(String[] args){
        new Server();
    }

    private ArrayList<TCPConnection> connections = new ArrayList<TCPConnection>();

    @Override
    public void onExceptionListener(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPException: "+e);
    }

    public void onConnectionListener(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        sendAll(tcpConnection + " connected");
    }

    public void onDisconnectListener(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        sendAll(tcpConnection + " disconnected");
    }

    public void onReceiveListener(TCPConnection tcpConnection, String data) {
        sendAll(data);
    }

    public Server() {
        System.out.println("Server running....");
        try {
            ServerSocket serverSocket = new ServerSocket(54345);
            while (true){
                try{
                new TCPConnection(this, serverSocket.accept());
                }
                catch (IOException e){
                    System.out.println("TCPException: "+e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private  void sendAll(String data){
        for (TCPConnection connection: connections){
            if (data != null)
                connection.sendString(data);
        }
    }


}
