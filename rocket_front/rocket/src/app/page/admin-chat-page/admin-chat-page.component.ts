import {Component, OnInit} from '@angular/core';
import {User} from "../../interfaces/User";
import {UserChatInfo} from "../../interfaces/UserChatInfo";
import {MessageInfo} from "../../interfaces/MessageInfo";
import {AdminService} from "../../services/admin/admin.service";
import {UserService} from "../../services/user/user.service";
import {ChatService} from "../../services/chat/chat.service";
import {ToastrService} from "ngx-toastr";
import {Store} from "@ngrx/store";
import {StoreType} from "../../shared/store/types";
import {MessageAction, MessageActionType} from "../../shared/store/message-slice/message.actions";


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

  constructor(private adminService: AdminService,
              private userService: UserService,
              private chatService: ChatService,
              private toastService: ToastrService,
              private store: Store<StoreType>) {
    this.userService.getUser().then(
      result => {
        this.loggedAdmin = result;
      }
    );

    this.adminService.getAllAdminsChat().then(
      result => {
        this.users = result;
        if (this.users.length === 0) {
          return;
        }
        this.store.select('messages').subscribe(
          resData => {
            this.talkingTo = this.users[0].email
            console.log(resData.messages)
            this.messages = resData.messages
              .filter(message => {
                return message.receiver === this.talkingTo || message.sender === this.talkingTo;
              });
          }
        )
      }
    );

  }

  ngOnInit(): void {
    // this.userService.getUser().then(
    //   result => {
    //     this.loggedAdmin = result;
    //   }
    // );
    //
    // this.adminService.getAllAdminsChat().then(
    //   result => {
    //     this.users = result;
    //     if (this.users.length === 0) {
    //       return;
    //     }
    //     this.store.select('messages').subscribe(
    //       resData => {
    //         this.talkingTo = this.users[0].email
    //         console.log(resData.messages)
    //         this.messages = resData.messages
    //           .filter(message => {
    //             return message.receiver === this.talkingTo || message.sender === this.talkingTo;
    //           });
    //       }
    //     )
    //   }
    // );

  }

  sendMessage() {
    const dto = {
      message: this.message,
      receiverEmail: this.talkingTo
    }
    if (this.message === "") {
      this.toastService.error("Message mustn't be blank")
    } else if (this.talkingTo === "") {
      this.toastService.error("You have to choose user")
    } else {
      this.chatService.sendMessage(dto).then(
        result => {
          this.store.dispatch(
            new MessageAction(
              MessageActionType.SET_MESSAGES,
              result
            )
          );
        }
      )
    }
    this.message = "";
  }

  getMessagesWith(email: string) {
    this.talkingTo = email;
    this.store.select('messages').subscribe(
      resData => {
        console.log(resData.messages)
        this.messages = resData.messages.filter(message => {
          return message.receiver === email || message.sender === email;
        });
      }
    )

  }

}
