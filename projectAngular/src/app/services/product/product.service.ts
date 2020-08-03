import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ApiUrl } from './../../API_URL/api_url';
import { Product } from './../../model/product';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private API_URL = ApiUrl.PRODUCT; 

  constructor(private http: HttpClient) { }

  public getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.API_URL}/products`).pipe(
      catchError(this.errorHandler)
    );
  }

  public getProductsById(productId: any): Observable<Product[]> {
    return this.http.post<Product[]>(`${this.API_URL}/getproductid`, {id: productId}).pipe(
      catchError(this.errorHandler)
    );
  }
  
  public getProduct(productId: any): Observable<Product> {
    return this.http.post<Product>(`${this.API_URL}/get-product`, {id: productId}).pipe(
      catchError(this.errorHandler)
    );
  }

  public deleteProductById(productId: any) {
    return this.http.delete(`${this.API_URL}/${productId}`, {responseType: 'text'}).pipe(
      catchError(this.errorHandler)
    );
  }

  public updateNewCategoryName(categoryNameNew: string, categoryNameOld: string) {
    return this.http.post(`${this.API_URL}/update-new-categoryname`, {categoryNameNew: categoryNameNew, categoryNameOld: categoryNameOld}, {responseType: 'text'}).pipe(
      catchError(this.errorHandler)
    );
  }

  public getProductsByCategoryId(categoryId: any): Observable<Product[]> {
    return this.http.post<Product[]>(`${this.API_URL}/bycategoryid`, {id: categoryId}).pipe(
      catchError(this.errorHandler)
    );
  }

  public getProductsByCategoryAndProductId(categoryId: any, productId: any): Observable<Product[]> {
    return this.http.post<Product[]>(`${this.API_URL}//bycategory-productid`, {categoryId: categoryId, id: productId}).pipe(
      catchError(this.errorHandler)
    );
  }

  public addProduct(product: Product): Observable<Product> {
    const formData = this.formDataCreate(product, false);
    const headers = new HttpHeaders();
    headers.append('Accept', 'application/json');
    return this.http.post<Product>(`${this.API_URL}/save-update`, formData, {headers: headers});
  }

  public updateProduct(product: Product): Observable<Product> {
    const formData = this.formDataCreate(product, true);
    const headers = new HttpHeaders();
    headers.append('Accept', 'application/json');
    return this.http.post<Product>(`${this.API_URL}/save-update`, formData, {headers: headers});
  }

  private formDataCreate(product: Product, addUpdate: boolean) {
    const formData = new FormData();
    formData.append("numberExtern", this.isUndefined(product.numberExtern));
    formData.append("name", this.isUndefined(product.name));
    formData.append("type", this.isUndefined(product.type));
    formData.append("description", this.isUndefined(product.description));
    formData.append("photo", this.isUndefined(product.photo));
    formData.append("price", this.isUndefined(product.price));
    formData.append("categoryId", product.categoryId);
    formData.append("categoryName", this.isUndefined(product.categoryName));
    if(addUpdate) {
      formData.append("id", product.id);
      return formData;
    } else {
      return formData;
    }
  }

  public isUndefined(attr: any) {
    return attr !== '' && attr !== undefined ? attr : '---';
  }

  public getAllTypes() {
    return this.http.get(`${this.API_URL}/types`).pipe(
      catchError(this.errorHandler)
    );
  }

  public getTypesById(categoryId: any) {
    return this.http.get(`${this.API_URL}/types/${categoryId}`).pipe(
      catchError(this.errorHandler)
    );
  }

  public getProductsByParams(productName:  string, numberExtern: string, type: string, categoryId: string): Observable<Product[]> {
    return this.http.post<Product[]>(`${this.API_URL}/search-by-paramsid`, {searchByName: productName, searchByNumberEx: numberExtern, searchByType: type, categoryId: categoryId}).pipe(
      catchError(this.errorHandler)
    );
  }

  private errorHandler(error: HttpErrorResponse) {
    console.log("Error en el servicio");
    return throwError(error);
  }
}
