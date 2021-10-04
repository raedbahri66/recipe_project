package com.bahri.recipe_project.repositories;

import com.bahri.recipe_project.domain.Category;
import com.bahri.recipe_project.domain.UnitOfMeasure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnitOfMesureRepository extends CrudRepository<UnitOfMeasure,Long> {

    Optional<UnitOfMeasure> findByUom(String uom);


}
