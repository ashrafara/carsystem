import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ISupplier, Supplier } from '../supplier.model';
import { SupplierService } from '../service/supplier.service';

@Component({
  selector: 'jhi-supplier-update',
  templateUrl: './supplier-update.component.html',
})
export class SupplierUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    supplierdate: [],
    quantity: [],
    item: [],
    note: [],
  });

  constructor(protected supplierService: SupplierService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ supplier }) => {
      this.updateForm(supplier);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const supplier = this.createFromForm();
    if (supplier.id !== undefined) {
      this.subscribeToSaveResponse(this.supplierService.update(supplier));
    } else {
      this.subscribeToSaveResponse(this.supplierService.create(supplier));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISupplier>>): void {
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

  protected updateForm(supplier: ISupplier): void {
    this.editForm.patchValue({
      id: supplier.id,
      name: supplier.name,
      supplierdate: supplier.supplierdate,
      quantity: supplier.quantity,
      item: supplier.item,
      note: supplier.note,
    });
  }

  protected createFromForm(): ISupplier {
    return {
      ...new Supplier(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      supplierdate: this.editForm.get(['supplierdate'])!.value,
      quantity: this.editForm.get(['quantity'])!.value,
      item: this.editForm.get(['item'])!.value,
      note: this.editForm.get(['note'])!.value,
    };
  }
}
