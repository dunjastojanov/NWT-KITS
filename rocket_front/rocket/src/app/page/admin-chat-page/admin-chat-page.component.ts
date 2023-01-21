import {Component, OnInit} from '@angular/core';
import {http} from "../../shared/api/axios-wrapper";
import {sideUser} from "../../interfaces/User";
import {UserChatInfo} from "../../interfaces/UserChatInfo";
import {MessageInfo} from "../../interfaces/MessageInfo";
import {AdminService} from "../../services/admin/admin.service";


@Component({
  selector: 'app-admin-chat-page',
  templateUrl: './admin-chat-page.component.html',
  styleUrls: ['./admin-chat-page.component.css']
})
export class AdminChatPageComponent implements OnInit {
  users: UserChatInfo[] = []
  loggedAdmin: sideUser | null = null;
  messages: MessageInfo[] = [];
  message: string = "";
  talkingTo: string = "";

  constructor(private adminService: AdminService) {

  }

  ngOnInit(): void {
    //TODO inicijalizuj socket
    this.adminService.getAllAdminsChat().then(
      result => {
        this.users = result;
      }
    );
    this.adminService.getAdminData().then(
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
    this.adminService.sendMessage(dto).then(
      result => {
        this.messages = result;
      }
    )
  }

  getMessagesWith(email: string) {
    this.talkingTo = email;
    this.adminService.getMessagesWith(email).then(result => {
      this.messages = result
    })
  }

  prettyPrintDate(sentAt: Date) {
    return sentAt.toLocaleDateString('en-US');
  }
}
