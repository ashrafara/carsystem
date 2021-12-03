import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFailure, getFailureIdentifier } from '../failure.model';

export type EntityResponseType = HttpResponse<IFailure>;
export type EntityArrayResponseType = HttpResponse<IFailure[]>;

@Injectable({ providedIn: 'root' })
export class FailureService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/failures');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(failure: IFailure): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(failure);
    return this.http
      .post<IFailure>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(failure: IFailure): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(failure);
    return this.http
      .put<IFailure>(`${this.resourceUrl}/${getFailureIdentifier(failure) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(failure: IFailure): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(failure);
    return this.http
      .patch<IFailure>(`${this.resourceUrl}/${getFailureIdentifier(failure) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFailure>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFailure[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFailureToCollectionIfMissing(failureCollection: IFailure[], ...failuresToCheck: (IFailure | null | undefined)[]): IFailure[] {
    const failures: IFailure[] = failuresToCheck.filter(isPresent);
    if (failures.length > 0) {
      const failureCollectionIdentifiers = failureCollection.map(failureItem => getFailureIdentifier(failureItem)!);
      const failuresToAdd = failures.filter(failureItem => {
        const failureIdentifier = getFailureIdentifier(failureItem);
        if (failureIdentifier == null || failureCollectionIdentifiers.includes(failureIdentifier)) {
          return false;
        }
        failureCollectionIdentifiers.push(failureIdentifier);
        return true;
      });
      return [...failuresToAdd, ...failureCollection];
    }
    return failureCollection;
  }

  protected convertDateFromClient(failure: IFailure): IFailure {
    return Object.assign({}, failure, {
      failureDate: failure.failureDate?.isValid() ? failure.failureDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.failureDate = res.body.failureDate ? dayjs(res.body.failureDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((failure: IFailure) => {
        failure.failureDate = failure.failureDate ? dayjs(failure.failureDate) : undefined;
      });
    }
    return res;
  }
}
