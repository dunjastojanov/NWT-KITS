import {Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store'
import {User} from '../interfaces/User';
import {LoggedUserAction, LoggedUserActionType} from '../shared/store/logged-user-slice/logged-user.actions';


import {StoreType} from '../shared/store/types';

@Component({
  selector: 'navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  showLoginModal = false;
  showRegisterModal = false;
  user: User | null = null;

  constructor(private store: Store<StoreType>) {
    let loggedUserSlice = store.select('loggedUser');
    loggedUserSlice.subscribe(
      resData => {
        this.user = resData.user;
      }
    )
  }

  hasRole(role: string): boolean {
    return this.user !== null && this.user?.roles.indexOf(role) !== -1;
  }

  ngOnInit(): void {

  }

  toggleLogin = (): void => {
    this.showRegisterModal = false;
    this.showLoginModal = !this.showLoginModal;
  }

  logout = (): void => {
    this.store.dispatch(new LoggedUserAction(LoggedUserActionType.LOGOUT));
  }

  toggleRegister = (): void => {
    this.showLoginModal = false;
    this.showRegisterModal = !this.showRegisterModal;
  }
}
