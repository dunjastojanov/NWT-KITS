import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CalculateShowRoutesComponent} from './calculate-show-routes.component';

describe('CalculateShowRoutesComponent', () => {
  let component: CalculateShowRoutesComponent;
  let fixture: ComponentFixture<CalculateShowRoutesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CalculateShowRoutesComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CalculateShowRoutesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
