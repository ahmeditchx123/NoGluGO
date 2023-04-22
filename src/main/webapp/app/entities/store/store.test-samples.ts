import dayjs from 'dayjs/esm';

import { IStore, NewStore } from './store.model';

export const sampleWithRequiredData: IStore = {
  id: '156f3007-ab41-48df-ae38-6c5c063a4222',
  name: 'Parkway Gorgeous',
  telephone: '1-649-862-9240',
  imgPath: 'Principal invoice transmitter',
  isDedicatedGlutenFree: false,
  hasDeliveryMode: true,
};

export const sampleWithPartialData: IStore = {
  id: 'cd525571-7760-4fe8-b5f2-cde98124e2d4',
  name: 'quantifying',
  description: 'Intelligent program metrics',
  telephone: '396.506.5863 x093',
  imgPath: 'proactive',
  isDedicatedGlutenFree: false,
  hasDeliveryMode: false,
  lastModifiedDate: dayjs('2023-04-08T07:58'),
};

export const sampleWithFullData: IStore = {
  id: '7e1e9e9f-371a-4889-8352-46a26e8d9e47',
  name: 'Applications',
  description: 'Turnpike Soap',
  telephone: '663.467.0395 x5313',
  imgPath: 'intermediate scalable Integration',
  isDedicatedGlutenFree: true,
  website: 'syndicate',
  hasDeliveryMode: false,
  createdBy: 'matrix',
  createdDate: dayjs('2023-04-08T03:52'),
  lastModifiedBy: 'hybrid Islands,',
  lastModifiedDate: dayjs('2023-04-07T19:44'),
};

export const sampleWithNewData: NewStore = {
  name: 'homogeneous Carolina',
  telephone: '1-815-261-9583 x31715',
  imgPath: 'Berkshire deposit',
  isDedicatedGlutenFree: false,
  hasDeliveryMode: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
