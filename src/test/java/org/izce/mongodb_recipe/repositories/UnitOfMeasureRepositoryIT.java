package org.izce.mongodb_recipe.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.izce.mongodb_recipe.model.UnitOfMeasure;
import org.izce.mongodb_recipe.services.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@DataMongoTest
public class UnitOfMeasureRepositoryIT {
	private static final String TEASPOON = "Teaspoon";
	
	@MockBean
    private StorageService storageService;
	
	@Autowired
	UnitOfMeasureRepository repository;
	

	@BeforeEach
	public void setUp() throws Exception {
		repository.deleteAll();
	}
	
    @Test
    public void testSaveUom() throws Exception {
        var uom = new UnitOfMeasure(TEASPOON);
        repository.save(uom);

        assertEquals(Long.valueOf(1L), repository.count());
    }

    @Test
    public void testFindByUom() throws Exception {
    	var uom = new UnitOfMeasure(TEASPOON);
    	repository.save(uom);

        var fetchedUOM = repository.findByUom(TEASPOON).get();

        assertEquals(TEASPOON, fetchedUOM.getUom());

    }

}
