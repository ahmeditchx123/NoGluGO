import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ReviewFormService } from './review-form.service';
import { ReviewService } from '../service/review.service';
import { IReview } from '../review.model';
import { IMenuItem } from 'app/entities/menu-item/menu-item.model';
import { MenuItemService } from 'app/entities/menu-item/service/menu-item.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

import { ReviewUpdateComponent } from './review-update.component';

describe('Review Management Update Component', () => {
  let comp: ReviewUpdateComponent;
  let fixture: ComponentFixture<ReviewUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reviewFormService: ReviewFormService;
  let reviewService: ReviewService;
  let menuItemService: MenuItemService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ReviewUpdateComponent],
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
      .overrideTemplate(ReviewUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReviewUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reviewFormService = TestBed.inject(ReviewFormService);
    reviewService = TestBed.inject(ReviewService);
    menuItemService = TestBed.inject(MenuItemService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call MenuItem query and add missing value', () => {
      const review: IReview = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const menuItem: IMenuItem = { id: 'd5c566ad-3495-4ccc-94fd-8f6d9325e3ce' };
      review.menuItem = menuItem;

      const menuItemCollection: IMenuItem[] = [{ id: 'a543054f-6f81-4f44-9930-abbdb984aacd' }];
      jest.spyOn(menuItemService, 'query').mockReturnValue(of(new HttpResponse({ body: menuItemCollection })));
      const additionalMenuItems = [menuItem];
      const expectedCollection: IMenuItem[] = [...additionalMenuItems, ...menuItemCollection];
      jest.spyOn(menuItemService, 'addMenuItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ review });
      comp.ngOnInit();

      expect(menuItemService.query).toHaveBeenCalled();
      expect(menuItemService.addMenuItemToCollectionIfMissing).toHaveBeenCalledWith(
        menuItemCollection,
        ...additionalMenuItems.map(expect.objectContaining)
      );
      expect(comp.menuItemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Product query and add missing value', () => {
      const review: IReview = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const product: IProduct = { id: 'a91a7f85-f67e-4b8d-afa1-b4dfd041c330' };
      review.product = product;

      const productCollection: IProduct[] = [{ id: '100c711c-4fe3-4560-9245-d0c9ad8deedd' }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ review });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining)
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const review: IReview = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const menuItem: IMenuItem = { id: '28b65830-cfbb-401b-a51a-478e9b79aa47' };
      review.menuItem = menuItem;
      const product: IProduct = { id: '8580fa5a-766b-4f5f-b56f-fcf17661215f' };
      review.product = product;

      activatedRoute.data = of({ review });
      comp.ngOnInit();

      expect(comp.menuItemsSharedCollection).toContain(menuItem);
      expect(comp.productsSharedCollection).toContain(product);
      expect(comp.review).toEqual(review);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReview>>();
      const review = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(reviewFormService, 'getReview').mockReturnValue(review);
      jest.spyOn(reviewService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ review });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: review }));
      saveSubject.complete();

      // THEN
      expect(reviewFormService.getReview).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(reviewService.update).toHaveBeenCalledWith(expect.objectContaining(review));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReview>>();
      const review = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(reviewFormService, 'getReview').mockReturnValue({ id: null });
      jest.spyOn(reviewService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ review: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: review }));
      saveSubject.complete();

      // THEN
      expect(reviewFormService.getReview).toHaveBeenCalled();
      expect(reviewService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReview>>();
      const review = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(reviewService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ review });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reviewService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMenuItem', () => {
      it('Should forward to menuItemService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(menuItemService, 'compareMenuItem');
        comp.compareMenuItem(entity, entity2);
        expect(menuItemService.compareMenuItem).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProduct', () => {
      it('Should forward to productService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(productService, 'compareProduct');
        comp.compareProduct(entity, entity2);
        expect(productService.compareProduct).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
