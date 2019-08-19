import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Wish } from 'app/shared/model/wish.model';
import { WishService } from './wish.service';
import { WishComponent } from './wish.component';
import { WishDetailComponent } from './wish-detail.component';
import { WishUpdateComponent } from './wish-update.component';
import { WishDeletePopupComponent } from './wish-delete-dialog.component';
import { IWish } from 'app/shared/model/wish.model';

@Injectable({ providedIn: 'root' })
export class WishResolve implements Resolve<IWish> {
  constructor(private service: WishService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IWish> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Wish>) => response.ok),
        map((wish: HttpResponse<Wish>) => wish.body)
      );
    }
    return of(new Wish());
  }
}

export const wishRoute: Routes = [
  {
    path: '',
    component: WishComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'wishListApplicationApp.wish.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: WishDetailComponent,
    resolve: {
      wish: WishResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'wishListApplicationApp.wish.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: WishUpdateComponent,
    resolve: {
      wish: WishResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'wishListApplicationApp.wish.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: WishUpdateComponent,
    resolve: {
      wish: WishResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'wishListApplicationApp.wish.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const wishPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: WishDeletePopupComponent,
    resolve: {
      wish: WishResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'wishListApplicationApp.wish.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
