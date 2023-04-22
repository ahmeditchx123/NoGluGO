import dayjs from 'dayjs/esm';

export interface IRestaurant {
  id: string;
  name?: string | null;
  description?: string | null;
  telephone?: string | null;
  imgPath?: string | null;
  isDedicatedGlutenFree?: boolean | null;
  website?: string | null;
  tableNumber?: number | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewRestaurant = Omit<IRestaurant, 'id'> & { id: null };
