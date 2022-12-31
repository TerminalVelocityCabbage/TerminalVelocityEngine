package com.terminalvelocitycabbage.engine.networking.packet;

import com.terminalvelocitycabbage.engine.debug.Log;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @param <T> A
 */
public class PacketRegistry {

    private final HashMap<Class<? extends Serializable>, Integer> packetTypes;

    public PacketRegistry() {
        this.packetTypes = new HashMap<>();
    }

    public int registerPacket(Class<? extends Serializable> packet) {
        if (packetTypes.containsKey(packet)) {
            Log.warn("Tried to register packet of same type " + packet + " twice, the second addition has been ignored.");
            return -1;
        }
        var opcode = packetTypes.size();
        packetTypes.put(packet, opcode);
        return opcode;
    }

    public int getOpcodeForPacket(Class<? extends Serializable> packetClass) {
        return packetTypes.get(packetClass);
    }
}
