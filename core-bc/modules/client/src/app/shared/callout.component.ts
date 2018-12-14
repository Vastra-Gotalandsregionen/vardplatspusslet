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
      background-color: #555;
      color: #fff;
      text-align: center;
      padding: 5px 10px;
      border-radius: 6px;
      position:absolute;
      z-index: 1;
    }
  `]
})
export class CalloutComponent {
  public content: String = '';
}
