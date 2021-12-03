import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DispatchDetailComponent } from './dispatch-detail.component';

describe('Dispatch Management Detail Component', () => {
  let comp: DispatchDetailComponent;
  let fixture: ComponentFixture<DispatchDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DispatchDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ dispatch: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DispatchDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DispatchDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load dispatch on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.dispatch).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
