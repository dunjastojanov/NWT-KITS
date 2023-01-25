import {Component, OnInit} from '@angular/core';
import {User} from "../../interfaces/User";
import {UserChatInfo} from "../../interfaces/UserChatInfo";
import {MessageInfo} from "../../interfaces/MessageInfo";
import {AdminService} from "../../services/admin/admin.service";
import {UserService} from "../../services/user/user.service";
import {ChatService} from "../../services/chat/chat.service";
import {ToastrService} from "ngx-toastr";


@Component({
  selector: 'app-admin-chat-page',
  templateUrl: './admin-chat-page.component.html',
  styleUrls: ['./admin-chat-page.component.css']
})
export class AdminChatPageComponent implements OnInit {
  users: UserChatInfo[] = []
  loggedAdmin: User | null = null;
  messages: MessageInfo[] = [];
  message: string = "";
  talkingTo: string = "";

  constructor(private adminService: AdminService, private userService: UserService, private chatService: ChatService, private toastService: ToastrService) {

  }

  ngOnInit(): void {
    //TODO inicijalizuj socket
    this.adminService.getAllAdminsChat().then(
      result => {
        this.users = result;
      }
    );
    this.userService.getUser().then(
      result => {
        this.loggedAdmin = result;
      }
    );
  }

  sendMessage() {
    const dto = {
      message: this.message,
      receiverEmail: this.talkingTo
    }
    console.log(this.talkingTo)
    if (this.message === "") {
      this.toastService.error("Message mustn't be blank")
    } else if (this.talkingTo === "") {
      this.toastService.error("You have to choose user")
    } else {
      this.chatService.sendMessage(dto).then(
        result => {
          this.messages = result;
        }
      )
    }
  }

  getMessagesWith(email: string) {
    this.talkingTo = email;
    this.chatService.getMessagesWith(email).then(result => {
      this.messages = result
    })
  }

}
