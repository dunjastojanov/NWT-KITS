import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CurrentRidePageComponent } from './current-ride-page.component';

describe('CurrentRidePageComponent', () => {
  let component: CurrentRidePageComponent;
  let fixture: ComponentFixture<CurrentRidePageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CurrentRidePageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CurrentRidePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
