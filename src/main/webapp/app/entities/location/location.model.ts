import dayjs from 'dayjs/esm';
import { IAddress } from 'app/entities/address/address.model';

export interface ILocation {
  id: string;
  lattitude?: number | null;
  longtitude?: number | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  address?: Pick<IAddress, 'id'> | null;
}

export type NewLocation = Omit<ILocation, 'id'> & { id: null };
