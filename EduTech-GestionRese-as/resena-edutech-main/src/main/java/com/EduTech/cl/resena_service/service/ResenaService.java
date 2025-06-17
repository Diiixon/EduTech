package com.EduTech.cl.resena_service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.EduTech.cl.resena_service.model.Resena;
import com.EduTech.cl.resena_service.repository.ResenaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ResenaService {

    @Autowired
    private ResenaRepository resenaRepository;

    public List<Resena> findAll() {
        return resenaRepository.findAll();
    }

    public Resena save(Resena resena) {
        return resenaRepository.save(resena);
    }

    public Optional<Resena> findById(Long id) {
        return resenaRepository.findById(id);
    }

    public List<Resena> findByIdCurso(Long idCurso) {
        return resenaRepository.findByIdCurso(idCurso);
    }

    public List<Resena> findByIdEstudiante(Long idEstudiante) {
        return resenaRepository.findByIdEstudiante(idEstudiante);
    }

    public void deleteById(Long id) {
        resenaRepository.deleteById(id);
    }


}
