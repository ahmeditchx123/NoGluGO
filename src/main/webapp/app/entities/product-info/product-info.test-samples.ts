import dayjs from 'dayjs/esm';

import { IProductInfo, NewProductInfo } from './product-info.model';

export const sampleWithRequiredData: IProductInfo = {
  id: 'e34183be-b8f1-4dd9-af06-3b67f5cb1bd5',
  qtyInStock: 60508,
  isGlutenFree: true,
};

export const sampleWithPartialData: IProductInfo = {
  id: 'abfb2f01-5a10-4546-9f24-dd40e14d60f9',
  qtyInStock: 57338,
  isGlutenFree: true,
  createdDate: dayjs('2023-04-07T21:08'),
  lastModifiedDate: dayjs('2023-04-08T01:58'),
};

export const sampleWithFullData: IProductInfo = {
  id: 'e3a557a1-4c74-4e65-a07a-28d8a8e0ba95',
  qtyInStock: 2837,
  isGlutenFree: false,
  createdBy: 'synthesize Intranet',
  createdDate: dayjs('2023-04-07T15:04'),
  lastModifiedBy: 'Supervisor black',
  lastModifiedDate: dayjs('2023-04-07T20:34'),
};

export const sampleWithNewData: NewProductInfo = {
  qtyInStock: 90207,
  isGlutenFree: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
