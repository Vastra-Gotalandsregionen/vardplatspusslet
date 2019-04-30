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
  selector: '[appCallout]'
})
export class CalloutDirective implements OnDestroy {

  @Input() appCallout: String = '';

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
      const calloutEl = this.calloutRef.location.nativeElement;

      const offset = this.offset(this.elementRef.nativeElement);
      const targetPosX = offset.left;
      const targetPosY = offset.top;

      calloutEl.style.left = (targetPosX + this.elementRef.nativeElement.offsetWidth + 12) + 'px';
      calloutEl.style.top = (targetPosY) + 'px';
    }
  }

  offset(el) {
    const rect = el.getBoundingClientRect(),
      scrollLeft = window.pageXOffset || document.documentElement.scrollLeft,
      scrollTop = window.pageYOffset || document.documentElement.scrollTop;
    return { top: rect.top + scrollTop, left: rect.left + scrollLeft };
  }

  @HostListener('mouseleave') onmouseleave() {
    this.hideCallout();
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
