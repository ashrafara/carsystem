import * as dayjs from 'dayjs';
import { ICar } from 'app/entities/car/car.model';
import { ISupplier } from 'app/entities/supplier/supplier.model';

export interface IDispatch {
  id?: number;
  dispatchDate?: dayjs.Dayjs | null;
  item?: string | null;
  quantity?: string | null;
  car?: ICar | null;
  supplier?: ISupplier | null;
}

export class Dispatch implements IDispatch {
  constructor(
    public id?: number,
    public dispatchDate?: dayjs.Dayjs | null,
    public item?: string | null,
    public quantity?: string | null,
    public car?: ICar | null,
    public supplier?: ISupplier | null
  ) {}
}

export function getDispatchIdentifier(dispatch: IDispatch): number | undefined {
  return dispatch.id;
}
