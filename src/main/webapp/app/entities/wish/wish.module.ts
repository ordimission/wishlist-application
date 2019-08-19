import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { WishListApplicationSharedModule } from 'app/shared';
import {
  WishComponent,
  WishDetailComponent,
  WishUpdateComponent,
  WishDeletePopupComponent,
  WishDeleteDialogComponent,
  wishRoute,
  wishPopupRoute
} from './';

const ENTITY_STATES = [...wishRoute, ...wishPopupRoute];

@NgModule({
  imports: [WishListApplicationSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [WishComponent, WishDetailComponent, WishUpdateComponent, WishDeleteDialogComponent, WishDeletePopupComponent],
  entryComponents: [WishComponent, WishUpdateComponent, WishDeleteDialogComponent, WishDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WishListApplicationWishModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
