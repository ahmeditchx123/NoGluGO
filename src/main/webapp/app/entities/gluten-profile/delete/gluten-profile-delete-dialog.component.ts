import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGlutenProfile } from '../gluten-profile.model';
import { GlutenProfileService } from '../service/gluten-profile.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './gluten-profile-delete-dialog.component.html',
})
export class GlutenProfileDeleteDialogComponent {
  glutenProfile?: IGlutenProfile;

  constructor(protected glutenProfileService: GlutenProfileService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.glutenProfileService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
