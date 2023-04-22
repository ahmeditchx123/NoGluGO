import dayjs from 'dayjs/esm';

import { IProduct, NewProduct } from './product.model';

export const sampleWithRequiredData: IProduct = {
  id: 'c442dfc5-f1e3-4a23-b568-16f6b07dcadc',
  name: 'Account experiences Garden',
  sku: 'Fresh',
  imgPath: 'RAM yellow silver',
  unitPrice: 19060,
  isAvailable: true,
};

export const sampleWithPartialData: IProduct = {
  id: '84beccd5-52ab-4e2c-9374-ca3b776a4568',
  name: 'enhance Berkshire payment',
  sku: 'Horizontal',
  imgPath: 'Progressive',
  unitPrice: 52888,
  isAvailable: true,
  createdDate: dayjs('2023-04-07T18:35'),
};

export const sampleWithFullData: IProduct = {
  id: '9bbcfc4d-92e5-4b6d-87a5-64eb7f6530bf',
  name: 'monetize',
  sku: 'Plains Towels',
  description: 'program green Dobra',
  imgPath: 'Shirt Adaptive',
  unitPrice: 15438,
  isAvailable: false,
  createdBy: 'B2C Incredible',
  createdDate: dayjs('2023-04-07T20:12'),
  lastModifiedBy: 'Gloves',
  lastModifiedDate: dayjs('2023-04-07T18:55'),
};

export const sampleWithNewData: NewProduct = {
  name: 'plum Officer',
  sku: 'Orchestrator',
  imgPath: 'Computer synthesize override',
  unitPrice: 86272,
  isAvailable: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
