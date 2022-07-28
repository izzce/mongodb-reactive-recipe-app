package org.izce.mongodb_recipe.services;

import java.io.IOException;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StorageService {
	
	Mono<Void> init();

	Mono<Void> store(MultipartFile file);
	
	Flux<Path> loadAll() throws IOException;

	Mono<Path> load(String filename) throws IOException;

	Mono<Resource> loadAsResource(String filename) throws IOException;

	Mono<Void> deleteAll();
	
}
