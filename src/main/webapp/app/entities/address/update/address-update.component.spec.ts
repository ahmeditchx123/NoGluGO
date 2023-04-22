import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AddressFormService } from './address-form.service';
import { AddressService } from '../service/address.service';
import { IAddress } from '../address.model';
import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { IStore } from 'app/entities/store/store.model';
import { StoreService } from 'app/entities/store/service/store.service';
import { IRestaurant } from 'app/entities/restaurant/restaurant.model';
import { RestaurantService } from 'app/entities/restaurant/service/restaurant.service';

import { AddressUpdateComponent } from './address-update.component';

describe('Address Management Update Component', () => {
  let comp: AddressUpdateComponent;
  let fixture: ComponentFixture<AddressUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let addressFormService: AddressFormService;
  let addressService: AddressService;
  let orderService: OrderService;
  let storeService: StoreService;
  let restaurantService: RestaurantService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AddressUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AddressUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AddressUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    addressFormService = TestBed.inject(AddressFormService);
    addressService = TestBed.inject(AddressService);
    orderService = TestBed.inject(OrderService);
    storeService = TestBed.inject(StoreService);
    restaurantService = TestBed.inject(RestaurantService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call orderSA query and add missing value', () => {
      const address: IAddress = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const orderSA: IOrder = { id: '16ba819e-cb08-4c1f-9b1a-dbc85d3eaa40' };
      address.orderSA = orderSA;

      const orderSACollection: IOrder[] = [{ id: '1a09a364-40dd-4e3e-804e-dc93c4214f4f' }];
      jest.spyOn(orderService, 'query').mockReturnValue(of(new HttpResponse({ body: orderSACollection })));
      const expectedCollection: IOrder[] = [orderSA, ...orderSACollection];
      jest.spyOn(orderService, 'addOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ address });
      comp.ngOnInit();

      expect(orderService.query).toHaveBeenCalled();
      expect(orderService.addOrderToCollectionIfMissing).toHaveBeenCalledWith(orderSACollection, orderSA);
      expect(comp.orderSASCollection).toEqual(expectedCollection);
    });

    it('Should call orderBA query and add missing value', () => {
      const address: IAddress = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const orderBA: IOrder = { id: '1c1344c9-4cd7-4fc2-96b4-2754ba6c08dd' };
      address.orderBA = orderBA;

      const orderBACollection: IOrder[] = [{ id: '8f784fa8-de87-48fc-810f-dad03b0626ae' }];
      jest.spyOn(orderService, 'query').mockReturnValue(of(new HttpResponse({ body: orderBACollection })));
      const expectedCollection: IOrder[] = [orderBA, ...orderBACollection];
      jest.spyOn(orderService, 'addOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ address });
      comp.ngOnInit();

      expect(orderService.query).toHaveBeenCalled();
      expect(orderService.addOrderToCollectionIfMissing).toHaveBeenCalledWith(orderBACollection, orderBA);
      expect(comp.orderBASCollection).toEqual(expectedCollection);
    });

    it('Should call store query and add missing value', () => {
      const address: IAddress = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const store: IStore = { id: '6ea2e7ac-e775-4932-aa3c-cabf2b60797c' };
      address.store = store;

      const storeCollection: IStore[] = [{ id: '7b2e9ae3-4dbf-4745-9647-068661ca11e9' }];
      jest.spyOn(storeService, 'query').mockReturnValue(of(new HttpResponse({ body: storeCollection })));
      const expectedCollection: IStore[] = [store, ...storeCollection];
      jest.spyOn(storeService, 'addStoreToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ address });
      comp.ngOnInit();

      expect(storeService.query).toHaveBeenCalled();
      expect(storeService.addStoreToCollectionIfMissing).toHaveBeenCalledWith(storeCollection, store);
      expect(comp.storesCollection).toEqual(expectedCollection);
    });

    it('Should call restaurant query and add missing value', () => {
      const address: IAddress = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const restaurant: IRestaurant = { id: '940e4e0c-603d-4ea1-9dd9-04a45f08ca50' };
      address.restaurant = restaurant;

      const restaurantCollection: IRestaurant[] = [{ id: '19b87787-95cf-4001-a9e3-0c3717b6e4bd' }];
      jest.spyOn(restaurantService, 'query').mockReturnValue(of(new HttpResponse({ body: restaurantCollection })));
      const expectedCollection: IRestaurant[] = [restaurant, ...restaurantCollection];
      jest.spyOn(restaurantService, 'addRestaurantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ address });
      comp.ngOnInit();

      expect(restaurantService.query).toHaveBeenCalled();
      expect(restaurantService.addRestaurantToCollectionIfMissing).toHaveBeenCalledWith(restaurantCollection, restaurant);
      expect(comp.restaurantsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const address: IAddress = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const orderSA: IOrder = { id: '19a4b62a-113f-43d8-a3ab-d0580bcbfc10' };
      address.orderSA = orderSA;
      const orderBA: IOrder = { id: 'b7a40be0-e560-4f22-8256-754e2aa6891d' };
      address.orderBA = orderBA;
      const store: IStore = { id: '87ae60f6-9364-4c4f-a51b-8e6dab013686' };
      address.store = store;
      const restaurant: IRestaurant = { id: '48d7834e-efcb-4e61-afe1-5d8c442558ec' };
      address.restaurant = restaurant;

      activatedRoute.data = of({ address });
      comp.ngOnInit();

      expect(comp.orderSASCollection).toContain(orderSA);
      expect(comp.orderBASCollection).toContain(orderBA);
      expect(comp.storesCollection).toContain(store);
      expect(comp.restaurantsCollection).toContain(restaurant);
      expect(comp.address).toEqual(address);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAddress>>();
      const address = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(addressFormService, 'getAddress').mockReturnValue(address);
      jest.spyOn(addressService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ address });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: address }));
      saveSubject.complete();

      // THEN
      expect(addressFormService.getAddress).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(addressService.update).toHaveBeenCalledWith(expect.objectContaining(address));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAddress>>();
      const address = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(addressFormService, 'getAddress').mockReturnValue({ id: null });
      jest.spyOn(addressService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ address: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: address }));
      saveSubject.complete();

      // THEN
      expect(addressFormService.getAddress).toHaveBeenCalled();
      expect(addressService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAddress>>();
      const address = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(addressService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ address });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(addressService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareOrder', () => {
      it('Should forward to orderService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(orderService, 'compareOrder');
        comp.compareOrder(entity, entity2);
        expect(orderService.compareOrder).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareStore', () => {
      it('Should forward to storeService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(storeService, 'compareStore');
        comp.compareStore(entity, entity2);
        expect(storeService.compareStore).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareRestaurant', () => {
      it('Should forward to restaurantService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(restaurantService, 'compareRestaurant');
        comp.compareRestaurant(entity, entity2);
        expect(restaurantService.compareRestaurant).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
