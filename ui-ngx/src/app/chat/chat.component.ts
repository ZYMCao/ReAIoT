import {Component} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {v4 as uuidv4} from 'uuid';
import {FormsModule} from '@angular/forms';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import { CommonModule } from "@angular/common";

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatToolbarModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ],
  templateUrl: './chat.component.html',
  styleUrl: './chat.component.css'
})
export class ChatComponent {
  message: string = '';
  response: string | null = null;
  dialogId: string = uuidv4();

  constructor(private http: HttpClient) {
  }

  sendMessage() {
    const apiUrl = `http://localhost:7845/api/dialog/${this.dialogId}`;
    const requestBody = {message: this.message};

    this.http.post(apiUrl, requestBody).subscribe(
      (data: any) => {
        this.response = data.response;
      },
      (error) => {
        console.error('Error sending message:', error);
        this.response = 'Error occurred while sending the message.';
      }
    );
  }
}
