import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ViewMoreButtonComponent} from './view-more-button.component';

describe('ViewMoreButtonComponent', () => {
  let component: ViewMoreButtonComponent;
  let fixture: ComponentFixture<ViewMoreButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ViewMoreButtonComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ViewMoreButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
