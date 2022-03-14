import dayjs from 'dayjs/esm';
import { IProduct } from 'app/entities/product/product.model';
import { IPurchaseOrder } from 'app/entities/purchase-order/purchase-order.model';

export interface IPurchaseOrderItem {
  id?: number;
  quantity?: number | null;
  unitCost?: number | null;
  receivedDate?: dayjs.Dayjs | null;
  inventoryPosted?: boolean | null;
  product?: IProduct;
  purchaseOrder?: IPurchaseOrder;
}

export class PurchaseOrderItem implements IPurchaseOrderItem {
  constructor(
    public id?: number,
    public quantity?: number | null,
    public unitCost?: number | null,
    public receivedDate?: dayjs.Dayjs | null,
    public inventoryPosted?: boolean | null,
    public product?: IProduct,
    public purchaseOrder?: IPurchaseOrder
  ) {
    this.inventoryPosted = this.inventoryPosted ?? false;
  }
}

export function getPurchaseOrderItemIdentifier(purchaseOrderItem: IPurchaseOrderItem): number | undefined {
  return purchaseOrderItem.id;
}
