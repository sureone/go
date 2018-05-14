import { Component } from '@angular/core';
import { NavController,ModalController, NavParams } from 'ionic-angular';
import {markdown} from 'markdown';
import { Thing } from '../../models/thing'

import { MediaPlugin, MediaObject } from '@ionic-native/media';
import { PlayerPage } from '../player/player';

import { ThingService } from '../../providers/thing.service';

  import { Platform } from 'ionic-angular';
  import { Transfer, FileUploadOptions, TransferObject } from '@ionic-native/transfer';
  import { File } from '@ionic-native/file';

/*
install ionic native media
ionic cordova plugin add cordova-plugin-media
npm install --save @ionic-native/media


iso backgound play audio

add below in info.list

Required background modes



file download

ionic cordova plugin add cordova-plugin-file-transfer
npm install --save @ionic-native/transfer
ionic cordova plugin add cordova-plugin-file
npm install --save @ionic-native/file
*/

@Component({
  selector: 'page-item-detail',
  templateUrl: 'item-detail.html'
})
export class ItemDetailPage {
  thing: Thing;
  ionViewDidEnter() {
      
  }

  constructor(public navCtrl: NavController, navParams: NavParams,
  private plt:Platform,private modalCtrl:ModalController,private media: MediaPlugin,private thingService: ThingService,private transfer: Transfer, private file: File) {
    this.thing = navParams.get('thing');



        this.thingService.syncLocalAttach(this.thing);

  }



  
  downloadAttach(){
    this.thingService.downloadThingAttach(this.thing);
  }




  playAudio(songidx){


  	this.navCtrl.push(PlayerPage, {
      thing: this.thing,
      start_song:songidx 
    });



  }

}
