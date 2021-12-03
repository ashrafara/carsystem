jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FailureService } from '../service/failure.service';
import { IFailure, Failure } from '../failure.model';
import { ICar } from 'app/entities/car/car.model';
import { CarService } from 'app/entities/car/service/car.service';

import { FailureUpdateComponent } from './failure-update.component';

describe('Failure Management Update Component', () => {
  let comp: FailureUpdateComponent;
  let fixture: ComponentFixture<FailureUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let failureService: FailureService;
  let carService: CarService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FailureUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(FailureUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FailureUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    failureService = TestBed.inject(FailureService);
    carService = TestBed.inject(CarService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Car query and add missing value', () => {
      const failure: IFailure = { id: 456 };
      const car: ICar = { id: 43428 };
      failure.car = car;

      const carCollection: ICar[] = [{ id: 17595 }];
      jest.spyOn(carService, 'query').mockReturnValue(of(new HttpResponse({ body: carCollection })));
      const additionalCars = [car];
      const expectedCollection: ICar[] = [...additionalCars, ...carCollection];
      jest.spyOn(carService, 'addCarToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ failure });
      comp.ngOnInit();

      expect(carService.query).toHaveBeenCalled();
      expect(carService.addCarToCollectionIfMissing).toHaveBeenCalledWith(carCollection, ...additionalCars);
      expect(comp.carsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const failure: IFailure = { id: 456 };
      const car: ICar = { id: 30380 };
      failure.car = car;

      activatedRoute.data = of({ failure });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(failure));
      expect(comp.carsSharedCollection).toContain(car);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Failure>>();
      const failure = { id: 123 };
      jest.spyOn(failureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ failure });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: failure }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(failureService.update).toHaveBeenCalledWith(failure);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Failure>>();
      const failure = new Failure();
      jest.spyOn(failureService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ failure });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: failure }));
      saveSubject.complete();

      // THEN
      expect(failureService.create).toHaveBeenCalledWith(failure);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Failure>>();
      const failure = { id: 123 };
      jest.spyOn(failureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ failure });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(failureService.update).toHaveBeenCalledWith(failure);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCarById', () => {
      it('Should return tracked Car primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCarById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
