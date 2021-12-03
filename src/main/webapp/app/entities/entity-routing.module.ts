import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'supplier',
        data: { pageTitle: 'Suppliers' },
        loadChildren: () => import('./supplier/supplier.module').then(m => m.SupplierModule),
      },
      {
        path: 'dispatch',
        data: { pageTitle: 'Dispatches' },
        loadChildren: () => import('./dispatch/dispatch.module').then(m => m.DispatchModule),
      },
      {
        path: 'failure',
        data: { pageTitle: 'Failures' },
        loadChildren: () => import('./failure/failure.module').then(m => m.FailureModule),
      },
      {
        path: 'car',
        data: { pageTitle: 'Cars' },
        loadChildren: () => import('./car/car.module').then(m => m.CarModule),
      },
      {
        path: 'employee',
        data: { pageTitle: 'Employees' },
        loadChildren: () => import('./employee/employee.module').then(m => m.EmployeeModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
