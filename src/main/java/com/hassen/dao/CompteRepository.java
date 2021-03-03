package com.hassen.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hassen.entities.Compte;

public interface CompteRepository extends JpaRepository<Compte, String> {

}
