import { Component } from '@angular/core';
import { NavController,ModalController } from 'ionic-angular';
import { Thing }        from '../../models/thing';
import { ThingService } from '../../providers/thing.service';
import { ItemDetailPage } from '../item-detail/item-detail';
import { ItemCreatePage } from '../item-create/item-create';

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {

  things: Thing[] = [];

  constructor(public navCtrl: NavController,private thingService: ThingService, public modalCtrl: ModalController) { }

  ngOnInit(): void {
    this.thingService.getThings()
      .then(things=>{
            this.things=things;
            for(var i=0;i<this.things.length;i++){
              var thing = this.things[i];
              if(thing['attaches']){
                for(var j=0;j<thing.attaches.length;j++){
                  var attach = thing.attaches[j];
                  attach['remoteurl']='https://www.boopo.cn:19023/ama/uploads/'+attach.file_name;
                  attach['localurl']=null;
                  if(attach['file_type']=="audio/mpeg"){
                    attach['isaudio']=true;
                  }else{
                    attach['isaudio']=false;
                  
                  }
                }
              }
            }
       });
  }
	
  /**
   * Navigate to the detail page for this item.
   */
  openThing(thing: Thing) {
    this.navCtrl.push(ItemDetailPage, {
      thing: thing 
    });
  }
  addThing() {
    let addModal = this.modalCtrl.create(ItemCreatePage);
    addModal.onDidDismiss(thing => {
      this.thingService.getThings()
      .then(things=>{
            this.things=things;
       });
    })
    addModal.present();
 
  }
}
