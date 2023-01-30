import { localStorageSync } from 'ngrx-store-localstorage';
import { ActionReducer, MetaReducer } from '@ngrx/store';
import { StoreType } from './shared/store/types';

function localStorageSyncReducer(
  reducer: ActionReducer<StoreType>
): ActionReducer<StoreType> {
  return localStorageSync({
    keys: [
      {
        currentRide: {
          encrypt: (state) => window.btoa(state),
          decrypt: (state) => window.atob(state),
        },
      },
      {
        loggedUser: {
          encrypt: (state) => window.btoa(state),
          decrypt: (state) => window.atob(state),
        },
      },
      {
        notifications: {
          encrypt: (state) => window.btoa(state),
          decrypt: (state) => window.atob(state),
        },
      },
      {
        messages: {
          encrypt: (state) => window.btoa(state),
          decrypt: (state) => window.atob(state),
        },
      },
      {
        activeVehicles: {
          encrypt: (state) => window.btoa(state),
          decrypt: (state) => window.atob(state),
        },
      },
    ],
    rehydrate: true,
  })(reducer);
}

export const metaReducers: Array<MetaReducer<StoreType, any>> = [
  localStorageSyncReducer,
];
