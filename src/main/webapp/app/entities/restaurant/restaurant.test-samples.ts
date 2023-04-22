import dayjs from 'dayjs/esm';

import { IRestaurant, NewRestaurant } from './restaurant.model';

export const sampleWithRequiredData: IRestaurant = {
  id: '9a4f2ee4-d45a-4122-b55a-c0059e33895f',
  name: 'Locks Toys',
  telephone: '302.645.1432 x70308',
  imgPath: 'Road',
  isDedicatedGlutenFree: false,
  tableNumber: 8889,
};

export const sampleWithPartialData: IRestaurant = {
  id: '2462b023-3f4c-46bc-902b-909d19ef7a51',
  name: 'syndicate Ports invoice',
  description: 'withdrawal Home',
  telephone: '923.612.1979',
  imgPath: 'projection',
  isDedicatedGlutenFree: true,
  tableNumber: 18573,
  createdBy: 'Turkish',
  createdDate: dayjs('2023-04-07T18:14'),
};

export const sampleWithFullData: IRestaurant = {
  id: 'afcebf3c-afcd-4427-9fcd-a600091deff0',
  name: 'niches Metal Kids',
  description: 'Director Dollar',
  telephone: '(354) 878-3454',
  imgPath: 'ivory',
  isDedicatedGlutenFree: true,
  website: 'New',
  tableNumber: 37075,
  createdBy: 'Kip',
  createdDate: dayjs('2023-04-07T22:00'),
  lastModifiedBy: 'Keyboard',
  lastModifiedDate: dayjs('2023-04-08T10:17'),
};

export const sampleWithNewData: NewRestaurant = {
  name: 'capacitor',
  telephone: '1-796-715-2900 x614',
  imgPath: 'Algeria open-source',
  isDedicatedGlutenFree: true,
  tableNumber: 86527,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
