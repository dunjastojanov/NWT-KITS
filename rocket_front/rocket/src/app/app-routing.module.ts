import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomepageComponent} from './page/homepage/homepage.component';
import {ProfilePageComponent} from "./page/profile-page/profile-page.component";
import {GeneralInformationComponent} from "./page/profile-page/general-information/general-information.component";
import {FavouriteRoutesComponent} from "./page/profile-page/favourite-routes/favourite-routes.component";
import {ProfileStatisticsComponent} from "./page/profile-page/profile-statistics/profile-statistics.component";
import {ProfileHistoryComponent} from "./page/profile-page/profile-history/profile-history.component";
import {AdminPageComponent} from "./page/admin-page/admin-page.component";
import {ClientsComponent} from "./page/admin-page/clients/clients.component";
import {DriversComponent} from "./page/admin-page/drivers/drivers.component";
import {AdminHistoryComponent} from "./page/admin-page/admin-history/admin-history.component";
import {AdminStatisticsComponent} from "./page/admin-page/admin-statistics/admin-statistics.component";

const routes: Routes = [
  {path: "", component: HomepageComponent},
  {
    path: "profile", component: ProfilePageComponent, children: [
      {path: "information", component: GeneralInformationComponent},
      {path: "favourites", component: FavouriteRoutesComponent},
      {path: "statistics", component: ProfileStatisticsComponent},
      {path: "history", component: ProfileHistoryComponent},
    ]
  },
  {
    path: "admin", component: AdminPageComponent, children: [
      {path: "information", component: GeneralInformationComponent},
      {path: "clients", component: ClientsComponent},
      {path: "drivers", component: DriversComponent},
      {path: "history", component: AdminHistoryComponent},
      {path: "statistics", component: AdminStatisticsComponent}

    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
