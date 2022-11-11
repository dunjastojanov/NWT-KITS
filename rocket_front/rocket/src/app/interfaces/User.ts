export interface User {
  email: string
  firstName: string;
  lastName: string;
  city?: string;
  phoneNumber?: string;
  status?: string;
  roles: string[];
  profilePicture: string;
}
