import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDispatch } from '../dispatch.model';

@Component({
  selector: 'jhi-dispatch-detail',
  templateUrl: './dispatch-detail.component.html',
})
export class DispatchDetailComponent implements OnInit {
  dispatch: IDispatch | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dispatch }) => {
      this.dispatch = dispatch;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
