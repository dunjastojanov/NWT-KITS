import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotificationLinkComponent } from './notification-link.component';

describe('NotificationLinkComponent', () => {
  let component: NotificationLinkComponent;
  let fixture: ComponentFixture<NotificationLinkComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NotificationLinkComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NotificationLinkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
