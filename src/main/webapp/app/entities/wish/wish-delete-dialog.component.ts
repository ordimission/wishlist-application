import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IWish } from 'app/shared/model/wish.model';
import { WishService } from './wish.service';

@Component({
  selector: 'jhi-wish-delete-dialog',
  templateUrl: './wish-delete-dialog.component.html'
})
export class WishDeleteDialogComponent {
  wish: IWish;

  constructor(protected wishService: WishService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.wishService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'wishListModification',
        content: 'Deleted an wish'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-wish-delete-popup',
  template: ''
})
export class WishDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ wish }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(WishDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.wish = wish;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/wish', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/wish', { outlets: { popup: null } }]);
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
