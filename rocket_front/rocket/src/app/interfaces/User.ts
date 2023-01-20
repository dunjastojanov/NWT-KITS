import { UserRidingStatus } from './Ride';

export interface User {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  city: string;
  phoneNumber: string;
  profilePicture: string;
  status?: string;
  roles: string[];
}

export interface sideUser {
  id?: string;
  firstName: string;
  lastName: string;
  email: string;
  profilePicture: string;
  role?: string;
}

export interface RidingPal {
  id?: string;
  firstName: string;
  lastName: string;
  email: string;
  profilePicture: string;
  role?: string;
  status?: UserRidingStatus;
}
