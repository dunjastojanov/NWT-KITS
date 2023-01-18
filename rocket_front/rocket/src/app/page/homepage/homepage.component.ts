import { Component, OnInit } from '@angular/core';
import { SocketService } from 'src/app/services/sockets/sockets.service';

@Component({
  selector: 'homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css'],
})
export class HomepageComponent implements OnInit {
  constructor(private socketService: SocketService) {}

  ngOnInit(): void {}

  initSockets() {
    this.socketService.initializeWebSocketConnection();
  }

  sendMessage() {
    this.socketService.sendMessageUsingSocket();
  }
}
