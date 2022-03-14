import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IInventoryTransaction } from '../inventory-transaction.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { InventoryTransactionService } from '../service/inventory-transaction.service';
import { InventoryTransactionDeleteDialogComponent } from '../delete/inventory-transaction-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-inventory-transaction',
  templateUrl: './inventory-transaction.component.html',
})
export class InventoryTransactionComponent implements OnInit {
  inventoryTransactions: IInventoryTransaction[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected inventoryTransactionService: InventoryTransactionService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.inventoryTransactions = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.isLoading = true;

    this.inventoryTransactionService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IInventoryTransaction[]>) => {
          this.isLoading = false;
          this.paginateInventoryTransactions(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.inventoryTransactions = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IInventoryTransaction): number {
    return item.id!;
  }

  delete(inventoryTransaction: IInventoryTransaction): void {
    const modalRef = this.modalService.open(InventoryTransactionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.inventoryTransaction = inventoryTransaction;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateInventoryTransactions(data: IInventoryTransaction[] | null, headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    if (linkHeader) {
      this.links = this.parseLinks.parse(linkHeader);
    } else {
      this.links = {
        last: 0,
      };
    }
    if (data) {
      for (const d of data) {
        this.inventoryTransactions.push(d);
      }
    }
  }
}
