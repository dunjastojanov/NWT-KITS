import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomepageComponent } from './page/homepage/homepage.component';
import {ProfilePageComponent} from "./page/profile-page/profile-page.component";
import {GeneralInformationComponent} from "./page/profile-page/general-information/general-information.component";
import {FavouriteRoutesComponent} from "./page/profile-page/favourite-routes/favourite-routes.component";
import {ProfileStatisticsComponent} from "./page/profile-page/profile-statistics/profile-statistics.component";
import {ProfileHistoryComponent} from "./page/profile-page/profile-history/profile-history.component";

const routes: Routes = [
  {path: "", component: HomepageComponent},
  {path: "profile", component: ProfilePageComponent, children: [
    {path: "information", component: GeneralInformationComponent},
    {path: "favourites", component: FavouriteRoutesComponent},
    {path: "statistics", component: ProfileStatisticsComponent},
    {path: "history", component: ProfileHistoryComponent},
  ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
