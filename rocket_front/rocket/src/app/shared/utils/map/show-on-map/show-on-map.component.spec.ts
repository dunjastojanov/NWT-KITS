import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowOnMapComponent } from './show-on-map.component';

describe('ShowOnMapComponent', () => {
  let component: ShowOnMapComponent;
  let fixture: ComponentFixture<ShowOnMapComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowOnMapComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShowOnMapComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
