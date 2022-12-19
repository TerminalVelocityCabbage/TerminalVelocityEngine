package com.terminalvelocitycabbage.engine.client.resources;

import com.terminalvelocitycabbage.engine.debug.Log;
import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UrlResource implements Resource {

	private final Identifier identifier;
	private final URL url;

	protected UrlResource(Identifier identifier, URL url) {
		this.identifier = identifier;
		this.url = url;
	}

	@Override
	public Identifier getIdentifier() {
		return identifier;
	}

	@Override
	public InputStream openStream() {
		try {
			return url.openStream();
		} catch (IOException e) {
			Log.crash("Resource Loading Error could not open Stream", new RuntimeException(e));
			return null;
		}
	}

	@Override
	public DataInputStream asDataStream() {
		try {
			return new DataInputStream(url.openStream());
		} catch (IOException e) {
			Log.crash("Resource Loading Error, could not get DataStream", new RuntimeException(e));
		}
		return null;
	}

	@Override
	public String asString() {
		try {
			return IOUtils.toString(openStream(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			Log.crash("Resource Loading Error, could not get as String", new RuntimeException(e));
		}
		return null;
	}

	@Override
	public ByteBuffer asByteBuffer() {

		ByteBuffer buffer = null;
		Path path = Paths.get(url.getPath().replaceFirst("/", "").replaceFirst("file:", ""));

		Log.info(path.toString());

		if (Files.isReadable(path)) {
			try(SeekableByteChannel sbc = Files.newByteChannel(path)) {
				buffer = BufferUtils.createByteBuffer((int)sbc.size() + 1);
				while(sbc.read(buffer) != -1);
			} catch(IOException e) {
				Log.crash("Could not read byte channel", new IOException(e));
			}
		}

		if (buffer != null) {
			buffer.flip();
		} else {
			Log.crash("Could not get this URL Resource as a ByteBuffer: " + identifier.toString(), new RuntimeException());
		}

		return buffer;
	}
}
