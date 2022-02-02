package org.izce.mongodb_recipe.repositories;

import org.izce.mongodb_recipe.model.Note;
import org.springframework.data.repository.CrudRepository;

public interface NoteRepository extends CrudRepository<Note, String> {

}
