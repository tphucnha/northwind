package com.tphucnha.northwind.web.rest;

import static com.tphucnha.northwind.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tphucnha.northwind.IntegrationTest;
import com.tphucnha.northwind.domain.Product;
import com.tphucnha.northwind.repository.ProductRepository;
import com.tphucnha.northwind.service.ProductService;
import com.tphucnha.northwind.service.dto.ProductDTO;
import com.tphucnha.northwind.service.mapper.ProductMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProductResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_STANDARD_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_STANDARD_COST = new BigDecimal(2);

    private static final BigDecimal DEFAULT_LIST_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LIST_PRICE = new BigDecimal(2);

    private static final Integer DEFAULT_REORDER_LEVEL = 1;
    private static final Integer UPDATED_REORDER_LEVEL = 2;

    private static final Integer DEFAULT_TARGET_LEVEL = 1;
    private static final Integer UPDATED_TARGET_LEVEL = 2;

    private static final Integer DEFAULT_QUANTITY_PER_UNIT = 1;
    private static final Integer UPDATED_QUANTITY_PER_UNIT = 2;

    private static final Boolean DEFAULT_DIS_CONTINUED = false;
    private static final Boolean UPDATED_DIS_CONTINUED = true;

    private static final Integer DEFAULT_MINIMUM_REORDER_QUANTITY = 1;
    private static final Integer UPDATED_MINIMUM_REORDER_QUANTITY = 2;

    private static final String ENTITY_API_URL = "/api/products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductRepository productRepository;

    @Mock
    private ProductRepository productRepositoryMock;

    @Autowired
    private ProductMapper productMapper;

    @Mock
    private ProductService productServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductMockMvc;

    private Product product;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createEntity(EntityManager em) {
        Product product = new Product()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .standardCost(DEFAULT_STANDARD_COST)
            .listPrice(DEFAULT_LIST_PRICE)
            .reorderLevel(DEFAULT_REORDER_LEVEL)
            .targetLevel(DEFAULT_TARGET_LEVEL)
            .quantityPerUnit(DEFAULT_QUANTITY_PER_UNIT)
            .disContinued(DEFAULT_DIS_CONTINUED)
            .minimumReorderQuantity(DEFAULT_MINIMUM_REORDER_QUANTITY);
        return product;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createUpdatedEntity(EntityManager em) {
        Product product = new Product()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .standardCost(UPDATED_STANDARD_COST)
            .listPrice(UPDATED_LIST_PRICE)
            .reorderLevel(UPDATED_REORDER_LEVEL)
            .targetLevel(UPDATED_TARGET_LEVEL)
            .quantityPerUnit(UPDATED_QUANTITY_PER_UNIT)
            .disContinued(UPDATED_DIS_CONTINUED)
            .minimumReorderQuantity(UPDATED_MINIMUM_REORDER_QUANTITY);
        return product;
    }

    @BeforeEach
    public void initTest() {
        product = createEntity(em);
    }

    @Test
    @Transactional
    void createProduct() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();
        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);
        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isCreated());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate + 1);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProduct.getStandardCost()).isEqualByComparingTo(DEFAULT_STANDARD_COST);
        assertThat(testProduct.getListPrice()).isEqualByComparingTo(DEFAULT_LIST_PRICE);
        assertThat(testProduct.getReorderLevel()).isEqualTo(DEFAULT_REORDER_LEVEL);
        assertThat(testProduct.getTargetLevel()).isEqualTo(DEFAULT_TARGET_LEVEL);
        assertThat(testProduct.getQuantityPerUnit()).isEqualTo(DEFAULT_QUANTITY_PER_UNIT);
        assertThat(testProduct.getDisContinued()).isEqualTo(DEFAULT_DIS_CONTINUED);
        assertThat(testProduct.getMinimumReorderQuantity()).isEqualTo(DEFAULT_MINIMUM_REORDER_QUANTITY);
    }

    @Test
    @Transactional
    void createProductWithExistingId() throws Exception {
        // Create the Product with an existing ID
        product.setId(1L);
        ProductDTO productDTO = productMapper.toDto(product);

        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProducts() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].standardCost").value(hasItem(sameNumber(DEFAULT_STANDARD_COST))))
            .andExpect(jsonPath("$.[*].listPrice").value(hasItem(sameNumber(DEFAULT_LIST_PRICE))))
            .andExpect(jsonPath("$.[*].reorderLevel").value(hasItem(DEFAULT_REORDER_LEVEL)))
            .andExpect(jsonPath("$.[*].targetLevel").value(hasItem(DEFAULT_TARGET_LEVEL)))
            .andExpect(jsonPath("$.[*].quantityPerUnit").value(hasItem(DEFAULT_QUANTITY_PER_UNIT)))
            .andExpect(jsonPath("$.[*].disContinued").value(hasItem(DEFAULT_DIS_CONTINUED.booleanValue())))
            .andExpect(jsonPath("$.[*].minimumReorderQuantity").value(hasItem(DEFAULT_MINIMUM_REORDER_QUANTITY)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductsWithEagerRelationshipsIsEnabled() throws Exception {
        when(productServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc
            .perform(get(ENTITY_API_URL_ID, product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(product.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.standardCost").value(sameNumber(DEFAULT_STANDARD_COST)))
            .andExpect(jsonPath("$.listPrice").value(sameNumber(DEFAULT_LIST_PRICE)))
            .andExpect(jsonPath("$.reorderLevel").value(DEFAULT_REORDER_LEVEL))
            .andExpect(jsonPath("$.targetLevel").value(DEFAULT_TARGET_LEVEL))
            .andExpect(jsonPath("$.quantityPerUnit").value(DEFAULT_QUANTITY_PER_UNIT))
            .andExpect(jsonPath("$.disContinued").value(DEFAULT_DIS_CONTINUED.booleanValue()))
            .andExpect(jsonPath("$.minimumReorderQuantity").value(DEFAULT_MINIMUM_REORDER_QUANTITY));
    }

    @Test
    @Transactional
    void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).get();
        // Disconnect from session so that the updates on updatedProduct are not directly saved in db
        em.detach(updatedProduct);
        updatedProduct
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .standardCost(UPDATED_STANDARD_COST)
            .listPrice(UPDATED_LIST_PRICE)
            .reorderLevel(UPDATED_REORDER_LEVEL)
            .targetLevel(UPDATED_TARGET_LEVEL)
            .quantityPerUnit(UPDATED_QUANTITY_PER_UNIT)
            .disContinued(UPDATED_DIS_CONTINUED)
            .minimumReorderQuantity(UPDATED_MINIMUM_REORDER_QUANTITY);
        ProductDTO productDTO = productMapper.toDto(updatedProduct);

        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProduct.getStandardCost()).isEqualByComparingTo(UPDATED_STANDARD_COST);
        assertThat(testProduct.getListPrice()).isEqualByComparingTo(UPDATED_LIST_PRICE);
        assertThat(testProduct.getReorderLevel()).isEqualTo(UPDATED_REORDER_LEVEL);
        assertThat(testProduct.getTargetLevel()).isEqualTo(UPDATED_TARGET_LEVEL);
        assertThat(testProduct.getQuantityPerUnit()).isEqualTo(UPDATED_QUANTITY_PER_UNIT);
        assertThat(testProduct.getDisContinued()).isEqualTo(UPDATED_DIS_CONTINUED);
        assertThat(testProduct.getMinimumReorderQuantity()).isEqualTo(UPDATED_MINIMUM_REORDER_QUANTITY);
    }

    @Test
    @Transactional
    void putNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductWithPatch() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct.listPrice(UPDATED_LIST_PRICE).reorderLevel(UPDATED_REORDER_LEVEL);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProduct.getStandardCost()).isEqualByComparingTo(DEFAULT_STANDARD_COST);
        assertThat(testProduct.getListPrice()).isEqualByComparingTo(UPDATED_LIST_PRICE);
        assertThat(testProduct.getReorderLevel()).isEqualTo(UPDATED_REORDER_LEVEL);
        assertThat(testProduct.getTargetLevel()).isEqualTo(DEFAULT_TARGET_LEVEL);
        assertThat(testProduct.getQuantityPerUnit()).isEqualTo(DEFAULT_QUANTITY_PER_UNIT);
        assertThat(testProduct.getDisContinued()).isEqualTo(DEFAULT_DIS_CONTINUED);
        assertThat(testProduct.getMinimumReorderQuantity()).isEqualTo(DEFAULT_MINIMUM_REORDER_QUANTITY);
    }

    @Test
    @Transactional
    void fullUpdateProductWithPatch() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .standardCost(UPDATED_STANDARD_COST)
            .listPrice(UPDATED_LIST_PRICE)
            .reorderLevel(UPDATED_REORDER_LEVEL)
            .targetLevel(UPDATED_TARGET_LEVEL)
            .quantityPerUnit(UPDATED_QUANTITY_PER_UNIT)
            .disContinued(UPDATED_DIS_CONTINUED)
            .minimumReorderQuantity(UPDATED_MINIMUM_REORDER_QUANTITY);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProduct.getStandardCost()).isEqualByComparingTo(UPDATED_STANDARD_COST);
        assertThat(testProduct.getListPrice()).isEqualByComparingTo(UPDATED_LIST_PRICE);
        assertThat(testProduct.getReorderLevel()).isEqualTo(UPDATED_REORDER_LEVEL);
        assertThat(testProduct.getTargetLevel()).isEqualTo(UPDATED_TARGET_LEVEL);
        assertThat(testProduct.getQuantityPerUnit()).isEqualTo(UPDATED_QUANTITY_PER_UNIT);
        assertThat(testProduct.getDisContinued()).isEqualTo(UPDATED_DIS_CONTINUED);
        assertThat(testProduct.getMinimumReorderQuantity()).isEqualTo(UPDATED_MINIMUM_REORDER_QUANTITY);
    }

    @Test
    @Transactional
    void patchNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeDelete = productRepository.findAll().size();

        // Delete the product
        restProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, product.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
