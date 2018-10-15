import {
  Component
} from '@angular/core'

@Component({
  selector: 'app-callout',
  template: `
    {{content}}
  `,
  styles: [`
    :host {
      visibility: visible;
      background-color: royalblue;
      color: #fff;
      text-align: center;
      padding: 5px;
      border-radius: 6px;
      position:absolute;
      z-index: 1;
    }
  `]
})
export class CalloutComponent {
  public content: String = '';
}
