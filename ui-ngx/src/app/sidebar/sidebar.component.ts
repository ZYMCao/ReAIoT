import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ChatService } from '../services/chat.service';
import { ChatDataService } from '../services/chat-data.service';
import { ChatCompletionMessage } from 'openai/resources';
import { v4 as uuidv4 } from 'uuid';
import { NgForOf, NgIf } from '@angular/common';

@Component({
  selector: 'app-sidebar',
  imports: [NgIf, NgForOf],
  templateUrl: './sidebar.component.html',
  standalone: true,
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent implements OnInit {
  constructor(
    private chatDataService: ChatDataService,
    private chatService: ChatService,
    private dialogModel: MatDialog,
  ) {
  }

  protected messages: ChatCompletionMessage[] = [];
  protected isHistoricalChat: boolean = false;
  protected chatHistories: ChatHistories = {
    chatHistoryDetails: [],
  };

  private setChatHistoriesToLocalStorage(chatHistories: ChatHistories) {
    localStorage.setItem('chatHistories', JSON.stringify(chatHistories));
  }

  private getCurrentChatHistoriesFromLocalStorage(): ChatHistories {
    const currentHistories = localStorage.getItem('chatHistories');

    if (currentHistories) {
      const histories = JSON.parse(currentHistories) as ChatHistories;
      return {chatHistoryDetails: histories.chatHistoryDetails};
    } else {
      return {chatHistoryDetails: []};
    }
  }

  ngOnInit(): void {
    this.chatService.getMessagesSubject().subscribe(messages => this.messages = messages);
    this.chatHistories = this.getCurrentChatHistoriesFromLocalStorage();
  }

  protected async addNewChat() {
    if (!this.isHistoricalChat) {
      const chatHistoryId = uuidv4();
      const title = (await this.chatService.getTitleFromChatGpt(this.messages)).choices[0].message?.content!;

      const chatHistory: ChatHistoryDetails = {
        id: chatHistoryId,
        messages: this.messages,
        title: title
      };

      this.chatHistories = this.getCurrentChatHistoriesFromLocalStorage();

      if (!this.checkIsChatHistoryExists(chatHistory.id)) {
        this.chatHistories.chatHistoryDetails.unshift(chatHistory);

        this.setChatHistoriesToLocalStorage(this.chatHistories);
      }
    }
    this.chatService.setMessagesSubject([]);
    this.isHistoricalChat = false;
  }

  private checkIsChatHistoryExists(id: string) {
    return this.chatHistories.chatHistoryDetails.some(c => c.id === id);
  }

  protected getHistoryChatMessages(id: string) {
    const history = this.chatHistories.chatHistoryDetails.find(c => c.id === id);

    if (history) {
      this.chatService.setMessagesSubject(history.messages);
      this.isHistoricalChat = true;
    }
  }

  protected deleteHistoricalChat(id: string) {
    this.chatHistories.chatHistoryDetails = this.chatHistories.chatHistoryDetails.filter(c => c.id !== id);
    this.setChatHistoriesToLocalStorage(this.chatHistories);
  }
}

export default interface ChatHistories {
  chatHistoryDetails: ChatHistoryDetails[];
}

export interface ChatHistoryDetails {
  id: string;
  title: string;
  messages: ChatCompletionMessage[];
}
