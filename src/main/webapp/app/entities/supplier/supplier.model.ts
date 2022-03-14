import { IProduct } from 'app/entities/product/product.model';

export interface ISupplier {
  id?: number;
  company?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  address?: string | null;
  phone?: string | null;
  products?: IProduct[] | null;
}

export class Supplier implements ISupplier {
  constructor(
    public id?: number,
    public company?: string | null,
    public firstName?: string | null,
    public lastName?: string | null,
    public address?: string | null,
    public phone?: string | null,
    public products?: IProduct[] | null
  ) {}
}

export function getSupplierIdentifier(supplier: ISupplier): number | undefined {
  return supplier.id;
}
