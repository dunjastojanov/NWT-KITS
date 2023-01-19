import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CurrentRidePalsComponent} from './current-ride-pals.component';

describe('CurrentRidePalsComponent', () => {
  let component: CurrentRidePalsComponent;
  let fixture: ComponentFixture<CurrentRidePalsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CurrentRidePalsComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CurrentRidePalsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
