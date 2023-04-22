import dayjs from 'dayjs/esm';

export interface ICart {
  id: string;
  totalItems?: number | null;
  totalPrice?: number | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  userId?: number | null;
}

export type NewCart = Omit<ICart, 'id'> & { id: null };
