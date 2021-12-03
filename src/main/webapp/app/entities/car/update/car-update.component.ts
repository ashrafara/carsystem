import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICar, Car } from '../car.model';
import { CarService } from '../service/car.service';

@Component({
  selector: 'jhi-car-update',
  templateUrl: './car-update.component.html',
})
export class CarUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    carIssue: [],
    carnNumber: [],
    carMotor: [],
    carShellNumber: [],
    classification: [],
    madein: [],
    panaelnumber: [],
    notes: [],
  });

  constructor(protected carService: CarService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ car }) => {
      this.updateForm(car);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const car = this.createFromForm();
    if (car.id !== undefined) {
      this.subscribeToSaveResponse(this.carService.update(car));
    } else {
      this.subscribeToSaveResponse(this.carService.create(car));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICar>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(car: ICar): void {
    this.editForm.patchValue({
      id: car.id,
      name: car.name,
      carIssue: car.carIssue,
      carnNumber: car.carnNumber,
      carMotor: car.carMotor,
      carShellNumber: car.carShellNumber,
      classification: car.classification,
      madein: car.madein,
      panaelnumber: car.panaelnumber,
      notes: car.notes,
    });
  }

  protected createFromForm(): ICar {
    return {
      ...new Car(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      carIssue: this.editForm.get(['carIssue'])!.value,
      carnNumber: this.editForm.get(['carnNumber'])!.value,
      carMotor: this.editForm.get(['carMotor'])!.value,
      carShellNumber: this.editForm.get(['carShellNumber'])!.value,
      classification: this.editForm.get(['classification'])!.value,
      madein: this.editForm.get(['madein'])!.value,
      panaelnumber: this.editForm.get(['panaelnumber'])!.value,
      notes: this.editForm.get(['notes'])!.value,
    };
  }
}
