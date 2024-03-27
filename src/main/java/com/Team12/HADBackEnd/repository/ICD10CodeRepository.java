package com.Team12.HADBackEnd.repository;

import com.Team12.HADBackEnd.models.ICD10Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ICD10CodeRepository extends JpaRepository<ICD10Code, Long> {

}


