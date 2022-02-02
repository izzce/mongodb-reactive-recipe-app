package org.izce.mongodb_recipe.repositories;

import java.util.Optional;

import org.izce.mongodb_recipe.model.UnitOfMeasure;
import org.springframework.data.repository.CrudRepository;

public interface UnitOfMeasureRepository extends CrudRepository<UnitOfMeasure, String> {
	Optional<UnitOfMeasure> findByUom(String uom);
	Optional<UnitOfMeasure> findByUomIgnoreCase(String uom);
}
