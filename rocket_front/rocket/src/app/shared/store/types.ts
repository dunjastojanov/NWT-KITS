import { User } from "src/app/interfaces/User"

export type stateType = {
    user: User | null;
}

export type storeType = {
    loggedUser : stateType
}