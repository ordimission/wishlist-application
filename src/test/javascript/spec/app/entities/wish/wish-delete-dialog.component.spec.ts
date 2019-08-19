/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { WishListApplicationTestModule } from '../../../test.module';
import { WishDeleteDialogComponent } from 'app/entities/wish/wish-delete-dialog.component';
import { WishService } from 'app/entities/wish/wish.service';

describe('Component Tests', () => {
  describe('Wish Management Delete Component', () => {
    let comp: WishDeleteDialogComponent;
    let fixture: ComponentFixture<WishDeleteDialogComponent>;
    let service: WishService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WishListApplicationTestModule],
        declarations: [WishDeleteDialogComponent]
      })
        .overrideTemplate(WishDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(WishDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(WishService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
