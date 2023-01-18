import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CurrentRideClientComponent} from './current-ride-client.component';

describe('CurrentRideClientComponent', () => {
  let component: CurrentRideClientComponent;
  let fixture: ComponentFixture<CurrentRideClientComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CurrentRideClientComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CurrentRideClientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
