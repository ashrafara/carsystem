import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFailure, Failure } from '../failure.model';
import { FailureService } from '../service/failure.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ICar } from 'app/entities/car/car.model';
import { CarService } from 'app/entities/car/service/car.service';

@Component({
  selector: 'jhi-failure-update',
  templateUrl: './failure-update.component.html',
})
export class FailureUpdateComponent implements OnInit {
  isSaving = false;

  carsSharedCollection: ICar[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    failureDate: [],
    carGuagefrom: [],
    carGuageTo: [],
    changepart: [],
    garageName: [],
    price: [],
    inovice1: [],
    inovice2: [],
    inovice3: [],
    inovice4: [],
    note: [],
    image: [],
    imageContentType: [],
    car: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected failureService: FailureService,
    protected carService: CarService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ failure }) => {
      this.updateForm(failure);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('carsystemApp.error', { message: err.message })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const failure = this.createFromForm();
    if (failure.id !== undefined) {
      this.subscribeToSaveResponse(this.failureService.update(failure));
    } else {
      this.subscribeToSaveResponse(this.failureService.create(failure));
    }
  }

  trackCarById(index: number, item: ICar): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFailure>>): void {
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

  protected updateForm(failure: IFailure): void {
    this.editForm.patchValue({
      id: failure.id,
      name: failure.name,
      failureDate: failure.failureDate,
      carGuagefrom: failure.carGuagefrom,
      carGuageTo: failure.carGuageTo,
      changepart: failure.changepart,
      garageName: failure.garageName,
      price: failure.price,
      inovice1: failure.inovice1,
      inovice2: failure.inovice2,
      inovice3: failure.inovice3,
      inovice4: failure.inovice4,
      note: failure.note,
      image: failure.image,
      imageContentType: failure.imageContentType,
      car: failure.car,
    });

    this.carsSharedCollection = this.carService.addCarToCollectionIfMissing(this.carsSharedCollection, failure.car);
  }

  protected loadRelationshipsOptions(): void {
    this.carService
      .query()
      .pipe(map((res: HttpResponse<ICar[]>) => res.body ?? []))
      .pipe(map((cars: ICar[]) => this.carService.addCarToCollectionIfMissing(cars, this.editForm.get('car')!.value)))
      .subscribe((cars: ICar[]) => (this.carsSharedCollection = cars));
  }

  protected createFromForm(): IFailure {
    return {
      ...new Failure(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      failureDate: this.editForm.get(['failureDate'])!.value,
      carGuagefrom: this.editForm.get(['carGuagefrom'])!.value,
      carGuageTo: this.editForm.get(['carGuageTo'])!.value,
      changepart: this.editForm.get(['changepart'])!.value,
      garageName: this.editForm.get(['garageName'])!.value,
      price: this.editForm.get(['price'])!.value,
      inovice1: this.editForm.get(['inovice1'])!.value,
      inovice2: this.editForm.get(['inovice2'])!.value,
      inovice3: this.editForm.get(['inovice3'])!.value,
      inovice4: this.editForm.get(['inovice4'])!.value,
      note: this.editForm.get(['note'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      car: this.editForm.get(['car'])!.value,
    };
  }
}
