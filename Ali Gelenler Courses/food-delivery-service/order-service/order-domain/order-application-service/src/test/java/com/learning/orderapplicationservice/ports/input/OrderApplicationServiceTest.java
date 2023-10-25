package com.learning.orderapplicationservice.ports.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.learning.commondomain.valueobjects.CustomerId;
import com.learning.commondomain.valueobjects.Money;
import com.learning.commondomain.valueobjects.OrderId;
import com.learning.commondomain.valueobjects.OrderStatus;
import com.learning.commondomain.valueobjects.ProductId;
import com.learning.commondomain.valueobjects.RestrauntId;
import com.learning.orderapplicationservice.commands.CreateOrderCommand;
import com.learning.orderapplicationservice.commands.OrderAddress;
import com.learning.orderapplicationservice.commands.OrderItemCommand;
import com.learning.orderapplicationservice.mapper.OrderMapper;
import com.learning.orderapplicationservice.ports.output.CustomerRepository;
import com.learning.orderapplicationservice.ports.output.OrderRepository;
import com.learning.orderapplicationservice.ports.output.RestrauntRepository;
import com.learning.orderapplicationservice.response.CreateOrderResponse;
import com.learning.orderservice.core.entities.Customer;
import com.learning.orderservice.core.entities.Order;
import com.learning.orderservice.core.entities.Product;
import com.learning.orderservice.core.entities.Restraunt;
import com.learning.orderservice.core.exceptions.OrderDomainException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = OrderTestConfiguration.class)
public class OrderApplicationServiceTest {

	@Autowired
	private OrderApplicationService orderApplicationService;

	@Autowired
	private OrderMapper orderDataMapper;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private RestrauntRepository restaurantRepository;

	private CreateOrderCommand createOrderCommand;
	private CreateOrderCommand createOrderCommandWrongPrice;
	private CreateOrderCommand createOrderCommandWrongProductPrice;
	private final UUID CUSTOMER_ID = UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb41");
	private final UUID RESTAURANT_ID = UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb45");
	private final UUID PRODUCT_ID = UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb48");
	private final UUID PRODUCT_ID_2 = UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb49");
	private final UUID ORDER_ID = UUID.fromString("15a497c1-0f4b-4eff-b9f4-c402c8c07afb");
	private final UUID SAGA_ID = UUID.fromString("15a497c1-0f4b-4eff-b9f4-c402c8c07afa");
	private final BigDecimal PRICE = new BigDecimal("200.00");

	@BeforeAll
	public void init() {
		createOrderCommand = CreateOrderCommand.builder().customerId(CUSTOMER_ID).restrauntId(RESTAURANT_ID)
				.orderAddress(OrderAddress.builder().street("street_1").postalCode("1000AB").city("Paris").build())
				.price(PRICE)
				.orderItems(List.of(
						OrderItemCommand.builder().productId(PRODUCT_ID).quantity(1).unitPrice(new BigDecimal("50.00"))
								.totalPrice(new BigDecimal("50.00")).build(),
						OrderItemCommand.builder().productId(PRODUCT_ID_2).quantity(3).unitPrice(new BigDecimal("50.00"))
								.totalPrice(new BigDecimal("150.00")).build()))
				.build();

		createOrderCommandWrongPrice = CreateOrderCommand.builder().customerId(CUSTOMER_ID).restrauntId(RESTAURANT_ID)
				.orderAddress(OrderAddress.builder().street("street_1").postalCode("1000AB").city("Paris").build())
				.price(new BigDecimal("250.00"))
				.orderItems(List.of(
						OrderItemCommand.builder().productId(PRODUCT_ID).quantity(1).unitPrice(new BigDecimal("50.00"))
								.totalPrice(new BigDecimal("50.00")).build(),
						OrderItemCommand.builder().productId(PRODUCT_ID_2).quantity(3).unitPrice(new BigDecimal("50.00"))
								.totalPrice(new BigDecimal("150.00")).build()))
				.build();

		createOrderCommandWrongProductPrice = CreateOrderCommand.builder().customerId(CUSTOMER_ID)
				.restrauntId(RESTAURANT_ID)
				.orderAddress(OrderAddress.builder().street("street_1").postalCode("1000AB").city("Paris").build())
				.price(new BigDecimal("210.00"))
				.orderItems(List.of(
						OrderItemCommand.builder().productId(PRODUCT_ID).quantity(1).unitPrice(new BigDecimal("60.00"))
								.totalPrice(new BigDecimal("60.00")).build(),
						OrderItemCommand.builder().productId(PRODUCT_ID_2).quantity(3).unitPrice(new BigDecimal("50.00"))
								.totalPrice(new BigDecimal("150.00")).build()))
				.build();

		Customer customer = new Customer(new CustomerId(CUSTOMER_ID), null);

		Map<ProductId, Product> productMap = Map.of(new ProductId(PRODUCT_ID),
				new Product(new ProductId(PRODUCT_ID), "product-1", new Money(new BigDecimal("50.00"))),
				new ProductId(PRODUCT_ID_2),
				new Product(new ProductId(PRODUCT_ID), "product-2", new Money(new BigDecimal("50.00"))));

		Restraunt restaurantResponse = new Restraunt(new RestrauntId(createOrderCommand.getRestrauntId()), productMap,
				true);

		Order order = orderDataMapper.createOrderFromOrderCommand(createOrderCommand);
		order.setId(new OrderId(ORDER_ID));

		when(customerRepository.findCustomer(CUSTOMER_ID)).thenReturn(Optional.of(customer));
		when(restaurantRepository
				.findRestaurantInformation(orderDataMapper.getRestrauntFromCreateOrderCommand(createOrderCommand)))
						.thenReturn(Optional.of(restaurantResponse));
		when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);
	}

	@Test
	public void testCreateOrder() {
		CreateOrderResponse createOrderResponse = orderApplicationService.createOrder(createOrderCommand);
		assertEquals(OrderStatus.PENDING, createOrderResponse.getOrderStatus());
		assertEquals("order created succesfully", createOrderResponse.getMessage());
		assertNotNull(createOrderResponse.getTrackingId());
	}

	@Test
	public void testCreateOrderWithWrongTotalPrice() {
		OrderDomainException orderDomainException = assertThrows(OrderDomainException.class,
				() -> orderApplicationService.createOrder(createOrderCommandWrongPrice));
		assertEquals("Total price: 250.00 is not equal to Order items total: 200.00!",
				orderDomainException.getMessage());
	}

	@Test
	public void testCreateOrderWithWrongProductPrice() {
		OrderDomainException orderDomainException = assertThrows(OrderDomainException.class,
				() -> orderApplicationService.createOrder(createOrderCommandWrongProductPrice));
		assertEquals("Order item price: 60.00 is not valid for product " + PRODUCT_ID,
				orderDomainException.getMessage());
	}

	@Test
	public void testCreateOrderWithPassiveRestaurant() {
		Map<ProductId, Product> productMap = Map.of(new ProductId(PRODUCT_ID),
				new Product(new ProductId(PRODUCT_ID), "product-1", new Money(new BigDecimal("50.00"))),
				new ProductId(PRODUCT_ID_2),
				new Product(new ProductId(PRODUCT_ID), "product-2", new Money(new BigDecimal("50.00"))));

		Restraunt restaurantResponse = new Restraunt(new RestrauntId(createOrderCommand.getRestrauntId()), productMap,
				false);
		
		when(restaurantRepository
				.findRestaurantInformation(orderDataMapper.getRestrauntFromCreateOrderCommand(createOrderCommand)))
						.thenReturn(Optional.of(restaurantResponse));
		OrderDomainException orderDomainException = assertThrows(OrderDomainException.class,
				() -> orderApplicationService.createOrder(createOrderCommand));
		assertEquals("Restaurant with id " + RESTAURANT_ID + " is currently not active!",
				orderDomainException.getMessage());
	}
}
