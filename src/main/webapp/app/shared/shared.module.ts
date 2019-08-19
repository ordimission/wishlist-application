import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { WishListApplicationSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [WishListApplicationSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [WishListApplicationSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WishListApplicationSharedModule {
  static forRoot() {
    return {
      ngModule: WishListApplicationSharedModule
    };
  }
}
