import { Component, OnInit, Input } from '@angular/core';
import { NgbModalConfig, NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ActiviteService } from 'src/app/_services/activite.service';
import { Activite, Adherent } from 'src/app/models';
import { FormGroup, FormBuilder } from '@angular/forms';
import { faCircleQuestion,faEnvelope, faCircleXmark, faCloudDownloadAlt, faBook, faScaleBalanced, faPencilSquare, faSquarePlus, faSquareMinus, faCircleCheck, faUserPlus } from '@fortawesome/free-solid-svg-icons';
import { AdherentService } from 'src/app/_services/adherent.service';
import { ParamService } from 'src/app/_services/param.service';



@Component({
  selector: 'ngbd-modal-component',
  templateUrl: './activites.component.html',
  styleUrls: ['./activites.component.css'],
})
export class ActivitesComponent implements OnInit {
  faCircleQuestion=faCircleQuestion
  faEnvelope = faEnvelope;
  faCircleXmark = faCircleXmark;
  faCloudDownloadAlt = faCloudDownloadAlt;
  faScaleBalanced = faScaleBalanced;
  faBook = faBook;
  faUserPlus = faUserPlus;
  faCircleCheck = faCircleCheck;
  faSquareMinus = faSquareMinus;
  faPencilSquare = faPencilSquare;
  faSquarePlus = faSquarePlus;
  newActivite: Activite = new Activite;
  activites: Activite[] = [];
  isFailed = false;
  ordre: string = 'nom';
  search: string = "";
  errorMessage = '';
  isLoginFailed = false;
  sens: boolean = false;
  filtres: Map<string, boolean> = new Map<string, boolean>();
  editProfileForm!: FormGroup;
  adherents: Adherent[] = []
  constructor(
    private adherentService: AdherentService,
    public activiteService: ActiviteService,
    private modalService: NgbModal, 
    public paramService: ParamService
    ) { }

  ngOnInit(): void {
    this.getActivites();
    this.getAdherents();
  }

  onSubmit() {
    this.modalService.dismissAll();


    this.activiteService.save(this.newActivite).subscribe(
      data => {

        this.getActivites()
        this.newActivite = new Activite;
      },
      err => {
        this.isFailed = true;
        this.errorMessage = err.message

      }
    );
  }

  ajouterProf(newActivite: Activite, adherent: Adherent) {
    newActivite.profs.push(adherent);
  }

  getAdherents() {
    this.adherentService.getAll().subscribe(
      data => {
        this.adherents = data;
      },
      err => {
        this.isFailed = true;
        this.errorMessage = err.message
      }
    );
  }
  openModal(targetModal: any, activite: Activite) {
    this.modalService.open(targetModal, {
      centered: true,
      backdrop: 'static'
    });
    this.newActivite = activite;
  }

  dismiss() {
    this.modalService.dismissAll();
    this.getActivites()
  }



  getActivites() {
    this.activiteService.getAll().subscribe(
      data => {
        this.activites = data;
      },
      err => {
        this.isFailed = true;
        this.errorMessage = err.message
      }
    );
  }



  filtre(nomFiltre: string, sens: boolean) {

    if (this.filtres.get(nomFiltre) != sens) {
      this.filtres.delete(nomFiltre)
    }

    if (this.filtres.get(nomFiltre) == sens) {
      this.filtres.delete(nomFiltre)
    } else {
      this.filtres.set(nomFiltre, sens);
    }
    this.activiteService.getAll().subscribe(
      data => {
        if (this.filtres.get('basket')) {
          this.activites = data.filter(a => a.groupe == "ALOD_B");
        } else if (!this.filtres.get('basket')) {
          this.activites = data.filter(a => a.groupe == "ALOD_G");
        }

        if (this.filtres.size == 0) {
          this.activites = data;
        }
      },
      err => {
        this.isFailed = true;
        this.errorMessage = err.message

      }
    );
  }
}
