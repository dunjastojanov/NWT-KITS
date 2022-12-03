import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateDriverNotificationComponent } from './update-driver-notification.component';

describe('UpdateDriverNotificationComponent', () => {
  let component: UpdateDriverNotificationComponent;
  let fixture: ComponentFixture<UpdateDriverNotificationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateDriverNotificationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateDriverNotificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
