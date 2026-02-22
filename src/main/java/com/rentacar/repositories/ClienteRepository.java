package com.rentacar.repositories;

import com.rentacar.entidades.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, String> {
    boolean existsByEmail(String email);
}
