package com.gogomaya.server.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.repository.annotation.RestResource;
import org.springframework.stereotype.Repository;

@Repository
@RestResource(path="profile", exported = true)
public interface GamerProfileRepository extends JpaRepository<GamerProfile, Long>, JpaSpecificationExecutor<GamerProfile> {

}
