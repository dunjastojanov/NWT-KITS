import {ComponentFixture, TestBed} from '@angular/core/testing';

import {PaypalTestComponent} from './paypal-test.component';

describe('PaypalTestComponent', () => {
  let component: PaypalTestComponent;
  let fixture: ComponentFixture<PaypalTestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PaypalTestComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(PaypalTestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
