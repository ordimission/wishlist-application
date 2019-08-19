import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IAnswerList } from 'app/shared/model/answer-list.model';

type EntityResponseType = HttpResponse<IAnswerList>;
type EntityArrayResponseType = HttpResponse<IAnswerList[]>;

@Injectable({ providedIn: 'root' })
export class AnswerListService {
  public resourceUrl = SERVER_API_URL + 'api/answer-lists';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/answer-lists';

  constructor(protected http: HttpClient) {}

  create(answerList: IAnswerList): Observable<EntityResponseType> {
    return this.http.post<IAnswerList>(this.resourceUrl, answerList, { observe: 'response' });
  }

  update(answerList: IAnswerList): Observable<EntityResponseType> {
    return this.http.put<IAnswerList>(this.resourceUrl, answerList, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAnswerList>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAnswerList[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAnswerList[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
