import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DispatchComponent } from './list/dispatch.component';
import { DispatchDetailComponent } from './detail/dispatch-detail.component';
import { DispatchUpdateComponent } from './update/dispatch-update.component';
import { DispatchDeleteDialogComponent } from './delete/dispatch-delete-dialog.component';
import { DispatchRoutingModule } from './route/dispatch-routing.module';

@NgModule({
  imports: [SharedModule, DispatchRoutingModule],
  declarations: [DispatchComponent, DispatchDetailComponent, DispatchUpdateComponent, DispatchDeleteDialogComponent],
  entryComponents: [DispatchDeleteDialogComponent],
})
export class DispatchModule {}
