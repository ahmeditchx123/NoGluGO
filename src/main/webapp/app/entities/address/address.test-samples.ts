import dayjs from 'dayjs/esm';

import { Governorate } from 'app/entities/enumerations/governorate.model';

import { IAddress, NewAddress } from './address.model';

export const sampleWithRequiredData: IAddress = {
  id: 'ebb15bca-82ee-471f-9fe5-759198cd6970',
  street1: 'Missouri',
  postalCode: 'Outdoors',
};

export const sampleWithPartialData: IAddress = {
  id: '471e71a2-0d55-4d29-adc1-58f2c2ec55da',
  street1: 'Italy',
  street2: 'Re-contextualized open-source override',
  postalCode: 'maximize withdrawal',
};

export const sampleWithFullData: IAddress = {
  id: 'b7a9c8a7-5c4f-4c5e-a6e5-00257b2e84f3',
  street1: 'Steel Mouse red',
  street2: 'solid',
  city: Governorate['BEJA'],
  postalCode: 'Circle',
  createdBy: 'neutral',
  createdDate: dayjs('2023-04-08T00:12'),
  lastModifiedBy: 'tangible',
  lastModifiedDate: dayjs('2023-04-08T09:25'),
};

export const sampleWithNewData: NewAddress = {
  street1: 'Division Concrete exploit',
  postalCode: 'modular',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
