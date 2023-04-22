import dayjs from 'dayjs/esm';

import { IArticle, NewArticle } from './article.model';

export const sampleWithRequiredData: IArticle = {
  id: '98a873ee-1442-4553-9412-ea586fba6b1e',
  name: 'channels Sports Poland',
  content: 'SAS Ball bricks-and-clicks',
};

export const sampleWithPartialData: IArticle = {
  id: '9567b618-f61b-4750-9b54-313f44f21b54',
  name: 'Devolved Supervisor solid',
  content: 'haptic',
  imgPath: 'end-to-end Cloned',
  createdBy: 'Engineer PCI Generic',
  lastModifiedBy: 'leverage deposit Multi-layered',
  userId: 42885,
};

export const sampleWithFullData: IArticle = {
  id: '959ca17c-e602-4303-89da-dbed5afe93cc',
  name: 'Burg International sticky',
  content: 'Table Pants Dinar',
  imgPath: 'engage',
  createdBy: 'neural synergies',
  createdDate: dayjs('2023-04-08T07:24'),
  lastModifiedBy: 'calculate Unbranded communities',
  lastModifiedDate: dayjs('2023-04-08T05:48'),
  userId: 77890,
};

export const sampleWithNewData: NewArticle = {
  name: 'Mexican',
  content: 'connect',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
