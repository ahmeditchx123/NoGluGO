import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GlutenProfileComponent } from '../list/gluten-profile.component';
import { GlutenProfileDetailComponent } from '../detail/gluten-profile-detail.component';
import { GlutenProfileUpdateComponent } from '../update/gluten-profile-update.component';
import { GlutenProfileRoutingResolveService } from './gluten-profile-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const glutenProfileRoute: Routes = [
  {
    path: '',
    component: GlutenProfileComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GlutenProfileDetailComponent,
    resolve: {
      glutenProfile: GlutenProfileRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GlutenProfileUpdateComponent,
    resolve: {
      glutenProfile: GlutenProfileRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GlutenProfileUpdateComponent,
    resolve: {
      glutenProfile: GlutenProfileRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(glutenProfileRoute)],
  exports: [RouterModule],
})
export class GlutenProfileRoutingModule {}
