package com.tphucnha.northwind.service;

import com.tphucnha.northwind.domain.PurchaseOrderItem;
import com.tphucnha.northwind.repository.PurchaseOrderItemRepository;
import com.tphucnha.northwind.service.dto.PurchaseOrderItemDTO;
import com.tphucnha.northwind.service.mapper.PurchaseOrderItemMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PurchaseOrderItem}.
 */
@Service
@Transactional
public class PurchaseOrderItemService {

    private final Logger log = LoggerFactory.getLogger(PurchaseOrderItemService.class);

    private final PurchaseOrderItemRepository purchaseOrderItemRepository;

    private final PurchaseOrderItemMapper purchaseOrderItemMapper;

    public PurchaseOrderItemService(
        PurchaseOrderItemRepository purchaseOrderItemRepository,
        PurchaseOrderItemMapper purchaseOrderItemMapper
    ) {
        this.purchaseOrderItemRepository = purchaseOrderItemRepository;
        this.purchaseOrderItemMapper = purchaseOrderItemMapper;
    }

    /**
     * Save a purchaseOrderItem.
     *
     * @param purchaseOrderItemDTO the entity to save.
     * @return the persisted entity.
     */
    public PurchaseOrderItemDTO save(PurchaseOrderItemDTO purchaseOrderItemDTO) {
        log.debug("Request to save PurchaseOrderItem : {}", purchaseOrderItemDTO);
        PurchaseOrderItem purchaseOrderItem = purchaseOrderItemMapper.toEntity(purchaseOrderItemDTO);
        purchaseOrderItem = purchaseOrderItemRepository.save(purchaseOrderItem);
        return purchaseOrderItemMapper.toDto(purchaseOrderItem);
    }

    /**
     * Partially update a purchaseOrderItem.
     *
     * @param purchaseOrderItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PurchaseOrderItemDTO> partialUpdate(PurchaseOrderItemDTO purchaseOrderItemDTO) {
        log.debug("Request to partially update PurchaseOrderItem : {}", purchaseOrderItemDTO);

        return purchaseOrderItemRepository
            .findById(purchaseOrderItemDTO.getId())
            .map(existingPurchaseOrderItem -> {
                purchaseOrderItemMapper.partialUpdate(existingPurchaseOrderItem, purchaseOrderItemDTO);

                return existingPurchaseOrderItem;
            })
            .map(purchaseOrderItemRepository::save)
            .map(purchaseOrderItemMapper::toDto);
    }

    /**
     * Get all the purchaseOrderItems.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PurchaseOrderItemDTO> findAll() {
        log.debug("Request to get all PurchaseOrderItems");
        return purchaseOrderItemRepository
            .findAll()
            .stream()
            .map(purchaseOrderItemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one purchaseOrderItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PurchaseOrderItemDTO> findOne(Long id) {
        log.debug("Request to get PurchaseOrderItem : {}", id);
        return purchaseOrderItemRepository.findById(id).map(purchaseOrderItemMapper::toDto);
    }

    /**
     * Delete the purchaseOrderItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PurchaseOrderItem : {}", id);
        purchaseOrderItemRepository.deleteById(id);
    }
}
