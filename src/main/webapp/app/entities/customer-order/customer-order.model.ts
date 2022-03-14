import dayjs from 'dayjs/esm';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { ICustomer } from 'app/entities/customer/customer.model';
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
  orderItems?: IOrderItem[] | null;
  customer?: ICustomer;
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
    public orderItems?: IOrderItem[] | null,
    public customer?: ICustomer
  ) {}
}

export function getCustomerOrderIdentifier(customerOrder: ICustomerOrder): number | undefined {
  return customerOrder.id;
}
