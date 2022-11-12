export interface User {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  city?: string;
  phoneNumber?: string;
  status?: string;
  role: string;
  profilePicture: string;
}

export interface sideUser {
  id: string;
  firstName: string;
  lastName: string;
  email?: string;
  profilePicture?: string;
}
