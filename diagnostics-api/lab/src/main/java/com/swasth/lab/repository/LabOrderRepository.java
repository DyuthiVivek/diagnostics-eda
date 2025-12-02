package com.swasth.lab.repository;

import com.swasth.lab.model.LabOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LabOrderRepository extends JpaRepository<LabOrder, UUID> {
}
