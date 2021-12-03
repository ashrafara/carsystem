import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFailure } from '../failure.model';
import { FailureService } from '../service/failure.service';

@Component({
  templateUrl: './failure-delete-dialog.component.html',
})
export class FailureDeleteDialogComponent {
  failure?: IFailure;

  constructor(protected failureService: FailureService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.failureService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
