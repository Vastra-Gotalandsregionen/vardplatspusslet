import {
  ChangeDetectorRef,
  Directive,
  ElementRef,
  Input,
  NgZone,
  OnDestroy,
  Optional,
  ViewContainerRef,
  ComponentFactoryResolver, ComponentRef, HostListener
} from '@angular/core';

import {CalloutComponent} from './callout.component';


@Directive({
  selector: '[appCallout]'
})
export class CalloutDirective implements OnDestroy{
  @Input() appCallout: String = '';

  private element: HTMLElement;
  private calloutRef: ComponentRef<CalloutComponent>;

  constructor(
    private elementRef: ElementRef,
    private viewContainer: ViewContainerRef,
    private componentFactoryResolver: ComponentFactoryResolver,
    @Optional() private changeDetector: ChangeDetectorRef,
    @Optional() private zone: NgZone,
  ) {

  }

  @HostListener('mouseenter') onmouseenter()
  {
    this.calloutRef = this.createCallout(this.appCallout);
    let calloutEl = this.calloutRef.location.nativeElement;
    let targetPos = this.elementRef.nativeElement.offsetLeft;
    calloutEl.style.left = targetPos.x + 'px';
    calloutEl.style.top = targetPos.y + 'px';
  }

  @HostListener('mouseleave') onmouseleave(){
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

    let calloutComponentFactory =
      this.componentFactoryResolver.resolveComponentFactory(CalloutComponent);
    let calloutComponentRef = this.viewContainer.createComponent(calloutComponentFactory);

    calloutComponentRef.instance.content = content;

    return calloutComponentRef;
  }
}
