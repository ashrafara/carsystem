import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FailureComponent } from '../list/failure.component';
import { FailureDetailComponent } from '../detail/failure-detail.component';
import { FailureUpdateComponent } from '../update/failure-update.component';
import { FailureRoutingResolveService } from './failure-routing-resolve.service';

const failureRoute: Routes = [
  {
    path: '',
    component: FailureComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FailureDetailComponent,
    resolve: {
      failure: FailureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FailureUpdateComponent,
    resolve: {
      failure: FailureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FailureUpdateComponent,
    resolve: {
      failure: FailureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(failureRoute)],
  exports: [RouterModule],
})
export class FailureRoutingModule {}
