/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WishListApplicationTestModule } from '../../../test.module';
import { AnswerListDetailComponent } from 'app/entities/answer-list/answer-list-detail.component';
import { AnswerList } from 'app/shared/model/answer-list.model';

describe('Component Tests', () => {
  describe('AnswerList Management Detail Component', () => {
    let comp: AnswerListDetailComponent;
    let fixture: ComponentFixture<AnswerListDetailComponent>;
    const route = ({ data: of({ answerList: new AnswerList(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WishListApplicationTestModule],
        declarations: [AnswerListDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(AnswerListDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AnswerListDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.answerList).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
