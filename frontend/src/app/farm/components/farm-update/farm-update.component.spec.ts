import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FarmUpdateComponent } from './farm-update.component';

describe('FarmUpdateComponent', () => {
  let component: FarmUpdateComponent;
  let fixture: ComponentFixture<FarmUpdateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FarmUpdateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FarmUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
