import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserChatModalComponent } from './user-chat-modal.component';

describe('UserChatModalComponent', () => {
  let component: UserChatModalComponent;
  let fixture: ComponentFixture<UserChatModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserChatModalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserChatModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
