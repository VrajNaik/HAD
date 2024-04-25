package com.Team12.HADBackEnd.repository;

import com.Team12.HADBackEnd.models.Citizen;
import com.Team12.HADBackEnd.models.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {
//    Optional<Long> findTopByCitizenOrderByFollowUpNoDesc(Citizen citizen);
    Optional<Response> findFirstByCitizenOrderByFollowUpNoDesc(Citizen citizen);
}
