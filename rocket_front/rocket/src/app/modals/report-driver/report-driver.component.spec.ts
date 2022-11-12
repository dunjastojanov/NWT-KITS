import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportDriverComponent } from './report-driver.component';

describe('ReportDriverComponent', () => {
  let component: ReportDriverComponent;
  let fixture: ComponentFixture<ReportDriverComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReportDriverComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReportDriverComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
