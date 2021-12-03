import { IDispatch } from 'app/entities/dispatch/dispatch.model';
import { IFailure } from 'app/entities/failure/failure.model';
import { IEmployee } from 'app/entities/employee/employee.model';

export interface ICar {
  id?: number;
  name?: string | null;
  carIssue?: number | null;
  carnNumber?: string | null;
  carMotor?: string | null;
  carShellNumber?: string | null;
  classification?: string | null;
  madein?: string | null;
  panaelnumber?: string | null;
  notes?: string | null;
  dispatches?: IDispatch[] | null;
  failures?: IFailure[] | null;
  employee?: IEmployee | null;
}

export class Car implements ICar {
  constructor(
    public id?: number,
    public name?: string | null,
    public carIssue?: number | null,
    public carnNumber?: string | null,
    public carMotor?: string | null,
    public carShellNumber?: string | null,
    public classification?: string | null,
    public madein?: string | null,
    public panaelnumber?: string | null,
    public notes?: string | null,
    public dispatches?: IDispatch[] | null,
    public failures?: IFailure[] | null,
    public employee?: IEmployee | null
  ) {}
}

export function getCarIdentifier(car: ICar): number | undefined {
  return car.id;
}
