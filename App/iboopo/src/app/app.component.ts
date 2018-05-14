import { Component, ViewChild } from '@angular/core';
import { Platform, Nav, Config } from 'ionic-angular';

import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { ThingService } from '../providers/thing.service';

import { SQLite, SQLiteObject } from '@ionic-native/sqlite';

import { WelcomePage } from '../pages/welcome/welcome';
import { LoginPage } from '../pages/login/login';
import { TranslateService } from '@ngx-translate/core'

@Component({
  templateUrl: `app.html`
})
export class MyApp {
  rootPage = WelcomePage;

  @ViewChild(Nav) nav: Nav;



  constructor(private translate: TranslateService, platform: Platform, private config: Config, statusBar: StatusBar, splashScreen: SplashScreen
    ,private thingService:ThingService,private plt:Platform,private sqlite: SQLite) {

    this.initTranslate();

    platform.ready().then(() => {
      // Okay, so the platform is ready and our plugins are available.
      // Here you can do any higher level native things you might need.
      statusBar.styleDefault();
      splashScreen.hide();

      if (this.plt.is('ios')) {  

      this.sqlite.create({
        name: 'data.db',
        location: 'default'
      }).then((db: SQLiteObject) => {
          this.thingService.db = db;

          var sql = 'create table IF NOT EXISTS things(id INTEGER PRIMARY KEY, title VARCHAR(512),content TEXT,author VARCHAR(64),cdate INTEGER,hasaudio INTEGER,hasvideo INTEGER)';
          db.executeSql(sql, {})
            .then(() => console.log('created things table'))
            .catch(e => console.log(e));

          sql = 'create table IF NOT EXISTS attaches(id INTEGER PRIMARY KEY, thingid INTEGER, remoteurl VARCHAR(512),localurl VARCHAR(512), isaudio INTEGER,isvedio INTEGER,filesize INTEGER,duration INTEGER,fileorder INTEGER)';
          db.executeSql(sql, {})
            .then(() => console.log('create attaches table'))
            .catch(e => console.log(e));  


      }).catch(e => console.log(e));
      }

    });
  }

  initTranslate() {
    // Set the default language for translation strings, and the current language.
    this.translate.setDefaultLang('en');

    if (this.translate.getBrowserLang() !== undefined) {
      this.translate.use(this.translate.getBrowserLang());
    } else {
      this.translate.use('en'); // Set your language here
    }

    this.translate.get(['BACK_BUTTON_TEXT']).subscribe(values => {
      this.config.set('ios', 'backButtonText', values.BACK_BUTTON_TEXT);
    });
  }

  openPage(page) {
    // Reset the content nav to have just this page
    // we wouldn't want the back button to show in this scenario
    this.nav.setRoot(page.component);
  }
}
