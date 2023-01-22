import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LobyComponent } from './loby.component';

describe('LobyComponent', () => {
  let component: LobyComponent;
  let fixture: ComponentFixture<LobyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LobyComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LobyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
