package com.tphucnha.northwind.web.rest;

import com.tphucnha.northwind.repository.PurchaseOrderItemRepository;
import com.tphucnha.northwind.service.PurchaseOrderItemService;
import com.tphucnha.northwind.service.dto.PurchaseOrderItemDTO;
import com.tphucnha.northwind.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.tphucnha.northwind.domain.PurchaseOrderItem}.
 */
@RestController
@RequestMapping("/api")
public class PurchaseOrderItemResource {

    private final Logger log = LoggerFactory.getLogger(PurchaseOrderItemResource.class);

    private static final String ENTITY_NAME = "purchaseOrderItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PurchaseOrderItemService purchaseOrderItemService;

    private final PurchaseOrderItemRepository purchaseOrderItemRepository;

    public PurchaseOrderItemResource(
        PurchaseOrderItemService purchaseOrderItemService,
        PurchaseOrderItemRepository purchaseOrderItemRepository
    ) {
        this.purchaseOrderItemService = purchaseOrderItemService;
        this.purchaseOrderItemRepository = purchaseOrderItemRepository;
    }

    /**
     * {@code POST  /purchase-order-items} : Create a new purchaseOrderItem.
     *
     * @param purchaseOrderItemDTO the purchaseOrderItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new purchaseOrderItemDTO, or with status {@code 400 (Bad Request)} if the purchaseOrderItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/purchase-order-items")
    public ResponseEntity<PurchaseOrderItemDTO> createPurchaseOrderItem(@Valid @RequestBody PurchaseOrderItemDTO purchaseOrderItemDTO)
        throws URISyntaxException {
        log.debug("REST request to save PurchaseOrderItem : {}", purchaseOrderItemDTO);
        if (purchaseOrderItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new purchaseOrderItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PurchaseOrderItemDTO result = purchaseOrderItemService.save(purchaseOrderItemDTO);
        return ResponseEntity
            .created(new URI("/api/purchase-order-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /purchase-order-items/:id} : Updates an existing purchaseOrderItem.
     *
     * @param id the id of the purchaseOrderItemDTO to save.
     * @param purchaseOrderItemDTO the purchaseOrderItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchaseOrderItemDTO,
     * or with status {@code 400 (Bad Request)} if the purchaseOrderItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the purchaseOrderItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/purchase-order-items/{id}")
    public ResponseEntity<PurchaseOrderItemDTO> updatePurchaseOrderItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PurchaseOrderItemDTO purchaseOrderItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PurchaseOrderItem : {}, {}", id, purchaseOrderItemDTO);
        if (purchaseOrderItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchaseOrderItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!purchaseOrderItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PurchaseOrderItemDTO result = purchaseOrderItemService.save(purchaseOrderItemDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, purchaseOrderItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /purchase-order-items/:id} : Partial updates given fields of an existing purchaseOrderItem, field will ignore if it is null
     *
     * @param id the id of the purchaseOrderItemDTO to save.
     * @param purchaseOrderItemDTO the purchaseOrderItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchaseOrderItemDTO,
     * or with status {@code 400 (Bad Request)} if the purchaseOrderItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the purchaseOrderItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the purchaseOrderItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/purchase-order-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PurchaseOrderItemDTO> partialUpdatePurchaseOrderItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PurchaseOrderItemDTO purchaseOrderItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PurchaseOrderItem partially : {}, {}", id, purchaseOrderItemDTO);
        if (purchaseOrderItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchaseOrderItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!purchaseOrderItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PurchaseOrderItemDTO> result = purchaseOrderItemService.partialUpdate(purchaseOrderItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, purchaseOrderItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /purchase-order-items} : get all the purchaseOrderItems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of purchaseOrderItems in body.
     */
    @GetMapping("/purchase-order-items")
    public List<PurchaseOrderItemDTO> getAllPurchaseOrderItems() {
        log.debug("REST request to get all PurchaseOrderItems");
        return purchaseOrderItemService.findAll();
    }

    /**
     * {@code GET  /purchase-order-items/:id} : get the "id" purchaseOrderItem.
     *
     * @param id the id of the purchaseOrderItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the purchaseOrderItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/purchase-order-items/{id}")
    public ResponseEntity<PurchaseOrderItemDTO> getPurchaseOrderItem(@PathVariable Long id) {
        log.debug("REST request to get PurchaseOrderItem : {}", id);
        Optional<PurchaseOrderItemDTO> purchaseOrderItemDTO = purchaseOrderItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(purchaseOrderItemDTO);
    }

    /**
     * {@code DELETE  /purchase-order-items/:id} : delete the "id" purchaseOrderItem.
     *
     * @param id the id of the purchaseOrderItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/purchase-order-items/{id}")
    public ResponseEntity<Void> deletePurchaseOrderItem(@PathVariable Long id) {
        log.debug("REST request to delete PurchaseOrderItem : {}", id);
        purchaseOrderItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
