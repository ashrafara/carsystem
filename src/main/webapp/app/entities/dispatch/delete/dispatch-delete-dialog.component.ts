import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDispatch } from '../dispatch.model';
import { DispatchService } from '../service/dispatch.service';

@Component({
  templateUrl: './dispatch-delete-dialog.component.html',
})
export class DispatchDeleteDialogComponent {
  dispatch?: IDispatch;

  constructor(protected dispatchService: DispatchService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dispatchService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
