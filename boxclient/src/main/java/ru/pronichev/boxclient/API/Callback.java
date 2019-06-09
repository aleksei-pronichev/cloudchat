package ru.pronichev.boxclient.API;

import packets.Packet;

public interface Callback {
    void callback(Packet packet);
}
