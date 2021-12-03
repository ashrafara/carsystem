jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DispatchService } from '../service/dispatch.service';
import { IDispatch, Dispatch } from '../dispatch.model';
import { ICar } from 'app/entities/car/car.model';
import { CarService } from 'app/entities/car/service/car.service';
import { ISupplier } from 'app/entities/supplier/supplier.model';
import { SupplierService } from 'app/entities/supplier/service/supplier.service';

import { DispatchUpdateComponent } from './dispatch-update.component';

describe('Dispatch Management Update Component', () => {
  let comp: DispatchUpdateComponent;
  let fixture: ComponentFixture<DispatchUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let dispatchService: DispatchService;
  let carService: CarService;
  let supplierService: SupplierService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DispatchUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(DispatchUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DispatchUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dispatchService = TestBed.inject(DispatchService);
    carService = TestBed.inject(CarService);
    supplierService = TestBed.inject(SupplierService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Car query and add missing value', () => {
      const dispatch: IDispatch = { id: 456 };
      const car: ICar = { id: 71330 };
      dispatch.car = car;

      const carCollection: ICar[] = [{ id: 51649 }];
      jest.spyOn(carService, 'query').mockReturnValue(of(new HttpResponse({ body: carCollection })));
      const additionalCars = [car];
      const expectedCollection: ICar[] = [...additionalCars, ...carCollection];
      jest.spyOn(carService, 'addCarToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ dispatch });
      comp.ngOnInit();

      expect(carService.query).toHaveBeenCalled();
      expect(carService.addCarToCollectionIfMissing).toHaveBeenCalledWith(carCollection, ...additionalCars);
      expect(comp.carsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Supplier query and add missing value', () => {
      const dispatch: IDispatch = { id: 456 };
      const supplier: ISupplier = { id: 6937 };
      dispatch.supplier = supplier;

      const supplierCollection: ISupplier[] = [{ id: 25169 }];
      jest.spyOn(supplierService, 'query').mockReturnValue(of(new HttpResponse({ body: supplierCollection })));
      const additionalSuppliers = [supplier];
      const expectedCollection: ISupplier[] = [...additionalSuppliers, ...supplierCollection];
      jest.spyOn(supplierService, 'addSupplierToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ dispatch });
      comp.ngOnInit();

      expect(supplierService.query).toHaveBeenCalled();
      expect(supplierService.addSupplierToCollectionIfMissing).toHaveBeenCalledWith(supplierCollection, ...additionalSuppliers);
      expect(comp.suppliersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const dispatch: IDispatch = { id: 456 };
      const car: ICar = { id: 96054 };
      dispatch.car = car;
      const supplier: ISupplier = { id: 11336 };
      dispatch.supplier = supplier;

      activatedRoute.data = of({ dispatch });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(dispatch));
      expect(comp.carsSharedCollection).toContain(car);
      expect(comp.suppliersSharedCollection).toContain(supplier);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Dispatch>>();
      const dispatch = { id: 123 };
      jest.spyOn(dispatchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dispatch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dispatch }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(dispatchService.update).toHaveBeenCalledWith(dispatch);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Dispatch>>();
      const dispatch = new Dispatch();
      jest.spyOn(dispatchService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dispatch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dispatch }));
      saveSubject.complete();

      // THEN
      expect(dispatchService.create).toHaveBeenCalledWith(dispatch);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Dispatch>>();
      const dispatch = { id: 123 };
      jest.spyOn(dispatchService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dispatch });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dispatchService.update).toHaveBeenCalledWith(dispatch);
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

    describe('trackSupplierById', () => {
      it('Should return tracked Supplier primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSupplierById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
