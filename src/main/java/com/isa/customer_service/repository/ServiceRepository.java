package com.isa.customer_service.repository;

import com.isa.customer_service.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<TimeSlot, Integer> {
    List<TimeSlot> findAll();
}

