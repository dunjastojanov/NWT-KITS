import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CurrentRideInfoComponent} from './current-ride-info.component';

describe('CurrentRideInfoComponent', () => {
  let component: CurrentRideInfoComponent;
  let fixture: ComponentFixture<CurrentRideInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CurrentRideInfoComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CurrentRideInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
