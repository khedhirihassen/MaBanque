package com.hassen.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hassen.dao.ClientRepository;
import com.hassen.dao.CompteRepository;
import com.hassen.dao.OperationRepository;
import com.hassen.entities.Compte;
import com.hassen.entities.CompteCourant;
import com.hassen.entities.Operation;
import com.hassen.entities.Retrait;
import com.hassen.entities.Versement;

@Service
@Transactional
public class BanqueServiceImpl implements IBanqueService {

	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private CompteRepository compteRepository;
	@Autowired
	private OperationRepository operationRepository;
	
	@Override
	public Compte consulterCompte(String codeCompte) {
		//Optional<Compte> cp = compteRepository.findById(codeCompte);
		Compte cp = compteRepository.getOne(codeCompte);
		//System.out.println(cp.getSolde());
		if (cp == null) {
			throw new RuntimeException("Compte introuvable");
		}
		return cp;
	}

	@Override
	public void verser(String codeCompte, double montant) {
		Compte cp = consulterCompte(codeCompte);
		Versement v = new Versement(new Date(), montant, cp);
		operationRepository.save(v);
		cp.setSolde(cp.getSolde()+montant);
		compteRepository.save(cp);
	}

	@Override
	public void retirer(String codeCompte, double montant) {
		Compte cp = consulterCompte(codeCompte);
		double facilitesCaisse = 0;
		if (cp instanceof CompteCourant) {
			facilitesCaisse = ((CompteCourant) cp).getDecouvert();
		}
		
		if (cp.getSolde()+facilitesCaisse <montant) {
			throw new RuntimeException("Solde Insufisant");
		}
		
		Retrait r = new Retrait(new Date(), montant, cp);
		operationRepository.save(r);
		cp.setSolde(cp.getSolde()-montant);
		compteRepository.save(cp);
		
	}

	@Override
	public void virement(String codeCompteExp, String codeCompteDest, double montant) {
		
		if (codeCompteExp.equals(codeCompteDest)) {
			throw new RuntimeException("Impossible virement sur le même compte");
		}
		retirer(codeCompteExp, montant);
		verser(codeCompteDest, montant);
	}

	@Override
	public Page<Operation> listOperation(String codeCompte, int page, int size) {
		PageRequest pageRequest = PageRequest.of(page, size);
		return operationRepository.listOperation(codeCompte, pageRequest);
	}
}
