package com.rentacar.repositories;

import com.rentacar.entidades.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, String> {

    @Query("SELECT v FROM Vehiculo v WHERE v.precioDia BETWEEN :min AND :max")
    List<Vehiculo> findByPrecioDiaBetween(@Param("min") Double min, @Param("max") Double max);
}
