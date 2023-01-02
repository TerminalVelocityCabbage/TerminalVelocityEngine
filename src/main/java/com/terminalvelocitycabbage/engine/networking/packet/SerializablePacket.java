package com.terminalvelocitycabbage.engine.networking.packet;

import com.github.simplenet.Client;
import com.github.simplenet.Server;
import com.github.simplenet.packet.Packet;
import com.terminalvelocitycabbage.engine.networking.SidedEntrypoint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class SerializablePacket implements Serializable {

    public abstract void interpretC2S(Client client);

    public abstract void interpretS2C(Server server, Client clientSender);

    public Packet pack(SidedEntrypoint entrypointInstance) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(this);
            return Packet.builder().putInt(entrypointInstance.getPacketRegistry().getOpcodeForPacket(this.getClass())); //TODO verify that this.getClass works
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
