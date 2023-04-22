import dayjs from 'dayjs/esm';

import { IMenu, NewMenu } from './menu.model';

export const sampleWithRequiredData: IMenu = {
  id: '88806d50-46b7-4116-8da8-88d81caf40ee',
  name: 'Cambridgeshire',
};

export const sampleWithPartialData: IMenu = {
  id: 'df50f42c-b6af-4584-a162-f8bc03fa6303',
  name: 'JBOD Programmable',
  description: 'neural Streets',
  createdDate: dayjs('2023-04-07T14:02'),
  lastModifiedBy: 'Distributed Tokelau viral',
};

export const sampleWithFullData: IMenu = {
  id: '45fe92dd-dbe7-4b0e-9500-8b79875d5839',
  name: 'sensor Wooden bandwidth',
  description: 'Indonesia HTTP',
  createdBy: 'Account',
  createdDate: dayjs('2023-04-07T21:31'),
  lastModifiedBy: 'blue Operative',
  lastModifiedDate: dayjs('2023-04-08T07:21'),
};

export const sampleWithNewData: NewMenu = {
  name: 'Buckinghamshire',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
