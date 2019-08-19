/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { WishListApplicationTestModule } from '../../../test.module';
import { WishUpdateComponent } from 'app/entities/wish/wish-update.component';
import { WishService } from 'app/entities/wish/wish.service';
import { Wish } from 'app/shared/model/wish.model';

describe('Component Tests', () => {
  describe('Wish Management Update Component', () => {
    let comp: WishUpdateComponent;
    let fixture: ComponentFixture<WishUpdateComponent>;
    let service: WishService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WishListApplicationTestModule],
        declarations: [WishUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(WishUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WishUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(WishService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Wish(123);
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
        const entity = new Wish();
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
