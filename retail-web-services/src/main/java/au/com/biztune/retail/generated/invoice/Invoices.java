
package au.com.biztune.retail.generated.invoice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Invoice" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Header">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="invoiceNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="invoiceDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="totalAmountGross" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *                             &lt;element name="totalAmountTax" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *                             &lt;element name="totalAmountNet" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *                             &lt;element name="totalAmountPaid" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *                             &lt;element name="valueChange" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *                             &lt;element name="valueRounding" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Boq">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="orderNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Project">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="projectNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="projectName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="referenceNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Client">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="clientName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="contactName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="contactNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="deliveryAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Products">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Product" maxOccurs="unbounded" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="sku" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="barcode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="ProductName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="supplier" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="unit" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="sellPrice" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *                                       &lt;element name="qty" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *                                       &lt;element name="grossAmount" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *                                       &lt;element name="taxName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="taxAmount" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *                                       &lt;element name="netAmount" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Payments">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Payment" maxOccurs="unbounded" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="paymentMethod" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="amount" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "invoice"
})
@XmlRootElement(name = "Invoices")
public class Invoices {

    @XmlElement(name = "Invoice")
    protected List<Invoices.Invoice> invoice;

    /**
     * Gets the value of the invoice property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the invoice property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInvoice().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Invoices.Invoice }
     * 
     * 
     */
    public List<Invoices.Invoice> getInvoice() {
        if (invoice == null) {
            invoice = new ArrayList<Invoices.Invoice>();
        }
        return this.invoice;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Header">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="invoiceNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="invoiceDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="totalAmountGross" type="{http://www.w3.org/2001/XMLSchema}float"/>
     *                   &lt;element name="totalAmountTax" type="{http://www.w3.org/2001/XMLSchema}float"/>
     *                   &lt;element name="totalAmountNet" type="{http://www.w3.org/2001/XMLSchema}float"/>
     *                   &lt;element name="totalAmountPaid" type="{http://www.w3.org/2001/XMLSchema}float"/>
     *                   &lt;element name="valueChange" type="{http://www.w3.org/2001/XMLSchema}float"/>
     *                   &lt;element name="valueRounding" type="{http://www.w3.org/2001/XMLSchema}float"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="Boq">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="orderNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="Project">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="projectNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="projectName" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="referenceNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="Client">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="clientName" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="contactName" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="contactNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="deliveryAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="Products">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="Product" maxOccurs="unbounded" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="sku" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="barcode" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="ProductName" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="supplier" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="unit" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="sellPrice" type="{http://www.w3.org/2001/XMLSchema}float"/>
     *                             &lt;element name="qty" type="{http://www.w3.org/2001/XMLSchema}float"/>
     *                             &lt;element name="grossAmount" type="{http://www.w3.org/2001/XMLSchema}float"/>
     *                             &lt;element name="taxName" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="taxAmount" type="{http://www.w3.org/2001/XMLSchema}float"/>
     *                             &lt;element name="netAmount" type="{http://www.w3.org/2001/XMLSchema}float"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="Payments">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="Payment" maxOccurs="unbounded" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="paymentMethod" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="amount" type="{http://www.w3.org/2001/XMLSchema}float"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "header",
        "boq",
        "project",
        "client",
        "products",
        "payments"
    })
    public static class Invoice {

        @XmlElement(name = "Header", required = true)
        protected Invoices.Invoice.Header header;
        @XmlElement(name = "Boq", required = true)
        protected Invoices.Invoice.Boq boq;
        @XmlElement(name = "Project", required = true)
        protected Invoices.Invoice.Project project;
        @XmlElement(name = "Client", required = true)
        protected Invoices.Invoice.Client client;
        @XmlElement(name = "Products", required = true)
        protected Invoices.Invoice.Products products;
        @XmlElement(name = "Payments", required = true)
        protected Invoices.Invoice.Payments payments;

        /**
         * Gets the value of the header property.
         * 
         * @return
         *     possible object is
         *     {@link Invoices.Invoice.Header }
         *     
         */
        public Invoices.Invoice.Header getHeader() {
            return header;
        }

        /**
         * Sets the value of the header property.
         * 
         * @param value
         *     allowed object is
         *     {@link Invoices.Invoice.Header }
         *     
         */
        public void setHeader(Invoices.Invoice.Header value) {
            this.header = value;
        }

        /**
         * Gets the value of the boq property.
         * 
         * @return
         *     possible object is
         *     {@link Invoices.Invoice.Boq }
         *     
         */
        public Invoices.Invoice.Boq getBoq() {
            return boq;
        }

        /**
         * Sets the value of the boq property.
         * 
         * @param value
         *     allowed object is
         *     {@link Invoices.Invoice.Boq }
         *     
         */
        public void setBoq(Invoices.Invoice.Boq value) {
            this.boq = value;
        }

        /**
         * Gets the value of the project property.
         * 
         * @return
         *     possible object is
         *     {@link Invoices.Invoice.Project }
         *     
         */
        public Invoices.Invoice.Project getProject() {
            return project;
        }

        /**
         * Sets the value of the project property.
         * 
         * @param value
         *     allowed object is
         *     {@link Invoices.Invoice.Project }
         *     
         */
        public void setProject(Invoices.Invoice.Project value) {
            this.project = value;
        }

        /**
         * Gets the value of the client property.
         * 
         * @return
         *     possible object is
         *     {@link Invoices.Invoice.Client }
         *     
         */
        public Invoices.Invoice.Client getClient() {
            return client;
        }

        /**
         * Sets the value of the client property.
         * 
         * @param value
         *     allowed object is
         *     {@link Invoices.Invoice.Client }
         *     
         */
        public void setClient(Invoices.Invoice.Client value) {
            this.client = value;
        }

        /**
         * Gets the value of the products property.
         * 
         * @return
         *     possible object is
         *     {@link Invoices.Invoice.Products }
         *     
         */
        public Invoices.Invoice.Products getProducts() {
            return products;
        }

        /**
         * Sets the value of the products property.
         * 
         * @param value
         *     allowed object is
         *     {@link Invoices.Invoice.Products }
         *     
         */
        public void setProducts(Invoices.Invoice.Products value) {
            this.products = value;
        }

        /**
         * Gets the value of the payments property.
         * 
         * @return
         *     possible object is
         *     {@link Invoices.Invoice.Payments }
         *     
         */
        public Invoices.Invoice.Payments getPayments() {
            return payments;
        }

        /**
         * Sets the value of the payments property.
         * 
         * @param value
         *     allowed object is
         *     {@link Invoices.Invoice.Payments }
         *     
         */
        public void setPayments(Invoices.Invoice.Payments value) {
            this.payments = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="orderNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "name",
            "orderNumber"
        })
        public static class Boq {

            @XmlElement(required = true)
            protected String name;
            @XmlElement(required = true)
            protected String orderNumber;

            /**
             * Gets the value of the name property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getName() {
                return name;
            }

            /**
             * Sets the value of the name property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setName(String value) {
                this.name = value;
            }

            /**
             * Gets the value of the orderNumber property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getOrderNumber() {
                return orderNumber;
            }

            /**
             * Sets the value of the orderNumber property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setOrderNumber(String value) {
                this.orderNumber = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="clientName" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="contactName" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="contactNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="deliveryAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "clientName",
            "contactName",
            "contactNo",
            "deliveryAddress"
        })
        public static class Client {

            @XmlElement(required = true)
            protected String clientName;
            @XmlElement(required = true)
            protected String contactName;
            @XmlElement(required = true)
            protected String contactNo;
            @XmlElement(required = true)
            protected String deliveryAddress;

            /**
             * Gets the value of the clientName property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getClientName() {
                return clientName;
            }

            /**
             * Sets the value of the clientName property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setClientName(String value) {
                this.clientName = value;
            }

            /**
             * Gets the value of the contactName property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getContactName() {
                return contactName;
            }

            /**
             * Sets the value of the contactName property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setContactName(String value) {
                this.contactName = value;
            }

            /**
             * Gets the value of the contactNo property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getContactNo() {
                return contactNo;
            }

            /**
             * Sets the value of the contactNo property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setContactNo(String value) {
                this.contactNo = value;
            }

            /**
             * Gets the value of the deliveryAddress property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDeliveryAddress() {
                return deliveryAddress;
            }

            /**
             * Sets the value of the deliveryAddress property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setDeliveryAddress(String value) {
                this.deliveryAddress = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="invoiceNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="invoiceDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="totalAmountGross" type="{http://www.w3.org/2001/XMLSchema}float"/>
         *         &lt;element name="totalAmountTax" type="{http://www.w3.org/2001/XMLSchema}float"/>
         *         &lt;element name="totalAmountNet" type="{http://www.w3.org/2001/XMLSchema}float"/>
         *         &lt;element name="totalAmountPaid" type="{http://www.w3.org/2001/XMLSchema}float"/>
         *         &lt;element name="valueChange" type="{http://www.w3.org/2001/XMLSchema}float"/>
         *         &lt;element name="valueRounding" type="{http://www.w3.org/2001/XMLSchema}float"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "invoiceNo",
            "invoiceDate",
            "totalAmountGross",
            "totalAmountTax",
            "totalAmountNet",
            "totalAmountPaid",
            "valueChange",
            "valueRounding"
        })
        public static class Header {

            @XmlElement(required = true)
            protected String invoiceNo;
            @XmlElement(required = true)
            protected String invoiceDate;
            protected float totalAmountGross;
            protected float totalAmountTax;
            protected float totalAmountNet;
            protected float totalAmountPaid;
            protected float valueChange;
            protected float valueRounding;

            /**
             * Gets the value of the invoiceNo property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getInvoiceNo() {
                return invoiceNo;
            }

            /**
             * Sets the value of the invoiceNo property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setInvoiceNo(String value) {
                this.invoiceNo = value;
            }

            /**
             * Gets the value of the invoiceDate property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getInvoiceDate() {
                return invoiceDate;
            }

            /**
             * Sets the value of the invoiceDate property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setInvoiceDate(String value) {
                this.invoiceDate = value;
            }

            /**
             * Gets the value of the totalAmountGross property.
             * 
             */
            public float getTotalAmountGross() {
                return totalAmountGross;
            }

            /**
             * Sets the value of the totalAmountGross property.
             * 
             */
            public void setTotalAmountGross(float value) {
                this.totalAmountGross = value;
            }

            /**
             * Gets the value of the totalAmountTax property.
             * 
             */
            public float getTotalAmountTax() {
                return totalAmountTax;
            }

            /**
             * Sets the value of the totalAmountTax property.
             * 
             */
            public void setTotalAmountTax(float value) {
                this.totalAmountTax = value;
            }

            /**
             * Gets the value of the totalAmountNet property.
             * 
             */
            public float getTotalAmountNet() {
                return totalAmountNet;
            }

            /**
             * Sets the value of the totalAmountNet property.
             * 
             */
            public void setTotalAmountNet(float value) {
                this.totalAmountNet = value;
            }

            /**
             * Gets the value of the totalAmountPaid property.
             * 
             */
            public float getTotalAmountPaid() {
                return totalAmountPaid;
            }

            /**
             * Sets the value of the totalAmountPaid property.
             * 
             */
            public void setTotalAmountPaid(float value) {
                this.totalAmountPaid = value;
            }

            /**
             * Gets the value of the valueChange property.
             * 
             */
            public float getValueChange() {
                return valueChange;
            }

            /**
             * Sets the value of the valueChange property.
             * 
             */
            public void setValueChange(float value) {
                this.valueChange = value;
            }

            /**
             * Gets the value of the valueRounding property.
             * 
             */
            public float getValueRounding() {
                return valueRounding;
            }

            /**
             * Sets the value of the valueRounding property.
             * 
             */
            public void setValueRounding(float value) {
                this.valueRounding = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="Payment" maxOccurs="unbounded" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="paymentMethod" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="amount" type="{http://www.w3.org/2001/XMLSchema}float"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "payment"
        })
        public static class Payments {

            @XmlElement(name = "Payment")
            protected List<Invoices.Invoice.Payments.Payment> payment;

            /**
             * Gets the value of the payment property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the payment property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getPayment().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Invoices.Invoice.Payments.Payment }
             * 
             * 
             */
            public List<Invoices.Invoice.Payments.Payment> getPayment() {
                if (payment == null) {
                    payment = new ArrayList<Invoices.Invoice.Payments.Payment>();
                }
                return this.payment;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="paymentMethod" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="amount" type="{http://www.w3.org/2001/XMLSchema}float"/>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "paymentMethod",
                "amount"
            })
            public static class Payment {

                @XmlElement(required = true)
                protected String paymentMethod;
                protected float amount;

                /**
                 * Gets the value of the paymentMethod property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getPaymentMethod() {
                    return paymentMethod;
                }

                /**
                 * Sets the value of the paymentMethod property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setPaymentMethod(String value) {
                    this.paymentMethod = value;
                }

                /**
                 * Gets the value of the amount property.
                 * 
                 */
                public float getAmount() {
                    return amount;
                }

                /**
                 * Sets the value of the amount property.
                 * 
                 */
                public void setAmount(float value) {
                    this.amount = value;
                }

            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="Product" maxOccurs="unbounded" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="sku" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="barcode" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="ProductName" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="supplier" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="unit" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="sellPrice" type="{http://www.w3.org/2001/XMLSchema}float"/>
         *                   &lt;element name="qty" type="{http://www.w3.org/2001/XMLSchema}float"/>
         *                   &lt;element name="grossAmount" type="{http://www.w3.org/2001/XMLSchema}float"/>
         *                   &lt;element name="taxName" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="taxAmount" type="{http://www.w3.org/2001/XMLSchema}float"/>
         *                   &lt;element name="netAmount" type="{http://www.w3.org/2001/XMLSchema}float"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "product"
        })
        public static class Products {

            @XmlElement(name = "Product")
            protected List<Invoices.Invoice.Products.Product> product;

            /**
             * Gets the value of the product property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the product property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getProduct().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Invoices.Invoice.Products.Product }
             * 
             * 
             */
            public List<Invoices.Invoice.Products.Product> getProduct() {
                if (product == null) {
                    product = new ArrayList<Invoices.Invoice.Products.Product>();
                }
                return this.product;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="sku" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="barcode" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="ProductName" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="supplier" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="unit" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="sellPrice" type="{http://www.w3.org/2001/XMLSchema}float"/>
             *         &lt;element name="qty" type="{http://www.w3.org/2001/XMLSchema}float"/>
             *         &lt;element name="grossAmount" type="{http://www.w3.org/2001/XMLSchema}float"/>
             *         &lt;element name="taxName" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="taxAmount" type="{http://www.w3.org/2001/XMLSchema}float"/>
             *         &lt;element name="netAmount" type="{http://www.w3.org/2001/XMLSchema}float"/>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "sku",
                "barcode",
                "productName",
                "description",
                "supplier",
                "unit",
                "sellPrice",
                "qty",
                "grossAmount",
                "taxName",
                "taxAmount",
                "netAmount"
            })
            public static class Product {

                @XmlElement(required = true)
                protected String sku;
                @XmlElement(required = true)
                protected String barcode;
                @XmlElement(name = "ProductName", required = true)
                protected String productName;
                @XmlElement(required = true)
                protected String description;
                @XmlElement(required = true)
                protected String supplier;
                @XmlElement(required = true)
                protected String unit;
                protected float sellPrice;
                protected float qty;
                protected float grossAmount;
                @XmlElement(required = true)
                protected String taxName;
                protected float taxAmount;
                protected float netAmount;

                /**
                 * Gets the value of the sku property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getSku() {
                    return sku;
                }

                /**
                 * Sets the value of the sku property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setSku(String value) {
                    this.sku = value;
                }

                /**
                 * Gets the value of the barcode property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getBarcode() {
                    return barcode;
                }

                /**
                 * Sets the value of the barcode property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setBarcode(String value) {
                    this.barcode = value;
                }

                /**
                 * Gets the value of the productName property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getProductName() {
                    return productName;
                }

                /**
                 * Sets the value of the productName property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setProductName(String value) {
                    this.productName = value;
                }

                /**
                 * Gets the value of the description property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getDescription() {
                    return description;
                }

                /**
                 * Sets the value of the description property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setDescription(String value) {
                    this.description = value;
                }

                /**
                 * Gets the value of the supplier property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getSupplier() {
                    return supplier;
                }

                /**
                 * Sets the value of the supplier property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setSupplier(String value) {
                    this.supplier = value;
                }

                /**
                 * Gets the value of the unit property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getUnit() {
                    return unit;
                }

                /**
                 * Sets the value of the unit property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setUnit(String value) {
                    this.unit = value;
                }

                /**
                 * Gets the value of the sellPrice property.
                 * 
                 */
                public float getSellPrice() {
                    return sellPrice;
                }

                /**
                 * Sets the value of the sellPrice property.
                 * 
                 */
                public void setSellPrice(float value) {
                    this.sellPrice = value;
                }

                /**
                 * Gets the value of the qty property.
                 * 
                 */
                public float getQty() {
                    return qty;
                }

                /**
                 * Sets the value of the qty property.
                 * 
                 */
                public void setQty(float value) {
                    this.qty = value;
                }

                /**
                 * Gets the value of the grossAmount property.
                 * 
                 */
                public float getGrossAmount() {
                    return grossAmount;
                }

                /**
                 * Sets the value of the grossAmount property.
                 * 
                 */
                public void setGrossAmount(float value) {
                    this.grossAmount = value;
                }

                /**
                 * Gets the value of the taxName property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getTaxName() {
                    return taxName;
                }

                /**
                 * Sets the value of the taxName property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setTaxName(String value) {
                    this.taxName = value;
                }

                /**
                 * Gets the value of the taxAmount property.
                 * 
                 */
                public float getTaxAmount() {
                    return taxAmount;
                }

                /**
                 * Sets the value of the taxAmount property.
                 * 
                 */
                public void setTaxAmount(float value) {
                    this.taxAmount = value;
                }

                /**
                 * Gets the value of the netAmount property.
                 * 
                 */
                public float getNetAmount() {
                    return netAmount;
                }

                /**
                 * Sets the value of the netAmount property.
                 * 
                 */
                public void setNetAmount(float value) {
                    this.netAmount = value;
                }

            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="projectNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="projectName" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="referenceNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "projectNo",
            "projectName",
            "referenceNo"
        })
        public static class Project {

            @XmlElement(required = true)
            protected String projectNo;
            @XmlElement(required = true)
            protected String projectName;
            @XmlElement(required = true)
            protected String referenceNo;

            /**
             * Gets the value of the projectNo property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getProjectNo() {
                return projectNo;
            }

            /**
             * Sets the value of the projectNo property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setProjectNo(String value) {
                this.projectNo = value;
            }

            /**
             * Gets the value of the projectName property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getProjectName() {
                return projectName;
            }

            /**
             * Sets the value of the projectName property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setProjectName(String value) {
                this.projectName = value;
            }

            /**
             * Gets the value of the referenceNo property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getReferenceNo() {
                return referenceNo;
            }

            /**
             * Sets the value of the referenceNo property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setReferenceNo(String value) {
                this.referenceNo = value;
            }

        }

    }

}
