import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CurrentRideMapComponent } from './current-ride-map.component';

describe('CurrentRideMapComponent', () => {
  let component: CurrentRideMapComponent;
  let fixture: ComponentFixture<CurrentRideMapComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CurrentRideMapComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CurrentRideMapComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
