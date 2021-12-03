import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISupplier, getSupplierIdentifier } from '../supplier.model';

export type EntityResponseType = HttpResponse<ISupplier>;
export type EntityArrayResponseType = HttpResponse<ISupplier[]>;

@Injectable({ providedIn: 'root' })
export class SupplierService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/suppliers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(supplier: ISupplier): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(supplier);
    return this.http
      .post<ISupplier>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(supplier: ISupplier): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(supplier);
    return this.http
      .put<ISupplier>(`${this.resourceUrl}/${getSupplierIdentifier(supplier) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(supplier: ISupplier): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(supplier);
    return this.http
      .patch<ISupplier>(`${this.resourceUrl}/${getSupplierIdentifier(supplier) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISupplier>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISupplier[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSupplierToCollectionIfMissing(supplierCollection: ISupplier[], ...suppliersToCheck: (ISupplier | null | undefined)[]): ISupplier[] {
    const suppliers: ISupplier[] = suppliersToCheck.filter(isPresent);
    if (suppliers.length > 0) {
      const supplierCollectionIdentifiers = supplierCollection.map(supplierItem => getSupplierIdentifier(supplierItem)!);
      const suppliersToAdd = suppliers.filter(supplierItem => {
        const supplierIdentifier = getSupplierIdentifier(supplierItem);
        if (supplierIdentifier == null || supplierCollectionIdentifiers.includes(supplierIdentifier)) {
          return false;
        }
        supplierCollectionIdentifiers.push(supplierIdentifier);
        return true;
      });
      return [...suppliersToAdd, ...supplierCollection];
    }
    return supplierCollection;
  }

  protected convertDateFromClient(supplier: ISupplier): ISupplier {
    return Object.assign({}, supplier, {
      supplierdate: supplier.supplierdate?.isValid() ? supplier.supplierdate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.supplierdate = res.body.supplierdate ? dayjs(res.body.supplierdate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((supplier: ISupplier) => {
        supplier.supplierdate = supplier.supplierdate ? dayjs(supplier.supplierdate) : undefined;
      });
    }
    return res;
  }
}
