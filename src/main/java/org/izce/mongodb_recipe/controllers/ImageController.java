package org.izce.mongodb_recipe.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.izce.mongodb_recipe.commands.RecipeCommand;
import org.izce.mongodb_recipe.services.ImageService;
import org.izce.mongodb_recipe.services.RecipeService;
import org.izce.mongodb_recipe.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ImageController {

	private final RecipeService recipeService;
	private final ImageService imageService;
	private final StorageService storageService;

	@Autowired
	public ImageController(RecipeService recipeService, ImageService imageService, StorageService storageService) {
		log.debug("ImageController...");
		this.recipeService = recipeService;
		this.imageService = imageService;
		this.storageService = storageService;
	}

	@GetMapping("/recipe/image/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

		Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}
	
	@PostMapping("recipe/image")
	@ResponseBody
	public Map<String, String> handleImagePostForNewRecipe(@RequestParam("image[]") MultipartFile [] imageFiles) {
		Map<String, String> map = new HashMap<>();
		
		for (int i = 0; i < imageFiles.length; i++) {
			log.debug("Storing image file: " + imageFiles[i].getOriginalFilename(), imageFiles[i]);
			storageService.store(imageFiles[i]);
			
			Path imagePath = storageService.load(imageFiles[i].getOriginalFilename());
			log.debug("Image stored at local path: " + imagePath);
			
			
			String urlPath = MvcUriComponentsBuilder.fromMethodName(ImageController.class, "serveFile", imagePath.getFileName().toString()).build().toUri().toString();

			map.put("imageurl_" + i, urlPath);
		}
		
		map.put("status", "OK");
		return map;
	}

	
	@PostMapping("recipe/{id}/image")
	@ResponseBody
	public Map<String, String> handleImagePostForExistingRecipe(
			@PathVariable String id, 
			@RequestParam("image[]") MultipartFile [] imageFiles) {
		for (var imageFile : imageFiles) {
			imageService.save(id, imageFile);
			// TODO how to return the url of new image?
		}
		return Map.of("imageurl", "imageurl", "status", "OK");
	}

	
	@GetMapping(value = "recipe/{id}/image", produces = MediaType.IMAGE_JPEG_VALUE)
	public void renderImageFromDB(
			@PathVariable String id, 
			HttpServletResponse response, 
			Model model) throws IOException {
		RecipeCommand recipeCommand = recipeService.findRecipeCommandById(id);

		if (recipeCommand.getImage() != null) {
			byte[] byteArray = new byte[recipeCommand.getImage().length];

			int i = 0;
			for (Byte wrappedByte : recipeCommand.getImage()) {
				byteArray[i++] = wrappedByte; // auto unboxing
			}

			InputStream is = new ByteArrayInputStream(byteArray);
			IOUtils.copy(is, response.getOutputStream());
		}
	}
	
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<?> handleStorageFileNotFound(RuntimeException exc) {
		return ResponseEntity.notFound().build();
	}
	
}

