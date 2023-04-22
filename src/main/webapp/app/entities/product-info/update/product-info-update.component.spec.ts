import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProductInfoFormService } from './product-info-form.service';
import { ProductInfoService } from '../service/product-info.service';
import { IProductInfo } from '../product-info.model';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

import { ProductInfoUpdateComponent } from './product-info-update.component';

describe('ProductInfo Management Update Component', () => {
  let comp: ProductInfoUpdateComponent;
  let fixture: ComponentFixture<ProductInfoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productInfoFormService: ProductInfoFormService;
  let productInfoService: ProductInfoService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProductInfoUpdateComponent],
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
      .overrideTemplate(ProductInfoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductInfoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productInfoFormService = TestBed.inject(ProductInfoFormService);
    productInfoService = TestBed.inject(ProductInfoService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call product query and add missing value', () => {
      const productInfo: IProductInfo = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const product: IProduct = { id: '17f56f33-cd31-4efa-bae4-e19b3d294803' };
      productInfo.product = product;

      const productCollection: IProduct[] = [{ id: 'beaa6a5c-f07c-44e1-9b56-fb74a63c05f9' }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const expectedCollection: IProduct[] = [product, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productInfo });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(productCollection, product);
      expect(comp.productsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const productInfo: IProductInfo = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const product: IProduct = { id: '5fba0f95-770f-41d7-a59d-9c4630f742c9' };
      productInfo.product = product;

      activatedRoute.data = of({ productInfo });
      comp.ngOnInit();

      expect(comp.productsCollection).toContain(product);
      expect(comp.productInfo).toEqual(productInfo);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductInfo>>();
      const productInfo = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(productInfoFormService, 'getProductInfo').mockReturnValue(productInfo);
      jest.spyOn(productInfoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productInfo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productInfo }));
      saveSubject.complete();

      // THEN
      expect(productInfoFormService.getProductInfo).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productInfoService.update).toHaveBeenCalledWith(expect.objectContaining(productInfo));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductInfo>>();
      const productInfo = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(productInfoFormService, 'getProductInfo').mockReturnValue({ id: null });
      jest.spyOn(productInfoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productInfo: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productInfo }));
      saveSubject.complete();

      // THEN
      expect(productInfoFormService.getProductInfo).toHaveBeenCalled();
      expect(productInfoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductInfo>>();
      const productInfo = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(productInfoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productInfo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productInfoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
