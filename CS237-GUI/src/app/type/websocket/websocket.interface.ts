export interface QueryRequest
  extends Readonly<{
    productColor: string;
  }> {}

export interface QueryResponseEvent extends Readonly<{ message: string }> {}

export interface ProductUpdateEvent
  extends Readonly<{
    productUpdateRecord: string;
  }> {}

export type WebsocketRequestTypeMap = {
  QueryRequest: QueryRequest;
};

export type WebsocketEventTypeMap = {
  QueryResponseEvent: QueryResponseEvent;
  ProductUpdateEvent: ProductUpdateEvent
};

// helper type definitions to generate the request and event types
type ValueOf<T> = T[keyof T];
type CustomUnionType<T> = ValueOf<{
  [P in keyof T]: {
  type: P;
} & T[P];
}>;

export type WebsocketRequestTypes = keyof WebsocketRequestTypeMap;
export type WebsocketRequest = CustomUnionType<WebsocketRequestTypeMap>;

export type WebsocketEventTypes = keyof WebsocketEventTypeMap;
export type WebsocketEvent = CustomUnionType<WebsocketEventTypeMap>;
