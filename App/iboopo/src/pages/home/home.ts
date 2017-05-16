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
      if (thing) {
        //this.things.add(thing);
      }
    })
    addModal.present();
 
  }
}
