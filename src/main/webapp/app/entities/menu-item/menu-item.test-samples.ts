import dayjs from 'dayjs/esm';

import { IMenuItem, NewMenuItem } from './menu-item.model';

export const sampleWithRequiredData: IMenuItem = {
  id: 'd3c2616a-122e-4e65-8e5d-d664b4da3ccf',
  name: 'International transmit',
  content: 'Technician Manager Buckinghamshire',
  imgPath: 'task-force Bacon Home',
  unitPrice: 99166,
};

export const sampleWithPartialData: IMenuItem = {
  id: 'a4774de2-5b99-4e9c-abca-54dcf9e1d2a3',
  name: 'Account',
  content: 'Legacy Kids Borders',
  imgPath: 'payment Ergonomic',
  unitPrice: 98038,
  createdBy: 'toolset Berkshire',
  createdDate: dayjs('2023-04-07T17:05'),
  lastModifiedBy: 'frictionless',
  lastModifiedDate: dayjs('2023-04-08T03:56'),
};

export const sampleWithFullData: IMenuItem = {
  id: '7b7f6b1c-1ce5-4472-913a-69ccb6d3ac9c',
  name: 'parse Pre-emptive',
  content: 'Orchestrator',
  imgPath: 'bypassing Awesome',
  unitPrice: 74126,
  createdBy: 'haptic defect applications',
  createdDate: dayjs('2023-04-07T22:37'),
  lastModifiedBy: 'Soft Vatu Licensed',
  lastModifiedDate: dayjs('2023-04-07T17:32'),
};

export const sampleWithNewData: NewMenuItem = {
  name: 'optimize',
  content: 'Ameliorated application Buckinghamshire',
  imgPath: 'Managed',
  unitPrice: 98263,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
