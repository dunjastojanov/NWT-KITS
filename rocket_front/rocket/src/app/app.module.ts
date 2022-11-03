import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './navbar/navbar.component';
import { HomepageComponent } from './page/homepage/homepage.component';
import { PrimaryButtonComponent } from './utils/buttons/primary/primary.component';
import { BannerComponent } from './page/homepage/banner/banner.component';
import { CalculateInfoComponent } from './page/homepage/calculate-info/calculate-info.component';
import { TitleComponent } from './utils/label/title/title.component';
import { MapInfoComponent } from './utils/map/map-info/map-info.component';
import { MapComponent } from './utils/map/map/map.component';
import { ChooseRouteComponent } from './components/routes/choose-route/choose-route.component';
import { InputComponent } from './utils/input/input/input.component';
import { SecondaryComponent } from './utils/buttons/secondary/secondary.component';
import { AboutComponent } from './components/about/about.component';
import { LocationsComponent } from './components/about/locations/locations.component';
import { ContactComponent } from './components/about/contact/contact.component';
import { LocationCardComponent } from './components/about/locations/location-card/location-card.component';
import { LabelComponent } from './utils/label/label/label.component';
import { CalculateShowRoutesComponent } from './components/routes/calculate-show-routes/calculate-show-routes.component';

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
    CalculateShowRoutesComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
