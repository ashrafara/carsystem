jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFailure, Failure } from '../failure.model';
import { FailureService } from '../service/failure.service';

import { FailureRoutingResolveService } from './failure-routing-resolve.service';

describe('Failure routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: FailureRoutingResolveService;
  let service: FailureService;
  let resultFailure: IFailure | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(FailureRoutingResolveService);
    service = TestBed.inject(FailureService);
    resultFailure = undefined;
  });

  describe('resolve', () => {
    it('should return IFailure returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFailure = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFailure).toEqual({ id: 123 });
    });

    it('should return new IFailure if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFailure = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultFailure).toEqual(new Failure());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Failure })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFailure = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFailure).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
