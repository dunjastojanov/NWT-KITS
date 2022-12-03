import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScheduledRideNotificationComponent } from './scheduled-ride-notification.component';

describe('ScheduledRideNotificationComponent', () => {
  let component: ScheduledRideNotificationComponent;
  let fixture: ComponentFixture<ScheduledRideNotificationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ScheduledRideNotificationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ScheduledRideNotificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
