import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { GlutenProfileFormService, GlutenProfileFormGroup } from './gluten-profile-form.service';
import { IGlutenProfile } from '../gluten-profile.model';
import { GlutenProfileService } from '../service/gluten-profile.service';
import { Diseas } from 'app/entities/enumerations/diseas.model';

@Component({
  selector: 'jhi-gluten-profile-update',
  templateUrl: './gluten-profile-update.component.html',
})
export class GlutenProfileUpdateComponent implements OnInit {
  isSaving = false;
  glutenProfile: IGlutenProfile | null = null;
  diseasValues = Object.keys(Diseas);

  editForm: GlutenProfileFormGroup = this.glutenProfileFormService.createGlutenProfileFormGroup();

  constructor(
    protected glutenProfileService: GlutenProfileService,
    protected glutenProfileFormService: GlutenProfileFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ glutenProfile }) => {
      this.glutenProfile = glutenProfile;
      if (glutenProfile) {
        this.updateForm(glutenProfile);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const glutenProfile = this.glutenProfileFormService.getGlutenProfile(this.editForm);
    if (glutenProfile.id !== null) {
      this.subscribeToSaveResponse(this.glutenProfileService.update(glutenProfile));
    } else {
      this.subscribeToSaveResponse(this.glutenProfileService.create(glutenProfile));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGlutenProfile>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(glutenProfile: IGlutenProfile): void {
    this.glutenProfile = glutenProfile;
    this.glutenProfileFormService.resetForm(this.editForm, glutenProfile);
  }
}
