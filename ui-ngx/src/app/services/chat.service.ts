import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { ChatCompletion, ChatCompletionMessage, ChatCompletionMessageParam } from 'openai/resources';

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  constructor() {
  }

  public getMessagesSubject(): Observable<ChatCompletionMessage[]> {
    return of([]) // ToDo
  }

  public setMessagesSubject(event: ChatCompletionMessage[]) {
    // ToDo
  }

  async createCompletionViaOpenAI(messages: ChatCompletionMessageParam[]): Promise<ChatCompletion> {
    // ToDo
    return {
      id: 'placeholder-id',
      object: 'chat.completion',
      created: Date.now(),
      model: 'gpt-3.5-turbo',
      choices: [],
      usage: {
        prompt_tokens: 0,
        completion_tokens: 0,
        total_tokens: 0
      }
    };

  }

  async getTitleFromChatGpt(messages: ChatCompletionMessageParam[]): Promise<ChatCompletion> {
    // ToDO
    return {
      id: 'placeholder-id',
      object: 'chat.completion',
      created: Date.now(),
      model: 'gpt-3.5-turbo',
      choices: [],
      usage: {
        prompt_tokens: 0,
        completion_tokens: 0,
        total_tokens: 0
      }
    };

  }
}
