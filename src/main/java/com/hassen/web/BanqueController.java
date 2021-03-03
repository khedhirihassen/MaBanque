package com.hassen.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.hassen.entities.Compte;
import com.hassen.entities.Operation;
import com.hassen.services.IBanqueService;

@Controller
public class BanqueController {

	@Autowired
	private IBanqueService iBanqueService;

	@RequestMapping("/operations")
	private String index() {
		return "comptes";
	}

	@RequestMapping("/consulterCompte")
	private String consulterCompte(Model model, String codeCompte,@RequestParam(name = "page",defaultValue = "0") int page,
		@RequestParam(name = "size",defaultValue = "4") int size) {
		model.addAttribute("codeCompte", codeCompte);
		try {
			Compte compte = iBanqueService.consulterCompte(codeCompte);
			Page<Operation> pageOperation = iBanqueService.listOperation(codeCompte, page, size);
			model.addAttribute("listOperations", pageOperation);
			int[] pages = new int[pageOperation.getTotalPages()];
			model.addAttribute("pages", pages);
			model.addAttribute("compte", compte);
		} catch (Exception e) {
			model.addAttribute("exception", e);
		}

		return "comptes";
	}

	@RequestMapping(value = "/saveOperation", method = RequestMethod.POST)
	public String saveOperation(Model model, String typeOperation, String codeCompte, double montant,
			String codeCompte2) {

		try {

			if (typeOperation.equals("VERS")) {
				iBanqueService.verser(codeCompte, montant);
			} else if (typeOperation.equals("RETR")) {
				iBanqueService.retirer(codeCompte, montant);
			} else if (typeOperation.equals("VIR")) {
				iBanqueService.virement(codeCompte, codeCompte2, montant);
			}

		} catch (Exception e) {
			model.addAttribute("error",e);
			return "redirect:/consulterCompte?codeCompte="+codeCompte+"&error"+e.getMessage();
		}

		return "redirect:/consulterCompte?codeCompte="+codeCompte;
	}

}
