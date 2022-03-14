import dayjs from 'dayjs/esm';
import { IProduct } from 'app/entities/product/product.model';
import { ICustomerOrder } from 'app/entities/customer-order/customer-order.model';
import { OrderItemStatus } from 'app/entities/enumerations/order-item-status.model';

export interface IOrderItem {
  id?: number;
  quantity?: number | null;
  unitPrice?: number | null;
  discount?: number | null;
  status?: OrderItemStatus | null;
  allocatedDate?: dayjs.Dayjs | null;
  products?: IProduct[] | null;
  order?: ICustomerOrder | null;
}

export class OrderItem implements IOrderItem {
  constructor(
    public id?: number,
    public quantity?: number | null,
    public unitPrice?: number | null,
    public discount?: number | null,
    public status?: OrderItemStatus | null,
    public allocatedDate?: dayjs.Dayjs | null,
    public products?: IProduct[] | null,
    public order?: ICustomerOrder | null
  ) {}
}

export function getOrderItemIdentifier(orderItem: IOrderItem): number | undefined {
  return orderItem.id;
}
