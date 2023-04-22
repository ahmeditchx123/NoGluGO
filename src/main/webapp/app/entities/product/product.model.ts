import dayjs from 'dayjs/esm';
import { ICartItem } from 'app/entities/cart-item/cart-item.model';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { IStore } from 'app/entities/store/store.model';

export interface IProduct {
  id: string;
  name?: string | null;
  sku?: string | null;
  description?: string | null;
  imgPath?: string | null;
  unitPrice?: number | null;
  isAvailable?: boolean | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  cartItem?: Pick<ICartItem, 'id'> | null;
  orderItem?: Pick<IOrderItem, 'id'> | null;
  store?: Pick<IStore, 'id'> | null;
}

export type NewProduct = Omit<IProduct, 'id'> & { id: null };
