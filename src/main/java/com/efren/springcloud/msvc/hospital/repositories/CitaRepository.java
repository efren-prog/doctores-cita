package com.efren.springcloud.msvc.hospital.repositories;

import com.efren.springcloud.msvc.hospital.entities.Cita;
import com.efren.springcloud.msvc.hospital.entities.Consultorio;
import com.efren.springcloud.msvc.hospital.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    boolean existsByConsultorioAndHorarioAndCanceladaFalse(Consultorio consultorio, LocalDateTime horario);

    boolean existsByDoctorAndHorarioAndCanceladaFalse(Doctor doctor, LocalDateTime horario);

    boolean existsByNombrePacienteAndHorarioBetweenAndCanceladaFalse(
            String nombrePaciente, LocalDateTime inicio, LocalDateTime fin);

    long countByDoctorAndHorarioBetweenAndCanceladaFalse(
            Doctor doctor, LocalDateTime inicioDelDia, LocalDateTime finDelDia);

    @Modifying
    @Query("UPDATE Cita c SET c.cancelada = true WHERE c.id = :id")
    int cancelarCita(@Param("id") Long id);

}
