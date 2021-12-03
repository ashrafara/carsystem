import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDispatch, getDispatchIdentifier } from '../dispatch.model';

export type EntityResponseType = HttpResponse<IDispatch>;
export type EntityArrayResponseType = HttpResponse<IDispatch[]>;

@Injectable({ providedIn: 'root' })
export class DispatchService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/dispatches');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(dispatch: IDispatch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dispatch);
    return this.http
      .post<IDispatch>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(dispatch: IDispatch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dispatch);
    return this.http
      .put<IDispatch>(`${this.resourceUrl}/${getDispatchIdentifier(dispatch) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(dispatch: IDispatch): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dispatch);
    return this.http
      .patch<IDispatch>(`${this.resourceUrl}/${getDispatchIdentifier(dispatch) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDispatch>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDispatch[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDispatchToCollectionIfMissing(dispatchCollection: IDispatch[], ...dispatchesToCheck: (IDispatch | null | undefined)[]): IDispatch[] {
    const dispatches: IDispatch[] = dispatchesToCheck.filter(isPresent);
    if (dispatches.length > 0) {
      const dispatchCollectionIdentifiers = dispatchCollection.map(dispatchItem => getDispatchIdentifier(dispatchItem)!);
      const dispatchesToAdd = dispatches.filter(dispatchItem => {
        const dispatchIdentifier = getDispatchIdentifier(dispatchItem);
        if (dispatchIdentifier == null || dispatchCollectionIdentifiers.includes(dispatchIdentifier)) {
          return false;
        }
        dispatchCollectionIdentifiers.push(dispatchIdentifier);
        return true;
      });
      return [...dispatchesToAdd, ...dispatchCollection];
    }
    return dispatchCollection;
  }

  protected convertDateFromClient(dispatch: IDispatch): IDispatch {
    return Object.assign({}, dispatch, {
      dispatchDate: dispatch.dispatchDate?.isValid() ? dispatch.dispatchDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dispatchDate = res.body.dispatchDate ? dayjs(res.body.dispatchDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((dispatch: IDispatch) => {
        dispatch.dispatchDate = dispatch.dispatchDate ? dayjs(dispatch.dispatchDate) : undefined;
      });
    }
    return res;
  }
}
