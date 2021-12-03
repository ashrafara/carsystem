import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFailure } from '../failure.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-failure-detail',
  templateUrl: './failure-detail.component.html',
})
export class FailureDetailComponent implements OnInit {
  failure: IFailure | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ failure }) => {
      this.failure = failure;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
