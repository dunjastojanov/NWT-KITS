import {Component, Input, OnInit} from '@angular/core';
import {UserService} from "../../services/user/user.service";
import {User} from "../../interfaces/User";
import {ChatService} from "../../services/chat/chat.service";
import {MessageInfo} from "../../interfaces/MessageInfo";

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

  constructor(private userService: UserService, private chatService: ChatService) {
    // TODO moram dodati inicijalizaciju sokete ukoliko je show true

  }
  ngOnInit(): void {
    this.userService.getUser().then(
      result => {
        this.loggedUser = result;
      }
    )
    this.chatService.getRandomAdmin().then(
      result => {
        this.admin = result;
      }
    )
    if (this.admin != null) {
      this.chatService.getMessagesWith(this.admin!.email).then(
        result => {
          this.messages = result;
        }
      )
    }
  }

  changeView(): void {
    this.show = !this.show
  }

  prettyPrintDate(sentAt: Date) {
    return sentAt.toLocaleDateString('en-US');
  }
  sendMessage() {
    const dto = {
      message: this.message,
      receiverEmail: this.admin!.email
    }
    this.chatService.sendMessage(dto).then(
      result => {
        this.messages = result;
      }
    )
  }

  isChatAvailable() {
    return this.loggedUser?.roles.includes("CLIENT") || this.loggedUser?.roles.includes("DRIVER");
  }
}

