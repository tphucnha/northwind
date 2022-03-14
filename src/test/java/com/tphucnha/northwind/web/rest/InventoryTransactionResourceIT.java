package com.tphucnha.northwind.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tphucnha.northwind.IntegrationTest;
import com.tphucnha.northwind.domain.InventoryTransaction;
import com.tphucnha.northwind.domain.enumeration.InventoryTransactionType;
import com.tphucnha.northwind.repository.InventoryTransactionRepository;
import com.tphucnha.northwind.service.dto.InventoryTransactionDTO;
import com.tphucnha.northwind.service.mapper.InventoryTransactionMapper;
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
 * Integration tests for the {@link InventoryTransactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InventoryTransactionResourceIT {

    private static final InventoryTransactionType DEFAULT_TRANSACTION_TYPE = InventoryTransactionType.PURCHASED;
    private static final InventoryTransactionType UPDATED_TRANSACTION_TYPE = InventoryTransactionType.SOLD;

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_QUANTITY = 1L;
    private static final Long UPDATED_QUANTITY = 2L;

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/inventory-transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InventoryTransactionRepository inventoryTransactionRepository;

    @Autowired
    private InventoryTransactionMapper inventoryTransactionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInventoryTransactionMockMvc;

    private InventoryTransaction inventoryTransaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InventoryTransaction createEntity(EntityManager em) {
        InventoryTransaction inventoryTransaction = new InventoryTransaction()
            .transactionType(DEFAULT_TRANSACTION_TYPE)
            .createDate(DEFAULT_CREATE_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE)
            .quantity(DEFAULT_QUANTITY)
            .comments(DEFAULT_COMMENTS);
        return inventoryTransaction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InventoryTransaction createUpdatedEntity(EntityManager em) {
        InventoryTransaction inventoryTransaction = new InventoryTransaction()
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .createDate(UPDATED_CREATE_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .quantity(UPDATED_QUANTITY)
            .comments(UPDATED_COMMENTS);
        return inventoryTransaction;
    }

    @BeforeEach
    public void initTest() {
        inventoryTransaction = createEntity(em);
    }

    @Test
    @Transactional
    void createInventoryTransaction() throws Exception {
        int databaseSizeBeforeCreate = inventoryTransactionRepository.findAll().size();
        // Create the InventoryTransaction
        InventoryTransactionDTO inventoryTransactionDTO = inventoryTransactionMapper.toDto(inventoryTransaction);
        restInventoryTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inventoryTransactionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the InventoryTransaction in the database
        List<InventoryTransaction> inventoryTransactionList = inventoryTransactionRepository.findAll();
        assertThat(inventoryTransactionList).hasSize(databaseSizeBeforeCreate + 1);
        InventoryTransaction testInventoryTransaction = inventoryTransactionList.get(inventoryTransactionList.size() - 1);
        assertThat(testInventoryTransaction.getTransactionType()).isEqualTo(DEFAULT_TRANSACTION_TYPE);
        assertThat(testInventoryTransaction.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testInventoryTransaction.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
        assertThat(testInventoryTransaction.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testInventoryTransaction.getComments()).isEqualTo(DEFAULT_COMMENTS);
    }

    @Test
    @Transactional
    void createInventoryTransactionWithExistingId() throws Exception {
        // Create the InventoryTransaction with an existing ID
        inventoryTransaction.setId(1L);
        InventoryTransactionDTO inventoryTransactionDTO = inventoryTransactionMapper.toDto(inventoryTransaction);

        int databaseSizeBeforeCreate = inventoryTransactionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInventoryTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inventoryTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventoryTransaction in the database
        List<InventoryTransaction> inventoryTransactionList = inventoryTransactionRepository.findAll();
        assertThat(inventoryTransactionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInventoryTransactions() throws Exception {
        // Initialize the database
        inventoryTransactionRepository.saveAndFlush(inventoryTransaction);

        // Get all the inventoryTransactionList
        restInventoryTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventoryTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));
    }

    @Test
    @Transactional
    void getInventoryTransaction() throws Exception {
        // Initialize the database
        inventoryTransactionRepository.saveAndFlush(inventoryTransaction);

        // Get the inventoryTransaction
        restInventoryTransactionMockMvc
            .perform(get(ENTITY_API_URL_ID, inventoryTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inventoryTransaction.getId().intValue()))
            .andExpect(jsonPath("$.transactionType").value(DEFAULT_TRANSACTION_TYPE.toString()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.intValue()))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS));
    }

    @Test
    @Transactional
    void getNonExistingInventoryTransaction() throws Exception {
        // Get the inventoryTransaction
        restInventoryTransactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInventoryTransaction() throws Exception {
        // Initialize the database
        inventoryTransactionRepository.saveAndFlush(inventoryTransaction);

        int databaseSizeBeforeUpdate = inventoryTransactionRepository.findAll().size();

        // Update the inventoryTransaction
        InventoryTransaction updatedInventoryTransaction = inventoryTransactionRepository.findById(inventoryTransaction.getId()).get();
        // Disconnect from session so that the updates on updatedInventoryTransaction are not directly saved in db
        em.detach(updatedInventoryTransaction);
        updatedInventoryTransaction
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .createDate(UPDATED_CREATE_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .quantity(UPDATED_QUANTITY)
            .comments(UPDATED_COMMENTS);
        InventoryTransactionDTO inventoryTransactionDTO = inventoryTransactionMapper.toDto(updatedInventoryTransaction);

        restInventoryTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inventoryTransactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inventoryTransactionDTO))
            )
            .andExpect(status().isOk());

        // Validate the InventoryTransaction in the database
        List<InventoryTransaction> inventoryTransactionList = inventoryTransactionRepository.findAll();
        assertThat(inventoryTransactionList).hasSize(databaseSizeBeforeUpdate);
        InventoryTransaction testInventoryTransaction = inventoryTransactionList.get(inventoryTransactionList.size() - 1);
        assertThat(testInventoryTransaction.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
        assertThat(testInventoryTransaction.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testInventoryTransaction.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
        assertThat(testInventoryTransaction.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testInventoryTransaction.getComments()).isEqualTo(UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void putNonExistingInventoryTransaction() throws Exception {
        int databaseSizeBeforeUpdate = inventoryTransactionRepository.findAll().size();
        inventoryTransaction.setId(count.incrementAndGet());

        // Create the InventoryTransaction
        InventoryTransactionDTO inventoryTransactionDTO = inventoryTransactionMapper.toDto(inventoryTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventoryTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inventoryTransactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inventoryTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventoryTransaction in the database
        List<InventoryTransaction> inventoryTransactionList = inventoryTransactionRepository.findAll();
        assertThat(inventoryTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInventoryTransaction() throws Exception {
        int databaseSizeBeforeUpdate = inventoryTransactionRepository.findAll().size();
        inventoryTransaction.setId(count.incrementAndGet());

        // Create the InventoryTransaction
        InventoryTransactionDTO inventoryTransactionDTO = inventoryTransactionMapper.toDto(inventoryTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inventoryTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventoryTransaction in the database
        List<InventoryTransaction> inventoryTransactionList = inventoryTransactionRepository.findAll();
        assertThat(inventoryTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInventoryTransaction() throws Exception {
        int databaseSizeBeforeUpdate = inventoryTransactionRepository.findAll().size();
        inventoryTransaction.setId(count.incrementAndGet());

        // Create the InventoryTransaction
        InventoryTransactionDTO inventoryTransactionDTO = inventoryTransactionMapper.toDto(inventoryTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryTransactionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inventoryTransactionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InventoryTransaction in the database
        List<InventoryTransaction> inventoryTransactionList = inventoryTransactionRepository.findAll();
        assertThat(inventoryTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInventoryTransactionWithPatch() throws Exception {
        // Initialize the database
        inventoryTransactionRepository.saveAndFlush(inventoryTransaction);

        int databaseSizeBeforeUpdate = inventoryTransactionRepository.findAll().size();

        // Update the inventoryTransaction using partial update
        InventoryTransaction partialUpdatedInventoryTransaction = new InventoryTransaction();
        partialUpdatedInventoryTransaction.setId(inventoryTransaction.getId());

        partialUpdatedInventoryTransaction.createDate(UPDATED_CREATE_DATE).modifiedDate(UPDATED_MODIFIED_DATE).quantity(UPDATED_QUANTITY);

        restInventoryTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventoryTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInventoryTransaction))
            )
            .andExpect(status().isOk());

        // Validate the InventoryTransaction in the database
        List<InventoryTransaction> inventoryTransactionList = inventoryTransactionRepository.findAll();
        assertThat(inventoryTransactionList).hasSize(databaseSizeBeforeUpdate);
        InventoryTransaction testInventoryTransaction = inventoryTransactionList.get(inventoryTransactionList.size() - 1);
        assertThat(testInventoryTransaction.getTransactionType()).isEqualTo(DEFAULT_TRANSACTION_TYPE);
        assertThat(testInventoryTransaction.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testInventoryTransaction.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
        assertThat(testInventoryTransaction.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testInventoryTransaction.getComments()).isEqualTo(DEFAULT_COMMENTS);
    }

    @Test
    @Transactional
    void fullUpdateInventoryTransactionWithPatch() throws Exception {
        // Initialize the database
        inventoryTransactionRepository.saveAndFlush(inventoryTransaction);

        int databaseSizeBeforeUpdate = inventoryTransactionRepository.findAll().size();

        // Update the inventoryTransaction using partial update
        InventoryTransaction partialUpdatedInventoryTransaction = new InventoryTransaction();
        partialUpdatedInventoryTransaction.setId(inventoryTransaction.getId());

        partialUpdatedInventoryTransaction
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .createDate(UPDATED_CREATE_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .quantity(UPDATED_QUANTITY)
            .comments(UPDATED_COMMENTS);

        restInventoryTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventoryTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInventoryTransaction))
            )
            .andExpect(status().isOk());

        // Validate the InventoryTransaction in the database
        List<InventoryTransaction> inventoryTransactionList = inventoryTransactionRepository.findAll();
        assertThat(inventoryTransactionList).hasSize(databaseSizeBeforeUpdate);
        InventoryTransaction testInventoryTransaction = inventoryTransactionList.get(inventoryTransactionList.size() - 1);
        assertThat(testInventoryTransaction.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
        assertThat(testInventoryTransaction.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testInventoryTransaction.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
        assertThat(testInventoryTransaction.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testInventoryTransaction.getComments()).isEqualTo(UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void patchNonExistingInventoryTransaction() throws Exception {
        int databaseSizeBeforeUpdate = inventoryTransactionRepository.findAll().size();
        inventoryTransaction.setId(count.incrementAndGet());

        // Create the InventoryTransaction
        InventoryTransactionDTO inventoryTransactionDTO = inventoryTransactionMapper.toDto(inventoryTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventoryTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inventoryTransactionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inventoryTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventoryTransaction in the database
        List<InventoryTransaction> inventoryTransactionList = inventoryTransactionRepository.findAll();
        assertThat(inventoryTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInventoryTransaction() throws Exception {
        int databaseSizeBeforeUpdate = inventoryTransactionRepository.findAll().size();
        inventoryTransaction.setId(count.incrementAndGet());

        // Create the InventoryTransaction
        InventoryTransactionDTO inventoryTransactionDTO = inventoryTransactionMapper.toDto(inventoryTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inventoryTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventoryTransaction in the database
        List<InventoryTransaction> inventoryTransactionList = inventoryTransactionRepository.findAll();
        assertThat(inventoryTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInventoryTransaction() throws Exception {
        int databaseSizeBeforeUpdate = inventoryTransactionRepository.findAll().size();
        inventoryTransaction.setId(count.incrementAndGet());

        // Create the InventoryTransaction
        InventoryTransactionDTO inventoryTransactionDTO = inventoryTransactionMapper.toDto(inventoryTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inventoryTransactionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InventoryTransaction in the database
        List<InventoryTransaction> inventoryTransactionList = inventoryTransactionRepository.findAll();
        assertThat(inventoryTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInventoryTransaction() throws Exception {
        // Initialize the database
        inventoryTransactionRepository.saveAndFlush(inventoryTransaction);

        int databaseSizeBeforeDelete = inventoryTransactionRepository.findAll().size();

        // Delete the inventoryTransaction
        restInventoryTransactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, inventoryTransaction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InventoryTransaction> inventoryTransactionList = inventoryTransactionRepository.findAll();
        assertThat(inventoryTransactionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
