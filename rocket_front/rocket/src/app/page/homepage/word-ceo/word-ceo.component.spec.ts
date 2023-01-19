import {ComponentFixture, TestBed} from '@angular/core/testing';

import {WordCeoComponent} from './word-ceo.component';

describe('WordCeoComponent', () => {
  let component: WordCeoComponent;
  let fixture: ComponentFixture<WordCeoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WordCeoComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(WordCeoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
