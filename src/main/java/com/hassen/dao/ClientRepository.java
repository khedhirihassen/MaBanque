package com.hassen.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hassen.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
