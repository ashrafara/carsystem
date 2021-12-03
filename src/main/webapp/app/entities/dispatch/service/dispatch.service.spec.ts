import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IDispatch, Dispatch } from '../dispatch.model';

import { DispatchService } from './dispatch.service';

describe('Dispatch Service', () => {
  let service: DispatchService;
  let httpMock: HttpTestingController;
  let elemDefault: IDispatch;
  let expectedResult: IDispatch | IDispatch[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DispatchService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      dispatchDate: currentDate,
      item: 'AAAAAAA',
      quantity: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dispatchDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Dispatch', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dispatchDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dispatchDate: currentDate,
        },
        returnedFromService
      );

      service.create(new Dispatch()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Dispatch', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          dispatchDate: currentDate.format(DATE_FORMAT),
          item: 'BBBBBB',
          quantity: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dispatchDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Dispatch', () => {
      const patchObject = Object.assign(
        {
          item: 'BBBBBB',
          quantity: 'BBBBBB',
        },
        new Dispatch()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dispatchDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Dispatch', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          dispatchDate: currentDate.format(DATE_FORMAT),
          item: 'BBBBBB',
          quantity: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dispatchDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Dispatch', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDispatchToCollectionIfMissing', () => {
      it('should add a Dispatch to an empty array', () => {
        const dispatch: IDispatch = { id: 123 };
        expectedResult = service.addDispatchToCollectionIfMissing([], dispatch);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dispatch);
      });

      it('should not add a Dispatch to an array that contains it', () => {
        const dispatch: IDispatch = { id: 123 };
        const dispatchCollection: IDispatch[] = [
          {
            ...dispatch,
          },
          { id: 456 },
        ];
        expectedResult = service.addDispatchToCollectionIfMissing(dispatchCollection, dispatch);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Dispatch to an array that doesn't contain it", () => {
        const dispatch: IDispatch = { id: 123 };
        const dispatchCollection: IDispatch[] = [{ id: 456 }];
        expectedResult = service.addDispatchToCollectionIfMissing(dispatchCollection, dispatch);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dispatch);
      });

      it('should add only unique Dispatch to an array', () => {
        const dispatchArray: IDispatch[] = [{ id: 123 }, { id: 456 }, { id: 67464 }];
        const dispatchCollection: IDispatch[] = [{ id: 123 }];
        expectedResult = service.addDispatchToCollectionIfMissing(dispatchCollection, ...dispatchArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const dispatch: IDispatch = { id: 123 };
        const dispatch2: IDispatch = { id: 456 };
        expectedResult = service.addDispatchToCollectionIfMissing([], dispatch, dispatch2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dispatch);
        expect(expectedResult).toContain(dispatch2);
      });

      it('should accept null and undefined values', () => {
        const dispatch: IDispatch = { id: 123 };
        expectedResult = service.addDispatchToCollectionIfMissing([], null, dispatch, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dispatch);
      });

      it('should return initial array if no Dispatch is added', () => {
        const dispatchCollection: IDispatch[] = [{ id: 123 }];
        expectedResult = service.addDispatchToCollectionIfMissing(dispatchCollection, undefined, null);
        expect(expectedResult).toEqual(dispatchCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
