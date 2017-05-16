import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import {markdown} from 'markdown';
import { Thing } from '../../models/thing'

@Component({
  selector: 'page-item-detail',
  templateUrl: 'item-detail.html'
})
export class ItemDetailPage {
  thing: Thing;
  ionViewDidEnter() {
      
  }

  constructor(public navCtrl: NavController, navParams: NavParams) {
    this.thing = navParams.get('thing');
  }

}
