package org.izce.mongodb_recipe.repositories.reactive;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.izce.mongodb_recipe.model.Category;
import org.izce.mongodb_recipe.services.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@DataMongoTest
public class CategoryReactiveRepositoryIT {
	private static final String TURKISH = "Turkish";

	@MockBean
	private StorageService storageService;

	@Autowired
	CategoryReactiveRepository repository;

	@BeforeEach
	public void setUp() throws Exception {
		repository.deleteAll().block();

		var category = new Category(TURKISH);
		repository.save(category).block();
	}

	@Test
	public void testSave() throws Exception {
		Long count = repository.count().block();

		assertEquals(Long.valueOf(1L), count);
	}

	@Test
	public void testFindByDescription() throws Exception {
		var fetchedCategory = repository.findByDescription(TURKISH).block();

		assertEquals(TURKISH, fetchedCategory.getDescription());
	}

	@Test
	public void testFindByDescriptionIgnoreCase() throws Exception {
		var fetchedCategory = repository.findByDescriptionIgnoreCase(TURKISH.toLowerCase()).block();

		assertEquals(TURKISH, fetchedCategory.getDescription());
	}
}
