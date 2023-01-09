package com.terminalvelocitycabbage.engine.networking.packet;

import com.terminalvelocitycabbage.engine.debug.Log;
import com.terminalvelocitycabbage.engine.server.ServerBase;

import java.io.Serializable;
import java.util.HashMap;

public class PacketRegistry implements Serializable {

    private final HashMap<Class<? extends SerializablePacket>, Integer> packetTypes;

    public PacketRegistry() {
        this.packetTypes = new HashMap<>();
        registerPacket(SyncPacketRegistryPacket.class);
    }

    public int registerPacket(Class<? extends SerializablePacket> packet) {
        if (packetTypes.containsKey(packet)) {
            Log.warn("Tried to register packet of same type " + packet + " twice, the second addition has been ignored.");
            return -1;
        }
        var opcode = packetTypes.size();
        packetTypes.put(packet, opcode);
        return opcode;
    }

    public int getOpcodeForPacket(Class<? extends SerializablePacket> packetClass) {
        if (!packetTypes.containsKey(packetClass)) Log.crash("Could not get opcode for packet " + packetClass.getName(), new RuntimeException("No packet registered for class " + packetClass.getName()));
        return packetTypes.get(packetClass);
    }

    /**
     * Syncs this packet registry to the one provided, to be used when a client joins a server and needs its packet opcodes synced
     * @param packetRegistry the packetregistry received by the server to set this client's packet registry to.
     */
    public void sync(PacketRegistry packetRegistry) {
        if (ServerBase.getInstance() != null) Log.crash("Not allowed to call sync on server packet registry", new RuntimeException("Illegal call to PacketRegistry#sync() on Server packet registry"));
        if (packetTypes.size() > 1) Log.crash("Can't sync an already populated chat registry", new RuntimeException("Tried to call sync on packet registry with packets already synced"));
        packetRegistry.packetTypes.forEach((aClass, integer) -> {
            if (!aClass.equals(SyncPacketRegistryPacket.class)) registerPacket(aClass);
        });
    }
}
