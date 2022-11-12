import { sideUser } from "./User";

export interface CurrentRide {
    rideId: string;
    ridingPals: sideUser[];
    isSplitFair: boolean;
    driver: sideUser;
    startAddress: string;
    endAddress: string;
    price: number;
    estimatedTime: string;
    vehicleLocation: {longitude: number, latitude: number};
    minutesToCome: number;
    isRouteFavorite: boolean;
}