package com.systempro.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.systempro.library.entity.Loan;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long>{

}
