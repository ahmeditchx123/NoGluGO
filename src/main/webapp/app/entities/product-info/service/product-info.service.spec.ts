import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IProductInfo } from '../product-info.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../product-info.test-samples';

import { ProductInfoService, RestProductInfo } from './product-info.service';

const requireRestSample: RestProductInfo = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('ProductInfo Service', () => {
  let service: ProductInfoService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductInfo | IProductInfo[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProductInfoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ProductInfo', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const productInfo = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productInfo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductInfo', () => {
      const productInfo = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productInfo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductInfo', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductInfo', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProductInfo', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addProductInfoToCollectionIfMissing', () => {
      it('should add a ProductInfo to an empty array', () => {
        const productInfo: IProductInfo = sampleWithRequiredData;
        expectedResult = service.addProductInfoToCollectionIfMissing([], productInfo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productInfo);
      });

      it('should not add a ProductInfo to an array that contains it', () => {
        const productInfo: IProductInfo = sampleWithRequiredData;
        const productInfoCollection: IProductInfo[] = [
          {
            ...productInfo,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductInfoToCollectionIfMissing(productInfoCollection, productInfo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductInfo to an array that doesn't contain it", () => {
        const productInfo: IProductInfo = sampleWithRequiredData;
        const productInfoCollection: IProductInfo[] = [sampleWithPartialData];
        expectedResult = service.addProductInfoToCollectionIfMissing(productInfoCollection, productInfo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productInfo);
      });

      it('should add only unique ProductInfo to an array', () => {
        const productInfoArray: IProductInfo[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const productInfoCollection: IProductInfo[] = [sampleWithRequiredData];
        expectedResult = service.addProductInfoToCollectionIfMissing(productInfoCollection, ...productInfoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productInfo: IProductInfo = sampleWithRequiredData;
        const productInfo2: IProductInfo = sampleWithPartialData;
        expectedResult = service.addProductInfoToCollectionIfMissing([], productInfo, productInfo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productInfo);
        expect(expectedResult).toContain(productInfo2);
      });

      it('should accept null and undefined values', () => {
        const productInfo: IProductInfo = sampleWithRequiredData;
        expectedResult = service.addProductInfoToCollectionIfMissing([], null, productInfo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productInfo);
      });

      it('should return initial array if no ProductInfo is added', () => {
        const productInfoCollection: IProductInfo[] = [sampleWithRequiredData];
        expectedResult = service.addProductInfoToCollectionIfMissing(productInfoCollection, undefined, null);
        expect(expectedResult).toEqual(productInfoCollection);
      });
    });

    describe('compareProductInfo', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductInfo(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = null;

        const compareResult1 = service.compareProductInfo(entity1, entity2);
        const compareResult2 = service.compareProductInfo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

        const compareResult1 = service.compareProductInfo(entity1, entity2);
        const compareResult2 = service.compareProductInfo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

        const compareResult1 = service.compareProductInfo(entity1, entity2);
        const compareResult2 = service.compareProductInfo(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
