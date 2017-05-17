import { Injectable }    from '@angular/core';
import { Headers, Http, RequestOptions, URLSearchParams  } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';
import { Thing } from '../models/thing';
import { markdown } from 'markdown';

@Injectable()
export class ThingService {

  private headers = new Headers({'Content-Type': 'application/json'});
  private url = 'https://www.boopo.cn:19023/ama/index.php/api';  // URL to web api
  _user: any;

  constructor(private http: Http) { }


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
    this._user = resp.user;
  }

  login(accountInfo: any) {
    // let seq = this.post('login', accountInfo).share();
    let seq = this.post('login', accountInfo);

    seq
      .map(res => res.json())
      .subscribe(res => {
        // If the API returned a successful response, mark the user as logged in
        if (res.code == 200) {
          this._loggedIn(res.rows[0]);
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

  createThing(title: string,text: string): Promise<Thing> {
    return null;
  }

  updateThing(thing: Thing): Promise<Thing> {
    return null;
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }
}

