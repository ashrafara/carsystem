import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IFailure, Failure } from '../failure.model';

import { FailureService } from './failure.service';

describe('Failure Service', () => {
  let service: FailureService;
  let httpMock: HttpTestingController;
  let elemDefault: IFailure;
  let expectedResult: IFailure | IFailure[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FailureService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      failureDate: currentDate,
      carGuagefrom: 0,
      carGuageTo: 0,
      changepart: 'AAAAAAA',
      garageName: 'AAAAAAA',
      price: 0,
      inovice1: 'AAAAAAA',
      inovice2: 'AAAAAAA',
      inovice3: 'AAAAAAA',
      inovice4: 'AAAAAAA',
      note: 'AAAAAAA',
      imageContentType: 'image/png',
      image: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          failureDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Failure', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          failureDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          failureDate: currentDate,
        },
        returnedFromService
      );

      service.create(new Failure()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Failure', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          failureDate: currentDate.format(DATE_FORMAT),
          carGuagefrom: 1,
          carGuageTo: 1,
          changepart: 'BBBBBB',
          garageName: 'BBBBBB',
          price: 1,
          inovice1: 'BBBBBB',
          inovice2: 'BBBBBB',
          inovice3: 'BBBBBB',
          inovice4: 'BBBBBB',
          note: 'BBBBBB',
          image: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          failureDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Failure', () => {
      const patchObject = Object.assign(
        {
          failureDate: currentDate.format(DATE_FORMAT),
          carGuagefrom: 1,
          carGuageTo: 1,
          garageName: 'BBBBBB',
          price: 1,
          inovice1: 'BBBBBB',
          inovice2: 'BBBBBB',
          inovice3: 'BBBBBB',
          inovice4: 'BBBBBB',
          image: 'BBBBBB',
        },
        new Failure()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          failureDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Failure', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          failureDate: currentDate.format(DATE_FORMAT),
          carGuagefrom: 1,
          carGuageTo: 1,
          changepart: 'BBBBBB',
          garageName: 'BBBBBB',
          price: 1,
          inovice1: 'BBBBBB',
          inovice2: 'BBBBBB',
          inovice3: 'BBBBBB',
          inovice4: 'BBBBBB',
          note: 'BBBBBB',
          image: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          failureDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Failure', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFailureToCollectionIfMissing', () => {
      it('should add a Failure to an empty array', () => {
        const failure: IFailure = { id: 123 };
        expectedResult = service.addFailureToCollectionIfMissing([], failure);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(failure);
      });

      it('should not add a Failure to an array that contains it', () => {
        const failure: IFailure = { id: 123 };
        const failureCollection: IFailure[] = [
          {
            ...failure,
          },
          { id: 456 },
        ];
        expectedResult = service.addFailureToCollectionIfMissing(failureCollection, failure);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Failure to an array that doesn't contain it", () => {
        const failure: IFailure = { id: 123 };
        const failureCollection: IFailure[] = [{ id: 456 }];
        expectedResult = service.addFailureToCollectionIfMissing(failureCollection, failure);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(failure);
      });

      it('should add only unique Failure to an array', () => {
        const failureArray: IFailure[] = [{ id: 123 }, { id: 456 }, { id: 4445 }];
        const failureCollection: IFailure[] = [{ id: 123 }];
        expectedResult = service.addFailureToCollectionIfMissing(failureCollection, ...failureArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const failure: IFailure = { id: 123 };
        const failure2: IFailure = { id: 456 };
        expectedResult = service.addFailureToCollectionIfMissing([], failure, failure2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(failure);
        expect(expectedResult).toContain(failure2);
      });

      it('should accept null and undefined values', () => {
        const failure: IFailure = { id: 123 };
        expectedResult = service.addFailureToCollectionIfMissing([], null, failure, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(failure);
      });

      it('should return initial array if no Failure is added', () => {
        const failureCollection: IFailure[] = [{ id: 123 }];
        expectedResult = service.addFailureToCollectionIfMissing(failureCollection, undefined, null);
        expect(expectedResult).toEqual(failureCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
