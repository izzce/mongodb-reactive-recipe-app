package org.izce.mongodb_recipe.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.izce.mongodb_recipe.commands.RecipeCommand;
import org.izce.mongodb_recipe.services.ImageService;
import org.izce.mongodb_recipe.services.RecipeService;
import org.izce.mongodb_recipe.services.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class ImageController {

	private final RecipeService recipeService;
	private final ImageService imageService;
	private final StorageService storageService;

	public ImageController(RecipeService recipeService, ImageService imageService, StorageService storageService) {
		log.debug("ImageController...");
		this.recipeService = recipeService;
		this.imageService = imageService;
		this.storageService = storageService;
	}

	@GetMapping("/recipe/image/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
		try {
			Resource file = storageService.loadAsResource(filename).block();
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
					"attachment; filename=\"" + file.getFilename() + "\"").body(file);
		} catch (IOException e) {
			log.error("Error", e);
			return ResponseEntity.notFound().build();
		}
		
	}
	
	@PostMapping(value = "recipe/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseBody
	public Map<String, String> handleImagePostForNewRecipe(@RequestPart("image") FilePart imageFile) {
		Map<String, String> map = new HashMap<>();
		
		log.debug("Storing image file: " + imageFile.filename(), imageFile.headers());
		storageService.store(imageFile);
		
		Path imagePath;
		try {
			imagePath = storageService.load(imageFile.filename()).block();
			log.debug("Image stored at local path: " + imagePath);
			String urlPath = UriComponentsBuilder.fromPath("/recipe/image/{filename:.+}")
					.path(imagePath.getFileName().toString()).build().toUri().toString();
					
			map.put("imageurl_0", urlPath);
		} catch (IOException e) {
			log.error("Error", e);
		}
		
		map.put("status", "OK");
		return map;
	}

	
	@PostMapping("recipe/{id}/image")
	@ResponseBody
	public Map<String, String> handleImagePostForExistingRecipe(
			@PathVariable String id, 
			@RequestPart("image") FilePart imageFile) {
		imageService.save(id, imageFile).block();
		
		return Map.of("imageurl", "imageurl", "status", "OK");
	}

	
	@GetMapping(value = "recipe/{id}/image", produces = MediaType.IMAGE_JPEG_VALUE)
	public void renderImageFromDB(@PathVariable String id, ServerHttpResponse resp) throws IOException {
		
		RecipeCommand recipeCommand = recipeService.findRecipeCommandById(id).block();

		if (recipeCommand.getImage() != null) {
			byte[] byteArray = ArrayUtils.toPrimitive(recipeCommand.getImage());
			InputStream is = new ByteArrayInputStream(byteArray);
			IOUtils.copy(is, resp.bufferFactory().allocateBuffer().asOutputStream());
		}
	}
	
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<?> handleStorageFileNotFound(RuntimeException exc) {
		return ResponseEntity.notFound().build();
	}
	
}

