import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CurrentRideButtonsComponent} from './current-ride-buttons.component';

describe('CurrentRideButtonsComponent', () => {
  let component: CurrentRideButtonsComponent;
  let fixture: ComponentFixture<CurrentRideButtonsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CurrentRideButtonsComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CurrentRideButtonsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
