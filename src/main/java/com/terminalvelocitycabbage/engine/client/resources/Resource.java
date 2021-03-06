package com.terminalvelocitycabbage.engine.client.resources;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Optional;

public interface Resource {

	Identifier getIdentifier();

	InputStream openStream() throws IOException;

	Optional<DataInputStream> asDataStream();

	Optional<ByteBuffer> asByteBuffer();

	Optional<String> asString();
}
