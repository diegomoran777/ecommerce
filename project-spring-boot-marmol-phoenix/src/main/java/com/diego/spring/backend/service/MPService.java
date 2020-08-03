package com.diego.spring.backend.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diego.spring.backend.model.Product;
import com.diego.spring.backend.model.Purchase;
import com.diego.spring.backend.model.ResponseIPN;
import com.diego.spring.backend.model.Sale;
import com.diego.spring.backend.model.UserProgram;
import com.mercadopago.resources.Preference;
import com.mercadopago.resources.Preference.AutoReturn;
import com.mercadopago.resources.datastructures.preference.BackUrls;
import com.mercadopago.resources.datastructures.preference.Item;
import com.mercadopago.resources.datastructures.preference.Payer;

@Service
public class MPService {
	
	@Autowired
	private GeneralService service;
	
	private final String APROVED = "approved";
	private final String SUCCESS = "purchase";
	private final String PENDING = "cafrrito";
	private final String FAILURE = "carrito";
	
	/**
	 * Set the urls back for the preference
	 * @param preference
	 */
	public void setBackUrls(Preference preference) {
		BackUrls backUrls = new BackUrls();
		preference.setBackUrls(
				backUrls.setSuccess(SUCCESS)
						.setPending(PENDING)
						.setFailure(FAILURE));
		preference.setAutoReturn(AutoReturn.approved);
	}
	
	/**
	 * Add items to the preference
	 * @param preference
	 * @param userName
	 */
	public void addItems(Preference preference, String userName) {
		Set<Product> productsFromCart = service.getProductsByUserCart(userName);
		
		for (Product p : productsFromCart) {
			Item item = new Item();
			item.setCategoryId(String.valueOf(p.getCategoryId()))
				.setId(String.valueOf(p.getId()))
				.setDescription(p.getDescription())
				.setPictureUrl(p.getPhoto())
				.setQuantity(p.getAmount())
				.setTitle(p.getName())
				.setUnitPrice(p.getPrice())
				.setCurrencyId("ARS");
			preference.appendItem(item);
		}
		
	    //Shipments cost
	    /*Shipments ship = new Shipments();
	    ship.setCost((float) 1000);
	    preference.setShipments(ship);*/
	}
	
	/**
	 * Set payer to the purchase
	 * @param preference
	 * @param userName
	 */
	public void setPayer(Preference preference, String userName) {
		UserProgram user = service.findUserByUserName(userName);
		Payer payer = new Payer();
		payer.setName(user.getUserName());
		payer.setEmail(user.getEmail());
		preference.setPayer(payer);
	}
	
	/**
	 * Verify if the status from the purchase is approved
	 * @param status
	 * @return boolean
	 */
	public boolean isAproved(String status) {
		return status.equals(this.APROVED);
	}
	
	/**
	 * Fill the purchase with the details
	 * @param responseIpn
	 */
	public void fillPurchase(ResponseIPN responseIpn) {
		String details = "";
		
		Purchase purchase = new Purchase();
		purchase.setDate_approved(responseIpn.getDate_approved());
		purchase.setInstallments(responseIpn.getInstallments());
		purchase.setStatus(responseIpn.getStatus());
		purchase.setPayment_method_id(responseIpn.getPayment_method_id());
		purchase.setPayment_type_id(responseIpn.getPayment_type_id());
        purchase.setPurchase_detail(fillDetails(responseIpn, details));
		purchase.setTotal(responseIpn.getTransaction_amount());
		UserProgram user =  service.findUserByEmail(responseIpn.getPayer().getEmail());
		user.addPurchase(purchase);
	}
	
	/**
	 * Fill the details
	 * @param responseIpn
	 * @param details
	 */
	public String fillDetails(ResponseIPN responseIpn, String details) {
		UserProgram user =  service.findUserByEmail(responseIpn.getPayer().getEmail());
		Set<Product> products = service.getProductsByUserCart(user.getUserName());
		
		for (Product p : products) {
			details = "PRODUCTO: ".concat(p.getName()).concat("\n")
					.concat("NRO DE PRODUCTO: ").concat(p.getNumberExtern()).concat("\n")
					.concat("CANTIDAD: ").concat(String.valueOf(p.getAmount())).concat("\n")
					.concat("PRECIO: ").concat(String.valueOf(p.getPrice())).concat("\n");
		}
		return details;
	}
	
	/**
	 * Fill the content to the email for the seller
	 * @param responseIpn
	 * @return content
	 */
	public String contentEmail(ResponseIPN responseIpn) {
		String detail = "";
		String content = "Le informamos la confirmacion de su compra en MARMOL FENIX \n"
				.concat("DETALLE DE LA COMPRA:\n")
				.concat(fillDetails(responseIpn, detail))
				.concat("\n").concat("\n")
				.concat("Proximamente nos comunicaremos con usted")
				.concat("GRACIAS POR SU COMPRA!!! \n")
				.concat("MARMOL FENIX \n")
				.concat("marmolfenix@hotmail.com\n")
				.concat("marmolfenix.com");
		return content;
	}
	
	/**
	 * Fill the sale with the details of the purchase
	 * @param responseIpn
	 */
	public void fillSale(ResponseIPN responseIpn) {
		UserProgram user =  service.findUserByEmail(responseIpn.getPayer().getEmail());
		String details = "";
		Sale sale = new Sale();
		sale.setDate_approved(responseIpn.getDate_approved());
		sale.setDelivered(false);
		sale.setInstallments(responseIpn.getInstallments());
		sale.setPayment_method_id(responseIpn.getPayment_method_id());
		sale.setPayment_type_id(responseIpn.getPayment_type_id());
		sale.setSale_detail(fillDetails(responseIpn, details));
		sale.setStatus(responseIpn.getStatus());
		sale.setTotal(responseIpn.getTransaction_amount());
		sale.setUserMail(responseIpn.getPayer().getEmail());
		sale.setUserName(user.getUserName());
		service.saveUpdateSale(sale);
	}
	
	/**
	 * Delete cart list from user when the purchase was done
	 * @param responseIpn
	 */
	public void deleteCartList(ResponseIPN responseIpn) {
		UserProgram user =  service.findUserByEmail(responseIpn.getPayer().getEmail());
		service.deleteAllProductsCartByUserName(user.getUserName());
	}
	

}
