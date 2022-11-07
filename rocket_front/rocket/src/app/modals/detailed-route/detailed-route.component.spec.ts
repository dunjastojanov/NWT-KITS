import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailedRouteComponent } from './detailed-route.component';

describe('DetailedRouteComponent', () => {
  let component: DetailedRouteComponent;
  let fixture: ComponentFixture<DetailedRouteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DetailedRouteComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailedRouteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
