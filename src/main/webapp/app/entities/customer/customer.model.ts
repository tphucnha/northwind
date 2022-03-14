import { ICustomerOrder } from 'app/entities/customer-order/customer-order.model';

export interface ICustomer {
  id?: number;
  company?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  address?: string | null;
  phone?: string | null;
  orders?: ICustomerOrder[] | null;
}

export class Customer implements ICustomer {
  constructor(
    public id?: number,
    public company?: string | null,
    public firstName?: string | null,
    public lastName?: string | null,
    public address?: string | null,
    public phone?: string | null,
    public orders?: ICustomerOrder[] | null
  ) {}
}

export function getCustomerIdentifier(customer: ICustomer): number | undefined {
  return customer.id;
}
