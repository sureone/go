import { Injectable }    from '@angular/core';
import { Headers, Http } from '@angular/http';
import { markdown } from 'markdown';
import 'rxjs/add/operator/toPromise';
import { Thing } from '../models/thing';
@Injectable()
export class ThingService {

  private headers = new Headers({'Content-Type': 'application/json'});
  private serviceUrl = 'https://www.boopo.cn:19023/ama/index.php/api';  // URL to web api

  constructor(private http: Http) { }

  getThings(): Promise<Thing[]> {
    const url = `${this.serviceUrl}/hot`;
    return this.http.get(url)
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
    const url = `${this.serviceUrl}/hot/${id}`;
    return this.http.get(url)
      .toPromise()
      .then(response => response.json().data as Thing)
      .catch(this.handleError);
  }

  delete(id: number): Promise<void> {
    const url = `${this.serviceUrl}/hot/${id}`;
    return this.http.delete(url, {headers: this.headers})
      .toPromise()
      .then(() => null)
      .catch(this.handleError);
  }

  create(title: string,text: string): Promise<Thing> {
    return this.http
      .post(this.serviceUrl, JSON.stringify({title: title,text:text}), {headers: this.headers})
      .toPromise()
      .then(res => res.json().data as Thing)
      .catch(this.handleError);
  }

  update(thing: Thing): Promise<Thing> {
    const url = `${this.serviceUrl}/hot/${thing.thingid}`;
    return this.http
      .put(url, JSON.stringify(thing), {headers: this.headers})
      .toPromise()
      .then(() => thing)
      .catch(this.handleError);
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }
}

