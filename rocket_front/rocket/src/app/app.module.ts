import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import {
  SocialLoginModule,
  GoogleLoginProvider,
  FacebookLoginProvider,
} from '@abacritt/angularx-social-login';
import { NgxStarsModule } from 'ngx-stars';
import { AuthGuardService } from './auth-guard.service';
import { AppRoutingModule } from './app-routing.module';
import { StoreModule } from '@ngrx/store';
import { AppComponent } from './app.component';
import { NavbarComponent } from './navbar/navbar.component';
import { HomepageComponent } from './page/homepage/homepage.component';
import { PrimaryButtonComponent } from './shared/utils/buttons/primary/primary.component';
import { BannerComponent } from './page/homepage/banner/banner.component';
import { CalculateInfoComponent } from './page/homepage/calculate-info/calculate-info.component';
import { TitleComponent } from './shared/utils/label/title/title.component';
import { MapInfoComponent } from './shared/utils/map/map-info/map-info.component';
import { MapComponent } from './shared/utils/map/map/map.component';
import { ChooseRouteComponent } from './components/routes/choose-route/choose-route.component';
import { InputComponent } from './shared/utils/input/input/input.component';
import { SecondaryComponent } from './shared/utils/buttons/secondary/secondary.component';
import { AboutComponent } from './components/about/about.component';
import { LocationsComponent } from './components/about/locations/locations.component';
import { ContactComponent } from './components/contact/contact.component';
import { LocationCardComponent } from './components/about/locations/location-card/location-card.component';
import { LabelComponent } from './shared/utils/label/label/label.component';
import { CalculateShowRoutesComponent } from './components/routes/calculate-show-routes/calculate-show-routes.component';
import { ProfilePageComponent } from './page/profile-page/profile-page.component';
import { VerticalNavbarComponent } from './vertical-navbar/vertical-navbar.component';
import { GeneralInformationComponent } from './page/profile-page/general-information/general-information.component';
import { FavouriteRoutesComponent } from './page/profile-page/favourite-routes/favourite-routes.component';
import { ProfileStatisticsComponent } from './page/profile-page/profile-statistics/profile-statistics.component';
import { ProfileHistoryComponent } from './page/profile-page/profile-history/profile-history.component';
import { ProfileBannerComponent } from './page/profile-page/profile-banner/profile-banner.component';
import { InfoComponent } from './shared/utils/label/info/info.component';
import { MediumTitleComponent } from './shared/utils/label/medium-title/medium-title.component';
import { LineChartComponent } from './shared/line-chart/line-chart.component';
import { LoginComponent } from './modals/login/login.component';
import { RegisterComponent } from './modals/register/register.component';
import { ForgotPasswordComponent } from './modals/forgot-password/forgot-password.component';
import { IconComponent } from './shared/utils/icon/icon.component';
import { HistoryTableComponent } from './shared/utils/history-table/history-table.component';
import { PaginationComponent } from './shared/utils/pagination/pagination.component';
import { SelectComponent } from './shared/utils/input/select/select.component';
import { EditProfileComponent } from './modals/edit-profile/edit-profile.component';
import { DetailedRouteComponent } from './modals/detailed-route/detailed-route.component';
import { PersonalInformationComponent } from './modals/edit-profile/personal-information/personal-information.component';
import { ProfilePictureComponent } from './modals/edit-profile/profile-picture/profile-picture.component';
import { WordCeoComponent } from './page/homepage/word-ceo/word-ceo.component';
import { StatisticsComponent } from './page/homepage/statistics/statistics.component';
import { AdminPageComponent } from './page/admin-page/admin-page.component';
import { DriversComponent } from './page/admin-page/drivers/drivers.component';
import { ClientsComponent } from './page/admin-page/clients/clients.component';
import { UserCardComponent } from './shared/utils/user-card/user-card.component';
import { SearchComponent } from './shared/utils/search/search.component';
import { UserInfoComponent } from './modals/user-info/user-info.component';
import { IconButtonSecondaryComponent } from './shared/utils/buttons/icon-button-secondary/icon-button-secondary.component';
import { AdminHistoryComponent } from './page/admin-page/admin-history/admin-history.component';
import { AdminStatisticsComponent } from './page/admin-page/admin-statistics/admin-statistics.component';
import { RegisterDriverComponent } from './modals/register-driver/register-driver.component';
import { MultiSelectWithIconsComponent } from './shared/utils/input/multi-select-with-icons/multi-select-with-icons.component';
import { ErrorComponent } from './modals/error/error.component';
import { SuccessComponent } from './modals/success/success.component';
import { loggedUserReducer } from './shared/store/logged-user-slice/logged-user.reducer';
import { destinationsReducer } from './shared/store/destinations-slice/destinations.reducer';
import { CurrentRidePageComponent } from './page/current-ride-page/current-ride-page.component';
import { CurrentRideInfoComponent } from './components/current-ride-info/current-ride-info.component';
import { FieldComponent } from './components/current-ride-info/field/field.component';
import { CheckboxComponent } from './shared/utils/checkbox/checkbox.component';
import { ReportDriverComponent } from './modals/report-driver/report-driver.component';
import { CurrentRideRouteComponent } from './components/current-ride-info/current-ride-route/current-ride-route.component';
import { CurrentRidePalsComponent } from './components/current-ride-info/current-ride-pals/current-ride-pals.component';
import { ButtonIconComponent } from './shared/utils/buttons/button-icon/button-icon.component';
import { CurrentRideClientComponent } from './components/current-ride-info/current-ride-client/current-ride-client.component';
import { CurrentRideButtonsComponent } from './components/current-ride-info/current-ride-buttons/current-ride-buttons.component';
import { metaReducers } from './local-storage.service';
import { InputDestinationComponent } from './components/routes/input-destination/input-destination.component';
import { RouteService } from './components/routes/route.service';
import { RouteInfoComponent } from './components/routes/route-info/route-info.component';
import { ChooseRoleComponent } from './modals/choose-role/choose-role.component';
import { ToastrModule } from 'ngx-toastr';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { RideRequestPageComponent } from './page/ride-request-page/ride-request-page.component';
import { RouteComponent } from './page/ride-request-page/route/route.component';
import { RequestNavbarComponent } from './page/ride-request-page/request-navbar/request-navbar.component';
import { DataInfoComponent } from './page/ride-request-page/data-info/data-info.component';
import { RideInfoReducer } from './shared/store/ride-info-slice/ride-info.reducer';
import { ShowOnMapComponent } from './shared/utils/map/show-on-map/show-on-map.component';
import { SocketService } from './services/sockets/sockets.service';
import { PaypalComponent } from './modals/paypal/paypal.component';
import { ViewMoreButtonComponent } from './shared/utils/history-table/view-more-button/view-more-button.component';
import { StarComponent } from './modals/detailed-route/large-star/star.component';
import { ReviewComponent } from './modals/review/review.component';
import { NotificationComponent } from './modals/notification/notification.component';
import { NotificationListItemComponent } from './shared/utils/notification-list-item/notification-list-item.component';
import { EmbeddedHtmlComponent } from './modals/notification/embeded-html/embedded-html.component';
import { SortPipe } from './shared/utils/history-table/sort.pipe';
import {ConfirmRideComponent} from "./page/ride-request-page/confirm-ride/confirm-ride.component";
import { CurrentRideReducer } from './shared/store/current-ride-slice/current-ride.reducer';
import { NotificationsReducer } from './shared/store/notifications-slice/notifications.reducer';
import { LobyComponent } from './page/ride-request-page/loby/loby.component';
import { StatusToggleComponent } from './navbar/status-toggle/status-toggle.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    HomepageComponent,
    PrimaryButtonComponent,
    ConfirmRideComponent,
    BannerComponent,
    CalculateInfoComponent,
    TitleComponent,
    MapInfoComponent,
    MapComponent,
    ChooseRouteComponent,
    InputComponent,
    SecondaryComponent,
    AboutComponent,
    LocationsComponent,
    ContactComponent,
    LocationCardComponent,
    LabelComponent,
    CalculateShowRoutesComponent,
    ProfilePageComponent,
    VerticalNavbarComponent,
    GeneralInformationComponent,
    FavouriteRoutesComponent,
    ProfileStatisticsComponent,
    ProfileHistoryComponent,
    ProfileBannerComponent,
    InfoComponent,
    MediumTitleComponent,
    LineChartComponent,
    CalculateShowRoutesComponent,
    LoginComponent,
    RegisterComponent,
    ForgotPasswordComponent,
    HistoryTableComponent,
    PaginationComponent,
    SelectComponent,
    EditProfileComponent,
    DetailedRouteComponent,
    PersonalInformationComponent,
    ProfilePictureComponent,
    IconComponent,
    WordCeoComponent,
    StatisticsComponent,
    AdminPageComponent,
    DriversComponent,
    ClientsComponent,
    UserCardComponent,
    SearchComponent,
    UserInfoComponent,
    IconButtonSecondaryComponent,
    AdminHistoryComponent,
    AdminStatisticsComponent,
    RegisterDriverComponent,
    MultiSelectWithIconsComponent,
    ErrorComponent,
    SuccessComponent,
    CurrentRidePageComponent,
    CurrentRideInfoComponent,
    FieldComponent,
    CheckboxComponent,
    ReportDriverComponent,
    CurrentRideRouteComponent,
    CurrentRidePalsComponent,
    ButtonIconComponent,
    CurrentRideClientComponent,
    CurrentRideButtonsComponent,
    InputDestinationComponent,
    RouteInfoComponent,
    ChooseRoleComponent,
    RideRequestPageComponent,
    RouteComponent,
    RequestNavbarComponent,
    ViewMoreButtonComponent,
    StarComponent,
    ReviewComponent,
    PaypalComponent,
    NotificationComponent,
    NotificationListItemComponent,
    DataInfoComponent,
    ShowOnMapComponent,
    EmbeddedHtmlComponent,
    LobyComponent,
    SortPipe,
    StatusToggleComponent,
  ],
  imports: [
    HttpClientModule,
    CommonModule,
    DragDropModule,
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    BrowserAnimationsModule,
    NgxStarsModule,
    ToastrModule.forRoot({
      timeOut: 3000,
      positionClass: 'toast-center-center',
      preventDuplicates: true,
    }),
    SocialLoginModule,
    StoreModule.forRoot(
      {
        loggedUser: loggedUserReducer,
        destinations: destinationsReducer,
        rideInfo: RideInfoReducer,
        currentRide: CurrentRideReducer,
        notifications: NotificationsReducer,
      },
      { metaReducers }
    ),
    SocialLoginModule,
  ],
  providers: [
    SocketService,
    RouteService,
    {
      provide: 'SocialAuthServiceConfig',
      useValue: {
        autoLogin: false, //keeps the user signed in
        providers: [
          {
            id: GoogleLoginProvider.PROVIDER_ID,
            provider: new GoogleLoginProvider(
              '863688763477-8llkkdmr3a6nf9m1hbubp7atv69eniu3.apps.googleusercontent.com'
            ), // your client id
          },
          {
            id: FacebookLoginProvider.PROVIDER_ID,
            provider: new FacebookLoginProvider(
              '863688763477-8llkkdmr3a6nf9m1hbubp7atv69eniu3.apps.googleusercontent.com'
            ),
          },
        ],
      },
    },
    AuthGuardService,
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
