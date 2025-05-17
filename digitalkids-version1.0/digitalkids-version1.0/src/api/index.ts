export { default as api } from './axios';
import * as userApi from './user';
import * as healthApi from './health';
import * as encyclopediaApi from './encyclopedia';
import * as kidsApi from './kids';
import * as productApi from './product';
import * as cartApi from './cart';
import * as orderApi from './order';
import * as addressApi from './address';
import * as searchApi from './search';
import * as adminApi from './admin';

export {
  userApi,
  healthApi,
  encyclopediaApi,
  kidsApi,
  productApi,
  cartApi,
  orderApi,
  addressApi,
  searchApi,
  adminApi
};

import api from './axios';
export default api; 