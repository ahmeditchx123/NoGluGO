import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { GlutenProfileFormService } from './gluten-profile-form.service';
import { GlutenProfileService } from '../service/gluten-profile.service';
import { IGlutenProfile } from '../gluten-profile.model';

import { GlutenProfileUpdateComponent } from './gluten-profile-update.component';

describe('GlutenProfile Management Update Component', () => {
  let comp: GlutenProfileUpdateComponent;
  let fixture: ComponentFixture<GlutenProfileUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let glutenProfileFormService: GlutenProfileFormService;
  let glutenProfileService: GlutenProfileService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [GlutenProfileUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(GlutenProfileUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GlutenProfileUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    glutenProfileFormService = TestBed.inject(GlutenProfileFormService);
    glutenProfileService = TestBed.inject(GlutenProfileService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const glutenProfile: IGlutenProfile = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

      activatedRoute.data = of({ glutenProfile });
      comp.ngOnInit();

      expect(comp.glutenProfile).toEqual(glutenProfile);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGlutenProfile>>();
      const glutenProfile = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(glutenProfileFormService, 'getGlutenProfile').mockReturnValue(glutenProfile);
      jest.spyOn(glutenProfileService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ glutenProfile });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: glutenProfile }));
      saveSubject.complete();

      // THEN
      expect(glutenProfileFormService.getGlutenProfile).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(glutenProfileService.update).toHaveBeenCalledWith(expect.objectContaining(glutenProfile));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGlutenProfile>>();
      const glutenProfile = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(glutenProfileFormService, 'getGlutenProfile').mockReturnValue({ id: null });
      jest.spyOn(glutenProfileService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ glutenProfile: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: glutenProfile }));
      saveSubject.complete();

      // THEN
      expect(glutenProfileFormService.getGlutenProfile).toHaveBeenCalled();
      expect(glutenProfileService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGlutenProfile>>();
      const glutenProfile = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(glutenProfileService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ glutenProfile });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(glutenProfileService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
