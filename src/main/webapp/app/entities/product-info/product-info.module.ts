import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProductInfoComponent } from './list/product-info.component';
import { ProductInfoDetailComponent } from './detail/product-info-detail.component';
import { ProductInfoUpdateComponent } from './update/product-info-update.component';
import { ProductInfoDeleteDialogComponent } from './delete/product-info-delete-dialog.component';
import { ProductInfoRoutingModule } from './route/product-info-routing.module';

@NgModule({
  imports: [SharedModule, ProductInfoRoutingModule],
  declarations: [ProductInfoComponent, ProductInfoDetailComponent, ProductInfoUpdateComponent, ProductInfoDeleteDialogComponent],
})
export class ProductInfoModule {}
