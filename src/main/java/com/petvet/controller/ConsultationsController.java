package com.petvet.controller;
import com.petvet.dto.ConsultationDTO;
import com.petvet.service.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/consultations")
public class ConsultationsController {

    @Autowired
    private ConsultationService consultationService;

    @GetMapping
    public List<ConsultationDTO> getAllConsultations() {
        return consultationService.getAllConsultations();
    }

    @GetMapping("/{id}")
    public ConsultationDTO getConsultationById(@PathVariable Long id) {
        return consultationService.getConsultationById(id);
    }

    @PostMapping
    public ConsultationDTO createConsultation(@RequestBody ConsultationDTO consultationDTO) {
        return consultationService.createConsultation(consultationDTO);
    }

    @PutMapping("/{id}")
    public ConsultationDTO updateConsultation(@PathVariable Long id, @RequestBody ConsultationDTO consultationDTO) {
        return consultationService.updateConsultation(id, consultationDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteConsultation(@PathVariable Long id) {
        consultationService.deleteConsultation(id);
    }
}
