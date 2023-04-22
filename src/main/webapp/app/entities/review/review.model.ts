import dayjs from 'dayjs/esm';
import { IMenuItem } from 'app/entities/menu-item/menu-item.model';
import { IProduct } from 'app/entities/product/product.model';

export interface IReview {
  id: string;
  title?: string | null;
  content?: string | null;
  rating?: number | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  userId?: number | null;
  menuItem?: Pick<IMenuItem, 'id'> | null;
  product?: Pick<IProduct, 'id'> | null;
}

export type NewReview = Omit<IReview, 'id'> & { id: null };
