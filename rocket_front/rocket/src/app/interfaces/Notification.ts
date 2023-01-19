export interface Notification {
  title: string;
  html: string;
  userId: string;
  type: "DRIVER_RIDE_REQUEST" | "PASSENGER_RIDE_REQUEST" | "UPDATE_DRIVER_REQUEST" | "BASIC";
  read: boolean;
  resourceId: string;
}
