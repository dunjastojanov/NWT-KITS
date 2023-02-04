import {Injectable} from "@angular/core";
import {BehaviorSubject, Observable} from "rxjs";

@Injectable()
export class RouteService {

  private _trigger = new BehaviorSubject<string>('');

  trigger$(): Observable<any> {
    return this._trigger.asObservable();
  }

  setTrigger(trigger: string): void {
    this._trigger.next(trigger);
  }

}
