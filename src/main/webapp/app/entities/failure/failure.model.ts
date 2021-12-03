import * as dayjs from 'dayjs';
import { ICar } from 'app/entities/car/car.model';

export interface IFailure {
  id?: number;
  name?: string | null;
  failureDate?: dayjs.Dayjs | null;
  carGuagefrom?: number | null;
  carGuageTo?: number | null;
  changepart?: string | null;
  garageName?: string | null;
  price?: number | null;
  inovice1?: string | null;
  inovice2?: string | null;
  inovice3?: string | null;
  inovice4?: string | null;
  note?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  car?: ICar | null;
}

export class Failure implements IFailure {
  constructor(
    public id?: number,
    public name?: string | null,
    public failureDate?: dayjs.Dayjs | null,
    public carGuagefrom?: number | null,
    public carGuageTo?: number | null,
    public changepart?: string | null,
    public garageName?: string | null,
    public price?: number | null,
    public inovice1?: string | null,
    public inovice2?: string | null,
    public inovice3?: string | null,
    public inovice4?: string | null,
    public note?: string | null,
    public imageContentType?: string | null,
    public image?: string | null,
    public car?: ICar | null
  ) {}
}

export function getFailureIdentifier(failure: IFailure): number | undefined {
  return failure.id;
}
