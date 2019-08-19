/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WishListApplicationTestModule } from '../../../test.module';
import { WishDetailComponent } from 'app/entities/wish/wish-detail.component';
import { Wish } from 'app/shared/model/wish.model';

describe('Component Tests', () => {
  describe('Wish Management Detail Component', () => {
    let comp: WishDetailComponent;
    let fixture: ComponentFixture<WishDetailComponent>;
    const route = ({ data: of({ wish: new Wish(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WishListApplicationTestModule],
        declarations: [WishDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(WishDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(WishDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.wish).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
