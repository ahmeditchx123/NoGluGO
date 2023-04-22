import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { AddressFormService, AddressFormGroup } from './address-form.service';
import { IAddress } from '../address.model';
import { AddressService } from '../service/address.service';
import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { IStore } from 'app/entities/store/store.model';
import { StoreService } from 'app/entities/store/service/store.service';
import { IRestaurant } from 'app/entities/restaurant/restaurant.model';
import { RestaurantService } from 'app/entities/restaurant/service/restaurant.service';
import { Governorate } from 'app/entities/enumerations/governorate.model';

@Component({
  selector: 'jhi-address-update',
  templateUrl: './address-update.component.html',
})
export class AddressUpdateComponent implements OnInit {
  isSaving = false;
  address: IAddress | null = null;
  governorateValues = Object.keys(Governorate);

  orderSASCollection: IOrder[] = [];
  orderBASCollection: IOrder[] = [];
  storesCollection: IStore[] = [];
  restaurantsCollection: IRestaurant[] = [];

  editForm: AddressFormGroup = this.addressFormService.createAddressFormGroup();

  constructor(
    protected addressService: AddressService,
    protected addressFormService: AddressFormService,
    protected orderService: OrderService,
    protected storeService: StoreService,
    protected restaurantService: RestaurantService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareOrder = (o1: IOrder | null, o2: IOrder | null): boolean => this.orderService.compareOrder(o1, o2);

  compareStore = (o1: IStore | null, o2: IStore | null): boolean => this.storeService.compareStore(o1, o2);

  compareRestaurant = (o1: IRestaurant | null, o2: IRestaurant | null): boolean => this.restaurantService.compareRestaurant(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ address }) => {
      this.address = address;
      if (address) {
        this.updateForm(address);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const address = this.addressFormService.getAddress(this.editForm);
    if (address.id !== null) {
      this.subscribeToSaveResponse(this.addressService.update(address));
    } else {
      this.subscribeToSaveResponse(this.addressService.create(address));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAddress>>): void {
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

  protected updateForm(address: IAddress): void {
    this.address = address;
    this.addressFormService.resetForm(this.editForm, address);

    this.orderSASCollection = this.orderService.addOrderToCollectionIfMissing<IOrder>(this.orderSASCollection, address.orderSA);
    this.orderBASCollection = this.orderService.addOrderToCollectionIfMissing<IOrder>(this.orderBASCollection, address.orderBA);
    this.storesCollection = this.storeService.addStoreToCollectionIfMissing<IStore>(this.storesCollection, address.store);
    this.restaurantsCollection = this.restaurantService.addRestaurantToCollectionIfMissing<IRestaurant>(
      this.restaurantsCollection,
      address.restaurant
    );
  }

  protected loadRelationshipsOptions(): void {
    this.orderService
      .query({ 'shippingAddressId.specified': 'false' })
      .pipe(map((res: HttpResponse<IOrder[]>) => res.body ?? []))
      .pipe(map((orders: IOrder[]) => this.orderService.addOrderToCollectionIfMissing<IOrder>(orders, this.address?.orderSA)))
      .subscribe((orders: IOrder[]) => (this.orderSASCollection = orders));

    this.orderService
      .query({ 'billingAddressId.specified': 'false' })
      .pipe(map((res: HttpResponse<IOrder[]>) => res.body ?? []))
      .pipe(map((orders: IOrder[]) => this.orderService.addOrderToCollectionIfMissing<IOrder>(orders, this.address?.orderBA)))
      .subscribe((orders: IOrder[]) => (this.orderBASCollection = orders));

    this.storeService
      .query({ 'storeAddressId.specified': 'false' })
      .pipe(map((res: HttpResponse<IStore[]>) => res.body ?? []))
      .pipe(map((stores: IStore[]) => this.storeService.addStoreToCollectionIfMissing<IStore>(stores, this.address?.store)))
      .subscribe((stores: IStore[]) => (this.storesCollection = stores));

    this.restaurantService
      .query({ 'restaurantAddressId.specified': 'false' })
      .pipe(map((res: HttpResponse<IRestaurant[]>) => res.body ?? []))
      .pipe(
        map((restaurants: IRestaurant[]) =>
          this.restaurantService.addRestaurantToCollectionIfMissing<IRestaurant>(restaurants, this.address?.restaurant)
        )
      )
      .subscribe((restaurants: IRestaurant[]) => (this.restaurantsCollection = restaurants));
  }
}
