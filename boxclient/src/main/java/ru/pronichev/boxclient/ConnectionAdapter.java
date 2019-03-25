package ru.pronichev.boxclient;
/* Работа с сетью
 *
 * @author Aleksei Pronichev
 * @version 23.02.2019
 */

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.io.IOException;
import java.net.Socket;

import packets.Packet;
import packets.PacketMessage;
import packets.PacketType;
import packets.files.FilePacket;
import packets.files.FilePacketType;
import packets.special.AuthorizationPacket;
import ru.pronichev.boxclient.API.Callback;
import service.FileService;

public class ConnectionAdapter {
    private String address;
    private int port;
    private Socket socket;
    private ObjectDecoderInputStream in;
    private ObjectEncoderOutputStream out;

    private boolean connected = false;

    private Callback callOnMsgReceived;
    private Callback callOnException;

    public ConnectionAdapter(String address, int port) {
        this.address = address;
        this.port = port;
    }

    private void resetConnection() {
        try {
            socket = new Socket(address, port);
            in = new ObjectDecoderInputStream(socket.getInputStream());
            out = new ObjectEncoderOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            connected = false;
        }
    }

    public void setCallOnMsgReceived(Callback callOnMsgReceived) {
        this.callOnMsgReceived = callOnMsgReceived;
    }

    public void setCallOnException(Callback callOnException) {
        this.callOnException = callOnException;
    }

    public void sendAuth(String login, String password) {
        if (!connected) connect();
        try {
            out.writeObject(new AuthorizationPacket(login, password));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        resetConnection();
        connected = true;
        Thread clientListenerThread = new Thread(() -> {
            try {
                while (connected) {
                    Packet packet = (Packet) in.readObject();
                    if (packet.getPacketType() == PacketType.CLOSE_CONNECTION) connected = false;
                    callOnMsgReceived.callback(packet);
                }
            } catch (IOException e) {
                callOnException.callback(new PacketMessage("Соединение с сервером разорвано"));
            } catch (ClassNotFoundException e) {
                callOnException.callback(new PacketMessage("Сервер послал некорректное сообщение, соединение разорвано"));
            } finally {
                closeConnection();
            }
        });
        clientListenerThread.setDaemon(true);
        clientListenerThread.start();
    }

    public boolean sendMsg(String msg) {
        try {
            out.writeObject(new PacketMessage(msg, PacketType.MESSAGE_BROADCAST));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void sendFile(String filename, FileService fileService) {
        try {
            out.writeObject(fileService.copy(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void copyServerFile(String filename) {
        try {
            out.writeObject(new FilePacket(filename, FilePacketType.FIlE_COPY));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteServerFile(String filename) {
        try {
            out.writeObject(new FilePacket(filename, FilePacketType.FIlE_DELETE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        connected = false;
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}