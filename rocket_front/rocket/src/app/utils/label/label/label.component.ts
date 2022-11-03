import { Component, OnInit, Input } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

@Component({
  selector: 'app-label',
  templateUrl: './label.component.html',
  styleUrls: ['./label.component.css']
})
export class LabelComponent implements OnInit {
  @Input('text') text!: string;
  @Input('icon') icon!: string;
  public logo: SafeHtml;

  constructor(private sanitizer: DomSanitizer) { 
    this.logo = sanitizer.bypassSecurityTrustHtml(this.icon);
  }

  ngOnInit(): void {
  }

}
