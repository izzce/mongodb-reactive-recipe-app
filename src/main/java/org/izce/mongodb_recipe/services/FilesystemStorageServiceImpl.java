package org.izce.mongodb_recipe.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class FilesystemStorageServiceImpl implements StorageService {
	private final Path rootLocation;

	public FilesystemStorageServiceImpl(StorageProperties properties) {
		this.rootLocation = Paths.get(properties.getLocation());
	}

	@Override
	public Mono<Void> init() {
		try {
			Files.createDirectories(rootLocation);
		} catch (IOException e) {
			log.error("Could not initialize storage", e);
			throw new RuntimeException(e);
		}
		return Mono.empty();
	}

	@Override
	public Mono<Void> store(MultipartFile file) {
		try {
			if (file.isEmpty()) {
				throw new RuntimeException("Failed to store empty file.");
			}
			Path dest = this.rootLocation.resolve(Paths.get(file.getOriginalFilename())).normalize().toAbsolutePath();
			if (!dest.getParent().equals(this.rootLocation.toAbsolutePath())) {
				// This is a security check
				throw new RuntimeException("Cannot store file outside current directory.");
			}

			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, dest, StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			log.error("Failed to store file.", e);
			throw new RuntimeException("Failed to store file.", e);
		}
		
		return Mono.empty();
	}

	@Override
	public Flux<Path> loadAll() throws IOException {
		try {
			return Flux.fromStream(Files.walk(this.rootLocation, 1).filter(path -> !path.equals(this.rootLocation))
					.map(this.rootLocation::relativize));
		} catch (IOException e) {
			throw new RuntimeException("Failed to read stored files", e);
		}
	}

	@Override
	public Mono<Path> load(String filename) {
		return Mono.just(rootLocation.resolve(filename));
	}

	@Override
	public Mono<Resource> loadAsResource(String filename) {
		try {
			Path file = load(filename).block();
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return Mono.just(resource);
			} else {
				log.error("Could not read file: " + filename);
				throw new RuntimeException("Could not read file: " + filename);

			}
		} catch (MalformedURLException e) {
			log.error("Could not read file: ", e);
			throw new RuntimeException("Could not read file: " + filename);
		}
	}

	@Override
	public Mono<Void> deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
		return Mono.empty();
	}
}
