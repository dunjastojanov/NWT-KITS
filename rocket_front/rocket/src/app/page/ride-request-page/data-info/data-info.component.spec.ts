import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DataInfoComponent } from './data-info.component';

describe('DataInfoComponent', () => {
  let component: DataInfoComponent;
  let fixture: ComponentFixture<DataInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DataInfoComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DataInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
