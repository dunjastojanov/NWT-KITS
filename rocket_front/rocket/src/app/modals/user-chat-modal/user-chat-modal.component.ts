import {Component, Input, OnInit} from '@angular/core';
import {UserService} from "../../services/user/user.service";
import {User} from "../../interfaces/User";
import {ChatService} from "../../services/chat/chat.service";
import {MessageInfo} from "../../interfaces/MessageInfo";
import {Store} from "@ngrx/store";
import {StoreType} from "../../shared/store/types";
import {ToastrService} from "ngx-toastr";
import {MessageAction, MessageActionType} from "../../shared/store/message-slice/message.actions";

@Component({
  selector: 'app-user-chat-modal',
  templateUrl: './user-chat-modal.component.html',
  styleUrls: ['./user-chat-modal.component.css']
})
export class UserChatModalComponent implements OnInit {

  @Input('open') open!: boolean;
  @Input('closeFunc') closeFunc!: () => void;

  show: boolean = true;

  loggedUser: User | null = null;

  admin: User | null = null;

  messages: MessageInfo[] = [];
  message: string = "";

  constructor(private userService: UserService,
              private chatService: ChatService,
              private store: Store<StoreType>,
              private toastService: ToastrService
  ) {


  }

  ngOnInit(): void {
    let loggedUserSlice = this.store.select('loggedUser');
    loggedUserSlice.subscribe(
      resData => {
        this.loggedUser = resData.user;
        if (this.isChatAvailable()) {
          this.chatService.getRandomAdmin().then(
            result => {
              this.admin = result;
              if (this.admin != null) {
                this.store.select('messages').subscribe(
                  resData => {
                    this.messages = resData.messages.filter(message => {
                      return message.receiver === this.admin?.email || message.sender === this.admin?.email;
                    });
                  }
                )
              }
            }
          )
        }
      }
    )
  }

  changeView(): void {
    this.show = !this.show
  }

  sendMessage() {
    const dto = {
      message: this.message,
      receiverEmail: this.admin!.email
    }
    if (this.message === "") {
      this.toastService.error("Message mustn't be blank")
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

  isChatAvailable() {
    return this.loggedUser?.roles.includes("CLIENT") || this.loggedUser?.roles.includes("DRIVER");
  }
}

