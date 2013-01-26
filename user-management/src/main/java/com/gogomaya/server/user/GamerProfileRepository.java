package com.gogomaya.server.user;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Component;

@Component
@GenericGenerator(name="system-uuid", strategy = "uuid")
public interface GamerProfileRepository extends JpaRepository<GamerProfile, String>, JpaSpecificationExecutor<GamerProfile> {

}
