package com.tphucnha.northwind.web.rest;

import static com.tphucnha.northwind.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tphucnha.northwind.IntegrationTest;
import com.tphucnha.northwind.domain.Product;
import com.tphucnha.northwind.domain.PurchaseOrder;
import com.tphucnha.northwind.domain.PurchaseOrderItem;
import com.tphucnha.northwind.repository.PurchaseOrderItemRepository;
import com.tphucnha.northwind.service.dto.PurchaseOrderItemDTO;
import com.tphucnha.northwind.service.mapper.PurchaseOrderItemMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PurchaseOrderItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PurchaseOrderItemResourceIT {

    private static final Long DEFAULT_QUANTITY = 1L;
    private static final Long UPDATED_QUANTITY = 2L;

    private static final BigDecimal DEFAULT_UNIT_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_UNIT_COST = new BigDecimal(2);

    private static final Instant DEFAULT_RECEIVED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RECEIVED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_INVENTORY_POSTED = false;
    private static final Boolean UPDATED_INVENTORY_POSTED = true;

    private static final String ENTITY_API_URL = "/api/purchase-order-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PurchaseOrderItemRepository purchaseOrderItemRepository;

    @Autowired
    private PurchaseOrderItemMapper purchaseOrderItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPurchaseOrderItemMockMvc;

    private PurchaseOrderItem purchaseOrderItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseOrderItem createEntity(EntityManager em) {
        PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem()
            .quantity(DEFAULT_QUANTITY)
            .unitCost(DEFAULT_UNIT_COST)
            .receivedDate(DEFAULT_RECEIVED_DATE)
            .inventoryPosted(DEFAULT_INVENTORY_POSTED);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        purchaseOrderItem.setProduct(product);
        // Add required entity
        PurchaseOrder purchaseOrder;
        if (TestUtil.findAll(em, PurchaseOrder.class).isEmpty()) {
            purchaseOrder = PurchaseOrderResourceIT.createEntity(em);
            em.persist(purchaseOrder);
            em.flush();
        } else {
            purchaseOrder = TestUtil.findAll(em, PurchaseOrder.class).get(0);
        }
        purchaseOrderItem.setPurchaseOrder(purchaseOrder);
        return purchaseOrderItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseOrderItem createUpdatedEntity(EntityManager em) {
        PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem()
            .quantity(UPDATED_QUANTITY)
            .unitCost(UPDATED_UNIT_COST)
            .receivedDate(UPDATED_RECEIVED_DATE)
            .inventoryPosted(UPDATED_INVENTORY_POSTED);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        purchaseOrderItem.setProduct(product);
        // Add required entity
        PurchaseOrder purchaseOrder;
        if (TestUtil.findAll(em, PurchaseOrder.class).isEmpty()) {
            purchaseOrder = PurchaseOrderResourceIT.createUpdatedEntity(em);
            em.persist(purchaseOrder);
            em.flush();
        } else {
            purchaseOrder = TestUtil.findAll(em, PurchaseOrder.class).get(0);
        }
        purchaseOrderItem.setPurchaseOrder(purchaseOrder);
        return purchaseOrderItem;
    }

    @BeforeEach
    public void initTest() {
        purchaseOrderItem = createEntity(em);
    }

    @Test
    @Transactional
    void createPurchaseOrderItem() throws Exception {
        int databaseSizeBeforeCreate = purchaseOrderItemRepository.findAll().size();
        // Create the PurchaseOrderItem
        PurchaseOrderItemDTO purchaseOrderItemDTO = purchaseOrderItemMapper.toDto(purchaseOrderItem);
        restPurchaseOrderItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderItemDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PurchaseOrderItem in the database
        List<PurchaseOrderItem> purchaseOrderItemList = purchaseOrderItemRepository.findAll();
        assertThat(purchaseOrderItemList).hasSize(databaseSizeBeforeCreate + 1);
        PurchaseOrderItem testPurchaseOrderItem = purchaseOrderItemList.get(purchaseOrderItemList.size() - 1);
        assertThat(testPurchaseOrderItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testPurchaseOrderItem.getUnitCost()).isEqualByComparingTo(DEFAULT_UNIT_COST);
        assertThat(testPurchaseOrderItem.getReceivedDate()).isEqualTo(DEFAULT_RECEIVED_DATE);
        assertThat(testPurchaseOrderItem.getInventoryPosted()).isEqualTo(DEFAULT_INVENTORY_POSTED);
    }

    @Test
    @Transactional
    void createPurchaseOrderItemWithExistingId() throws Exception {
        // Create the PurchaseOrderItem with an existing ID
        purchaseOrderItem.setId(1L);
        PurchaseOrderItemDTO purchaseOrderItemDTO = purchaseOrderItemMapper.toDto(purchaseOrderItem);

        int databaseSizeBeforeCreate = purchaseOrderItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchaseOrderItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrderItem in the database
        List<PurchaseOrderItem> purchaseOrderItemList = purchaseOrderItemRepository.findAll();
        assertThat(purchaseOrderItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderItems() throws Exception {
        // Initialize the database
        purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        // Get all the purchaseOrderItemList
        restPurchaseOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseOrderItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].unitCost").value(hasItem(sameNumber(DEFAULT_UNIT_COST))))
            .andExpect(jsonPath("$.[*].receivedDate").value(hasItem(DEFAULT_RECEIVED_DATE.toString())))
            .andExpect(jsonPath("$.[*].inventoryPosted").value(hasItem(DEFAULT_INVENTORY_POSTED.booleanValue())));
    }

    @Test
    @Transactional
    void getPurchaseOrderItem() throws Exception {
        // Initialize the database
        purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        // Get the purchaseOrderItem
        restPurchaseOrderItemMockMvc
            .perform(get(ENTITY_API_URL_ID, purchaseOrderItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(purchaseOrderItem.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.intValue()))
            .andExpect(jsonPath("$.unitCost").value(sameNumber(DEFAULT_UNIT_COST)))
            .andExpect(jsonPath("$.receivedDate").value(DEFAULT_RECEIVED_DATE.toString()))
            .andExpect(jsonPath("$.inventoryPosted").value(DEFAULT_INVENTORY_POSTED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingPurchaseOrderItem() throws Exception {
        // Get the purchaseOrderItem
        restPurchaseOrderItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPurchaseOrderItem() throws Exception {
        // Initialize the database
        purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        int databaseSizeBeforeUpdate = purchaseOrderItemRepository.findAll().size();

        // Update the purchaseOrderItem
        PurchaseOrderItem updatedPurchaseOrderItem = purchaseOrderItemRepository.findById(purchaseOrderItem.getId()).get();
        // Disconnect from session so that the updates on updatedPurchaseOrderItem are not directly saved in db
        em.detach(updatedPurchaseOrderItem);
        updatedPurchaseOrderItem
            .quantity(UPDATED_QUANTITY)
            .unitCost(UPDATED_UNIT_COST)
            .receivedDate(UPDATED_RECEIVED_DATE)
            .inventoryPosted(UPDATED_INVENTORY_POSTED);
        PurchaseOrderItemDTO purchaseOrderItemDTO = purchaseOrderItemMapper.toDto(updatedPurchaseOrderItem);

        restPurchaseOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, purchaseOrderItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseOrderItem in the database
        List<PurchaseOrderItem> purchaseOrderItemList = purchaseOrderItemRepository.findAll();
        assertThat(purchaseOrderItemList).hasSize(databaseSizeBeforeUpdate);
        PurchaseOrderItem testPurchaseOrderItem = purchaseOrderItemList.get(purchaseOrderItemList.size() - 1);
        assertThat(testPurchaseOrderItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testPurchaseOrderItem.getUnitCost()).isEqualByComparingTo(UPDATED_UNIT_COST);
        assertThat(testPurchaseOrderItem.getReceivedDate()).isEqualTo(UPDATED_RECEIVED_DATE);
        assertThat(testPurchaseOrderItem.getInventoryPosted()).isEqualTo(UPDATED_INVENTORY_POSTED);
    }

    @Test
    @Transactional
    void putNonExistingPurchaseOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderItemRepository.findAll().size();
        purchaseOrderItem.setId(count.incrementAndGet());

        // Create the PurchaseOrderItem
        PurchaseOrderItemDTO purchaseOrderItemDTO = purchaseOrderItemMapper.toDto(purchaseOrderItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, purchaseOrderItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrderItem in the database
        List<PurchaseOrderItem> purchaseOrderItemList = purchaseOrderItemRepository.findAll();
        assertThat(purchaseOrderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPurchaseOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderItemRepository.findAll().size();
        purchaseOrderItem.setId(count.incrementAndGet());

        // Create the PurchaseOrderItem
        PurchaseOrderItemDTO purchaseOrderItemDTO = purchaseOrderItemMapper.toDto(purchaseOrderItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrderItem in the database
        List<PurchaseOrderItem> purchaseOrderItemList = purchaseOrderItemRepository.findAll();
        assertThat(purchaseOrderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPurchaseOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderItemRepository.findAll().size();
        purchaseOrderItem.setId(count.incrementAndGet());

        // Create the PurchaseOrderItem
        PurchaseOrderItemDTO purchaseOrderItemDTO = purchaseOrderItemMapper.toDto(purchaseOrderItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchaseOrderItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchaseOrderItem in the database
        List<PurchaseOrderItem> purchaseOrderItemList = purchaseOrderItemRepository.findAll();
        assertThat(purchaseOrderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePurchaseOrderItemWithPatch() throws Exception {
        // Initialize the database
        purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        int databaseSizeBeforeUpdate = purchaseOrderItemRepository.findAll().size();

        // Update the purchaseOrderItem using partial update
        PurchaseOrderItem partialUpdatedPurchaseOrderItem = new PurchaseOrderItem();
        partialUpdatedPurchaseOrderItem.setId(purchaseOrderItem.getId());

        partialUpdatedPurchaseOrderItem.quantity(UPDATED_QUANTITY);

        restPurchaseOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchaseOrderItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchaseOrderItem))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseOrderItem in the database
        List<PurchaseOrderItem> purchaseOrderItemList = purchaseOrderItemRepository.findAll();
        assertThat(purchaseOrderItemList).hasSize(databaseSizeBeforeUpdate);
        PurchaseOrderItem testPurchaseOrderItem = purchaseOrderItemList.get(purchaseOrderItemList.size() - 1);
        assertThat(testPurchaseOrderItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testPurchaseOrderItem.getUnitCost()).isEqualByComparingTo(DEFAULT_UNIT_COST);
        assertThat(testPurchaseOrderItem.getReceivedDate()).isEqualTo(DEFAULT_RECEIVED_DATE);
        assertThat(testPurchaseOrderItem.getInventoryPosted()).isEqualTo(DEFAULT_INVENTORY_POSTED);
    }

    @Test
    @Transactional
    void fullUpdatePurchaseOrderItemWithPatch() throws Exception {
        // Initialize the database
        purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        int databaseSizeBeforeUpdate = purchaseOrderItemRepository.findAll().size();

        // Update the purchaseOrderItem using partial update
        PurchaseOrderItem partialUpdatedPurchaseOrderItem = new PurchaseOrderItem();
        partialUpdatedPurchaseOrderItem.setId(purchaseOrderItem.getId());

        partialUpdatedPurchaseOrderItem
            .quantity(UPDATED_QUANTITY)
            .unitCost(UPDATED_UNIT_COST)
            .receivedDate(UPDATED_RECEIVED_DATE)
            .inventoryPosted(UPDATED_INVENTORY_POSTED);

        restPurchaseOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchaseOrderItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchaseOrderItem))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseOrderItem in the database
        List<PurchaseOrderItem> purchaseOrderItemList = purchaseOrderItemRepository.findAll();
        assertThat(purchaseOrderItemList).hasSize(databaseSizeBeforeUpdate);
        PurchaseOrderItem testPurchaseOrderItem = purchaseOrderItemList.get(purchaseOrderItemList.size() - 1);
        assertThat(testPurchaseOrderItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testPurchaseOrderItem.getUnitCost()).isEqualByComparingTo(UPDATED_UNIT_COST);
        assertThat(testPurchaseOrderItem.getReceivedDate()).isEqualTo(UPDATED_RECEIVED_DATE);
        assertThat(testPurchaseOrderItem.getInventoryPosted()).isEqualTo(UPDATED_INVENTORY_POSTED);
    }

    @Test
    @Transactional
    void patchNonExistingPurchaseOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderItemRepository.findAll().size();
        purchaseOrderItem.setId(count.incrementAndGet());

        // Create the PurchaseOrderItem
        PurchaseOrderItemDTO purchaseOrderItemDTO = purchaseOrderItemMapper.toDto(purchaseOrderItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, purchaseOrderItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrderItem in the database
        List<PurchaseOrderItem> purchaseOrderItemList = purchaseOrderItemRepository.findAll();
        assertThat(purchaseOrderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPurchaseOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderItemRepository.findAll().size();
        purchaseOrderItem.setId(count.incrementAndGet());

        // Create the PurchaseOrderItem
        PurchaseOrderItemDTO purchaseOrderItemDTO = purchaseOrderItemMapper.toDto(purchaseOrderItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrderItem in the database
        List<PurchaseOrderItem> purchaseOrderItemList = purchaseOrderItemRepository.findAll();
        assertThat(purchaseOrderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPurchaseOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderItemRepository.findAll().size();
        purchaseOrderItem.setId(count.incrementAndGet());

        // Create the PurchaseOrderItem
        PurchaseOrderItemDTO purchaseOrderItemDTO = purchaseOrderItemMapper.toDto(purchaseOrderItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchaseOrderItem in the database
        List<PurchaseOrderItem> purchaseOrderItemList = purchaseOrderItemRepository.findAll();
        assertThat(purchaseOrderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePurchaseOrderItem() throws Exception {
        // Initialize the database
        purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        int databaseSizeBeforeDelete = purchaseOrderItemRepository.findAll().size();

        // Delete the purchaseOrderItem
        restPurchaseOrderItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, purchaseOrderItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PurchaseOrderItem> purchaseOrderItemList = purchaseOrderItemRepository.findAll();
        assertThat(purchaseOrderItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
