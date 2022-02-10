package org.elsys.ip.courseproject.model;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface QuestionRepository extends CrudRepository<Question, UUID> {
}
