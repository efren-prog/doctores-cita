package com.efren.springcloud.msvc.hospital.controllers;

import com.efren.springcloud.msvc.hospital.entities.Cita;
import com.efren.springcloud.msvc.hospital.entities.Consultorio;
import com.efren.springcloud.msvc.hospital.entities.Doctor;
import com.efren.springcloud.msvc.hospital.services.CitaService;
import com.efren.springcloud.msvc.hospital.services.ConsultorioService;
import com.efren.springcloud.msvc.hospital.services.DoctorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/citas")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private ConsultorioService consultorioService;

    @GetMapping("/consultorios")
    public String listarConsultorios(Model model) {
        model.addAttribute("consultorios", consultorioService.listarTodos());
        return "consultorios/lista";
    }


    @GetMapping("/")
    public String listarCitas(Model model) {
        List<Cita> citas = citaService.listarTodas();
        System.out.println("Citas encontradas: " + citas.size());
        model.addAttribute("citas", citas);
        return "citas/lista";
    }

    @GetMapping
    public String listarDoctores(Model model) {
        model.addAttribute("doctores", doctorService.listarTodos());
        return "doctores/lista";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNuevaCita(Model model) {
        model.addAttribute("cita", new Cita());
        model.addAttribute("doctores", doctorService.listarTodos());
        model.addAttribute("consultorios", consultorioService.listarTodos());
        return "citas/formulario";
    }

    @PostMapping
    public String guardarCita(@Valid @ModelAttribute("cita") Cita cita, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("doctores", doctorService.listarTodos());
            model.addAttribute("consultorios", consultorioService.listarTodos());
            return "citas/formulario";
        }

        try {
            Cita citaGuardada = citaService.guardarCita(cita);
            return "redirect:/cita?exito=Cita+agendada+correctamente+ID+" + citaGuardada.getId();
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar: " + e.getMessage());
            model.addAttribute("doctores", doctorService.listarTodos());
            model.addAttribute("consultorios", consultorioService.listarTodos());
            return "citas/formulario";
        }
    }

    @GetMapping("/editar/{id}")
    public String editarCita(@PathVariable Long id, Model model) {
        Cita cita = citaService.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

        model.addAttribute("cita", cita);
        model.addAttribute("doctores", doctorService.listarTodos());
        model.addAttribute("consultorios", consultorioService.listarTodos());
        return "citas/formulario";
    }

    @GetMapping("/cancelar/{id}")
    public String cancelarCita(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            citaService.cancelarCita(id);
            redirectAttributes.addAttribute("exito", "Cita cancelada correctamente");
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addAttribute("error", e.getMessage());
        }
        return "redirect:/citas";
    }

    @GetMapping("/buscar")
    public String buscarCitas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) Long consultorioId,
            Model model) {

        Doctor doctor = doctorId != null ? doctorService.obtenerPorId(doctorId) : null;
        Consultorio consultorio = consultorioId != null ? consultorioService.obtenerPorId(consultorioId) : null;

        List<Cita> citas = citaService.buscarCitas(fecha, doctor, consultorio);
        model.addAttribute("citas", citas);  // Corregido a "citas"
        model.addAttribute("doctores", doctorService.listarTodos());
        model.addAttribute("consultorios", consultorioService.listarTodos());
        model.addAttribute("fechaSeleccionada", fecha);
        model.addAttribute("doctorSeleccionado", doctorId);
        model.addAttribute("consultorioSeleccionado", consultorioId);

        return "citas/buscar";
    }
}
