import dayjs from 'dayjs/esm';
import { ICart } from 'app/entities/cart/cart.model';

export interface ICartItem {
  id: string;
  qty?: number | null;
  totalPrice?: number | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  cart?: Pick<ICart, 'id'> | null;
}

export type NewCartItem = Omit<ICartItem, 'id'> & { id: null };
