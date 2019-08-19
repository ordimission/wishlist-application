export interface IAnswerList {
  id?: number;
}

export class AnswerList implements IAnswerList {
  constructor(public id?: number) {}
}
