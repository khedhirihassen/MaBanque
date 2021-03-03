package com.hassen.services;

import org.springframework.data.domain.Page;

import com.hassen.entities.Compte;
import com.hassen.entities.Operation;

public interface IBanqueService {

	public Compte consulterCompte(String codeCompte);
	public void verser(String codeCompte, double montant);
	public void retirer(String codeCompte, double montant);
	public void virement(String codeCompteExp,String codeCompteDest,double montant);
	public Page<Operation> listOperation(String codeCompte,int page,int size);
	
}
