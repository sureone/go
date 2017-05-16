import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { Thing }        from '../../models/thing';
import { ThingService } from '../../providers/thing.service';
import { ItemDetailPage } from '../item-detail/item-detail';

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {


  //private api = 'https://www.boopo.cn:19023/ama/index.php/api'; // 服务器地址
  //private api = "/api";
  //private hot = '/hot';  // 获取全部
  //private getGundam = '/detail';		

  
  things: Thing[] = [];
  
  constructor(public navCtrl: NavController,private thingService: ThingService) { }

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
}
