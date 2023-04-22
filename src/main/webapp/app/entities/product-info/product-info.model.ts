import dayjs from 'dayjs/esm';
import { IProduct } from 'app/entities/product/product.model';

export interface IProductInfo {
  id: string;
  qtyInStock?: number | null;
  isGlutenFree?: boolean | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  product?: Pick<IProduct, 'id'> | null;
}

export type NewProductInfo = Omit<IProductInfo, 'id'> & { id: null };
