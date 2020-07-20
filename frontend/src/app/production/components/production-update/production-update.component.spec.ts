import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductionUpdateComponent } from './production-update.component';

describe('ProductionUpdateComponent', () => {
  let component: ProductionUpdateComponent;
  let fixture: ComponentFixture<ProductionUpdateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProductionUpdateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProductionUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
