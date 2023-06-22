package org.example;

public interface TCPConnectionListener {

    void onExceptionListener(TCPConnection tcpConnection, Exception e);
    void onConnectionListener(TCPConnection tcpConnection);
    void onDisconnectListener(TCPConnection tcpConnection);
    void onReceiveListener(TCPConnection tcpConnection, String data);
}
