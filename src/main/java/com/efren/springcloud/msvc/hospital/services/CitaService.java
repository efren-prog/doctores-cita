package com.efren.springcloud.msvc.hospital.services;
import com.efren.springcloud.msvc.hospital.entities.Cita;
import com.efren.springcloud.msvc.hospital.entities.Consultorio;
import com.efren.springcloud.msvc.hospital.entities.Doctor;
import com.efren.springcloud.msvc.hospital.repositories.CitaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// CitaService.java
@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    @PersistenceContext
    EntityManager em;

    public Cita guardarCita(Cita cita) {
        // Validar reglas de negocio
        validarDisponibilidadConsultorio(cita);
        validarDisponibilidadDoctor(cita);
        validarCitasPaciente(cita);
        validarMaxCitasDoctor(cita);

        return citaRepository.save(cita);
    }

    private void validarDisponibilidadConsultorio(Cita cita) {
        boolean existe = citaRepository.existsByConsultorioAndHorarioAndCanceladaFalse(
                cita.getConsultorio(), cita.getHorario());
        if (existe) {
            throw new IllegalStateException("El consultorio ya tiene una cita a esa hora");
        }
    }

    private void validarDisponibilidadDoctor(Cita cita) {
        boolean existe = citaRepository.existsByDoctorAndHorarioAndCanceladaFalse(
                cita.getDoctor(), cita.getHorario());
        if (existe) {
            throw new IllegalStateException("El doctor ya tiene una cita a esa hora");
        }
    }

    private void validarCitasPaciente(Cita cita) {
        LocalDateTime inicio = cita.getHorario().minusHours(2);
        LocalDateTime fin = cita.getHorario().plusHours(2);

        boolean existe = citaRepository.existsByNombrePacienteAndHorarioBetweenAndCanceladaFalse(
                cita.getNombrePaciente(), inicio, fin);
        if (existe) {
            throw new IllegalStateException("El paciente ya tiene una cita con menos de 2 horas de diferencia");
        }
    }

    private void validarMaxCitasDoctor(Cita cita) {
        LocalDate fecha = cita.getHorario().toLocalDate();
        long count = citaRepository.countByDoctorAndHorarioBetweenAndCanceladaFalse(
                cita.getDoctor(),
                fecha.atStartOfDay(),
                fecha.plusDays(1).atStartOfDay());

        if (count >= 8) {
            throw new IllegalStateException("El doctor ya tiene 8 citas para este día");
        }
    }

    public List<Cita> buscarCitas(LocalDate fecha, Doctor doctor, Consultorio consultorio) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Cita> cq = cb.createQuery(Cita.class);
        Root<Cita> cita = cq.from(Cita.class);

        List<Predicate> predicates = new ArrayList<>();

        if (fecha != null) {
            predicates.add(cb.equal(cita.get("fecha"), fecha));
        }

        if (doctor != null) {
            predicates.add(cb.equal(cita.get("doctor"), doctor));
        }

        if (consultorio != null) {
            predicates.add(cb.equal(cita.get("consultorio"), consultorio));
        }

        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        TypedQuery<Cita> query = em.createQuery(cq);
        return query.getResultList();
    }

    public List<Cita> listarTodas() {
        return citaRepository.findAll();
    }

    public Optional<Cita> obtenerPorId(Long id) {
        return citaRepository.findById(id);
    }

    public void cancelarCita(Long id) {
        int updated = citaRepository.cancelarCita(id);
        if (updated == 0) {
            throw new IllegalArgumentException("Cita no encontrada con ID: " + id);
        }
    }

}