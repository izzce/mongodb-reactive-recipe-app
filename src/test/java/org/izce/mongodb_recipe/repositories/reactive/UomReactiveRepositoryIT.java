package org.izce.mongodb_recipe.repositories.reactive;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.izce.mongodb_recipe.model.UnitOfMeasure;
import org.izce.mongodb_recipe.services.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@DataMongoTest
public class UomReactiveRepositoryIT {
	private static final String TEASPOON = "Teaspoon";
	
	@MockBean
    private StorageService storageService;
	
	@Autowired
	UomReactiveRepository repository;

	@BeforeEach
	public void setUp() throws Exception {
		repository.deleteAll().block();
	}
	
    @Test
    public void testSaveUom() throws Exception {
        var uom = new UnitOfMeasure(TEASPOON);
        repository.save(uom).block();

        Long count = repository.count().block();

        assertEquals(Long.valueOf(1L), count);
    }

    @Test
    public void testFindByUom() throws Exception {
    	var uom = new UnitOfMeasure(TEASPOON);
    	repository.save(uom).block();

        var fetchedUOM = repository.findByUom(TEASPOON).block();

        assertEquals(TEASPOON, fetchedUOM.getUom());

    }
}
