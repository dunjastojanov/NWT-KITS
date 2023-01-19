import {ComponentFixture, TestBed} from '@angular/core/testing';

import {MultiSelectWithIconsComponent} from './multi-select-with-icons.component';

describe('MultiSelectWithIconsComponent', () => {
  let component: MultiSelectWithIconsComponent;
  let fixture: ComponentFixture<MultiSelectWithIconsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MultiSelectWithIconsComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(MultiSelectWithIconsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
