package com.gestion.user.controllers;

import com.gestion.user.models.Adhesion;
import com.gestion.user.services.AdhesionServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api_adhesion/adhesion")
public class AdhesionController {

@Autowired
AdhesionServices adhesionServices;


	@GetMapping("/updateTypePaiement")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> updateTypePaiement(@PathParam("inscriptionId") Long adhesionId, @PathParam("typePaiement") String typePaiement) {
		return ResponseEntity.ok(adhesionServices.updateTypePaiement(adhesionId, typePaiement));
	}


	@DeleteMapping("/deleteAdhesion")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteAdhesion(@PathParam("adhesionId") Long adhesionId) {
		adhesionServices.deleteAdhesion(adhesionId);
		return ResponseEntity.ok(adhesionId+" deleted");
	}

	@GetMapping("/updateDocumentsSecretariat")
	@PreAuthorize("hasRole('SECRETAIRE') or hasRole('BUREAU') or hasRole('ADMINISTRATEUR') or hasRole('ADMIN')")
	public ResponseEntity<?> updateDocumentsSecretariat(@PathParam("inscriptionId") Long adhesionId, @PathParam("statut") Boolean statut) {
		return ResponseEntity.ok(adhesionServices.updateDocumentsSecretariat(adhesionId, statut));
	}

	@GetMapping("/updatePaiementSecretariat")
	@PreAuthorize("hasRole('SECRETAIRE') or hasRole('BUREAU') or hasRole('ADMINISTRATEUR') or hasRole('ADMIN')")
	public ResponseEntity<?> updatePaiementSecretariat(@PathParam("inscriptionId") Long adhesionId, @PathParam("tarif") Integer tarif, @PathParam("statut") Boolean statut) {
		return ResponseEntity.ok(adhesionServices.updatePaiementSecretariat(adhesionId, tarif, statut));
	}

	@GetMapping("/updateFlag")
	@PreAuthorize("hasRole('SECRETAIRE') or hasRole('BUREAU') or hasRole('ADMINISTRATEUR') or hasRole('ADMIN')")
	public ResponseEntity<?> updateFlag(@PathParam("inscriptionId") Long adhesionId, @PathParam("statut") Boolean statut) {
		return ResponseEntity.ok(adhesionServices.updateFlag(adhesionId, statut));
	}


	@GetMapping("/enregistrerRemarque")
	@PreAuthorize("hasRole('SECRETAIRE') or hasRole('BUREAU') or hasRole('ADMINISTRATEUR') or hasRole('ADMIN')")
	public ResponseEntity<?> enregistrerRemarque(@PathParam("inscriptionId") Long adhesionId, @PathParam("remarqueSecretariat") String remarqueSecretariat) {
		return ResponseEntity.ok(adhesionServices.enregistrerRemarque(adhesionId, remarqueSecretariat));
	}

	@GetMapping("/choisirStatut")
	@PreAuthorize("hasRole('SECRETAIRE') or hasRole('BUREAU') or hasRole('ADMINISTRATEUR') or hasRole('ADMIN')")
	public ResponseEntity<?> choisirStatut(@PathParam("inscriptionId") Long adhesionId, @PathParam("statutActuel") String statutActuel) {
		return ResponseEntity.ok(adhesionServices.choisirStatut(adhesionId, statutActuel));
	}

	@GetMapping("/all")
	@PreAuthorize("hasRole('SECRETAIRE') or hasRole('BUREAU') or hasRole('ADMINISTRATEUR') or hasRole('ADMIN')")
	public ResponseEntity<?> getAll() {
		return ResponseEntity.ok(adhesionServices.getAll());
	}


	@PostMapping("/save")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> save(@RequestBody List<Adhesion> adhesions, @PathParam("tribuId") Long tribuId) {
		return ResponseEntity.ok(adhesionServices.save(adhesions, tribuId));
	}

	@PostMapping("/update")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> save(@RequestBody Adhesion adhesion) {
		return ResponseEntity.ok(adhesionServices.update(adhesion));
	}
}
