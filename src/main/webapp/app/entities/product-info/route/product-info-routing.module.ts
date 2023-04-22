import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProductInfoComponent } from '../list/product-info.component';
import { ProductInfoDetailComponent } from '../detail/product-info-detail.component';
import { ProductInfoUpdateComponent } from '../update/product-info-update.component';
import { ProductInfoRoutingResolveService } from './product-info-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const productInfoRoute: Routes = [
  {
    path: '',
    component: ProductInfoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProductInfoDetailComponent,
    resolve: {
      productInfo: ProductInfoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProductInfoUpdateComponent,
    resolve: {
      productInfo: ProductInfoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProductInfoUpdateComponent,
    resolve: {
      productInfo: ProductInfoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(productInfoRoute)],
  exports: [RouterModule],
})
export class ProductInfoRoutingModule {}
