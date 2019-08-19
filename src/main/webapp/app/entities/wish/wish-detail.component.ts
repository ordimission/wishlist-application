import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWish } from 'app/shared/model/wish.model';

@Component({
  selector: 'jhi-wish-detail',
  templateUrl: './wish-detail.component.html'
})
export class WishDetailComponent implements OnInit {
  wish: IWish;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ wish }) => {
      this.wish = wish;
    });
  }

  previousState() {
    window.history.back();
  }
}
