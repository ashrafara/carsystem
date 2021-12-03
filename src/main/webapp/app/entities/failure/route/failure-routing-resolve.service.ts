import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFailure, Failure } from '../failure.model';
import { FailureService } from '../service/failure.service';

@Injectable({ providedIn: 'root' })
export class FailureRoutingResolveService implements Resolve<IFailure> {
  constructor(protected service: FailureService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFailure> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((failure: HttpResponse<Failure>) => {
          if (failure.body) {
            return of(failure.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Failure());
  }
}
