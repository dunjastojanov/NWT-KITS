import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CurrentRideRouteComponent } from './current-ride-route.component';

describe('CurrentRideRouteComponent', () => {
  let component: CurrentRideRouteComponent;
  let fixture: ComponentFixture<CurrentRideRouteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CurrentRideRouteComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CurrentRideRouteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
