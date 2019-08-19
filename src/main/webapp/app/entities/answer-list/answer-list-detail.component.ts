import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAnswerList } from 'app/shared/model/answer-list.model';

@Component({
  selector: 'jhi-answer-list-detail',
  templateUrl: './answer-list-detail.component.html'
})
export class AnswerListDetailComponent implements OnInit {
  answerList: IAnswerList;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ answerList }) => {
      this.answerList = answerList;
    });
  }

  previousState() {
    window.history.back();
  }
}
