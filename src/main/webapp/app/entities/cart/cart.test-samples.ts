import dayjs from 'dayjs/esm';

import { ICart, NewCart } from './cart.model';

export const sampleWithRequiredData: ICart = {
  id: '8decf029-cc1a-4fc6-b857-a64cf5a2293d',
};

export const sampleWithPartialData: ICart = {
  id: '116c77b6-e9a4-4a3e-88cf-9b6a11a81aef',
  totalPrice: 73208,
  createdBy: 'Station Landing experiences',
};

export const sampleWithFullData: ICart = {
  id: 'f1f85dcd-b7d5-4444-a8cd-73ddc00ed843',
  totalItems: 20365,
  totalPrice: 82297,
  createdBy: 'Soft portals Table',
  createdDate: dayjs('2023-04-08T11:17'),
  lastModifiedBy: 'Generic lime Wooden',
  lastModifiedDate: dayjs('2023-04-07T23:37'),
  userId: 10967,
};

export const sampleWithNewData: NewCart = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
