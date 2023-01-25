export interface Notif {
  title: string;
  id: string;
  html: string;
  userId: string;
  type: "DRIVER_RIDE_REQUEST" | "PASSENGER_RIDE_REQUEST" | "UPDATE_DRIVER_DATA_REQUEST" | "UPDATE_DRIVER_PICTURE_REQUEST" | "RIDE_CANCELED" | "RIDE_CONFIRMED" | "RIDE_SCHEDULED" | "USER_BLOCKED" |"RIDE_REVIEW";
  read: boolean;
  entityId: string;
  sent: string[];
}
