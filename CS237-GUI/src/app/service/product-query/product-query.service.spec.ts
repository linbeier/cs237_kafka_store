import { TestBed } from '@angular/core/testing';

import { ProductQueryService } from './product-query.service';

describe('ProductQueryService', () => {
  let service: ProductQueryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProductQueryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
