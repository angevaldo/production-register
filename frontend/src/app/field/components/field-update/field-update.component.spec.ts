import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FieldUpdateComponent } from './field-update.component';

describe('FieldUpdateComponent', () => {
  let component: FieldUpdateComponent;
  let fixture: ComponentFixture<FieldUpdateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FieldUpdateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FieldUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
