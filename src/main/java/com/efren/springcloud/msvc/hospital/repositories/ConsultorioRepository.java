package com.efren.springcloud.msvc.hospital.repositories;

import com.efren.springcloud.msvc.hospital.entities.Consultorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultorioRepository extends JpaRepository<Consultorio, Long> {
}
