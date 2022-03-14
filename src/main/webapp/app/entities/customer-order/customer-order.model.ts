import dayjs from 'dayjs/esm';
import { ICustomer } from 'app/entities/customer/customer.model';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { IInventoryTransaction } from 'app/entities/inventory-transaction/inventory-transaction.model';
import { OrderStatus } from 'app/entities/enumerations/order-status.model';

export interface ICustomerOrder {
  id?: number;
  orderDate?: dayjs.Dayjs | null;
  shippedDate?: dayjs.Dayjs | null;
  shipAddress?: string | null;
  shippingFee?: number | null;
  taxes?: number | null;
  paymentMethod?: string | null;
  paidDate?: dayjs.Dayjs | null;
  status?: OrderStatus | null;
  notes?: string | null;
  customers?: ICustomer[] | null;
  orderItems?: IOrderItem[] | null;
  inventoryTransaction?: IInventoryTransaction | null;
}

export class CustomerOrder implements ICustomerOrder {
  constructor(
    public id?: number,
    public orderDate?: dayjs.Dayjs | null,
    public shippedDate?: dayjs.Dayjs | null,
    public shipAddress?: string | null,
    public shippingFee?: number | null,
    public taxes?: number | null,
    public paymentMethod?: string | null,
    public paidDate?: dayjs.Dayjs | null,
    public status?: OrderStatus | null,
    public notes?: string | null,
    public customers?: ICustomer[] | null,
    public orderItems?: IOrderItem[] | null,
    public inventoryTransaction?: IInventoryTransaction | null
  ) {}
}

export function getCustomerOrderIdentifier(customerOrder: ICustomerOrder): number | undefined {
  return customerOrder.id;
}
