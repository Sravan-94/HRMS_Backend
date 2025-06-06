package com.zynlogics.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zynlogics.Entity.DocumentsCompeny;

@Repository
public interface CompenyDocumentsRepo extends JpaRepository<DocumentsCompeny, Integer> {

}
