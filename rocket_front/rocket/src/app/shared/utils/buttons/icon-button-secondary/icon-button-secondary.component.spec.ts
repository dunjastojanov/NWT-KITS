import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IconButtonSecondaryComponent } from './icon-button-secondary.component';

describe('IconButtonSecondaryComponent', () => {
  let component: IconButtonSecondaryComponent;
  let fixture: ComponentFixture<IconButtonSecondaryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IconButtonSecondaryComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IconButtonSecondaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
