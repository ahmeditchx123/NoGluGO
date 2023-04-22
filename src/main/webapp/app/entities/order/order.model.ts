import dayjs from 'dayjs/esm';
import { OrderStatus } from 'app/entities/enumerations/order-status.model';
import { DeliveryMethod } from 'app/entities/enumerations/delivery-method.model';
import { PaymentMethod } from 'app/entities/enumerations/payment-method.model';

export interface IOrder {
  id: string;
  totalPrice?: number | null;
  totalItems?: number | null;
  status?: OrderStatus | null;
  deliveryMethod?: DeliveryMethod | null;
  paymentMethod?: PaymentMethod | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  userId?: number | null;
}

export type NewOrder = Omit<IOrder, 'id'> & { id: null };
