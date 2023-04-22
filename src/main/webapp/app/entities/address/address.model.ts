import dayjs from 'dayjs/esm';
import { IOrder } from 'app/entities/order/order.model';
import { IStore } from 'app/entities/store/store.model';
import { IRestaurant } from 'app/entities/restaurant/restaurant.model';
import { Governorate } from 'app/entities/enumerations/governorate.model';

export interface IAddress {
  id: string;
  street1?: string | null;
  street2?: string | null;
  city?: Governorate | null;
  postalCode?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  orderSA?: Pick<IOrder, 'id'> | null;
  orderBA?: Pick<IOrder, 'id'> | null;
  store?: Pick<IStore, 'id'> | null;
  restaurant?: Pick<IRestaurant, 'id'> | null;
}

export type NewAddress = Omit<IAddress, 'id'> & { id: null };
