import { ICategory } from 'app/entities/category/category.model';
import { ISupplier } from 'app/entities/supplier/supplier.model';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { IPurchaseOrderItem } from 'app/entities/purchase-order-item/purchase-order-item.model';
import { IInventoryTransaction } from 'app/entities/inventory-transaction/inventory-transaction.model';

export interface IProduct {
  id?: number;
  code?: string | null;
  name?: string | null;
  description?: string | null;
  standardCost?: number | null;
  listPrice?: number | null;
  reorderLevel?: number | null;
  targetLevel?: number | null;
  quantityPerUnit?: number | null;
  disContinued?: boolean | null;
  minimumReorderQuantity?: number | null;
  category?: ICategory | null;
  suppliers?: ISupplier[] | null;
  orderItem?: IOrderItem | null;
  purchaseOrderItem?: IPurchaseOrderItem | null;
  inventoryTransaction?: IInventoryTransaction | null;
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public code?: string | null,
    public name?: string | null,
    public description?: string | null,
    public standardCost?: number | null,
    public listPrice?: number | null,
    public reorderLevel?: number | null,
    public targetLevel?: number | null,
    public quantityPerUnit?: number | null,
    public disContinued?: boolean | null,
    public minimumReorderQuantity?: number | null,
    public category?: ICategory | null,
    public suppliers?: ISupplier[] | null,
    public orderItem?: IOrderItem | null,
    public purchaseOrderItem?: IPurchaseOrderItem | null,
    public inventoryTransaction?: IInventoryTransaction | null
  ) {
    this.disContinued = this.disContinued ?? false;
  }
}

export function getProductIdentifier(product: IProduct): number | undefined {
  return product.id;
}
