import dayjs from 'dayjs/esm';
import { IProduct } from 'app/entities/product/product.model';
import { InventoryTransactionType } from 'app/entities/enumerations/inventory-transaction-type.model';

export interface IInventoryTransaction {
  id?: number;
  transactionType?: InventoryTransactionType | null;
  createDate?: dayjs.Dayjs | null;
  modifiedDate?: dayjs.Dayjs | null;
  quantity?: number | null;
  comments?: string | null;
  product?: IProduct;
}

export class InventoryTransaction implements IInventoryTransaction {
  constructor(
    public id?: number,
    public transactionType?: InventoryTransactionType | null,
    public createDate?: dayjs.Dayjs | null,
    public modifiedDate?: dayjs.Dayjs | null,
    public quantity?: number | null,
    public comments?: string | null,
    public product?: IProduct
  ) {}
}

export function getInventoryTransactionIdentifier(inventoryTransaction: IInventoryTransaction): number | undefined {
  return inventoryTransaction.id;
}
