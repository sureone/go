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
  }



    downloadAttachFiles(fileTransfer,thing,idx){
        if(idx==thing.attaches.length) return;
        var attach = thing.attaches[idx];
        if(attach.file_type=="audio/mpeg"){
          const url = 'https://www.boopo.cn:19023/ama/uploads/'+attach.file_name;
          fileTransfer.download(url, this.file.dataDirectory + attach.id+".mp3").then((entry) => {
            console.log('data dataDirectory:'+this.file.dataDirectory);
            console.log('download complete: ' + entry.toURL());
            var isaudio =1;
            var isvedio = 0;
            var localurl = this.file.dataDirectory + attach.id+".mp3";
            var sql = 'REPLACE INTO attaches(id,thingid,remoteurl,localurl,isaudio,isvedio) \
             VALUES('+attach.id+','+thing.thingid+',\''+url+'\',\''+localurl+'\','+isaudio+','+isvedio+')';
            this.thingService.db.executeSql(sql, {})
              .then(() => console.log('insert or update attach:'+attach.file_name))
              .catch(e => console.log(e));

            idx++;
            this.downloadAttachFiles(fileTransfer,thing,idx);

          }, (error) => {
            // handle error
          });
        }else{
           idx++;
              this.downloadAttachFiles(fileTransfer,thing,idx);

        }
    }

    downloadAttach(){

    	if (this.plt.is('ios')) { 
      const fileTransfer: TransferObject = this.transfer.create();

      if(this.thing['attaches']){
        this.downloadAttachFiles(fileTransfer,this.thing,0);
      }else{
        console.log("no attache file");
      }
  		}
  	
    }




  playAudio(songidx){


  	this.navCtrl.push(PlayerPage, {
      thing: this.thing,
      start_song:songidx 
    });



  }

}
