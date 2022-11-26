import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InputDestinationComponent } from './input-destination.component';

describe('InputDestinationComponent', () => {
  let component: InputDestinationComponent;
  let fixture: ComponentFixture<InputDestinationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InputDestinationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InputDestinationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
