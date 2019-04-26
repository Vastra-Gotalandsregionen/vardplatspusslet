import {
  ChangeDetectorRef,
  Directive,
  ElementRef,
  Input,
  NgZone,
  OnDestroy,
  Optional,
  ViewContainerRef,
  ComponentFactoryResolver, ComponentRef, HostListener, EmbeddedViewRef, Injector, ApplicationRef
} from '@angular/core';

import {CalloutComponent} from './callout.component';


@Directive({
  selector: '[appCallout]',
  host: {
    '[style.position]': '"relative"'
  }
})
export class CalloutDirective implements OnDestroy {
  @Input() appCallout: String = '';

  private element: HTMLElement;
  private calloutRef: ComponentRef<CalloutComponent>;

  constructor(
    private elementRef: ElementRef,
    private viewContainer: ViewContainerRef,
    private componentFactoryResolver: ComponentFactoryResolver,
    private injector: Injector,
    private appRef: ApplicationRef,
    @Optional() private changeDetector: ChangeDetectorRef,
    @Optional() private zone: NgZone,
  ) {

  }

  @HostListener('mouseenter') onmouseenter() {
    if (this.appCallout && this.appCallout.length > 0) {
      this.calloutRef = this.createCallout(this.appCallout);
      let calloutEl = this.calloutRef.location.nativeElement;
      let targetPosX = this.elementRef.nativeElement.offsetLeft;
      let targetPosY = this.elementRef.nativeElement.offsetTop;

      calloutEl.style.left = (targetPosX + this.elementRef.nativeElement.offsetWidth + 12) + 'px';
      calloutEl.style.top = (targetPosY) + 'px';
    }
  }

  @HostListener('mouseleave') onmouseleave() {
    this.hideCallout();
  }

  ngOnInit() {
    this.element = this.elementRef.nativeElement;
  }

  ngOnDestroy() {
    this.hideCallout();
  }

  hideCallout() {
    if (this.calloutRef) {
      this.calloutRef.destroy();
      this.calloutRef = null;
    }
  }

  private createCallout(content: String): ComponentRef<CalloutComponent> {
    this.viewContainer.clear();

    const calloutComponentRef = this.componentFactoryResolver
      .resolveComponentFactory(CalloutComponent)
      .create(this.injector);

    // 2. Attach component to the appRef so that it's inside the ng component tree
    this.appRef.attachView(calloutComponentRef.hostView);

    const domElem = (calloutComponentRef.hostView as EmbeddedViewRef<any>)
      .rootNodes[0] as HTMLElement;

    document.body.appendChild(domElem);

    calloutComponentRef.instance.content = content;

    return calloutComponentRef;
  }
}
