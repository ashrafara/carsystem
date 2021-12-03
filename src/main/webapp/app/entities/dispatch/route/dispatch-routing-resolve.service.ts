import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDispatch, Dispatch } from '../dispatch.model';
import { DispatchService } from '../service/dispatch.service';

@Injectable({ providedIn: 'root' })
export class DispatchRoutingResolveService implements Resolve<IDispatch> {
  constructor(protected service: DispatchService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDispatch> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dispatch: HttpResponse<Dispatch>) => {
          if (dispatch.body) {
            return of(dispatch.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Dispatch());
  }
}
