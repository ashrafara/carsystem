import { ICar } from 'app/entities/car/car.model';

export interface IEmployee {
  id?: number;
  name?: string | null;
  position?: string | null;
  phone?: string | null;
  note?: string | null;
  car?: ICar | null;
}

export class Employee implements IEmployee {
  constructor(
    public id?: number,
    public name?: string | null,
    public position?: string | null,
    public phone?: string | null,
    public note?: string | null,
    public car?: ICar | null
  ) {}
}

export function getEmployeeIdentifier(employee: IEmployee): number | undefined {
  return employee.id;
}
