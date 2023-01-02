package com.terminalvelocitycabbage.engine.networking;

import com.terminalvelocitycabbage.engine.networking.packet.PacketRegistry;

public interface SidedEntrypoint {

    PacketRegistry packetRegistry = new PacketRegistry();

    default PacketRegistry getPacketRegistry() {
        return packetRegistry;
    }
}
