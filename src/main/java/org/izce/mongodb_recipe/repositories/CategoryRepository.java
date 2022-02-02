package org.izce.mongodb_recipe.repositories;

import java.util.Optional;

import org.izce.mongodb_recipe.model.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, String> {
	Optional<Category> findByDescription(String description);
	Optional<Category> findByDescriptionIgnoreCase(String description);
}
