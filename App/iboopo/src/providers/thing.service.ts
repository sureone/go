  import { Injectable }    from '@angular/core';
  import { Headers, Http, RequestOptions, URLSearchParams  } from '@angular/http';
  import 'rxjs/add/operator/map';
  import 'rxjs/add/operator/toPromise';
  import { Thing } from '../models/thing';
  import { markdown } from 'markdown'
  import { Storage } from '@ionic/storage';
import { MusicControls } from '@ionic-native/music-controls';
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
    public BASE_PATH = "https://www.boopo.cn:19023/ama/uploads/";

    public db:SQLiteObject =  null;
    public cur_media = {
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


    syncLocalAttach(thing){

      var sql = 'select id,localurl from attaches where thingid='+thing.thingid;
            this.db.executeSql(sql, {})
              .then((rs) => {
                  console.log('local attaches:' + rs.rows.length);
                  console.log('thing attaches:' + thing.attaches.length);
                  for (var j=0;j<rs.rows.length;j++){
                    var item = rs.rows.item(j);
                    for(var i=0;i<thing.attaches.length;i++){
                      var attach = thing.attaches[i];
                      if(attach.id == item.id){
                        console.log("localurl of "+i+":"+item.localurl);
                        if(item.localurl!=null){
                          attach['localurl']=item.localurl;
                          attach.indicator="checkmark";
                        }
                      }
                    }  
                  }
                  
               })
              .catch(e => console.log(e));

    }




    constructor(private plt:Platform,public http: Http,private storage:Storage,
      private media: MediaPlugin,private musicControls: MusicControls,private sqlite: SQLite,private transfer: Transfer, private file: File){ 
      

    }



      downloadAttachFiles(fileTransfer,thing,idx,oneonly){
        if(idx==thing.attaches.length) return;
        var attach = thing.attaches[idx];
        if(attach.file_type=="audio/mpeg" && !(attach.localurl)){
          attach.indicator='refresh';
          console.log("current local url"+attach.localurl);
          const url = 'https://www.boopo.cn:19023/ama/uploads/'+attach.file_name;
          fileTransfer.download(url, this.file.dataDirectory + attach.id+".mp3").then((entry) => {
            console.log('data dataDirectory:'+this.file.dataDirectory);
            console.log('download complete: ' + entry.toURL());
            var isaudio =1;
            var isvedio = 0;
            attach.indicator='checkmark';
            var localurl = this.file.dataDirectory + attach.id+".mp3";
            attach.localurl = localurl;
            var sql = 'REPLACE INTO attaches(id,thingid,remoteurl,localurl,isaudio,isvedio) \
             VALUES('+attach.id+','+thing.thingid+',\''+url+'\',\''+localurl+'\','+isaudio+','+isvedio+')';
            this.db.executeSql(sql, {})
              .then(() => console.log('insert or update attach:'+attach.file_name))
              .catch(e => console.log(e));

            idx++;
            if(!oneonly)
              this.downloadAttachFiles(fileTransfer,thing,idx,oneonly);

          }, (error) => {
            // handle error
          });
        }else{
           idx++;
              this.downloadAttachFiles(fileTransfer,thing,idx,oneonly);

        }
    }

    downloadThingAttach(thing){

      if (this.plt.is('ios')) { 
      const fileTransfer: TransferObject = this.transfer.create();

      if(thing['attaches']){
        this.downloadAttachFiles(fileTransfer,thing,0,false);
      }else{
        console.log("no attache file");
      }
      }
    
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
  private has_music_controll=false;
  createMusicControll(){
    if(!this.has_music_controll) this.has_music_controll=true;
    else
      return;
    this.musicControls.create({
      track       : '',        // optional, default : ''
      artist      : '',                       // optional, default : ''
      cover       : '',      // optional, default : nothing
      // cover can be a local path (use fullpath 'file:///storage/emulated/...', or only 'my_image.jpg' if my_image.jpg is in the www folder of your app)
      //           or a remote url ('http://...', 'https://...', 'ftp://...')
      isPlaying   : true,                         // optional, default : true
      dismissable : true,                         // optional, default : false

      // hide previous/next/close buttons:
      hasPrev   : true,      // show previous button, optional, default: true
      hasNext   : true,      // show next button, optional, default: true
      hasClose  : true,       // show close button, optional, default: false

    // iOS only, optional
      album       : '',     // optional, default: ''
      duration : 0, // optional, default: 0
      elapsed : 0, // optional, default: 0

      // Android only, optional
      // text displayed in the status bar when the notification (and the ticker) are updated
      ticker    : ''
     });

     this.musicControls.subscribe().subscribe(action => {

       switch(action) {
           case 'music-controls-next':
               // Do something

              this.playNewAudio(this.cur_media.thing,this.cur_media.start_song+1);
               break;
           case 'music-controls-previous':
              this.playNewAudio(this.cur_media.thing,this.cur_media.start_song-1);
               // Do something
               break;
           case 'music-controls-pause':
               // Do something
               this.cur_media.mfile.pause();

               break;
           case 'music-controls-play':

               this.cur_media.mfile.play();
               // Do something
               break;
           case 'music-controls-destroy':
               // Do something
               break;

           // Headset events (Android only)
           case 'music-controls-media-button' :
               // Do something
               break;
           case 'music-controls-headset-unplugged':
               // Do something
               break;
           case 'music-controls-headset-plugged':
               // Do something
               break;
           default:
               break;
       }

     });

     this.musicControls.listen(); // activates the observable above

     this.musicControls.updateIsPlaying(true);

  }

  playNewAudio(thing,start_song){
    this.cur_media.thing = thing;
 
    if(start_song>=this.cur_media.thing.attaches.length){
      return;
    }

    if(start_song<0){
      return;
    }

    this.cur_media.start_song = start_song;

    
    if(this.cur_media.songid!=0){
        if(this.cur_media.songid!=thing.attaches[start_song].id){

          if(this.cur_media.mfile!=null){
            this.cur_media.status="stop";
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
  
  mediaTimer=null;


  playerTimer(self)
  {

           
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
              if(self.cur_media.status=='play'){
                self.mediaTimer = setTimeout(function(){self.playerTimer(self)},1000);
              }
            }else{
               console.log("1111 sec");
               self.mediaTimer=null;
             }

  }
  playAudio(idx){

    if(idx>=this.cur_media.thing.attaches.length){
      return;
    }

    var attach = this.cur_media.thing.attaches[idx];


    this.cur_media.songid=attach.id;
    this.cur_media.start_song=idx;


    this.db.executeSql('select localurl,remoteurl from attaches where id='+attach.id, {})
    .then((rs) => {

      var localurl=null;
      if(rs.rows.length>0){
        console.log('localurl:' + rs.rows.item(0).localurl);
        var localurl = rs.rows.item(0).localurl;

        if(localurl && localurl.indexOf("file://")>-1){
          localurl = localurl.substr("file://".length);
        }
      }

      //localurl = attach.file_name;
      // Create a MediaPlugin instance.  Expects path to file or url as argument
    // We can optionally pass a second argument to track the status of the media
    
    const onStatusUpdate = (status) =>{
      console.log("status:"+status);
      // this.mfile.seekTo(40000);
      
      


      if(status==2){


         var self = this;

      

         
        this.createMusicControll();
    

        let duration = this.cur_media.mfile.getDuration();
        
        this.cur_media.status = "play";
        console.log("duration"+duration);
        this.cur_media.duration=(duration*1000);


           if(this.mediaTimer==null){
           this.mediaTimer=setTimeout(function(){
              self.playerTimer(self)},1000);
        }
      }

      if(status==3){
        
        this.cur_media.status = "pause";
      }

      if(status==4){

        

        if(this.cur_media.status=='play'){
          this.playNewAudio(this.cur_media.thing,this.cur_media.start_song+1);
        }

        this.cur_media.status = "stop";
        this.mediaTimer = null;

      }
      

    } 
    const onSuccess = () => console.log('onSuccess');
    const onError = (error) => {
      console.error(error.message)
    };


    var fileurl = localurl?localurl:attach['remoteurl'];
    this.cur_media.mfile = this.media.create(fileurl, onStatusUpdate, onSuccess, onError);

    if(localurl==null){

      const fileTransfer: TransferObject = this.transfer.create();
      this.downloadAttachFiles(fileTransfer,this.cur_media.thing,idx,true);
    }


    console.log("play:"+fileurl);
    // play the file


    
    this.cur_media.mfile.play({ playAudioWhenScreenIsLocked : true });


    



    


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

