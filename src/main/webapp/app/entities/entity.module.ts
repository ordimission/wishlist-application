import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'wish-list',
        loadChildren: () => import('./wish-list/wish-list.module').then(m => m.WishListApplicationWishListModule)
      },
      {
        path: 'wish',
        loadChildren: () => import('./wish/wish.module').then(m => m.WishListApplicationWishModule)
      },
      {
        path: 'answer-list',
        loadChildren: () => import('./answer-list/answer-list.module').then(m => m.WishListApplicationAnswerListModule)
      },
      {
        path: 'answer',
        loadChildren: () => import('./answer/answer.module').then(m => m.WishListApplicationAnswerModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WishListApplicationEntityModule {}
