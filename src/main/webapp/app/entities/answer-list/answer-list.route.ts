import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { AnswerList } from 'app/shared/model/answer-list.model';
import { AnswerListService } from './answer-list.service';
import { AnswerListComponent } from './answer-list.component';
import { AnswerListDetailComponent } from './answer-list-detail.component';
import { AnswerListUpdateComponent } from './answer-list-update.component';
import { AnswerListDeletePopupComponent } from './answer-list-delete-dialog.component';
import { IAnswerList } from 'app/shared/model/answer-list.model';

@Injectable({ providedIn: 'root' })
export class AnswerListResolve implements Resolve<IAnswerList> {
  constructor(private service: AnswerListService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IAnswerList> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<AnswerList>) => response.ok),
        map((answerList: HttpResponse<AnswerList>) => answerList.body)
      );
    }
    return of(new AnswerList());
  }
}

export const answerListRoute: Routes = [
  {
    path: '',
    component: AnswerListComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'wishListApplicationApp.answerList.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: AnswerListDetailComponent,
    resolve: {
      answerList: AnswerListResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'wishListApplicationApp.answerList.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: AnswerListUpdateComponent,
    resolve: {
      answerList: AnswerListResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'wishListApplicationApp.answerList.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: AnswerListUpdateComponent,
    resolve: {
      answerList: AnswerListResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'wishListApplicationApp.answerList.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const answerListPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: AnswerListDeletePopupComponent,
    resolve: {
      answerList: AnswerListResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'wishListApplicationApp.answerList.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
