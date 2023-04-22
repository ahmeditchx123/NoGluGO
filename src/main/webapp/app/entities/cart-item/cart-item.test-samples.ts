import dayjs from 'dayjs/esm';

import { ICartItem, NewCartItem } from './cart-item.model';

export const sampleWithRequiredData: ICartItem = {
  id: '707834c0-7635-4f83-8c94-f5ac98a8bea2',
  qty: 67804,
  totalPrice: 40089,
};

export const sampleWithPartialData: ICartItem = {
  id: '44282d94-67da-40a4-8063-7acd10f44d36',
  qty: 70816,
  totalPrice: 57452,
  lastModifiedDate: dayjs('2023-04-07T21:42'),
};

export const sampleWithFullData: ICartItem = {
  id: '245f8f6c-0e1e-4bd1-b194-7ba001437cec',
  qty: 86707,
  totalPrice: 2200,
  createdBy: 'Bedfordshire Granite Rhode',
  createdDate: dayjs('2023-04-08T00:26'),
  lastModifiedBy: 'Investment',
  lastModifiedDate: dayjs('2023-04-08T09:04'),
};

export const sampleWithNewData: NewCartItem = {
  qty: 5103,
  totalPrice: 56487,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
