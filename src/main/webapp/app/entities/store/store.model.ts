import dayjs from 'dayjs/esm';

export interface IStore {
  id: string;
  name?: string | null;
  description?: string | null;
  telephone?: string | null;
  imgPath?: string | null;
  isDedicatedGlutenFree?: boolean | null;
  website?: string | null;
  hasDeliveryMode?: boolean | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewStore = Omit<IStore, 'id'> & { id: null };
