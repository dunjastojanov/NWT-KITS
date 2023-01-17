import {Component, OnInit} from '@angular/core';
import {User} from "../../../interfaces/User";
import {UserService} from "../../../services/user/user.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-profile-picture',
  templateUrl: './profile-picture.component.html',
  styleUrls: ['./profile-picture.component.css']
})
export class ProfilePictureComponent implements OnInit {
  user: User | null = null;
  selectedImage: File | null = null;
  profilePicture: string | ArrayBuffer | null = null;
  constructor(private service: UserService, private toastr: ToastrService) {
    this.setUser().then(() => {
      if (this.user !== null) {
        if (this.user.profilePicture !== null) {
        }
      }
    });
  }
  async setUser() {
    this.user = await this.service.getUser();
  }

  ngOnInit(): void {
  }

  onSave() {
    if (this.selectedImage) {
      this.service.editProfileImage(this.selectedImage).then(result => {
        if (result.object) {
          this.toastr.success(result.message)
        }
        }
      ).catch(
        () => {
          this.toastr.error("Error with saving image")
        }
      )
    }
  }

  onFileSelected(event:Event) {
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
