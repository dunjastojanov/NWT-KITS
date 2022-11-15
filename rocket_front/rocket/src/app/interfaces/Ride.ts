import { sideUser } from "./User";

export interface CurrentRide {
    rideId: string;
    client?: sideUser;
    ridingPals: sideUser[];
    isSplitFair?: boolean;
    driver: sideUser;
    startAddress: string;
    endAddress: string;
    price: number;
    estimatedTime: string;
    vehicleLocation: {longitude: number, latitude: number};
    minutesToCome: number;
    isRouteFavorite?: boolean;
}