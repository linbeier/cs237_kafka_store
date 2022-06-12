import {Component, OnDestroy, OnInit} from '@angular/core';
import * as L from 'leaflet';
import {latLng, MapOptions, tileLayer, Map as M, marker, Marker, Layer} from 'leaflet';
// @ts-ignore
import icons from 'leaflet-color-number-markers';
import {WebsocketService} from "../../service/websocket/websocket.service";
import { UntilDestroy, untilDestroyed } from "@ngneat/until-destroy";
import {Product} from "../../type/product/product";
import {ProductQueryService} from "../../service/product-query/product-query.service";

@UntilDestroy()
@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss']
})

export class MapComponent implements OnInit, OnDestroy {

  markerClusterGroup!: L.MarkerClusterGroup;
  map!: M;
  mapOptions!: MapOptions;
  // Maps a product to its indexed position in the markerClusterGroup
  productIdToInfos = new Map<String, {product: Product, clusterIdx: number}>();
  inputData: string = 'White';

  constructor(private websocketService: WebsocketService,
              private productQueryService: ProductQueryService) {
    this.markerClusterGroup = L.markerClusterGroup({removeOutsideVisibleBounds: true});
  }

  ngOnInit() {
    this.initializeMapOptions();
  }

  ngOnDestroy() {
    this.websocketService.closeWebsocket();
  }

  onMapReady(map: M) {
    this.map = map;
    this.markerClusterGroup.addTo(this.map);
    this.websocketService.openWebsocket("White");
    this.handleNewData();
    this.submitSearch("White");
    // setInterval(()=>{
    //   this.markerClusterGroup.eachLayer(layer => {
    //     const randInt = Math.floor(Math.random() * (Math.floor(1000) - Math.ceil(0) + 1)) + Math.ceil(0);
    //     const randIcon = MapComponent.getDefaultIcon(randInt);
    //     (layer as Marker).setIcon(randIcon);
    //   })
    // }, 5000);
    // setInterval(()=> {
    //   const totalMarkers = this.markerClusterGroup.getLayers().length;
    //   const randomLayerId = Math.floor(Math.random() * (Math.floor(totalMarkers-1) - Math.ceil(0) + 1)) + Math.ceil(0);
    //   this.markerClusterGroup.removeLayer(this.markerClusterGroup.getLayer(randomLayerId) as Layer);
    //   this.markerClusterGroup.addLayer(MapComponent.randomMarker());
    // }, 0.1);
  }

  private handleNewData() {
    this.websocketService.productUpdateSubject.asObservable().pipe(untilDestroyed(this)).subscribe(product => {
      if (!this.productIdToInfos.has(product.id)) {
        if (product.quantity > 0) {
          const newMarker = MapComponent.createMarker(product);
          this.markerClusterGroup.addLayer(newMarker);
          this.productIdToInfos.set(product.id, {
            product: product,
            clusterIdx: this.markerClusterGroup.getLayerId(newMarker)
          });
          // const id =(this.productIdToInfos.get(product.id) as {product: Product, clusterIdx: number}).clusterIdx;
          // (this.markerClusterGroup.getLayer(id) as Marker).bindTooltip(id.toString());
        }
      } else {
        const info = this.productIdToInfos.get(product.id) as {product: Product, clusterIdx: number};
        if (product.quantity > 0) {
          if (product.quantity >= 1000) product.quantity = 999;
          (this.markerClusterGroup.getLayer(info.clusterIdx) as Marker).setIcon(MapComponent.getDefaultIcon(product.quantity));
        } else {
          this.markerClusterGroup.removeLayer(this.markerClusterGroup.getLayer(info.clusterIdx) as Layer);
        }
      }
    })
  }

  private initializeMapOptions() {
    this.mapOptions = {
      center: latLng(33.90, -118.20),
      zoom: 12,
      layers: [
        tileLayer(
          'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
          {
            maxZoom: 19,
            attribution: 'Map data © OpenStreetMap contributors'
          })
      ],
    };
  }

  private batchAddMarkers(products: Product[]) {
    const markerClusterData: L.Marker[] = [];

    for (const product of products) {
      const newMarker = MapComponent.createMarker(product);
      markerClusterData.push(newMarker);
    }
    this.markerClusterGroup.addLayers(markerClusterData);
    for (const product of products) {
      const newMarker = MapComponent.createMarker(product);
      this.productIdToInfos[product.id] = {
        product: product,
        clusterIdx: this.markerClusterGroup.getLayerId(newMarker),
      }
    }
  }

  private static randomMarker(): Marker {
    const randInt = Math.floor(Math.random() * (Math.floor(1000) - Math.ceil(0) + 1)) + Math.ceil(0);
    const randIcon = MapComponent.getDefaultIcon(randInt);
    const randCoordinate = latLng([
      (Math.random() * (33.8 - 34.1) + 34.1),
      (Math.random() * (-118.36689 - -118) + -118)
    ]);
    const newMarker = marker(randCoordinate).setIcon(randIcon);
    newMarker.bindPopup("<b>Hello world!</b><br>I am a popup.");
    return newMarker;
  }

  private static createMarker(product: Product): Marker {
    const newMarker =  marker(latLng(product.lati, product.longti));
    let q = product.quantity;
    if (q > 1000)
      q = 999
    newMarker.setIcon(MapComponent.getDefaultIcon(q));
    return newMarker.bindPopup(MapComponent.createPopupString(product));
  }

  private static getDefaultIcon(quantity: number) {
    return icons.black.numbers[quantity];
  }

  private static createPopupString(product: Product): string {
    return `${product.brand}\n${product.name}\n${product.price}`
  }

  submitSearch(inputData: string) {
    this.markerClusterGroup.clearLayers();
    this.productQueryService.queryProduct(inputData).subscribe(value => {
      this.batchAddMarkers(value);
    });
    this.websocketService.sendNewRequest(inputData);
  }
}
