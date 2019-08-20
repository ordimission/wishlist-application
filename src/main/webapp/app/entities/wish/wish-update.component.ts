import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IWish, Wish } from 'app/shared/model/wish.model';
import { WishService } from './wish.service';
import { IWishList } from 'app/shared/model/wish-list.model';
import { WishListService } from 'app/entities/wish-list';
import { IUser, UserService } from 'app/core';

@Component({
  selector: 'jhi-wish-update',
  templateUrl: './wish-update.component.html'
})
export class WishUpdateComponent implements OnInit {
  isSaving: boolean;

  wishlists: IWishList[];

  users: IUser[];

  editForm = this.fb.group({
    id: [],
    description: [],
    unitPrice: [],
    quantity: [],
    unit: [],
    model: [],
    brand: [],
    wishList: [],
    user: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected wishService: WishService,
    protected wishListService: WishListService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ wish }) => {
      this.updateForm(wish);
    });
    this.wishListService
      .query({ filter: 'wish-is-null' })
      .pipe(
        filter((mayBeOk: HttpResponse<IWishList[]>) => mayBeOk.ok),
        map((response: HttpResponse<IWishList[]>) => response.body)
      )
      .subscribe(
        (res: IWishList[]) => {
          if (!this.editForm.get('wishList').value || !this.editForm.get('wishList').value.id) {
            this.wishlists = res;
          } else {
            this.wishListService
              .find(this.editForm.get('wishList').value.id)
              .pipe(
                filter((subResMayBeOk: HttpResponse<IWishList>) => subResMayBeOk.ok),
                map((subResponse: HttpResponse<IWishList>) => subResponse.body)
              )
              .subscribe(
                (subRes: IWishList) => (this.wishlists = [subRes].concat(res)),
                (subRes: HttpErrorResponse) => this.onError(subRes.message)
              );
          }
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(wish: IWish) {
    this.editForm.patchValue({
      id: wish.id,
      description: wish.description,
      unitPrice: wish.unitPrice,
      quantity: wish.quantity,
      unit: wish.unit,
      model: wish.model,
      brand: wish.brand,
      wishList: wish.wishList,
      user: wish.user
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const wish = this.createFromForm();
    if (wish.id !== undefined) {
      this.subscribeToSaveResponse(this.wishService.update(wish));
    } else {
      this.subscribeToSaveResponse(this.wishService.create(wish));
    }
  }

  private createFromForm(): IWish {
    return {
      ...new Wish(),
      id: this.editForm.get(['id']).value,
      description: this.editForm.get(['description']).value,
      unitPrice: this.editForm.get(['unitPrice']).value,
      quantity: this.editForm.get(['quantity']).value,
      unit: this.editForm.get(['unit']).value,
      model: this.editForm.get(['model']).value,
      brand: this.editForm.get(['brand']).value,
      wishList: this.editForm.get(['wishList']).value,
      user: this.editForm.get(['user']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWish>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackWishListById(index: number, item: IWishList) {
    return item.id;
  }

  trackUserById(index: number, item: IUser) {
    return item.id;
  }
}
