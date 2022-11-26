import { Action } from "@ngrx/store";

export enum DestinationsActionType {
    ADD = "ADD",
    REMOVE = "REMOVE",
    UPDATE = "UPDATE"
}

export type UpdatePayloadType = {index: number, address: string}

export class DestinationsAction implements Action {
    constructor(readonly type: string, public payload: string | number | UpdatePayloadType) { }
}
