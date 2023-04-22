import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGlutenProfile } from '../gluten-profile.model';

@Component({
  selector: 'jhi-gluten-profile-detail',
  templateUrl: './gluten-profile-detail.component.html',
})
export class GlutenProfileDetailComponent implements OnInit {
  glutenProfile: IGlutenProfile | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ glutenProfile }) => {
      this.glutenProfile = glutenProfile;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
