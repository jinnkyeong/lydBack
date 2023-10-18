package com.loveyourdog.brokingservice.repository.customer;

import com.loveyourdog.brokingservice.model.entity.Customer;
import com.loveyourdog.brokingservice.model.entity.Dogwalker;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRespository extends JpaRepository<Customer,Long> {
//    Optional<Customer> findByEmail(String email);
    @EntityGraph(attributePaths = {"roles"},
            type = EntityGraph.EntityGraphType.LOAD)
    Optional<Customer> findWithRolesByEmail(String email);

    Optional<Customer> findBySocialId(String socialId);

}