package org.izce.mongodb_recipe.repositories.reactive;

import org.izce.mongodb_recipe.model.Note;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface NoteReactiveRepository extends ReactiveMongoRepository<Note, String> {

}
