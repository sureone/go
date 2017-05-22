import { Component } from '@angular/core';
import { NavController, ViewController,NavParams } from 'ionic-angular';
import {markdown} from 'markdown';
import { Thing } from '../../models/thing'


import { ThingService } from '../../providers/thing.service';

  import { Platform } from 'ionic-angular';
import { Transfer, FileUploadOptions, TransferObject } from '@ionic-native/transfer';
import { File } from '@ionic-native/file';

/*
*/

@Component({
  selector: 'page-player',
  templateUrl: 'player.html'
})
export class PlayerPage {
  constructor(public navCtrl: NavController, navParams: NavParams,
  private plt:Platform,private viewCtrl: ViewController,private thingService: ThingService,private transfer: Transfer, private file: File) {
    var thing = navParams.get('thing');
    var start_song = navParams.get('start_song');



    this.thingService.playNewAudio(thing,start_song);
  }

  playOrpause(){
    this.thingService.playOrpause();
  }

  playNext(){
    this.thingService.playNewAudio(this.thingService.cur_media.thing,this.thingService.cur_media.start_song+1);
  }

  playPrev(){

    this.thingService.playNewAudio(this.thingService.cur_media.thing,this.thingService.cur_media.start_song-1);
  }
    


}
