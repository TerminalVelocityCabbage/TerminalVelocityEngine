package com.terminalvelocitycabbage.engine.client.resources;

import java.util.*;
import java.util.function.Predicate;

public class ClassLoaderResourceManager implements ResourceManager {

	private final List<UrlResource> loadedResources = new ArrayList<>();
	private final ClassLoader classLoader;
	private final String prefix;
	private final String root;

	public ClassLoaderResourceManager(ClassLoader classLoader, String prefix, String root) {
		this.classLoader = classLoader;
		this.prefix = prefix;
		this.root = root;
	}

	@Override
	public Optional<Resource> getResource(Identifier identifier) {
		return Optional.ofNullable(classLoader.getResource(prefix + "/" + identifier.getNamespace() + "/" + root + "/" + identifier.getPath()))
				.map(url -> {
					UrlResource resource = new UrlResource(identifier, url);
					loadedResources.add(resource);
					return resource;
				});
	}

	@Override
	public Collection<Identifier> findResources(Predicate<Identifier> predicate) {
		return Collections.emptyList();
	}

	@Override
	public void invalidate() {
		loadedResources.removeIf(UrlResource::isInvalid);
	}
}
