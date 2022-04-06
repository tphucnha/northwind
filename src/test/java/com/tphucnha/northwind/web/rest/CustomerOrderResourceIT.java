package com.tphucnha.northwind.web.rest;

import com.tphucnha.northwind.IntegrationTest;
import com.tphucnha.northwind.domain.Customer;
import com.tphucnha.northwind.domain.CustomerOrder;
import com.tphucnha.northwind.domain.enumeration.OrderStatus;
import com.tphucnha.northwind.repository.CustomerOrderRepository;
import com.tphucnha.northwind.repository.CustomerRepository;
import com.tphucnha.northwind.service.dto.CustomerDTO;
import com.tphucnha.northwind.service.dto.CustomerOrderDTO;
import com.tphucnha.northwind.service.mapper.CustomerOrderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static com.tphucnha.northwind.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CustomerOrderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomerOrderResourceIT {

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_SHIPPED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SHIPPED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_SHIP_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_SHIP_ADDRESS = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_SHIPPING_FEE = new BigDecimal(1);
    private static final BigDecimal UPDATED_SHIPPING_FEE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TAXES = new BigDecimal(1);
    private static final BigDecimal UPDATED_TAXES = new BigDecimal(2);

    private static final String DEFAULT_PAYMENT_METHOD = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_METHOD = "BBBBBBBBBB";

    private static final Instant DEFAULT_PAID_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAID_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final OrderStatus DEFAULT_STATUS = OrderStatus.NEW;
    private static final OrderStatus UPDATED_STATUS = OrderStatus.INVOICED;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_FIRSTNAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/customer-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerOrderMapper customerOrderMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerOrderMockMvc;

    private CustomerOrder customerOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerOrder createEntity(EntityManager em) {
        CustomerOrder customerOrder = new CustomerOrder()
            .orderDate(DEFAULT_ORDER_DATE)
            .shippedDate(DEFAULT_SHIPPED_DATE)
            .shipAddress(DEFAULT_SHIP_ADDRESS)
            .shippingFee(DEFAULT_SHIPPING_FEE)
            .taxes(DEFAULT_TAXES)
            .paymentMethod(DEFAULT_PAYMENT_METHOD)
            .paidDate(DEFAULT_PAID_DATE)
            .status(DEFAULT_STATUS)
            .notes(DEFAULT_NOTES);
        // Add required entity
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            customer = CustomerResourceIT.createEntity(em);
            em.persist(customer);
            em.flush();
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        customerOrder.setCustomer(customer);
        return customerOrder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerOrder createUpdatedEntity(EntityManager em) {
        CustomerOrder customerOrder = new CustomerOrder()
            .orderDate(UPDATED_ORDER_DATE)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .shipAddress(UPDATED_SHIP_ADDRESS)
            .shippingFee(UPDATED_SHIPPING_FEE)
            .taxes(UPDATED_TAXES)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .paidDate(UPDATED_PAID_DATE)
            .status(UPDATED_STATUS)
            .notes(UPDATED_NOTES);
        // Add required entity
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            customer = CustomerResourceIT.createUpdatedEntity(em);
            em.persist(customer);
            em.flush();
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        customerOrder.setCustomer(customer);
        return customerOrder;
    }

    @BeforeEach
    public void initTest() {
        customerOrder = createEntity(em);
    }

    @Test
    @Transactional
    void createCustomerOrder() throws Exception {
        int databaseSizeBeforeCreate = customerOrderRepository.findAll().size();
        // Create the CustomerOrder
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.toDto(customerOrder);
        restCustomerOrderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerOrderDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CustomerOrder in the database
        List<CustomerOrder> customerOrderList = customerOrderRepository.findAll();
        assertThat(customerOrderList).hasSize(databaseSizeBeforeCreate + 1);
        CustomerOrder testCustomerOrder = customerOrderList.get(customerOrderList.size() - 1);
        assertThat(testCustomerOrder.getOrderDate()).isEqualTo(DEFAULT_ORDER_DATE);
        assertThat(testCustomerOrder.getShippedDate()).isEqualTo(DEFAULT_SHIPPED_DATE);
        assertThat(testCustomerOrder.getShipAddress()).isEqualTo(DEFAULT_SHIP_ADDRESS);
        assertThat(testCustomerOrder.getShippingFee()).isEqualByComparingTo(DEFAULT_SHIPPING_FEE);
        assertThat(testCustomerOrder.getTaxes()).isEqualByComparingTo(DEFAULT_TAXES);
        assertThat(testCustomerOrder.getPaymentMethod()).isEqualTo(DEFAULT_PAYMENT_METHOD);
        assertThat(testCustomerOrder.getPaidDate()).isEqualTo(DEFAULT_PAID_DATE);
        assertThat(testCustomerOrder.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCustomerOrder.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void createOrderWithNewCustomer() throws Exception {
        int databaseSizeBeforeCreate = customerOrderRepository.findAll().size();
        // Create the CustomerOrder
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.toDto(customerOrder);
        // New customer with firstName only
        CustomerDTO customerDTO = new CustomerDTO(DEFAULT_CUSTOMER_FIRSTNAME);
        customerOrderDTO.setCustomer(customerDTO);
        restCustomerOrderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerOrderDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CustomerOrder in the database
        List<CustomerOrder> customerOrderList = customerOrderRepository.findAll();
        assertThat(customerOrderList).hasSize(databaseSizeBeforeCreate + 1);
        CustomerOrder testCustomerOrder = customerOrderList.get(customerOrderList.size() - 1);
        assertThat(testCustomerOrder.getOrderDate()).isEqualTo(DEFAULT_ORDER_DATE);
        assertThat(testCustomerOrder.getShippedDate()).isEqualTo(DEFAULT_SHIPPED_DATE);
        assertThat(testCustomerOrder.getShipAddress()).isEqualTo(DEFAULT_SHIP_ADDRESS);
        assertThat(testCustomerOrder.getShippingFee()).isEqualByComparingTo(DEFAULT_SHIPPING_FEE);
        assertThat(testCustomerOrder.getTaxes()).isEqualByComparingTo(DEFAULT_TAXES);
        assertThat(testCustomerOrder.getPaymentMethod()).isEqualTo(DEFAULT_PAYMENT_METHOD);
        assertThat(testCustomerOrder.getPaidDate()).isEqualTo(DEFAULT_PAID_DATE);
        assertThat(testCustomerOrder.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCustomerOrder.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testCustomerOrder.getCustomer().getFirstName()).isEqualTo(DEFAULT_CUSTOMER_FIRSTNAME);
    }

    @Test
    @Transactional
    void createCustomerOrderWithExistingId() throws Exception {
        // Create the CustomerOrder with an existing ID
        customerOrder.setId(1L);
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.toDto(customerOrder);

        int databaseSizeBeforeCreate = customerOrderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerOrderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerOrder in the database
        List<CustomerOrder> customerOrderList = customerOrderRepository.findAll();
        assertThat(customerOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCustomerOrders() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList
        restCustomerOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].shipAddress").value(hasItem(DEFAULT_SHIP_ADDRESS)))
            .andExpect(jsonPath("$.[*].shippingFee").value(hasItem(sameNumber(DEFAULT_SHIPPING_FEE))))
            .andExpect(jsonPath("$.[*].taxes").value(hasItem(sameNumber(DEFAULT_TAXES))))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD)))
            .andExpect(jsonPath("$.[*].paidDate").value(hasItem(DEFAULT_PAID_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @Test
    @Transactional
    void getCustomerOrder() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        // Get the customerOrder
        restCustomerOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, customerOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customerOrder.getId().intValue()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.shippedDate").value(DEFAULT_SHIPPED_DATE.toString()))
            .andExpect(jsonPath("$.shipAddress").value(DEFAULT_SHIP_ADDRESS))
            .andExpect(jsonPath("$.shippingFee").value(sameNumber(DEFAULT_SHIPPING_FEE)))
            .andExpect(jsonPath("$.taxes").value(sameNumber(DEFAULT_TAXES)))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD))
            .andExpect(jsonPath("$.paidDate").value(DEFAULT_PAID_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getNonExistingCustomerOrder() throws Exception {
        // Get the customerOrder
        restCustomerOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCustomerOrder() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        int databaseSizeBeforeUpdate = customerOrderRepository.findAll().size();

        // Update the customerOrder
        CustomerOrder updatedCustomerOrder = customerOrderRepository.findById(customerOrder.getId()).get();
        // Disconnect from session so that the updates on updatedCustomerOrder are not directly saved in db
        em.detach(updatedCustomerOrder);
        updatedCustomerOrder
            .orderDate(UPDATED_ORDER_DATE)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .shipAddress(UPDATED_SHIP_ADDRESS)
            .shippingFee(UPDATED_SHIPPING_FEE)
            .taxes(UPDATED_TAXES)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .paidDate(UPDATED_PAID_DATE)
            .status(UPDATED_STATUS)
            .notes(UPDATED_NOTES);
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.toDto(updatedCustomerOrder);

        restCustomerOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerOrderDTO))
            )
            .andExpect(status().isOk());

        // Validate the CustomerOrder in the database
        List<CustomerOrder> customerOrderList = customerOrderRepository.findAll();
        assertThat(customerOrderList).hasSize(databaseSizeBeforeUpdate);
        CustomerOrder testCustomerOrder = customerOrderList.get(customerOrderList.size() - 1);
        assertThat(testCustomerOrder.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
        assertThat(testCustomerOrder.getShippedDate()).isEqualTo(UPDATED_SHIPPED_DATE);
        assertThat(testCustomerOrder.getShipAddress()).isEqualTo(UPDATED_SHIP_ADDRESS);
        assertThat(testCustomerOrder.getShippingFee()).isEqualByComparingTo(UPDATED_SHIPPING_FEE);
        assertThat(testCustomerOrder.getTaxes()).isEqualByComparingTo(UPDATED_TAXES);
        assertThat(testCustomerOrder.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
        assertThat(testCustomerOrder.getPaidDate()).isEqualTo(UPDATED_PAID_DATE);
        assertThat(testCustomerOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCustomerOrder.getNotes()).isEqualTo(UPDATED_NOTES);
    }


    @Test
    @Transactional
    void putOrderWithNewCustomer() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        int databaseSizeBeforeUpdate = customerOrderRepository.findAll().size();
        int customerDatabaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customerOrder
        CustomerOrder updatedCustomerOrder = customerOrderRepository.findById(customerOrder.getId()).get();
        // Disconnect from session so that the updates on updatedCustomerOrder are not directly saved in db
        em.detach(updatedCustomerOrder);
        updatedCustomerOrder
            .orderDate(UPDATED_ORDER_DATE)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .shipAddress(UPDATED_SHIP_ADDRESS)
            .shippingFee(UPDATED_SHIPPING_FEE)
            .taxes(UPDATED_TAXES)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .paidDate(UPDATED_PAID_DATE)
            .status(UPDATED_STATUS)
            .notes(UPDATED_NOTES);
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.toDto(updatedCustomerOrder);
        customerOrderDTO.setCustomer(new CustomerDTO(UPDATED_CUSTOMER_FIRSTNAME));

        restCustomerOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerOrderDTO))
            )
            .andExpect(status().isOk());

        // Validate Customer
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate + 1);

        // Validate the CustomerOrder in the database
        List<CustomerOrder> customerOrderList = customerOrderRepository.findAll();
        assertThat(customerOrderList).hasSize(databaseSizeBeforeUpdate);
        CustomerOrder testCustomerOrder = customerOrderList.get(customerOrderList.size() - 1);
        assertThat(testCustomerOrder.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
        assertThat(testCustomerOrder.getShippedDate()).isEqualTo(UPDATED_SHIPPED_DATE);
        assertThat(testCustomerOrder.getShipAddress()).isEqualTo(UPDATED_SHIP_ADDRESS);
        assertThat(testCustomerOrder.getShippingFee()).isEqualByComparingTo(UPDATED_SHIPPING_FEE);
        assertThat(testCustomerOrder.getTaxes()).isEqualByComparingTo(UPDATED_TAXES);
        assertThat(testCustomerOrder.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
        assertThat(testCustomerOrder.getPaidDate()).isEqualTo(UPDATED_PAID_DATE);
        assertThat(testCustomerOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCustomerOrder.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testCustomerOrder.getCustomer().getFirstName()).isEqualTo(UPDATED_CUSTOMER_FIRSTNAME);
    }

    @Test
    @Transactional
    void putNonExistingCustomerOrder() throws Exception {
        int databaseSizeBeforeUpdate = customerOrderRepository.findAll().size();
        customerOrder.setId(count.incrementAndGet());

        // Create the CustomerOrder
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.toDto(customerOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerOrder in the database
        List<CustomerOrder> customerOrderList = customerOrderRepository.findAll();
        assertThat(customerOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomerOrder() throws Exception {
        int databaseSizeBeforeUpdate = customerOrderRepository.findAll().size();
        customerOrder.setId(count.incrementAndGet());

        // Create the CustomerOrder
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.toDto(customerOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerOrder in the database
        List<CustomerOrder> customerOrderList = customerOrderRepository.findAll();
        assertThat(customerOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomerOrder() throws Exception {
        int databaseSizeBeforeUpdate = customerOrderRepository.findAll().size();
        customerOrder.setId(count.incrementAndGet());

        // Create the CustomerOrder
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.toDto(customerOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerOrderMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerOrderDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerOrder in the database
        List<CustomerOrder> customerOrderList = customerOrderRepository.findAll();
        assertThat(customerOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerOrderWithPatch() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        int databaseSizeBeforeUpdate = customerOrderRepository.findAll().size();

        // Update the customerOrder using partial update
        CustomerOrder partialUpdatedCustomerOrder = new CustomerOrder();
        partialUpdatedCustomerOrder.setId(customerOrder.getId());

        partialUpdatedCustomerOrder
            .shipAddress(UPDATED_SHIP_ADDRESS)
            .shippingFee(UPDATED_SHIPPING_FEE)
            .paidDate(UPDATED_PAID_DATE)
            .status(UPDATED_STATUS)
            .notes(UPDATED_NOTES);

        restCustomerOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomerOrder))
            )
            .andExpect(status().isOk());

        // Validate the CustomerOrder in the database
        List<CustomerOrder> customerOrderList = customerOrderRepository.findAll();
        assertThat(customerOrderList).hasSize(databaseSizeBeforeUpdate);
        CustomerOrder testCustomerOrder = customerOrderList.get(customerOrderList.size() - 1);
        assertThat(testCustomerOrder.getOrderDate()).isEqualTo(DEFAULT_ORDER_DATE);
        assertThat(testCustomerOrder.getShippedDate()).isEqualTo(DEFAULT_SHIPPED_DATE);
        assertThat(testCustomerOrder.getShipAddress()).isEqualTo(UPDATED_SHIP_ADDRESS);
        assertThat(testCustomerOrder.getShippingFee()).isEqualByComparingTo(UPDATED_SHIPPING_FEE);
        assertThat(testCustomerOrder.getTaxes()).isEqualByComparingTo(DEFAULT_TAXES);
        assertThat(testCustomerOrder.getPaymentMethod()).isEqualTo(DEFAULT_PAYMENT_METHOD);
        assertThat(testCustomerOrder.getPaidDate()).isEqualTo(UPDATED_PAID_DATE);
        assertThat(testCustomerOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCustomerOrder.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void fullUpdateCustomerOrderWithPatch() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        int databaseSizeBeforeUpdate = customerOrderRepository.findAll().size();

        // Update the customerOrder using partial update
        CustomerOrder partialUpdatedCustomerOrder = new CustomerOrder();
        partialUpdatedCustomerOrder.setId(customerOrder.getId());

        partialUpdatedCustomerOrder
            .orderDate(UPDATED_ORDER_DATE)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .shipAddress(UPDATED_SHIP_ADDRESS)
            .shippingFee(UPDATED_SHIPPING_FEE)
            .taxes(UPDATED_TAXES)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .paidDate(UPDATED_PAID_DATE)
            .status(UPDATED_STATUS)
            .notes(UPDATED_NOTES);

        restCustomerOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomerOrder))
            )
            .andExpect(status().isOk());

        // Validate the CustomerOrder in the database
        List<CustomerOrder> customerOrderList = customerOrderRepository.findAll();
        assertThat(customerOrderList).hasSize(databaseSizeBeforeUpdate);
        CustomerOrder testCustomerOrder = customerOrderList.get(customerOrderList.size() - 1);
        assertThat(testCustomerOrder.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
        assertThat(testCustomerOrder.getShippedDate()).isEqualTo(UPDATED_SHIPPED_DATE);
        assertThat(testCustomerOrder.getShipAddress()).isEqualTo(UPDATED_SHIP_ADDRESS);
        assertThat(testCustomerOrder.getShippingFee()).isEqualByComparingTo(UPDATED_SHIPPING_FEE);
        assertThat(testCustomerOrder.getTaxes()).isEqualByComparingTo(UPDATED_TAXES);
        assertThat(testCustomerOrder.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
        assertThat(testCustomerOrder.getPaidDate()).isEqualTo(UPDATED_PAID_DATE);
        assertThat(testCustomerOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCustomerOrder.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void patchNonExistingCustomerOrder() throws Exception {
        int databaseSizeBeforeUpdate = customerOrderRepository.findAll().size();
        customerOrder.setId(count.incrementAndGet());

        // Create the CustomerOrder
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.toDto(customerOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerOrderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerOrder in the database
        List<CustomerOrder> customerOrderList = customerOrderRepository.findAll();
        assertThat(customerOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomerOrder() throws Exception {
        int databaseSizeBeforeUpdate = customerOrderRepository.findAll().size();
        customerOrder.setId(count.incrementAndGet());

        // Create the CustomerOrder
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.toDto(customerOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerOrder in the database
        List<CustomerOrder> customerOrderList = customerOrderRepository.findAll();
        assertThat(customerOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomerOrder() throws Exception {
        int databaseSizeBeforeUpdate = customerOrderRepository.findAll().size();
        customerOrder.setId(count.incrementAndGet());

        // Create the CustomerOrder
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.toDto(customerOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerOrderMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerOrderDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerOrder in the database
        List<CustomerOrder> customerOrderList = customerOrderRepository.findAll();
        assertThat(customerOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomerOrder() throws Exception {
        // Initialize the database
        customerOrderRepository.saveAndFlush(customerOrder);

        int databaseSizeBeforeDelete = customerOrderRepository.findAll().size();

        // Delete the customerOrder
        restCustomerOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, customerOrder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CustomerOrder> customerOrderList = customerOrderRepository.findAll();
        assertThat(customerOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
