import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ProductInfoFormService, ProductInfoFormGroup } from './product-info-form.service';
import { IProductInfo } from '../product-info.model';
import { ProductInfoService } from '../service/product-info.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

@Component({
  selector: 'jhi-product-info-update',
  templateUrl: './product-info-update.component.html',
})
export class ProductInfoUpdateComponent implements OnInit {
  isSaving = false;
  productInfo: IProductInfo | null = null;

  productsCollection: IProduct[] = [];

  editForm: ProductInfoFormGroup = this.productInfoFormService.createProductInfoFormGroup();

  constructor(
    protected productInfoService: ProductInfoService,
    protected productInfoFormService: ProductInfoFormService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productInfo }) => {
      this.productInfo = productInfo;
      if (productInfo) {
        this.updateForm(productInfo);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productInfo = this.productInfoFormService.getProductInfo(this.editForm);
    if (productInfo.id !== null) {
      this.subscribeToSaveResponse(this.productInfoService.update(productInfo));
    } else {
      this.subscribeToSaveResponse(this.productInfoService.create(productInfo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductInfo>>): void {
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

  protected updateForm(productInfo: IProductInfo): void {
    this.productInfo = productInfo;
    this.productInfoFormService.resetForm(this.editForm, productInfo);

    this.productsCollection = this.productService.addProductToCollectionIfMissing<IProduct>(this.productsCollection, productInfo.product);
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query({ 'informationId.specified': 'false' })
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.productInfo?.product))
      )
      .subscribe((products: IProduct[]) => (this.productsCollection = products));
  }
}
