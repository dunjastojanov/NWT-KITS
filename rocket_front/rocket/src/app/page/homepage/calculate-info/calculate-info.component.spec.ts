import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CalculateInfoComponent } from './calculate-info.component';

describe('CalculateInfoComponent', () => {
  let component: CalculateInfoComponent;
  let fixture: ComponentFixture<CalculateInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CalculateInfoComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CalculateInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
