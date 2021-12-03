import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEmployee, Employee } from '../employee.model';
import { EmployeeService } from '../service/employee.service';
import { ICar } from 'app/entities/car/car.model';
import { CarService } from 'app/entities/car/service/car.service';

@Component({
  selector: 'jhi-employee-update',
  templateUrl: './employee-update.component.html',
})
export class EmployeeUpdateComponent implements OnInit {
  isSaving = false;

  carsCollection: ICar[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    position: [],
    phone: [],
    note: [],
    car: [],
  });

  constructor(
    protected employeeService: EmployeeService,
    protected carService: CarService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employee }) => {
      this.updateForm(employee);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employee = this.createFromForm();
    if (employee.id !== undefined) {
      this.subscribeToSaveResponse(this.employeeService.update(employee));
    } else {
      this.subscribeToSaveResponse(this.employeeService.create(employee));
    }
  }

  trackCarById(index: number, item: ICar): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployee>>): void {
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

  protected updateForm(employee: IEmployee): void {
    this.editForm.patchValue({
      id: employee.id,
      name: employee.name,
      position: employee.position,
      phone: employee.phone,
      note: employee.note,
      car: employee.car,
    });

    this.carsCollection = this.carService.addCarToCollectionIfMissing(this.carsCollection, employee.car);
  }

  protected loadRelationshipsOptions(): void {
    this.carService
      .query({ 'employeeId.specified': 'false' })
      .pipe(map((res: HttpResponse<ICar[]>) => res.body ?? []))
      .pipe(map((cars: ICar[]) => this.carService.addCarToCollectionIfMissing(cars, this.editForm.get('car')!.value)))
      .subscribe((cars: ICar[]) => (this.carsCollection = cars));
  }

  protected createFromForm(): IEmployee {
    return {
      ...new Employee(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      position: this.editForm.get(['position'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      note: this.editForm.get(['note'])!.value,
      car: this.editForm.get(['car'])!.value,
    };
  }
}
