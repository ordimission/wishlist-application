export interface IWishList {
  id?: number;
  description?: string;
}

export class WishList implements IWishList {
  constructor(public id?: number, public description?: string) {}
}
