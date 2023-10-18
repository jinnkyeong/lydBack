package com.loveyourdog.brokingservice.repository.admin;

import com.loveyourdog.brokingservice.model.entity.Admin;
import com.loveyourdog.brokingservice.model.entity.Commision;
import com.loveyourdog.brokingservice.model.entity.Customer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin,Long> {
    @EntityGraph(attributePaths = {"roles"},
            type = EntityGraph.EntityGraphType.LOAD)
    Optional<Admin> findWithRolesBySign(String sign);
}
