import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProductInfo } from '../product-info.model';
import { ProductInfoService } from '../service/product-info.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './product-info-delete-dialog.component.html',
})
export class ProductInfoDeleteDialogComponent {
  productInfo?: IProductInfo;

  constructor(protected productInfoService: ProductInfoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.productInfoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
