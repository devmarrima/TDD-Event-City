package com.devsuperior.bds02.services;

import com.devsuperior.bds02.dto.EventDTO;
import com.devsuperior.bds02.entities.City;
import com.devsuperior.bds02.entities.Event;
import com.devsuperior.bds02.repositories.CityRepository;
import com.devsuperior.bds02.repositories.EventRepository;
import com.devsuperior.bds02.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {

    @Autowired
    private EventRepository repository;

    @Autowired
    private CityRepository cityRepository;

    @Transactional(readOnly = true)
    public EventDTO findById(Long id){
        Event entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        return new EventDTO(entity);
    }

    @Transactional
    public EventDTO update(Long id, EventDTO dto){
        try {
            Event entity = repository.getReferenceById(id);
            entity.setName(dto.getName());
            entity.setDate(dto.getDate());
            entity.setUrl(dto.getUrl());

            City city =  cityRepository.findById(dto.getCityId()).orElseThrow(() ->new ResourceNotFoundException("City not found"));
            entity.setCity(city);
            entity = repository.save(entity);
            return new EventDTO(entity);
        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Event not found");
        }
    }


}
