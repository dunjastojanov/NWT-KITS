import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MediumTitleComponent } from './medium-title.component';

describe('MediumTitleComponent', () => {
  let component: MediumTitleComponent;
  let fixture: ComponentFixture<MediumTitleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MediumTitleComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MediumTitleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
