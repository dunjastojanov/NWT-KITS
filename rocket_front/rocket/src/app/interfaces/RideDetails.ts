export interface RideDetails {
  id: string;
  driver: string;
  passengers: string[];
  routeLocation: string;
  driverProfileImage: string;
  start: string;
  end: string;
  price: number;
  duration: number;
  rating: number;
  reviews: {
    reviewer: string;
    rating: number;
    text: string;
    reviewerProfileImage: string;
  }[];
}
