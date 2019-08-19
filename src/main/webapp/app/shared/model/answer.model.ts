import { IAnswerList } from 'app/shared/model/answer-list.model';
import { IWish } from 'app/shared/model/wish.model';

export interface IAnswer {
  id?: number;
  quantity?: number;
  unit?: string;
  model?: string;
  brand?: string;
  answerList?: IAnswerList;
  wish?: IWish;
}

export class Answer implements IAnswer {
  constructor(
    public id?: number,
    public quantity?: number,
    public unit?: string,
    public model?: string,
    public brand?: string,
    public answerList?: IAnswerList,
    public wish?: IWish
  ) {}
}
