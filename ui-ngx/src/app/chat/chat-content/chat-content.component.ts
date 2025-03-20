import { AfterViewChecked, AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MarkdownService } from 'ngx-markdown';
import { ChatService } from '../../services/chat.service';
import { NgForOf, NgIf } from '@angular/common';

@Component({
  selector: 'app-chat-content',
  imports: [NgIf, NgForOf],
  templateUrl: './chat-content.component.html',
  standalone: true,
  styleUrl: './chat-content.component.css'
})
export class ChatContentComponent implements OnInit, AfterViewChecked, AfterViewInit {
  constructor(
    private chatService: ChatService,
    private markdownService: MarkdownService,
    private snackBar: MatSnackBar
  ) {
  }

  protected isBusy: boolean = false;
  public messages: any[] = [];
  @ViewChild('textInput', {static: true}) textInputRef!: ElementRef;

  ngAfterViewChecked(): void {
    this.scrollToBottom();
  }

  scrollToBottom() {
    window.scrollTo(0, document.body.scrollHeight);
  }

  ngAfterViewInit(): void {
    this.textInputRef.nativeElement.focus();
  }

  ngOnInit(): void {
    this.scrollToBottom();

    this.chatService.getMessagesSubject().subscribe(messages => {
      this.messages = messages;
    });
  }

  async createCompletion(textInput: HTMLTextAreaElement) {

    const prompt = textInput.value;
    if (prompt.length <= 1 || this.isBusy) {
      textInput.value = '';
      return;
    }
    textInput.value = '';
    const message: any = {
      role: 'user',
      content: prompt,
    };

    this.messages.push(message);
    try {
      this.isBusy = true;
      const completion = await this.chatService.createCompletionViaOpenAI(this.messages);
      const completionMessage = this.markdownService.parse(completion.choices[0].message?.content!);

      const responseMessage: any = {
        role: 'assistant',
        content: completionMessage,
      };

      this.messages.push(responseMessage);
    } catch (err) {
      this.snackBar.open(
        'API Request Failed, please check after some time.',
        'Close',
        {
          horizontalPosition: 'end',
          verticalPosition: 'bottom',
          duration: 5000,
        }
      );
    }

    this.chatService.setMessagesSubject(this.messages);
    this.isBusy = false;
    this.scrollToBottom();
  }
}
