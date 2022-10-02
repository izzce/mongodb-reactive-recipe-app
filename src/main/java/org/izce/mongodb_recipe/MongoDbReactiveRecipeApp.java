package org.izce.mongodb_recipe;

import org.izce.mongodb_recipe.cascaders.CascadeSaveMongoEventListener;
import org.izce.mongodb_recipe.formatters.CategoryFormatter;
import org.izce.mongodb_recipe.formatters.IngredientFormatter;
import org.izce.mongodb_recipe.services.StorageProperties;
import org.izce.mongodb_recipe.services.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;


@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
@EnableMongoRepositories
public class MongoDbReactiveRecipeApp {

	public static void main(String[] args) {
		SpringApplication.run(MongoDbReactiveRecipeApp.class, args);
	}

    @Configuration
    static class RecipeAppConfig implements WebFluxConfigurer {
        @Override
        public void addFormatters(FormatterRegistry registry) {
            registry.addFormatter(new CategoryFormatter());
            registry.addFormatter(new IngredientFormatter());
        }
    }
    
    @Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.deleteAll();
			storageService.init();
		};
	}

    @Bean
    CascadeSaveMongoEventListener cascadeSaveMongoEventListener() {
        return new CascadeSaveMongoEventListener();
    }
}
