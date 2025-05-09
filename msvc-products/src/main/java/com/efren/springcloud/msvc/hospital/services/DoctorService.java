package com.efren.springcloud.msvc.hospital.services;

import com.efren.springcloud.msvc.hospital.entities.Doctor;
import com.efren.springcloud.msvc.hospital.repositories.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    public List<Doctor> listarTodos() {
        return doctorRepository.findAll();
    }

    public Doctor obtenerPorId(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor no encontrado con ID: " + id));
    }

    public Doctor guardarDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public void eliminarDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

}
