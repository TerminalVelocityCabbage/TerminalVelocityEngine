package com.terminalvelocitycabbage.engine.client.resources;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public interface Resource {

	Identifier getIdentifier();

	InputStream openStream() throws IOException;

	DataInputStream asDataStream();

	ByteBuffer asByteBuffer();

	String asString();
}
