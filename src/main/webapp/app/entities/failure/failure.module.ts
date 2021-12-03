import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FailureComponent } from './list/failure.component';
import { FailureDetailComponent } from './detail/failure-detail.component';
import { FailureUpdateComponent } from './update/failure-update.component';
import { FailureDeleteDialogComponent } from './delete/failure-delete-dialog.component';
import { FailureRoutingModule } from './route/failure-routing.module';

@NgModule({
  imports: [SharedModule, FailureRoutingModule],
  declarations: [FailureComponent, FailureDetailComponent, FailureUpdateComponent, FailureDeleteDialogComponent],
  entryComponents: [FailureDeleteDialogComponent],
})
export class FailureModule {}
