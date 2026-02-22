package com.rentacar.repositories;

import com.rentacar.entidades.Alquiler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AlquilerRepository extends JpaRepository<Alquiler, Long> {

    @Query("SELECT a FROM Alquiler a WHERE a.vehiculo.matricula = :matricula AND " +
           "((:fechaInicio BETWEEN a.fechaInicio AND a.fechaFin) OR " +
           "(:fechaFin BETWEEN a.fechaInicio AND a.fechaFin) OR " +
           "(a.fechaInicio BETWEEN :fechaInicio AND :fechaFin))")
    List<Alquiler> findConflictingRentals(@Param("matricula") String matricula,
                                          @Param("fechaInicio") LocalDate fechaInicio,
                                          @Param("fechaFin") LocalDate fechaFin);

    @Query("SELECT SUM(a.costoTotal) FROM Alquiler a WHERE MONTH(a.fechaInicio) = :mes AND YEAR(a.fechaInicio) = :anio")
    Double calculateIncomeByMonthAndYear(@Param("mes") int mes, @Param("anio") int anio);

    // Nuevo método para contar alquileres activos de un cliente
    long countByClienteDniAndFechaFinAfter(String clienteDni, LocalDate fechaActual);
}
