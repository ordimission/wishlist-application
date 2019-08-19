/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { WishListApplicationTestModule } from '../../../test.module';
import { AnswerListUpdateComponent } from 'app/entities/answer-list/answer-list-update.component';
import { AnswerListService } from 'app/entities/answer-list/answer-list.service';
import { AnswerList } from 'app/shared/model/answer-list.model';

describe('Component Tests', () => {
  describe('AnswerList Management Update Component', () => {
    let comp: AnswerListUpdateComponent;
    let fixture: ComponentFixture<AnswerListUpdateComponent>;
    let service: AnswerListService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WishListApplicationTestModule],
        declarations: [AnswerListUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(AnswerListUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AnswerListUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AnswerListService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new AnswerList(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new AnswerList();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
