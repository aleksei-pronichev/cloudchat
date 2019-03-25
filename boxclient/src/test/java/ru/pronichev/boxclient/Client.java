package ru.pronichev.boxclient;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import packets.Packet;
import packets.PacketMessage;
import packets.PacketResult;
import packets.PacketType;
import packets.special.AuthorizationPacket;
import packets.special.NoValidPacket;

import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        ObjectEncoderOutputStream oeos = null;
        ObjectDecoderInputStream odis = null;
        try (Socket socket = new Socket("localhost", 8189)) {
            oeos = new ObjectEncoderOutputStream(socket.getOutputStream());
            Packet textMessage1 = new Packet();
            Packet textMessage2 = new AuthorizationPacket("admin", "adm");
            Packet textMessage3 = new PacketMessage("Я админ");
            oeos.writeObject(textMessage1);
            oeos.flush();
            odis = new ObjectDecoderInputStream(socket.getInputStream());
            PacketResult msgFromServer = (PacketResult)odis.readObject();
//            oeos.writeObject(textMessage2);
//            oeos.flush();
//            oeos.writeObject(textMessage3);
//            oeos.flush();
//            oeos.writeObject(textMessage3);
//            oeos.flush();
//            oeos.writeObject(textMessage3);
//            oeos.flush();
            System.out.println("Answer from server: " + msgFromServer.getMessage());
            msgFromServer = (PacketResult)odis.readObject();
            System.out.println("Answer from server: " + msgFromServer.getMessage());
            msgFromServer = (PacketResult)odis.readObject();
            System.out.println("Answer from server: " + msgFromServer.getMessage());
            msgFromServer = (PacketResult)odis.readObject();
            System.out.println("Answer from server: " + msgFromServer.getMessage());
            msgFromServer = (PacketResult)odis.readObject();
            System.out.println("Answer from server: " + msgFromServer.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                oeos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                odis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
