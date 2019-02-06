import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Bed} from "../../../domain/bed";
import {Patient} from "../../../domain/patient";
import {Unit} from "../../../domain/unit";
import {HttpClient} from "@angular/common/http";
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {SelectableItem} from "vgr-komponentkartan";
import {Patientexamination} from "../../../domain/patientexamination";
import {PatientEvent} from "../../../domain/patient-event";
import {CareBurdenChoice} from "../../../domain/careburdenchoice";
import {DropdownItem} from "../../../domain/DropdownItem";

@Component({
  selector: 'app-bed-form',
  templateUrl: './bed-form.component.html',
  styleUrls: ['./bed-form.component.scss']
})
export class BedFormComponent implements OnInit {

  @Input('clinicId') clinicId;
  @Input('unit') unit: Unit;
  @Input('bed') bed;

  @Output('collapse') collapseEvent = new EventEmitter();
  @Output('openDeleteModal') openDeleteModalEvent = new EventEmitter();
  @Output('save') saveEvent = new EventEmitter();

  bedForm: FormGroup;
  prevDate: Date;
  prevInfo: string;
  prevklinik: string;
  prevSekret: string;
  prevSmitta: string;
  prevCleanGroup: string;
  prevCleanInfo: string;
  busyInfo: string;

  @Input('genderDropdownItems') genderDropdownItems: DropdownItem<string>[];
  @Input('sskDropdownItems') sskDropdownItems: DropdownItem<number>[];
  @Input('leaveStatusesDropdownItems') leaveStatusesDropdownItems: DropdownItem<string>[];
  @Input('servingKlinikerDropdownItems') servingKlinikerDropdownItems: DropdownItem<number>[];
  @Input('cleaningAlternativesDropdownItems') cleaningAlternativesDropdownItems: DropdownItem<number>[];
  @Input('amningOptions') amningOptions: SelectableItem<number>[];
  @Input('informationOptions') informationOptions: SelectableItem<number>[];
  @Input('careBurdenValuesOptions') careBurdenValuesOptions: DropdownItem<number>[];
  @Input('dietMotherDropdownItems') dietMotherDropdownItems: DropdownItem<number>[];
  @Input('dietChildDropdownItems') dietChildDropdownItems: DropdownItem<number>[];
  @Input('dietDropdownItems') dietDropdownItems: DropdownItem<number>[];

  constructor(private http: HttpClient,
              private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.initForm(this.bed);
  }

  private initForm(bed: Bed) {
    if (!bed) {
      bed = new Bed();
    }
    let patient;
    if (!bed.patient) {
      patient = new Patient();
    } else {
      patient = bed.patient;
    }

    this.bedForm = this.formBuilder.group({
      id: [bed.id],
      occupied: [bed.occupied],
      label: [bed.label, [Validators.required]],
      patient: this.formBuilder.group({
        id: [patient.id],
        label: [patient.label],
        leaveStatus: [patient.leaveStatus],
        gender: [patient.gender],
        leftDate: [patient.leftDate],
        plannedLeaveDate: [patient.plannedLeaveDate],
        carePlan: [patient.carePlan],
        tolkGroup: this.formBuilder.group({
          interpreter: [patient.interpreter],
          interpretDate: [patient.interpretDate],
          interpretInfo: [patient.interpretInfo]
        }),
        akutPatient: [patient.akutPatient],
        electiv23O: [patient.electiv23O],
        electiv24O: [patient.electiv24O],
        vuxenPatient: [patient.vuxenPatient],
        sekretessGroup: this.formBuilder.group({
          sekretess: [patient.sekretess],
          sekretessInfo: [patient.sekretessInfo]
        }),
        kostMorGroup: this.formBuilder.group({
          dietMother: [patient.dietMother ? patient.dietMother.id : null],
          infoDietMother: [patient.infoDietMother]
        }),
        kostBarnGroup: this.formBuilder.group({
          dietChild: [patient.dietChild ? patient.dietChild.id : null],
          infoDietChild: [patient.infoDietChild]
        }),
        kostGroup: this.formBuilder.group({
          diet: [patient.diet ? patient.diet.id : null],
          infoDiet: [patient.infoDiet]
        }),

        infekterad: [patient.infekterad],
        patientExaminations: this.formBuilder.array(this.buildExaminationGroup(patient.patientExaminations)),
        patientEvents: this.formBuilder.array(this.buildEventGroup(patient.patientEvents)),
        infectionSensitive: [patient.infectionSensitive],
        smittaGroup: this.formBuilder.group({
          smitta: [patient.smitta],
          smittaInfo: [patient.smittaInfo]
        }),
        pal: [patient.pal],
        morRond: [patient.morRond],
        barnRond: [patient.barnRond],
        rond: [patient.rond],
        amning: +[patient.amning],
        infoGroup: this.formBuilder.group({
          information: +[patient.information],
          kommentar: [patient.kommentar]
        }),
        careBurdenChoices: this.formBuilder.array(this.buildCareBurdenChoiceGroup(patient))
      }),
      ssk: bed.ssk ? bed.ssk.id : null,
      waitingforbedGroup: this.formBuilder.group({
          servingKlinik: [bed.servingClinic != null ? bed.servingClinic.id : null],
          waitingpatient: [bed.patientWaits ? bed.patientWaits : null]
        }
      ),
      cleaningGroup: this.formBuilder.group({
        cleaningAlternative: [bed.cleaningAlternative != null ? bed.cleaningAlternative.id : null],
        cleaningInfo: [bed.cleaningInfo ? bed.cleaningInfo : null],
        cleaningalternativeExists: [bed.cleaningalternativeExists ? bed.cleaningalternativeExists : null]
      }),

      relatedInformation: [bed.relatedInformation]
    });

    this.bedForm.get('patient.tolkGroup.interpreter').valueChanges
      .subscribe((checked: boolean) => {
        if (!checked || checked == null) {
          this.prevDate = this.bedForm.get('patient.tolkGroup.interpretDate').value;
          this.prevInfo = this.bedForm.get('patient.tolkGroup.interpretInfo').value;
          this.bedForm.get('patient.tolkGroup.interpretDate').setValue(null);
          this.bedForm.get('patient.tolkGroup.interpretInfo').setValue(null);
        } else if (checked) {
          if (this.bedForm.get('patient.tolkGroup.interpretDate').value == null
            && this.bedForm.get('patient.tolkGroup.interpretInfo').value == null) {
            this.bedForm.get('patient.tolkGroup.interpretDate').setValue(this.prevDate);
            this.bedForm.get('patient.tolkGroup.interpretInfo').setValue(this.prevInfo);
          }
        }
      });

    this.bedForm.get('cleaningGroup.cleaningalternativeExists').valueChanges
      .subscribe((checked: boolean) => {
        if (!checked || checked == null) {
          this.prevCleanGroup = this.bedForm.get('cleaningGroup.cleaningAlternative').value;
          this.prevCleanInfo = this.bedForm.get('cleaningGroup.cleaningInfo').value;
          this.bedForm.get('cleaningGroup.cleaningAlternative').setValue(null);
          this.bedForm.get('cleaningGroup.cleaningInfo').setValue(null);
        } else if (checked) {
          if (this.bedForm.get('cleaningGroup.cleaningAlternative').value == null
            && this.bedForm.get('cleaningGroup.cleaningInfo').value == null) {
            this.bedForm.get('cleaningGroup.cleaningAlternative').setValue(this.prevCleanGroup);
            this.bedForm.get('cleaningGroup.cleaningInfo').setValue(this.prevCleanInfo);
          }
        }
      });
    this.bedForm.get('waitingforbedGroup.waitingpatient').valueChanges.subscribe(
      (checked: boolean) => {
        if (!checked || checked == null) {
          this.prevklinik = this.bedForm.get('waitingforbedGroup.servingKlinik').value;
          this.bedForm.get('waitingforbedGroup.servingKlinik').setValue(null);
        } else if (checked) {
          if (this.bedForm.get('waitingforbedGroup.servingKlinik').value == null) {
            this.bedForm.get('waitingforbedGroup.servingKlinik').setValue(this.prevklinik);
          }
        }
      });

    this.bedForm.get('patient.sekretessGroup.sekretess').valueChanges
      .subscribe((checked: boolean) => {
        if (!checked || checked == null) {
          this.prevSekret = this.bedForm.get('patient.sekretessGroup.sekretessInfo').value;
          this.bedForm.get('patient.sekretessGroup.sekretessInfo').setValue(null);
        } else if (checked) {
          if (this.bedForm.get('patient.sekretessGroup.sekretessInfo').value == null) {
            this.bedForm.get('patient.sekretessGroup.sekretessInfo').setValue(this.prevSekret);
          }
        }
      });

    this.bedForm.get('patient.smittaGroup.smitta').valueChanges
      .subscribe((checked: boolean) => {
        if (!checked || checked == null) {
          this.prevSmitta = this.bedForm.get('patient.smittaGroup.smitta').value;
          this.bedForm.get('patient.sekretessGroup.smittaInfo').setValue(null);
        } else if (checked) {
          if (this.bedForm.get('patient.smittaGroup.smittaInfo').value == null) {
            this.bedForm.get('patient.smittaGroup.smittaInfo').setValue(this.prevSmitta);
          }
        }
      });
  }

  save() {
    let bed = new Bed();
    let bedModel = this.bedForm.value;
    bed.id = bedModel.id;
    bed.label = bedModel.label;
    bed.occupied = !!bedModel.occupied;
    if (bed.occupied) {
      bed.patient = new Patient();
      bed.patient.id = bedModel.patient.id;
      bed.patient.label = bedModel.patient.label;
      bed.patient.leaveStatus = bedModel.patient.leaveStatus;
      bed.patient.leftDate = bedModel.patient.leftDate;
      bed.patient.gender = bedModel.patient.gender ? bedModel.patient.gender : null;
      bed.patient.plannedLeaveDate = bedModel.patient.plannedLeaveDate ? bedModel.patient.plannedLeaveDate : null;
      bed.patient.carePlan = bedModel.patient.carePlan ? bedModel.patient.carePlan : null;
      bed.patient.interpreter = bedModel.patient.tolkGroup.interpreter ? bedModel.patient.tolkGroup.interpreter : null;
      bed.patient.interpretDate = bedModel.patient.tolkGroup.interpretDate ? bedModel.patient.tolkGroup.interpretDate : null;
      bed.patient.interpretInfo = bedModel.patient.tolkGroup.interpretInfo ? bedModel.patient.tolkGroup.interpretInfo : null;
      bed.patient.akutPatient = bedModel.patient.akutPatient ? bedModel.patient.akutPatient : null;
      bed.patient.electiv23O = bedModel.patient.electiv23O ? bedModel.patient.electiv23O : null;
      bed.patient.electiv24O = bedModel.patient.electiv24O ? bedModel.patient.electiv24O : null;
      bed.patient.vuxenPatient = bedModel.patient.vuxenPatient ? bedModel.patient.vuxenPatient : null;
      bed.patient.patientExaminations = bedModel.patient.patientExaminations ? this.filterExams(bedModel.patient.patientExaminations) : null;
      bed.patient.sekretess = bedModel.patient.sekretessGroup.sekretess ? bedModel.patient.sekretessGroup.sekretess : null;
      bed.patient.sekretessInfo = bedModel.patient.sekretessGroup.sekretessInfo ? bedModel.patient.sekretessGroup.sekretessInfo : null;
      bed.patient.infekterad = bedModel.patient.infekterad ? bedModel.patient.infekterad : null;
      bed.patient.infectionSensitive = bedModel.patient.infectionSensitive ? bedModel.patient.infectionSensitive : null;
      bed.patient.smitta = bedModel.patient.smittaGroup.smitta ? bedModel.patient.smittaGroup.smitta : null;
      bed.patient.smittaInfo = bedModel.patient.smittaGroup.smittaInfo ? bedModel.patient.smittaGroup.smittaInfo : null;
      bed.patient.pal = bedModel.patient.pal ? bedModel.patient.pal : null;
      bed.patient.patientEvents = bedModel.patient.patientEvents ? this.filterEvents(bedModel.patient.patientEvents) : null;
      bed.patient.morRond = bedModel.patient.morRond ? bedModel.patient.morRond : null;
      bed.patient.barnRond = bedModel.patient.barnRond ? bedModel.patient.barnRond : null;
      bed.patient.rond = bedModel.patient.rond ? bedModel.patient.rond : null;
      bed.patient.amning = bedModel.patient.amning ? bedModel.patient.amning : null;
      bed.patient.information = bedModel.patient.infoGroup.information ? bedModel.patient.infoGroup.information : null;
      bed.patient.kommentar = bedModel.patient.infoGroup.kommentar ? bedModel.patient.infoGroup.kommentar : null;
      bed.patient.dietMother = bedModel.patient.kostMorGroup.dietMother ? bedModel.patient.kostMorGroup.dietMother : null;
      if (bedModel.patient.kostMorGroup.dietMother) {
        bed.patient.dietMother = this.unit.dietForMothers.find(diet => diet.id == bedModel.patient.kostMorGroup.dietMother)
      }
      bed.patient.infoDietMother = bedModel.patient.kostMorGroup.infoDietMother ? bedModel.patient.kostMorGroup.infoDietMother : null;

      if (bedModel.patient.kostBarnGroup.dietChild) {
        bed.patient.dietChild = this.unit.dietForChildren.find(diet => diet.id == bedModel.patient.kostBarnGroup.dietChild)
      }
      bed.patient.infoDietChild = bedModel.patient.kostBarnGroup.infoDietChild ? bedModel.patient.kostBarnGroup.infoDietChild : null;

      if (bedModel.patient.kostGroup.diet) {
        bed.patient.diet = this.unit.dietForPatients.find(diet => diet.id == bedModel.patient.kostGroup.diet)
      }
      bed.patient.infoDiet = bedModel.patient.kostGroup.infoDiet ? bedModel.patient.kostGroup.infoDiet : null;
      bed.patient.careBurdenChoices = bedModel.patient.careBurdenChoices ? bedModel.patient.careBurdenChoices.map(
        cbc => {
          return {
            id: cbc.id,
            careBurdenCategory: cbc.careBurdenCategory,
            careBurdenValue: this.unit.careBurdenValues.find(careBurdenValue => careBurdenValue.id === cbc.careBurdenValueId)
          }
        }) : null;

    } else {
      bed.patient = null;
    }

    if (bedModel.ssk) {
      bed.ssk = this.unit.ssks.find(ssk => ssk.id === bedModel.ssk);
    }

    if (bedModel.waitingforbedGroup.servingKlinik) {
      bed.servingClinic = this.unit.servingClinics.find(klinik => klinik.id === bedModel.waitingforbedGroup.servingKlinik);
    }
    if (bedModel.cleaningGroup.cleaningalternativeExists) {
      bed.cleaningalternativeExists = bedModel.cleaningGroup.cleaningalternativeExists;
      bed.cleaningAlternative = this.unit.cleaningAlternatives.find(cg => cg.id === bedModel.cleaningGroup.cleaningAlternative);
      bed.cleaningInfo = bedModel.cleaningGroup.cleaningInfo;
    }
    else {
      bed.cleaningalternativeExists = false;
    }
    bed.patientWaits = bedModel.waitingforbedGroup.waitingpatient;
    bed.relatedInformation = bedModel.relatedInformation;

    this.http.put('/api/bed/' + this.clinicId + '/' + this.unit.id, bed)
      .subscribe(bed => {
        // this.ngOnInit();
        this.saveEvent.emit();
      });
  }

  executeHasLeft() {
    let bed = JSON.parse(JSON.stringify(this.bed)); // Clone
    bed.unit = this.unit;

    this.http.post('/api/bed/patientHasLeft', bed)
      .subscribe(_ => {
        this.saveEvent.emit();
      });
  }

  cancel() {
    this.collapseEvent.emit();
  }

  openDeleteModal(bed: Bed) {
    this.openDeleteModalEvent.emit();
  }

  private buildExaminationGroup(examinations: Patientexamination[]): FormGroup[] {
    if (!examinations || examinations.length === 0) {
      return [];
    }
    return examinations.map(examinationklinik => {
      return this.formBuilder.group({
        id: examinationklinik.id,
        examination: examinationklinik.examination,
        examinationDate: examinationklinik.examinationDate,
        examinationtime: examinationklinik.examinationtime
      })
    });
  }

  CreateSPatientExamination(): FormGroup {
    return this.formBuilder.group({
      id: null,
      examination: null,
      examinationDate: null,
      examinationtime: null
    });
  }

  deleteExamination(index: number) {
    this.patientExaminations.removeAt(index);
  }

  get patientExaminations(): FormArray {
    return <FormArray>this.bedForm.get('patient.patientExaminations');
  }


  addExamination() {
    this.patientExaminations.push(this.CreateSPatientExamination());
  }

  private filterExams(src: Patientexamination[]) {
    return src.filter(exam => exam.examination != null && exam.examinationDate != null)
  }

  private buildEventGroup(patientEvents: PatientEvent[]): FormGroup[] {
    if (!patientEvents || patientEvents.length === 0) {
      return [];
    }
    return patientEvents.map(patientevent => {
      return this.formBuilder.group({
        id: patientevent.id,
        event: patientevent.event,
        eventDate: patientevent.eventDate,
        eventTime: patientevent.eventTime,
        eventInfo: patientevent.eventInfo
      })
    });
  }

  CreateSPatientEvent(): FormGroup {
    return this.formBuilder.group({
      id: null,
      event: null,
      eventDate: null,
      eventTime: null,
      eventInfo: null
    });
  }

  deletePatientEvent(index: number) {
    this.patientEvents.removeAt(index);
  }

  get patientEvents(): FormArray {
    return <FormArray>this.bedForm.get('patient.patientEvents');
  }


  addPatientEvent() {
    this.patientEvents.push(this.CreateSPatientEvent());
  }

  private filterEvents(src: PatientEvent[]) {
    return src.filter(event => event.event != null && event.eventDate != null)
  }

  deleteDate() {
    this.bedForm.get('patient.plannedLeaveDate').setValue(null);
  }

  deleteGickHemDate() {
    this.bedForm.get('patient.leftDate').setValue(null);
  }

  private buildCareBurdenChoiceGroup(patient: Patient): FormGroup[] {
    if (!patient) return [];
    return this.unit.careBurdenCategories.map(category => {
      let careBurdenChoice = this.getChoiceFromPatient(patient, category.id);
      return this.formBuilder.group({
        id: careBurdenChoice ? careBurdenChoice.id : null,
        careBurdenCategory: category,
        careBurdenValueId: careBurdenChoice && careBurdenChoice.careBurdenValue ? careBurdenChoice.careBurdenValue.id : null
      })
    });
  }

  getChoiceFromPatient(patient: Patient, categoryId: number): CareBurdenChoice {
    return patient.careBurdenChoices.find(cbc => cbc.careBurdenCategory.id === categoryId);
  }

}
