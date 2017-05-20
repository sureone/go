  import { Injectable }    from '@angular/core';
  import { Headers, Http, RequestOptions, URLSearchParams  } from '@angular/http';
  import 'rxjs/add/operator/map';
  import 'rxjs/add/operator/toPromise';
  import { Thing } from '../models/thing';
  import { markdown } from 'markdown'
  import { Storage } from '@ionic/storage';

import { MediaPlugin, MediaObject } from '@ionic-native/media';

  import { Transfer, FileUploadOptions, TransferObject } from '@ionic-native/transfer';
  import { File } from '@ionic-native/file';

  import { SQLite, SQLiteObject } from '@ionic-native/sqlite';

  import { Platform } from 'ionic-angular';
  /*
  sqlite
  ionic cordova plugin add cordova-sqlite-storage
  npm install --save @ionic-native/sqlite
  */
  @Injectable()
  export class ThingService {

    private headers = new Headers({'Content-Type': 'application/json'});
    private url = 'https://www.boopo.cn:19023/ama/index.php/api';  // URL to web api
    _user: any;

    public db:SQLiteObject =  null;
    public cur_media: { thing:Thing,status: String, songid: any, start_song:any,
        percent:any,position:any,lastposition:any,duration:any, 
        cur_time:String,total_time:String,mfile: MediaObject}
         = {
      thing:null,
      status: null,
      songid: 0,
      start_song:0,
      percent:0.,
      position:0,
      lastposition:0,
      duration:0,
      cur_time:'00:00',
      total_time:'00:00',
      mfile: null
    };


    constructor(private plt:Platform,public http: Http,private storage:Storage,
      private media: MediaPlugin,private sqlite: SQLite,private transfer: Transfer, private file: File){ 
      

    }

  playOrpause(){
    if (this.cur_media.status=='play'){
      
      this.cur_media.mfile.pause();
    }else{
      
      this.cur_media.mfile.play();
    }
  }

  formatTime(seconds):String {
    return [
        seconds / 60 % 60,
        seconds % 60
    ]
        .join(":")
        .replace(/\b(\d)\b/g, "0$1");
  }


  seekAudio(){

   
    if(this.cur_media.position<this.cur_media.duration &&
    Math.abs(this.cur_media.position-this.cur_media.lastposition)>1000){
       console.log("seekto "+this.cur_media.position);
      this.cur_media.mfile.seekTo(this.cur_media.position);

      this.cur_media.lastposition=this.cur_media.position;


    }
  }

  playNewAudio(thing,start_song){
    this.cur_media.thing = thing;
    this.cur_media.start_song = start_song;
    
    if(this.cur_media.songid!=0){
        if(this.cur_media.songid!=thing.attaches[start_song].id){

          if(this.cur_media.mfile!=null){
            this.cur_media.mfile.stop();
            this.cur_media.mfile.release();

          }    
          this.playAudio(start_song);    
          

        }else if(this.cur_media.mfile!=null && this.cur_media.status=='pause'){
            this.cur_media.mfile.play();
        }
       

    }else{


      console.log("play song:"+start_song);

      if (this.plt.is('ios')) {  
        
        this.playAudio(start_song);
      }
    }
  }
  


  playAudio(idx){

    var attach = this.cur_media.thing.attaches[idx];


    this.cur_media.songid=attach.id;


    this.db.executeSql('select localurl,remoteurl from attaches where id='+attach.id, {})
    .then((rs) => {
      console.log('localurl:' + rs.rows.item(0).localurl);
      console.log(attach);
      var localurl = rs.rows.item(0).localurl;

      if(localurl && localurl.indexOf("file://")>-1){
        localurl = localurl.substr("file://".length);
      }

      //localurl = attach.file_name;
      // Create a MediaPlugin instance.  Expects path to file or url as argument
    // We can optionally pass a second argument to track the status of the media
    
    const onStatusUpdate = (status) =>{
      console.log("status:"+status);
      // this.mfile.seekTo(40000);
      
      


      if(status==2){
        let duration = this.cur_media.mfile.getDuration();
        
        this.cur_media.status = "play";
        console.log("duration"+duration);
        this.cur_media.duration=(duration*1000);
      }

      if(status==3){
        
        this.cur_media.status = "pause";
      }
      

    } 
    const onSuccess = () => console.log('onSuccess');
    const onError = (error) => console.error(error.message);

    var fileurl = localurl?localurl:attach['remoteurl'];
    this.cur_media.mfile = this.media.create(fileurl, onStatusUpdate, onSuccess, onError);

    console.log("play:"+fileurl);
    // play the file


    
    this.cur_media.mfile.play({ playAudioWhenScreenIsLocked : true });

     // Update media position every second
     var self = this;
      var mediaTimer = setInterval(function () {
          // get media position
          if(self.cur_media){
          self.cur_media.mfile.getCurrentPosition().then((position) => {
              if (position > -1) {
                    console.log((position) + " sec");
                    self.cur_media.position=(position*1000);

                    self.cur_media.lastposition = self.cur_media.position;

                   // self.cur_media.percent = position/this.cur_media.duration;


                  //  self.cur_media.cur_time=self.formatTime(position);
                  // self.cur_media.total_time=self.formatTime(self.cur_media.duration);

                }


          });
        }else{
           console.log("1111 sec");
         }
             

            
      }, 1000);
    

    



    


    }).catch(e => console.log(e))


      

  // // pause the file
  // file.pause();

  // // get current playback position
  // file.getCurrentPosition().then((position) => {
  //   console.log(position);
  // });

  // // get file duration
  // let duration = file.getDuration();
  // console.log(duration);

  // // skip to 10 seconds (expects int value in ms)
  // file.seekTo(10000);

  // // stop playing the file
  // file.stop();

  // // release the native audio resource
  // // Platform Quirks:
  // // iOS simply create a new instance and the old one will be overwritten
  // // Android you must call release() to destroy instances of media when you are done
  // file.release();

  }



    get(endpoint: string, params?: any, options?: RequestOptions) {
      if (!options) {
        options = new RequestOptions();
      }

      // Support easy query params for GET requests
      if (params) {
        let p = new URLSearchParams();
        for (let k in params) {
          p.set(k, params[k]);
        }
        // Set the search field if we have params and don't already have
        // a search field set in options.
        options.search = !options.search && p || options.search;
      }

      return this.http.get(this.url + '/' + endpoint, options);
    }

    post(endpoint: string, body: any, options?: RequestOptions) {
      return this.http.post(this.url + '/' + endpoint, body);
    }

    put(endpoint: string, body: any, options?: RequestOptions) {
      return this.http.put(this.url + '/' + endpoint, body, options);
    }

    delete(endpoint: string, options?: RequestOptions) {
      return this.http.delete(this.url + '/' + endpoint, options);
    }

    patch(endpoint: string, body: any, options?: RequestOptions) {
      return this.http.put(this.url + '/' + endpoint, body, options);
    }

    
    /**
     * Process a login/signup response to store user data
     */
    _loggedIn(resp) {
      this._user = resp.rows[0];
      this._user.token = resp.token;

    }

    login(accountInfo: any) {
      let seq = this.post('login', accountInfo).share();
      //let seq = this.post('login', accountInfo);

      seq
        .map(res => res.json())
        .subscribe(res => {
          // If the API returned a successful response, mark the user as logged in
          if (res.code == 200) {

            this.storage.set("account.user",accountInfo.user);

            this.storage.set("account.passwd",accountInfo.passwd);
              this._loggedIn(res);
          } else {
          }
        }, err => {
          console.error('ERROR', err);
        });

      return seq;
    }




    getThings(): Promise<Thing[]> {


      
      return this.get('hot')
                 .toPromise()
                 .then(response =>{
                     var things: Thing[] =  response.json() as Thing[];
                     var icons: string[]=['logo-angular','ios-appstore','md-appstore','md-baseball','ios-basketball','md-bicycle','ios-bowtie'];
                     
                     for (let i = 0; i < things.length; i++) {
                          things[i]['icon']=icons[Math.floor(Math.random() * icons.length)];
                          things[i].text = markdown.toHTML(things[i].text);


                          if (this.plt.is('ios')) {  

                            var sql = 'REPLACE INTO things(id,title,content) VALUES('+things[i].thingid+',\''+things[i].title+'\',\''+things[i].text+'\')';
                            this.db.executeSql(sql, {})
                              .then(() => console.log('insert or update thing:'+things[i].title))
                              .catch(e => console.log(e));
                            }


                          } 
                     return things;
                  })
                 .catch(this.handleError);
    }


    getThing(id: number): Promise<Thing> {
      return null;
    }

    deleteThing(id: number): Promise<void> {
      return null;
    }

    createThing(title: string,content: string) {
      let seq = this.post('create', {title:title,content:content,token:this._user.token}).share();

      seq
        .map(res => res.json())
        .subscribe(res => {
          console.log(res);
        }, err => {
          console.error('ERROR', err);
        });

      return seq;
    }

    updateThing(thing: Thing): Promise<Thing> {
      return null;
    }

    private handleError(error: any): Promise<any> {
      console.error('An error occurred', error); // for demo purposes only
      return Promise.reject(error.message || error);
    }
  }

