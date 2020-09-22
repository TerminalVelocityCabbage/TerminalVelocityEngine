package com.terminalvelocitycabbage.engine.resources;

import java.util.*;
import java.util.function.Predicate;

public class ClassLoaderResourceManager implements ResourceManager {

	private final List<UrlResource> loadedResources = new ArrayList<>();
	private final ClassLoader classLoader;
	private final String prefix;

	public ClassLoaderResourceManager(ClassLoader classLoader, String prefix) {
		this.classLoader = classLoader;
		this.prefix = prefix;
	}

	@Override
	public Optional<Resource> getResource(Identifier identifier) {
		System.out.println(classLoader.getResource(prefix + identifier.getNamespace() + "/" + identifier.getPath()));
		return Optional.ofNullable(classLoader.getResource(prefix + identifier.getNamespace() + "/" + identifier.getPath()))
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
