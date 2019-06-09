package ru.pronichev.boxclient;

/* Тестирование отправки сообщений на сервер
 *
 * @author Aleksei Pronichev
 * @version 18.02.2019
 */

import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import packets.Packet;
import packets.PacketType;
import packets.special.AuthorizationPacket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MyClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8189);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String s = input.readLine();
                            if (s == null) return;
                            System.out.println("Server: " + s);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.setDaemon(true);
            t.start();

            //Ниже тестируем отправку пакетов

            //обычный текс с консоли
//            while (t.isAlive()) out.write(console.readLine().getBytes());

            //Пакет
            Packet p;
            p = new AuthorizationPacket("Login", "Password");
        p.setPacketType(PacketType.MESSAGE);
            ObjectEncoderOutputStream outO = new ObjectEncoderOutputStream(socket.getOutputStream());
            outO.writeObject(p);
            outO.flush();
            p = new Packet();
            p.setPacketType(PacketType.NONE);
            outO.writeObject(p);
            outO.flush();
            outO.writeObject(p);
            outO.flush();
            outO.writeObject(p);
            outO.flush();
            Thread.sleep(50000);

            System.out.print("Соединение потеряно");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
