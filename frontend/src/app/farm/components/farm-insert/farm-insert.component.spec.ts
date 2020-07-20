import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FarmInsertComponent } from './farm-insert.component';

describe('FarmInsertComponent', () => {
  let component: FarmInsertComponent;
  let fixture: ComponentFixture<FarmInsertComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FarmInsertComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FarmInsertComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should insert', () => {
    expect(component).toBeTruthy();
  });
});
