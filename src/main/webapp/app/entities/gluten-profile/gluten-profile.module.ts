import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GlutenProfileComponent } from './list/gluten-profile.component';
import { GlutenProfileDetailComponent } from './detail/gluten-profile-detail.component';
import { GlutenProfileUpdateComponent } from './update/gluten-profile-update.component';
import { GlutenProfileDeleteDialogComponent } from './delete/gluten-profile-delete-dialog.component';
import { GlutenProfileRoutingModule } from './route/gluten-profile-routing.module';

@NgModule({
  imports: [SharedModule, GlutenProfileRoutingModule],
  declarations: [GlutenProfileComponent, GlutenProfileDetailComponent, GlutenProfileUpdateComponent, GlutenProfileDeleteDialogComponent],
})
export class GlutenProfileModule {}
