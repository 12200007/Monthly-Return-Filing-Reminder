package com.simple_form.repository;

import com.simple_form.model.CustomersDetailsModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomersDetailsRepository extends JpaRepository<CustomersDetailsModel, Long> {
}
