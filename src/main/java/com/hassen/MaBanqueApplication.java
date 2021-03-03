package com.hassen;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.hassen.dao.ClientRepository;
import com.hassen.dao.CompteRepository;
import com.hassen.dao.OperationRepository;
import com.hassen.entities.Client;
import com.hassen.entities.Compte;
import com.hassen.entities.CompteCourant;
import com.hassen.entities.CompteEpargne;
import com.hassen.entities.Retrait;
import com.hassen.entities.Versement;
import com.hassen.services.IBanqueService;



@SpringBootApplication
public class MaBanqueApplication implements CommandLineRunner {

	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private CompteRepository compteRepository;
	@Autowired
	private OperationRepository operationRepository;
	@Autowired
	private IBanqueService iBanqueService;
	
	public static void main(String[] args) {
		
		SpringApplication.run(MaBanqueApplication.class, args);
		
		
		
	}

	@Override
	public void run(String... args) throws Exception {
	
		Client c1 = clientRepository.save(new Client("Hassen", "Hassen@gmail.com"));
		Client c2 = clientRepository.save(new Client("Rihab", "Rihab@gmail.com"));
		
		Compte cp1 = compteRepository.save(new CompteCourant("c1", new Date(), 9000, c1, 6000));
		Compte cp2 = compteRepository.save(new CompteEpargne("c2", new Date(), 8900, c2, 5.5));
		
		operationRepository.save(new Versement(new Date(), 5200, cp1));
		operationRepository.save(new Versement(new Date(), 200, cp1));
		operationRepository.save(new Versement(new Date(), 700, cp1));
		operationRepository.save(new Retrait(new Date(), 4200, cp1));
		
		operationRepository.save(new Versement(new Date(), 3200, cp2));
		operationRepository.save(new Versement(new Date(), 100, cp2));
		operationRepository.save(new Versement(new Date(), 1000, cp2));
		operationRepository.save(new Retrait(new Date(), 200, cp2));
		
		iBanqueService.verser("c1", 10000);
		
	}

}
