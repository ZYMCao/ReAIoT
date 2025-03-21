import { Component } from '@angular/core';
import { ChatContentComponent } from './chat-content/chat-content.component';
import { SidebarComponent } from '../sidebar/sidebar.component';

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [ChatContentComponent, SidebarComponent],
  templateUrl: './chat.component.html',
  styleUrl: './chat.component.scss'
})
export class ChatComponent {
  constructor() {
  }
}
