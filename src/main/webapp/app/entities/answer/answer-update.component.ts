import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IAnswer, Answer } from 'app/shared/model/answer.model';
import { AnswerService } from './answer.service';
import { IAnswerList } from 'app/shared/model/answer-list.model';
import { AnswerListService } from 'app/entities/answer-list';
import { IWish } from 'app/shared/model/wish.model';
import { WishService } from 'app/entities/wish';
import { IUser, UserService } from 'app/core';

@Component({
  selector: 'jhi-answer-update',
  templateUrl: './answer-update.component.html'
})
export class AnswerUpdateComponent implements OnInit {
  isSaving: boolean;

  answerlists: IAnswerList[];

  wishes: IWish[];

  users: IUser[];

  editForm = this.fb.group({
    id: [],
    quantity: [],
    unit: [],
    model: [],
    brand: [],
    answerList: [],
    wish: [],
    user: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected answerService: AnswerService,
    protected answerListService: AnswerListService,
    protected wishService: WishService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ answer }) => {
      this.updateForm(answer);
    });
    this.answerListService
      .query({ filter: 'answer-is-null' })
      .pipe(
        filter((mayBeOk: HttpResponse<IAnswerList[]>) => mayBeOk.ok),
        map((response: HttpResponse<IAnswerList[]>) => response.body)
      )
      .subscribe(
        (res: IAnswerList[]) => {
          if (!this.editForm.get('answerList').value || !this.editForm.get('answerList').value.id) {
            this.answerlists = res;
          } else {
            this.answerListService
              .find(this.editForm.get('answerList').value.id)
              .pipe(
                filter((subResMayBeOk: HttpResponse<IAnswerList>) => subResMayBeOk.ok),
                map((subResponse: HttpResponse<IAnswerList>) => subResponse.body)
              )
              .subscribe(
                (subRes: IAnswerList) => (this.answerlists = [subRes].concat(res)),
                (subRes: HttpErrorResponse) => this.onError(subRes.message)
              );
          }
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    this.wishService
      .query({ filter: 'answer-is-null' })
      .pipe(
        filter((mayBeOk: HttpResponse<IWish[]>) => mayBeOk.ok),
        map((response: HttpResponse<IWish[]>) => response.body)
      )
      .subscribe(
        (res: IWish[]) => {
          if (!this.editForm.get('wish').value || !this.editForm.get('wish').value.id) {
            this.wishes = res;
          } else {
            this.wishService
              .find(this.editForm.get('wish').value.id)
              .pipe(
                filter((subResMayBeOk: HttpResponse<IWish>) => subResMayBeOk.ok),
                map((subResponse: HttpResponse<IWish>) => subResponse.body)
              )
              .subscribe(
                (subRes: IWish) => (this.wishes = [subRes].concat(res)),
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

  updateForm(answer: IAnswer) {
    this.editForm.patchValue({
      id: answer.id,
      quantity: answer.quantity,
      unit: answer.unit,
      model: answer.model,
      brand: answer.brand,
      answerList: answer.answerList,
      wish: answer.wish,
      user: answer.user
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const answer = this.createFromForm();
    if (answer.id !== undefined) {
      this.subscribeToSaveResponse(this.answerService.update(answer));
    } else {
      this.subscribeToSaveResponse(this.answerService.create(answer));
    }
  }

  private createFromForm(): IAnswer {
    return {
      ...new Answer(),
      id: this.editForm.get(['id']).value,
      quantity: this.editForm.get(['quantity']).value,
      unit: this.editForm.get(['unit']).value,
      model: this.editForm.get(['model']).value,
      brand: this.editForm.get(['brand']).value,
      answerList: this.editForm.get(['answerList']).value,
      wish: this.editForm.get(['wish']).value,
      user: this.editForm.get(['user']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnswer>>) {
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

  trackAnswerListById(index: number, item: IAnswerList) {
    return item.id;
  }

  trackWishById(index: number, item: IWish) {
    return item.id;
  }

  trackUserById(index: number, item: IUser) {
    return item.id;
  }
}
