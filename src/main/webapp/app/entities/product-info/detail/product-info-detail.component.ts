import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProductInfo } from '../product-info.model';

@Component({
  selector: 'jhi-product-info-detail',
  templateUrl: './product-info-detail.component.html',
})
export class ProductInfoDetailComponent implements OnInit {
  productInfo: IProductInfo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productInfo }) => {
      this.productInfo = productInfo;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
