package pt.ua.tqs.ecocharger.ecocharger.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pt.ua.tqs.ecocharger.ecocharger.models.Administrator;


@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Long>{
    Optional<Administrator> findByEmail(String email);
    Optional<Administrator> findById(Long id);
    Optional<List<Administrator>> findByName(String name);
}   
