import dayjs from 'dayjs/esm';

import { OrderStatus } from 'app/entities/enumerations/order-status.model';
import { DeliveryMethod } from 'app/entities/enumerations/delivery-method.model';
import { PaymentMethod } from 'app/entities/enumerations/payment-method.model';

import { IOrder, NewOrder } from './order.model';

export const sampleWithRequiredData: IOrder = {
  id: '75b8b579-d1ae-490d-bf79-9f7f3c2b3616',
  totalPrice: 19671,
  totalItems: 32103,
};

export const sampleWithPartialData: IOrder = {
  id: '40bf9737-f5d3-42e3-97fd-522f50ce5129',
  totalPrice: 15454,
  totalItems: 25563,
  status: OrderStatus['PREPARING'],
  deliveryMethod: DeliveryMethod['RETRIEVAL'],
  paymentMethod: PaymentMethod['PAYMENT_IN_DELIVERY'],
  createdDate: dayjs('2023-04-07T15:51'),
  lastModifiedDate: dayjs('2023-04-08T02:48'),
};

export const sampleWithFullData: IOrder = {
  id: '38a2e0d8-101b-4fa9-93dd-a1a8bdca824e',
  totalPrice: 75380,
  totalItems: 9589,
  status: OrderStatus['EXHIBITED'],
  deliveryMethod: DeliveryMethod['SHIPPING'],
  paymentMethod: PaymentMethod['PAYMENT_IN_RETRIEVAL'],
  createdBy: 'lavender',
  createdDate: dayjs('2023-04-08T02:53'),
  lastModifiedBy: 'Fantastic backing Pizza',
  lastModifiedDate: dayjs('2023-04-07T12:16'),
  userId: 13402,
};

export const sampleWithNewData: NewOrder = {
  totalPrice: 62062,
  totalItems: 25114,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
