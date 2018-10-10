import {
  Component,
  NgModule,
  VERSION,
  Optional,
  ChangeDetectorRef
} from '@angular/core'

@Component({
  selector: 'app-callout',
  template: `
    {{content}}
  `,
  styles: [`
    :host {
      visibility: visible;
      width: 120px;
      background-color: royalblue;
      color: #fff;
      text-align: center;
      padding: 5px 0;
      border-radius: 6px;
      position:absolute;
      z-index: 1;
    }
  `]
})
export class CalloutComponent {
  public content: String = '';
}
