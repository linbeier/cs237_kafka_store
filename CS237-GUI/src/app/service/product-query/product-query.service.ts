import { Injectable } from '@angular/core';
import {AppSettings} from "../../common/app-settings/app-settings";
import {Observable} from "rxjs";
import {Product} from "../../type/product/product";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class ProductQueryService {

  public static readonly QUERY_ENDPOINT = 'query'
  private serverUrl: string = `${AppSettings.getApiEndpoint()}/${ProductQueryService.QUERY_ENDPOINT}/white`;

  constructor(private http: HttpClient) { }

  public queryProduct(): Observable<Product[]> {
    return this.http.get<Product[]>(this.serverUrl);
  }
}
