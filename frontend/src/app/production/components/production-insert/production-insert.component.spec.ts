import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductionInsertComponent } from './production-insert.component';

describe('ProductionInsertComponent', () => {
  let component: ProductionInsertComponent;
  let fixture: ComponentFixture<ProductionInsertComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProductionInsertComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProductionInsertComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should insert', () => {
    expect(component).toBeTruthy();
  });
});
