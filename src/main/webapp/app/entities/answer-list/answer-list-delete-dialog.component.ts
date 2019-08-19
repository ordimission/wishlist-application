import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAnswerList } from 'app/shared/model/answer-list.model';
import { AnswerListService } from './answer-list.service';

@Component({
  selector: 'jhi-answer-list-delete-dialog',
  templateUrl: './answer-list-delete-dialog.component.html'
})
export class AnswerListDeleteDialogComponent {
  answerList: IAnswerList;

  constructor(
    protected answerListService: AnswerListService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.answerListService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'answerListListModification',
        content: 'Deleted an answerList'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-answer-list-delete-popup',
  template: ''
})
export class AnswerListDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ answerList }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(AnswerListDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.answerList = answerList;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/answer-list', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/answer-list', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
