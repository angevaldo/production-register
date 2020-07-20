import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FieldInsertComponent } from './field-insert.component';

describe('FieldInsertComponent', () => {
  let component: FieldInsertComponent;
  let fixture: ComponentFixture<FieldInsertComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FieldInsertComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FieldInsertComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should insert', () => {
    expect(component).toBeTruthy();
  });
});
