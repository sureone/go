import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { Http } from '@angular/http';
import 'rxjs/add/operator/map';

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {


  private api = 'https://www.boopo.cn:19023/ama/index.php/api'; // 服务器地址
  // private api = "/api";
  private hot = '/hot';  // 获取全部
  private getGundam = '/detail';		

  icons: string[];
  items: Array<{ title: string, note: string, icon: string }>;
	

  constructor(public navCtrl: NavController,public http: Http) {
    this.icons = ['flask', 'wifi', 'beer', 'football', 'basketball', 'paper-plane',
      'american-football', 'boat', 'bluetooth', 'build'];

    this.items = [];
    

    this.http.get(this.api+this.hot).map(res => res.json()).subscribe(data => {
        this.items = data;
        console.log(data.data);
        for (let i = 0; i < this.items.length; i++) {
        	this.items[i]['icon']=this.icons[Math.floor(Math.random() * this.icons.length)];
	    }
    });
  }

}
