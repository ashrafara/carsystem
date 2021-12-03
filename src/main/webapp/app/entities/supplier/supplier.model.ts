import * as dayjs from 'dayjs';
import { IDispatch } from 'app/entities/dispatch/dispatch.model';

export interface ISupplier {
  id?: number;
  name?: string | null;
  supplierdate?: dayjs.Dayjs | null;
  quantity?: number | null;
  item?: string | null;
  note?: string | null;
  dispatches?: IDispatch[] | null;
}

export class Supplier implements ISupplier {
  constructor(
    public id?: number,
    public name?: string | null,
    public supplierdate?: dayjs.Dayjs | null,
    public quantity?: number | null,
    public item?: string | null,
    public note?: string | null,
    public dispatches?: IDispatch[] | null
  ) {}
}

export function getSupplierIdentifier(supplier: ISupplier): number | undefined {
  return supplier.id;
}
