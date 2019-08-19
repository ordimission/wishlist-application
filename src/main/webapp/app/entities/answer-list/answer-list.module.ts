import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { WishListApplicationSharedModule } from 'app/shared';
import {
  AnswerListComponent,
  AnswerListDetailComponent,
  AnswerListUpdateComponent,
  AnswerListDeletePopupComponent,
  AnswerListDeleteDialogComponent,
  answerListRoute,
  answerListPopupRoute
} from './';

const ENTITY_STATES = [...answerListRoute, ...answerListPopupRoute];

@NgModule({
  imports: [WishListApplicationSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    AnswerListComponent,
    AnswerListDetailComponent,
    AnswerListUpdateComponent,
    AnswerListDeleteDialogComponent,
    AnswerListDeletePopupComponent
  ],
  entryComponents: [AnswerListComponent, AnswerListUpdateComponent, AnswerListDeleteDialogComponent, AnswerListDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WishListApplicationAnswerListModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
