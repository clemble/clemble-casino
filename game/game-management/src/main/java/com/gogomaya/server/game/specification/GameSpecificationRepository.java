package com.gogomaya.server.game.specification;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GameSpecificationRepository extends JpaRepository<GameSpecification, SpecificationName> {

    @Query(value = "select specification from GameSpecification specification where specification.name.name = :name")
    public List<GameSpecification> findByGame(@Param("name") String name);

}
