import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDispatch, Dispatch } from '../dispatch.model';
import { DispatchService } from '../service/dispatch.service';
import { ICar } from 'app/entities/car/car.model';
import { CarService } from 'app/entities/car/service/car.service';
import { ISupplier } from 'app/entities/supplier/supplier.model';
import { SupplierService } from 'app/entities/supplier/service/supplier.service';

@Component({
  selector: 'jhi-dispatch-update',
  templateUrl: './dispatch-update.component.html',
})
export class DispatchUpdateComponent implements OnInit {
  isSaving = false;

  carsSharedCollection: ICar[] = [];
  suppliersSharedCollection: ISupplier[] = [];

  editForm = this.fb.group({
    id: [],
    dispatchDate: [],
    item: [],
    quantity: [],
    car: [],
    supplier: [],
  });

  constructor(
    protected dispatchService: DispatchService,
    protected carService: CarService,
    protected supplierService: SupplierService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dispatch }) => {
      this.updateForm(dispatch);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dispatch = this.createFromForm();
    if (dispatch.id !== undefined) {
      this.subscribeToSaveResponse(this.dispatchService.update(dispatch));
    } else {
      this.subscribeToSaveResponse(this.dispatchService.create(dispatch));
    }
  }

  trackCarById(index: number, item: ICar): number {
    return item.id!;
  }

  trackSupplierById(index: number, item: ISupplier): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDispatch>>): void {
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

  protected updateForm(dispatch: IDispatch): void {
    this.editForm.patchValue({
      id: dispatch.id,
      dispatchDate: dispatch.dispatchDate,
      item: dispatch.item,
      quantity: dispatch.quantity,
      car: dispatch.car,
      supplier: dispatch.supplier,
    });

    this.carsSharedCollection = this.carService.addCarToCollectionIfMissing(this.carsSharedCollection, dispatch.car);
    this.suppliersSharedCollection = this.supplierService.addSupplierToCollectionIfMissing(
      this.suppliersSharedCollection,
      dispatch.supplier
    );
  }

  protected loadRelationshipsOptions(): void {
    this.carService
      .query()
      .pipe(map((res: HttpResponse<ICar[]>) => res.body ?? []))
      .pipe(map((cars: ICar[]) => this.carService.addCarToCollectionIfMissing(cars, this.editForm.get('car')!.value)))
      .subscribe((cars: ICar[]) => (this.carsSharedCollection = cars));

    this.supplierService
      .query()
      .pipe(map((res: HttpResponse<ISupplier[]>) => res.body ?? []))
      .pipe(
        map((suppliers: ISupplier[]) =>
          this.supplierService.addSupplierToCollectionIfMissing(suppliers, this.editForm.get('supplier')!.value)
        )
      )
      .subscribe((suppliers: ISupplier[]) => (this.suppliersSharedCollection = suppliers));
  }

  protected createFromForm(): IDispatch {
    return {
      ...new Dispatch(),
      id: this.editForm.get(['id'])!.value,
      dispatchDate: this.editForm.get(['dispatchDate'])!.value,
      item: this.editForm.get(['item'])!.value,
      quantity: this.editForm.get(['quantity'])!.value,
      car: this.editForm.get(['car'])!.value,
      supplier: this.editForm.get(['supplier'])!.value,
    };
  }
}
