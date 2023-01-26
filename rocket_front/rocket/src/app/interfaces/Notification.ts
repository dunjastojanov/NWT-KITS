export interface Notif {
  title: string;
  id: string;
  html: string;
  userId: string;
  type:
    | 'DRIVER_RIDE_REQUEST'
    | 'PASSENGER_RIDE_REQUEST'
    | 'UPDATE_DRIVER_REQUEST'
    | 'RIDE_CANCELED'
    | 'RIDE_CONFIRMED'
    | 'RIDE_SCHEDULED';
  read: boolean;
  entityId: string;
  sent: string[] | Date;
}
