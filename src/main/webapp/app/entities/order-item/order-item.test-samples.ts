import dayjs from 'dayjs/esm';

import { IOrderItem, NewOrderItem } from './order-item.model';

export const sampleWithRequiredData: IOrderItem = {
  id: '9db4928b-a829-49d0-a2c6-04ecf1208cb6',
  qty: 29017,
  totalPrice: 19312,
};

export const sampleWithPartialData: IOrderItem = {
  id: 'dde6e65c-5f87-4343-9f33-60285631f903',
  qty: 11392,
  totalPrice: 5314,
  createdBy: 'functionalities',
  lastModifiedDate: dayjs('2023-04-08T05:25'),
};

export const sampleWithFullData: IOrderItem = {
  id: 'e815a555-8e96-4371-9800-7fc36a5411a2',
  qty: 64019,
  totalPrice: 98710,
  createdBy: 'application',
  createdDate: dayjs('2023-04-08T06:39'),
  lastModifiedBy: 'intangible Extended hardware',
  lastModifiedDate: dayjs('2023-04-07T12:23'),
};

export const sampleWithNewData: NewOrderItem = {
  qty: 45415,
  totalPrice: 37660,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
