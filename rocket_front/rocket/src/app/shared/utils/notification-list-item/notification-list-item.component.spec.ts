import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotificationListItemComponent } from './notification-list-item.component';

describe('NotificationListComponent', () => {
  let component: NotificationListItemComponent;
  let fixture: ComponentFixture<NotificationListItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NotificationListItemComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NotificationListItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
