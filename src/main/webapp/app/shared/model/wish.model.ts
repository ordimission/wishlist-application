import { IWishList } from 'app/shared/model/wish-list.model';

export interface IWish {
  id?: number;
  description?: string;
  unitPrice?: number;
  quantity?: number;
  unit?: string;
  model?: string;
  brand?: string;
  wishList?: IWishList;
}

export class Wish implements IWish {
  constructor(
    public id?: number,
    public description?: string,
    public unitPrice?: number,
    public quantity?: number,
    public unit?: string,
    public model?: string,
    public brand?: string,
    public wishList?: IWishList
  ) {}
}
