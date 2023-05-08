package com.gestion.adhesion.services;

import com.gestion.adhesion.models.Accord;
import com.gestion.adhesion.models.Adherent;
import com.gestion.adhesion.models.Adhesion;
import com.gestion.adhesion.repository.AdhesionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AdhesionServices {

    @Autowired
    AdhesionRepository adhesionRepository;
    @Autowired
    TribuServices tribuServices;
    @Autowired
    AdherentServices adherentServices;
    @Autowired
    ActiviteServices activiteServices;

    @Autowired
    EmailService emailService;

    public List<Adhesion> getAll(){
        return adhesionRepository.findAll();
    }

    public Adhesion update(Adhesion frontAdhesion){
        Adhesion dataAdhesion = adhesionRepository.findById(frontAdhesion.getId()).get();
        dataAdhesion.setTypeReglement(frontAdhesion.getTypeReglement());

        Accord frontaccordImage = frontAdhesion.getAccords().stream().filter(accord -> "Reglement Interieur".equals(accord.getNom())).findFirst().get();
        Accord dataaccordImage = dataAdhesion.getAccords().stream().filter(accord -> "Reglement Interieur".equals(accord.getNom())).findFirst().get();
        dataaccordImage.setEtat(frontaccordImage.getEtat());
        dataaccordImage.setDatePassage(frontaccordImage.getDatePassage());


        if(frontAdhesion.getAccords().stream().anyMatch(accord -> "Attestation Sante".equals(accord.getNom()))) {
            Accord frontaccordAS = frontAdhesion.getAccords().stream().filter(accord -> "Attestation Sante".equals(accord.getNom())).findFirst().get();
            Accord dataaccordAS = dataAdhesion.getAccords().stream().filter(accord -> "Attestation Sante".equals(accord.getNom())).findFirst().get();
            dataaccordAS.setEtat(frontaccordAS.getEtat());
            dataaccordAS.setDatePassage(frontaccordAS.getDatePassage());
        }

        if(frontAdhesion.getAccords().stream().anyMatch(accord -> "Vie du Club".equals(accord.getNom()))) {
            Accord frontaccordVC = frontAdhesion.getAccords().stream().filter(accord -> "Vie du Club".equals(accord.getNom())).findFirst().get();
            Accord dataaccordVC = dataAdhesion.getAccords().stream().filter(accord -> "Vie du Club".equals(accord.getNom())).findFirst().get();
            dataaccordVC.setEtat(frontaccordVC.getEtat());
            dataaccordVC.setDatePassage(frontaccordVC.getDatePassage());
        }

        if(frontAdhesion.getAccords().stream().anyMatch(accord -> "Autorisation Parentale".equals(accord.getNom()))) {
            Accord frontaccordAP = frontAdhesion.getAccords().stream().filter(accord -> "Autorisation Parentale".equals(accord.getNom())).findFirst().get();
            Accord dataaccordAP = dataAdhesion.getAccords().stream().filter(accord -> "Autorisation Parentale".equals(accord.getNom())).findFirst().get();
            dataaccordAP.setEtat(frontaccordAP.getEtat());
            dataaccordAP.setDatePassage(frontaccordAP.getDatePassage());
        }

        if(frontAdhesion.getAccords().stream().anyMatch(accord -> "Prise en Charge".equals(accord.getNom()))) {
            Accord frontaccordPC = frontAdhesion.getAccords().stream().filter(accord -> "Prise en Charge".equals(accord.getNom())).findFirst().get();
            Accord dataaccordPC = dataAdhesion.getAccords().stream().filter(accord -> "Prise en Charge".equals(accord.getNom())).findFirst().get();
            dataaccordPC.setEtat(frontaccordPC.getEtat());
            dataaccordPC.setDatePassage(frontaccordPC.getDatePassage());
        }

        dataAdhesion.setStatutActuel("Attente validation secrétariat");

        return adhesionRepository.save(dataAdhesion);
    }

    public Adhesion saveUnique(Adhesion adhesion){
        return adhesionRepository.save(adhesion);
    }
    public List<Adhesion> save(List<Adhesion> adhesions){

        adhesions.stream().forEach(adhesion -> {

            adhesion.setId(null);
            adhesion.setRappel(false);
            adhesion.setDateAjoutPanier(LocalDate.now());
            adhesion.setAdherent(adherentServices.getById(adhesion.getAdherent().getId()));
            adhesion.setActivite(activiteServices.getById(adhesion.getActivite().getId()));

            adhesion.getAccords().add(new Accord("Reglement Interieur"));

            if(adhesion.getActivite().isCertificatMedical() && adhesion.getActivite().getDureeVieCertif() > 1) {
                adhesion.getAccords().add(new Accord("Attestation Sante"));
            }

            if(adhesion.getActivite().isVieClub()) {
                adhesion.getAccords().add(new Accord("Vie du Club"));
            }

            if(adhesion.getActivite().isAutorisationParentale() && adhesion.getAdherent().isMineur()) {
                adhesion.getAccords().add(new Accord("Autorisation Parentale"));
            }

            if(adhesion.getActivite().isPriseEnCharge() && adhesion.getAdherent().isMineur()) {
                adhesion.getAccords().add(new Accord("Prise en Charge"));
            }

            if(adhesion.getActivite().getAdhesions().stream().filter(adh -> adh.getStatutActuel() == "Attente validation adhérent" || adh.getStatutActuel() == "Validée").count() >= adhesion.getActivite().getNbPlaces()){
                adhesion.setStatutActuel("Liste d\'attente");
            }else{
                adhesion.setStatutActuel("Attente validation adhérent");
            }

            adhesionRepository.save(adhesion);
        });

        return adhesions;
    }



    public Adhesion updateDocumentsSecretariat(Long adhesionId, Boolean statut){
        Adhesion adhesion = adhesionRepository.findById(adhesionId).get();
        adhesion.setValidDocumentSecretariat(statut);
        return adhesionRepository.save(adhesion);
    }

    public Adhesion updatePaiementSecretariat(Long adhesionId, Integer tarif, Boolean statut){
        Adhesion adhesion = adhesionRepository.findById(adhesionId).get();
        if(!statut){
            adhesion.setTarif(adhesion.getActivite().getTarif());
        }else{
            adhesion.setTarif(tarif);
        }

        adhesion.setValidPaiementSecretariat(statut);
        return adhesionRepository.save(adhesion);
    }


    public Adhesion updateFlag(Long adhesionId, Boolean statut){
        Adhesion adhesion = adhesionRepository.findById(adhesionId).get();
        adhesion.setFlag(statut);
        return adhesionRepository.save(adhesion);
    }

    public Adhesion enregistrerRemarque(Long adhesionId, String remarqueSecretariat){
        Adhesion adhesion = adhesionRepository.findById(adhesionId).get();
        adhesion.setRemarqueSecretariat(remarqueSecretariat);
        return adhesionRepository.save(adhesion);
    }

    public Adhesion choisirStatut(Long adhesionId, String nouveauStatut){
        Adhesion adhesion = adhesionRepository.findById(adhesionId).get();

        if("Validée".equals(nouveauStatut) && !"Validée".equals(adhesion.getStatutActuel())){
            adhesion.setInscrit(true);
            emailService.sendAutoMail(adhesion, "Sujet_Mail_Validation", "Corp_Mail_Validation");
        }else if(!"Validée".equals(nouveauStatut)){
            adhesion.setInscrit(false);
        }

        if("Attente validation adhérent".equals(nouveauStatut) && "Sur liste d'attente".equals(adhesion.getStatutActuel())){
            emailService.sendAutoMail(adhesion, "Sujet_Mail_Reactivation", "Corp_Mail_Reactivation");
        }

        if("Annulée".equals(nouveauStatut) && !"Annulée".equals(adhesion.getStatutActuel())){
            emailService.sendAutoMail(adhesion, "Sujet_Mail_Annulation", "Corp_Mail_Annulation");
        }

        adhesion.setStatutActuel(nouveauStatut);
        adhesion.setDateChangementStatut(LocalDate.now());
        return adhesionRepository.save(adhesion);
    }

    public Adhesion updateTypePaiement(Long adhesionId, String typePaiement){
        Adhesion inscription = adhesionRepository.findById(adhesionId).get();
        inscription.setTypeReglement(typePaiement);
        return adhesionRepository.save(inscription);
    }

    public void deleteAdhesion(Long adhesionId){
        adhesionRepository.deleteById(adhesionId);
    }

}