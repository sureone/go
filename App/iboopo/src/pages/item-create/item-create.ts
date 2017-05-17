import { Component } from '@angular/core';
import { Validators, FormBuilder, FormGroup } from '@angular/forms';
import { NavController, ViewController ,ToastController} from 'ionic-angular';
import { Thing } from "../../models/thing"
import 'rxjs/add/operator/map';
import { ThingService } from "../../providers/thing.service"
@Component({
  selector: 'page-item-create',
  templateUrl: 'item-create.html'
})
export class ItemCreatePage {

  isReadyToSave: boolean;

  thing: Thing;

  form: FormGroup;

  constructor(public navCtrl: NavController, public viewCtrl: ViewController, formBuilder: FormBuilder,public thingService: ThingService,
  public toastCtrl:ToastController) {
    this.form = formBuilder.group({
      title: ['', Validators.required],
      content: ['']
    });

    // Watch the form for changes, and
    this.form.valueChanges.subscribe((v) => {
      this.isReadyToSave = this.form.valid;
    });
  }

  ionViewDidLoad() {

  }


  /**
   * The user cancelled, so we dismiss without sending data back.
   */
  cancel() {
    this.viewCtrl.dismiss();
  }




  /**
   * The user is done and wants to create the item, so return it
   * back to the presenter.
   */
  done() {
    if (!this.form.valid) { return; }

    this.thingService.createThing(this.form.value.title,this.form.value.content)
      .map(res => res.json())
      .subscribe((res) => {
        if(res.code==200)
          this.viewCtrl.dismiss(this.form.value);
        else{
          let toast = this.toastCtrl.create({
            message: "create error",
            duration: 3000,
            position: 'top'
          });
          toast.present();
        }

      }, (err) => {
        
        let toast = this.toastCtrl.create({
          message: "create error",
          duration: 3000,
          position: 'top'
        });
        toast.present();
      });

  }
}
