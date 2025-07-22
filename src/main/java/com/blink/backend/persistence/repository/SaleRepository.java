package com.blink.backend.persistence.repository;

import com.blink.backend.persistence.entity.appointment.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<Sale,Integer> {


}
