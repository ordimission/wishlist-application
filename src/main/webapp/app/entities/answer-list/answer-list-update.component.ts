import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IAnswerList, AnswerList } from 'app/shared/model/answer-list.model';
import { AnswerListService } from './answer-list.service';

@Component({
  selector: 'jhi-answer-list-update',
  templateUrl: './answer-list-update.component.html'
})
export class AnswerListUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: []
  });

  constructor(protected answerListService: AnswerListService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ answerList }) => {
      this.updateForm(answerList);
    });
  }

  updateForm(answerList: IAnswerList) {
    this.editForm.patchValue({
      id: answerList.id
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const answerList = this.createFromForm();
    if (answerList.id !== undefined) {
      this.subscribeToSaveResponse(this.answerListService.update(answerList));
    } else {
      this.subscribeToSaveResponse(this.answerListService.create(answerList));
    }
  }

  private createFromForm(): IAnswerList {
    return {
      ...new AnswerList(),
      id: this.editForm.get(['id']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnswerList>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
