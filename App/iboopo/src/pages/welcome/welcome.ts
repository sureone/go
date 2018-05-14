import { Component } from '@angular/core';
import { NavController,ToastController } from 'ionic-angular';
import { Storage } from '@ionic/storage';
import { LoginPage } from '../login/login';
import { TabsPage } from '../tabs/tabs';
import { ThingService } from '../../providers/thing.service';
/**
 * The Welcome Page is a splash page that quickly describes the app,
 * and then directs the user to create an account or log in.
 * If you'd like to immediately put the user onto a login/signup page,
 * we recommend not using the Welcome page.
*/
@Component({
  selector: 'page-welcome',
  templateUrl: 'welcome.html'
})
export class WelcomePage {

  constructor(public navCtrl: NavController,private storage: Storage,
    public toastCtrl: ToastController,
    public thingService: ThingService) { }

 
  ionViewDidEnter() {
    var user = null;
    this.storage.get('account.user').then((val) => {
        user = val;
        if(user == null){
            this.navCtrl.push(LoginPage);
        }else{

            this.storage.get('account.passwd').then((val) => {
                var passwd = val;
                this.thingService.login({user:user,passwd:passwd}).map(resp => resp.json()).subscribe((resp) => {
                  if(resp.code==200){
                    this.navCtrl.push(TabsPage);
                  }
                }, (err) => {
                  
                  // Unable to log in
                  let toast = this.toastCtrl.create({
                    message: "login error",
                    duration: 3000,
                    position: 'top'
                  });
                  toast.present();
                });
            });;

        }
    });
   

  }

}
