import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';

@Component({
  selector: 'embedded-html',
  template: `
    <div #embedded></div>
  `,
  styles: []
})
export class EmbeddedHtmlComponent implements OnInit {
  @Input('html') html!: string;
  @ViewChild('embedded', {static: true}) container: ElementRef | null = null;

  constructor() {
  }

  ngOnInit(): void {
    if (this.container) {
      this.container.nativeElement.innerHTML = this.html;
    }
  }

}
