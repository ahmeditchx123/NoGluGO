import dayjs from 'dayjs/esm';
import { IRestaurant } from 'app/entities/restaurant/restaurant.model';

export interface IMenu {
  id: string;
  name?: string | null;
  description?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  restaurant?: Pick<IRestaurant, 'id'> | null;
}

export type NewMenu = Omit<IMenu, 'id'> & { id: null };
