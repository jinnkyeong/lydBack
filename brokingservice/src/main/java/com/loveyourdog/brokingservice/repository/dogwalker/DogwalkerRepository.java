package com.loveyourdog.brokingservice.repository.dogwalker;

import com.loveyourdog.brokingservice.model.entity.Application;
import com.loveyourdog.brokingservice.model.entity.Dogwalker;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DogwalkerRepository extends JpaRepository<Dogwalker,Long> {
    @EntityGraph(attributePaths = {"roles"},
            type = EntityGraph.EntityGraphType.LOAD)
    Optional<Dogwalker> findWithRolesByEmail(String email);

    List<Dogwalker> findByAddrStateAndAddrTown(String addrState, String addrTown);

    Optional<Dogwalker> findBySocialId(String socialId);
}
