package com.maeemresen.testcontainers.workshop.spring.repository;

import com.maeemresen.testcontainers.workshop.spring.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Emre Şen (maemresen@yazilim.vip), 11/12/2022
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    Optional<Person> findTopByNameIgnoreCase(String name);
}
