import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ProductFormService, ProductFormGroup } from './product-form.service';
import { IProduct } from '../product.model';
import { ProductService } from '../service/product.service';
import { ICartItem } from 'app/entities/cart-item/cart-item.model';
import { CartItemService } from 'app/entities/cart-item/service/cart-item.service';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { IStore } from 'app/entities/store/store.model';
import { StoreService } from 'app/entities/store/service/store.service';

@Component({
  selector: 'jhi-product-update',
  templateUrl: './product-update.component.html',
})
export class ProductUpdateComponent implements OnInit {
  isSaving = false;
  product: IProduct | null = null;

  cartItemsSharedCollection: ICartItem[] = [];
  orderItemsSharedCollection: IOrderItem[] = [];
  storesSharedCollection: IStore[] = [];

  editForm: ProductFormGroup = this.productFormService.createProductFormGroup();

  constructor(
    protected productService: ProductService,
    protected productFormService: ProductFormService,
    protected cartItemService: CartItemService,
    protected orderItemService: OrderItemService,
    protected storeService: StoreService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCartItem = (o1: ICartItem | null, o2: ICartItem | null): boolean => this.cartItemService.compareCartItem(o1, o2);

  compareOrderItem = (o1: IOrderItem | null, o2: IOrderItem | null): boolean => this.orderItemService.compareOrderItem(o1, o2);

  compareStore = (o1: IStore | null, o2: IStore | null): boolean => this.storeService.compareStore(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ product }) => {
      this.product = product;
      if (product) {
        this.updateForm(product);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const product = this.productFormService.getProduct(this.editForm);
    if (product.id !== null) {
      this.subscribeToSaveResponse(this.productService.update(product));
    } else {
      this.subscribeToSaveResponse(this.productService.create(product));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProduct>>): void {
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

  protected updateForm(product: IProduct): void {
    this.product = product;
    this.productFormService.resetForm(this.editForm, product);

    this.cartItemsSharedCollection = this.cartItemService.addCartItemToCollectionIfMissing<ICartItem>(
      this.cartItemsSharedCollection,
      product.cartItem
    );
    this.orderItemsSharedCollection = this.orderItemService.addOrderItemToCollectionIfMissing<IOrderItem>(
      this.orderItemsSharedCollection,
      product.orderItem
    );
    this.storesSharedCollection = this.storeService.addStoreToCollectionIfMissing<IStore>(this.storesSharedCollection, product.store);
  }

  protected loadRelationshipsOptions(): void {
    this.cartItemService
      .query()
      .pipe(map((res: HttpResponse<ICartItem[]>) => res.body ?? []))
      .pipe(
        map((cartItems: ICartItem[]) => this.cartItemService.addCartItemToCollectionIfMissing<ICartItem>(cartItems, this.product?.cartItem))
      )
      .subscribe((cartItems: ICartItem[]) => (this.cartItemsSharedCollection = cartItems));

    this.orderItemService
      .query()
      .pipe(map((res: HttpResponse<IOrderItem[]>) => res.body ?? []))
      .pipe(
        map((orderItems: IOrderItem[]) =>
          this.orderItemService.addOrderItemToCollectionIfMissing<IOrderItem>(orderItems, this.product?.orderItem)
        )
      )
      .subscribe((orderItems: IOrderItem[]) => (this.orderItemsSharedCollection = orderItems));

    this.storeService
      .query()
      .pipe(map((res: HttpResponse<IStore[]>) => res.body ?? []))
      .pipe(map((stores: IStore[]) => this.storeService.addStoreToCollectionIfMissing<IStore>(stores, this.product?.store)))
      .subscribe((stores: IStore[]) => (this.storesSharedCollection = stores));
  }
}
