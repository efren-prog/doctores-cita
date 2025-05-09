package com.efren.springcloud.msvc.hospital.services;

import com.efren.springcloud.msvc.hospital.entities.Consultorio;
import com.efren.springcloud.msvc.hospital.repositories.ConsultorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultorioService {

    @Autowired
    private ConsultorioRepository consultorioRepository;

    public List<Consultorio> listarTodos() {
        return consultorioRepository.findAll();
    }

    public Consultorio obtenerPorId(Long id) {
        return consultorioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultorio no encontrado con ID: " + id));
    }

    public Consultorio guardarConsultorio(Consultorio consultorio) {
        return consultorioRepository.save(consultorio);
    }

    public void eliminarConsultorio(Long id) {
        consultorioRepository.deleteById(id);
    }

}
