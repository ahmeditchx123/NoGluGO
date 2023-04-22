import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProductFormService } from './product-form.service';
import { ProductService } from '../service/product.service';
import { IProduct } from '../product.model';
import { ICartItem } from 'app/entities/cart-item/cart-item.model';
import { CartItemService } from 'app/entities/cart-item/service/cart-item.service';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { IStore } from 'app/entities/store/store.model';
import { StoreService } from 'app/entities/store/service/store.service';

import { ProductUpdateComponent } from './product-update.component';

describe('Product Management Update Component', () => {
  let comp: ProductUpdateComponent;
  let fixture: ComponentFixture<ProductUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productFormService: ProductFormService;
  let productService: ProductService;
  let cartItemService: CartItemService;
  let orderItemService: OrderItemService;
  let storeService: StoreService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProductUpdateComponent],
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
      .overrideTemplate(ProductUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productFormService = TestBed.inject(ProductFormService);
    productService = TestBed.inject(ProductService);
    cartItemService = TestBed.inject(CartItemService);
    orderItemService = TestBed.inject(OrderItemService);
    storeService = TestBed.inject(StoreService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CartItem query and add missing value', () => {
      const product: IProduct = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const cartItem: ICartItem = { id: 'd2f62737-68e0-4fc2-9da2-3ce6b39e6c8f' };
      product.cartItem = cartItem;

      const cartItemCollection: ICartItem[] = [{ id: 'df92b485-dce0-4950-89a8-418494f0bf87' }];
      jest.spyOn(cartItemService, 'query').mockReturnValue(of(new HttpResponse({ body: cartItemCollection })));
      const additionalCartItems = [cartItem];
      const expectedCollection: ICartItem[] = [...additionalCartItems, ...cartItemCollection];
      jest.spyOn(cartItemService, 'addCartItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(cartItemService.query).toHaveBeenCalled();
      expect(cartItemService.addCartItemToCollectionIfMissing).toHaveBeenCalledWith(
        cartItemCollection,
        ...additionalCartItems.map(expect.objectContaining)
      );
      expect(comp.cartItemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call OrderItem query and add missing value', () => {
      const product: IProduct = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const orderItem: IOrderItem = { id: '4ea56a7f-23d6-4d04-bc8c-ab6d6d1275b7' };
      product.orderItem = orderItem;

      const orderItemCollection: IOrderItem[] = [{ id: '6ea742d0-bdf3-4435-ba3f-e7c0f41d02cc' }];
      jest.spyOn(orderItemService, 'query').mockReturnValue(of(new HttpResponse({ body: orderItemCollection })));
      const additionalOrderItems = [orderItem];
      const expectedCollection: IOrderItem[] = [...additionalOrderItems, ...orderItemCollection];
      jest.spyOn(orderItemService, 'addOrderItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(orderItemService.query).toHaveBeenCalled();
      expect(orderItemService.addOrderItemToCollectionIfMissing).toHaveBeenCalledWith(
        orderItemCollection,
        ...additionalOrderItems.map(expect.objectContaining)
      );
      expect(comp.orderItemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Store query and add missing value', () => {
      const product: IProduct = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const store: IStore = { id: '24374494-f0b5-430e-8a08-9a53ea99ae69' };
      product.store = store;

      const storeCollection: IStore[] = [{ id: '2af0140d-d6a4-41cb-9792-ecbac1b18642' }];
      jest.spyOn(storeService, 'query').mockReturnValue(of(new HttpResponse({ body: storeCollection })));
      const additionalStores = [store];
      const expectedCollection: IStore[] = [...additionalStores, ...storeCollection];
      jest.spyOn(storeService, 'addStoreToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(storeService.query).toHaveBeenCalled();
      expect(storeService.addStoreToCollectionIfMissing).toHaveBeenCalledWith(
        storeCollection,
        ...additionalStores.map(expect.objectContaining)
      );
      expect(comp.storesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const product: IProduct = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const cartItem: ICartItem = { id: 'd546fe43-f85e-483f-b11b-4ae166f50568' };
      product.cartItem = cartItem;
      const orderItem: IOrderItem = { id: '9f79cde0-f0ce-493f-bb14-53cb6a551f7f' };
      product.orderItem = orderItem;
      const store: IStore = { id: '30dad5f2-9193-4a60-a1b1-754915c0b764' };
      product.store = store;

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(comp.cartItemsSharedCollection).toContain(cartItem);
      expect(comp.orderItemsSharedCollection).toContain(orderItem);
      expect(comp.storesSharedCollection).toContain(store);
      expect(comp.product).toEqual(product);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProduct>>();
      const product = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(productFormService, 'getProduct').mockReturnValue(product);
      jest.spyOn(productService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: product }));
      saveSubject.complete();

      // THEN
      expect(productFormService.getProduct).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productService.update).toHaveBeenCalledWith(expect.objectContaining(product));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProduct>>();
      const product = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(productFormService, 'getProduct').mockReturnValue({ id: null });
      jest.spyOn(productService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: product }));
      saveSubject.complete();

      // THEN
      expect(productFormService.getProduct).toHaveBeenCalled();
      expect(productService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProduct>>();
      const product = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(productService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCartItem', () => {
      it('Should forward to cartItemService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(cartItemService, 'compareCartItem');
        comp.compareCartItem(entity, entity2);
        expect(cartItemService.compareCartItem).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareOrderItem', () => {
      it('Should forward to orderItemService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(orderItemService, 'compareOrderItem');
        comp.compareOrderItem(entity, entity2);
        expect(orderItemService.compareOrderItem).toHaveBeenCalledWith(entity, entity2);
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
  });
});
