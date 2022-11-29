import { Action } from "@ngrx/store";

export enum DestinationsActionType {
    ADD = "ADD",
    REMOVE = "REMOVE",
    UPDATE = "UPDATE",
    ADD_ROUTE_TIME = "ADD_ROUTE_TIME",
    ADD_ROUTE_DISTANCE = "ADD_ROUTE_DISTANCE"
}

export type UpdatePayloadType = {index: number, address: string}

export class DestinationsAction implements Action {
    constructor(readonly type: string, public payload: string | number | UpdatePayloadType) { }
}
