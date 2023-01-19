import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RequestNavbarComponent} from './request-navbar.component';

describe('RequestNavbarComponent', () => {
  let component: RequestNavbarComponent;
  let fixture: ComponentFixture<RequestNavbarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RequestNavbarComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RequestNavbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
