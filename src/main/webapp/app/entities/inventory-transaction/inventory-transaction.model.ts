import dayjs from 'dayjs/esm';
import { ICustomerOrder } from 'app/entities/customer-order/customer-order.model';
import { IProduct } from 'app/entities/product/product.model';
import { IPurchaseOrder } from 'app/entities/purchase-order/purchase-order.model';
import { InventoryTransactionType } from 'app/entities/enumerations/inventory-transaction-type.model';

export interface IInventoryTransaction {
  id?: number;
  transactionType?: InventoryTransactionType | null;
  createDate?: dayjs.Dayjs | null;
  modifiedDate?: dayjs.Dayjs | null;
  quantity?: number | null;
  comments?: string | null;
  orders?: ICustomerOrder[] | null;
  products?: IProduct[] | null;
  purchaseOrders?: IPurchaseOrder[] | null;
}

export class InventoryTransaction implements IInventoryTransaction {
  constructor(
    public id?: number,
    public transactionType?: InventoryTransactionType | null,
    public createDate?: dayjs.Dayjs | null,
    public modifiedDate?: dayjs.Dayjs | null,
    public quantity?: number | null,
    public comments?: string | null,
    public orders?: ICustomerOrder[] | null,
    public products?: IProduct[] | null,
    public purchaseOrders?: IPurchaseOrder[] | null
  ) {}
}

export function getInventoryTransactionIdentifier(inventoryTransaction: IInventoryTransaction): number | undefined {
  return inventoryTransaction.id;
}
