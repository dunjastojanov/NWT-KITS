import {Component, OnInit} from '@angular/core';
import {User} from "../../../interfaces/User";
import {UserService} from "../../../services/user/user.service";
import {ToastrService} from "ngx-toastr";
import {Store} from "@ngrx/store";
import {StoreType} from "../../../shared/store/types";
import {LoggedUserAction, LoggedUserActionType} from "../../../shared/store/logged-user-slice/logged-user.actions";

@Component({
  selector: 'app-profile-picture',
  templateUrl: './profile-picture.component.html',
  styleUrls: ['./profile-picture.component.css']
})
export class ProfilePictureComponent implements OnInit {
  user: User | null = null;
  selectedImage: File | null = null;
  profilePicture: string | ArrayBuffer | null = null;

  constructor(private store: Store<StoreType>, private service: UserService, private toastr: ToastrService) {
    let loggedUserSlice = store.select('loggedUser');
    loggedUserSlice.subscribe(
      resData => {
        this.user = resData.user;
        if (this.user) {
          this.profilePicture = this.user.profilePicture;
        }
      }
    )
  }

  ngOnInit(): void {
  }

  onSave() {
    if (this.selectedImage) {
      this.service.editProfileImage(this.selectedImage).then(result => {
          if (result.object) {
            this.toastr.success(result.message);
            this.service.refreshUser();
          }

        }
      ).catch(
        () => {
          this.toastr.error("Error with saving image")
        }
      )
    }
  }

  onFileSelected(event: Event) {
    const target = event.target as HTMLInputElement;
    if (target.files && target.files.length > 0) {
      this.selectedImage = target.files[0];
      this.displayPreview(this.selectedImage);
    }
  }

  private displayPreview(file: Blob) {
    const reader = new FileReader();
    reader.onload = () => this.profilePicture = reader.result;
    reader.readAsDataURL(file);
  }
}
