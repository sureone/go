import { Component } from '@angular/core';
import { NavController, ToastController } from 'ionic-angular';

import { HomePage } from '../home/home';
import { ThingService } from '../../providers/thing.service';



@Component({
  selector: 'page-login',
  templateUrl: 'login.html'
})
export class LoginPage {
  // The account fields for the login form.
  // If you're using the username field with or without email, make
  // sure to add it to the type
  account: { user: string, passwd: string } = {
    user: 'ama',
    passwd: '123'
  };

  // Our translated text strings
  private loginErrorString: string;

  constructor(public navCtrl: NavController,
    public thingService: ThingService,
    public toastCtrl: ToastController) {
    this.loginErrorString = "login failed";
  }

  // Attempt to login in through our User service
  doLogin() {
    this.thingService.login(this.account).subscribe((resp) => {
      this.navCtrl.push(HomePage);
    }, (err) => {
      this.navCtrl.push(HomePage);
      // Unable to log in
      let toast = this.toastCtrl.create({
        message: this.loginErrorString,
        duration: 3000,
        position: 'top'
      });
      toast.present();
    });
  }
}
