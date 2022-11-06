import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
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
import { ContactComponent } from './components/about/contact/contact.component';
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
import { LineChartComponent } from './page/profile-page/profile-statistics/line-chart/line-chart.component';
import { LoginComponent } from './modals/login/login.component';
import { RegisterComponent } from './modals/register/register.component';
import { ForgotPasswordComponent } from './modals/forgot-password/forgot-password.component';
import { IconComponent } from './shared/utils/icon/icon.component';
import { HistoryTableComponent } from './shared/utils/history-table/history-table.component';
import { PaginationComponent } from './shared/utils/pagination/pagination.component';
import { SelectComponent } from './shared/utils/input/select/select.component';
import { EditProfileComponent } from './modals/edit-profile/edit-profile.component';
import { DeleteProfileComponent } from './modals/delete-profile/delete-profile.component';
import { DetailedRouteComponent } from './modals/detailed-route/detailed-route.component';
import { PersonalInformationComponent } from './modals/edit-profile/personal-information/personal-information.component';
import { PaymentInformationComponent } from './modals/edit-profile/payment-information/payment-information.component';
import { ProfilePictureComponent } from './modals/edit-profile/profile-picture/profile-picture.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    HomepageComponent,
    PrimaryButtonComponent,
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
    IconComponent,
    HistoryTableComponent,
    PaginationComponent,
    SelectComponent,
    EditProfileComponent,
    DeleteProfileComponent,
    DetailedRouteComponent,
    PersonalInformationComponent,
    PaymentInformationComponent,
    ProfilePictureComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
