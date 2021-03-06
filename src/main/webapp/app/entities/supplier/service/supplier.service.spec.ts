import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISupplier, Supplier } from '../supplier.model';

import { SupplierService } from './supplier.service';

describe('Supplier Service', () => {
  let service: SupplierService;
  let httpMock: HttpTestingController;
  let elemDefault: ISupplier;
  let expectedResult: ISupplier | ISupplier[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SupplierService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      supplierdate: currentDate,
      quantity: 0,
      item: 'AAAAAAA',
      note: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          supplierdate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Supplier', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          supplierdate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          supplierdate: currentDate,
        },
        returnedFromService
      );

      service.create(new Supplier()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Supplier', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          supplierdate: currentDate.format(DATE_FORMAT),
          quantity: 1,
          item: 'BBBBBB',
          note: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          supplierdate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Supplier', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          supplierdate: currentDate.format(DATE_FORMAT),
          note: 'BBBBBB',
        },
        new Supplier()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          supplierdate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Supplier', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          supplierdate: currentDate.format(DATE_FORMAT),
          quantity: 1,
          item: 'BBBBBB',
          note: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          supplierdate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Supplier', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSupplierToCollectionIfMissing', () => {
      it('should add a Supplier to an empty array', () => {
        const supplier: ISupplier = { id: 123 };
        expectedResult = service.addSupplierToCollectionIfMissing([], supplier);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(supplier);
      });

      it('should not add a Supplier to an array that contains it', () => {
        const supplier: ISupplier = { id: 123 };
        const supplierCollection: ISupplier[] = [
          {
            ...supplier,
          },
          { id: 456 },
        ];
        expectedResult = service.addSupplierToCollectionIfMissing(supplierCollection, supplier);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Supplier to an array that doesn't contain it", () => {
        const supplier: ISupplier = { id: 123 };
        const supplierCollection: ISupplier[] = [{ id: 456 }];
        expectedResult = service.addSupplierToCollectionIfMissing(supplierCollection, supplier);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(supplier);
      });

      it('should add only unique Supplier to an array', () => {
        const supplierArray: ISupplier[] = [{ id: 123 }, { id: 456 }, { id: 16918 }];
        const supplierCollection: ISupplier[] = [{ id: 123 }];
        expectedResult = service.addSupplierToCollectionIfMissing(supplierCollection, ...supplierArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const supplier: ISupplier = { id: 123 };
        const supplier2: ISupplier = { id: 456 };
        expectedResult = service.addSupplierToCollectionIfMissing([], supplier, supplier2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(supplier);
        expect(expectedResult).toContain(supplier2);
      });

      it('should accept null and undefined values', () => {
        const supplier: ISupplier = { id: 123 };
        expectedResult = service.addSupplierToCollectionIfMissing([], null, supplier, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(supplier);
      });

      it('should return initial array if no Supplier is added', () => {
        const supplierCollection: ISupplier[] = [{ id: 123 }];
        expectedResult = service.addSupplierToCollectionIfMissing(supplierCollection, undefined, null);
        expect(expectedResult).toEqual(supplierCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
