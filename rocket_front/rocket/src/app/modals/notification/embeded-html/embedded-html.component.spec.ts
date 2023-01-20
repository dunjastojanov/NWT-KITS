import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmbeddedHtmlComponent } from './embedded-html.component';

describe('EmbededHtmlComponent', () => {
  let component: EmbeddedHtmlComponent;
  let fixture: ComponentFixture<EmbeddedHtmlComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EmbeddedHtmlComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmbeddedHtmlComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
