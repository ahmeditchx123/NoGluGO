import dayjs from 'dayjs/esm';
import { IOrder } from 'app/entities/order/order.model';

export interface IOrderItem {
  id: string;
  qty?: number | null;
  totalPrice?: number | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  order?: Pick<IOrder, 'id'> | null;
}

export type NewOrderItem = Omit<IOrderItem, 'id'> & { id: null };
