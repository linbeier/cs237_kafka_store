import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {Product} from "../../type/product/product";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class ProductQueryService {
  private serverUrl: string = `http://localhost:8080/kafka/history_product?color=`;

  constructor(private http: HttpClient) { }

  public queryProduct(productColor: string): Observable<Product[]> {
    return this.http.get<Product[]>(this.serverUrl + productColor);
  }
}
