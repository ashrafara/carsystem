import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DispatchComponent } from '../list/dispatch.component';
import { DispatchDetailComponent } from '../detail/dispatch-detail.component';
import { DispatchUpdateComponent } from '../update/dispatch-update.component';
import { DispatchRoutingResolveService } from './dispatch-routing-resolve.service';

const dispatchRoute: Routes = [
  {
    path: '',
    component: DispatchComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DispatchDetailComponent,
    resolve: {
      dispatch: DispatchRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DispatchUpdateComponent,
    resolve: {
      dispatch: DispatchRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DispatchUpdateComponent,
    resolve: {
      dispatch: DispatchRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dispatchRoute)],
  exports: [RouterModule],
})
export class DispatchRoutingModule {}
