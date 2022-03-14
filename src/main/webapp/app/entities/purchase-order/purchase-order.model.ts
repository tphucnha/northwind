import dayjs from 'dayjs/esm';
import { IPurchaseOrderItem } from 'app/entities/purchase-order-item/purchase-order-item.model';
import { ISupplier } from 'app/entities/supplier/supplier.model';
import { PurchaseOrderStatus } from 'app/entities/enumerations/purchase-order-status.model';

export interface IPurchaseOrder {
  id?: number;
  status?: PurchaseOrderStatus | null;
  createDate?: dayjs.Dayjs | null;
  expectedDate?: dayjs.Dayjs | null;
  paymentDate?: dayjs.Dayjs | null;
  paymentMethod?: string | null;
  paymentAmount?: number | null;
  orderItems?: IPurchaseOrderItem[] | null;
  supplier?: ISupplier;
}

export class PurchaseOrder implements IPurchaseOrder {
  constructor(
    public id?: number,
    public status?: PurchaseOrderStatus | null,
    public createDate?: dayjs.Dayjs | null,
    public expectedDate?: dayjs.Dayjs | null,
    public paymentDate?: dayjs.Dayjs | null,
    public paymentMethod?: string | null,
    public paymentAmount?: number | null,
    public orderItems?: IPurchaseOrderItem[] | null,
    public supplier?: ISupplier
  ) {}
}

export function getPurchaseOrderIdentifier(purchaseOrder: IPurchaseOrder): number | undefined {
  return purchaseOrder.id;
}
