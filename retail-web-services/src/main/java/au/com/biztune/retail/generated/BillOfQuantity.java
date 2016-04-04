
package au.com.biztune.retail.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
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
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="Header">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="OrderNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="Note" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
 *                   &lt;element name="ProjectNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ProjectName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ProjectAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="QuoteNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ReferenceNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="DateRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="TotalCost" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *                   &lt;element name="TotalPrice" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
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
 *                   &lt;element name="ClientCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ClientName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ContactName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="Add1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="BillOfQuantities">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Product" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="CatalogueNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                             &lt;element name="Reference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                             &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                             &lt;element name="SupplierCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                             &lt;element name="Supplier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                             &lt;element name="Unit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                             &lt;element name="TaxName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                             &lt;element name="Cost" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *                             &lt;element name="SellPrice" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *                             &lt;element name="Qty" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
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
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "headerOrProjectOrClient"
})
@XmlRootElement(name = "BillOfQuantity")
public class BillOfQuantity {

    @XmlElements({
        @XmlElement(name = "Header", type = BillOfQuantity.Header.class),
        @XmlElement(name = "Project", type = BillOfQuantity.Project.class),
        @XmlElement(name = "Client", type = BillOfQuantity.Client.class),
        @XmlElement(name = "BillOfQuantities", type = BillOfQuantity.BillOfQuantities.class)
    })
    protected List<Object> headerOrProjectOrClient;

    /**
     * Gets the value of the headerOrProjectOrClient property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the headerOrProjectOrClient property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHeaderOrProjectOrClient().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BillOfQuantity.Header }
     * {@link BillOfQuantity.Project }
     * {@link BillOfQuantity.Client }
     * {@link BillOfQuantity.BillOfQuantities }
     * 
     * 
     */
    public List<Object> getHeaderOrProjectOrClient() {
        if (headerOrProjectOrClient == null) {
            headerOrProjectOrClient = new ArrayList<Object>();
        }
        return this.headerOrProjectOrClient;
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
     *                   &lt;element name="CatalogueNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                   &lt;element name="Reference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                   &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                   &lt;element name="SupplierCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                   &lt;element name="Supplier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                   &lt;element name="Unit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                   &lt;element name="TaxName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                   &lt;element name="Cost" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
     *                   &lt;element name="SellPrice" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
     *                   &lt;element name="Qty" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
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
    public static class BillOfQuantities {

        @XmlElement(name = "Product")
        protected List<BillOfQuantity.BillOfQuantities.Product> product;

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
         * {@link BillOfQuantity.BillOfQuantities.Product }
         * 
         * 
         */
        public List<BillOfQuantity.BillOfQuantities.Product> getProduct() {
            if (product == null) {
                product = new ArrayList<BillOfQuantity.BillOfQuantities.Product>();
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
         *         &lt;element name="CatalogueNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *         &lt;element name="Reference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *         &lt;element name="SupplierCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *         &lt;element name="Supplier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *         &lt;element name="Unit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *         &lt;element name="TaxName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *         &lt;element name="Cost" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
         *         &lt;element name="SellPrice" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
         *         &lt;element name="Qty" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
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
            "catalogueNo",
            "reference",
            "description",
            "supplierCode",
            "supplier",
            "unit",
            "taxName",
            "cost",
            "sellPrice",
            "qty"
        })
        public static class Product {

            @XmlElement(name = "CatalogueNo")
            protected String catalogueNo;
            @XmlElement(name = "Reference")
            protected String reference;
            @XmlElement(name = "Description")
            protected String description;
            @XmlElement(name = "SupplierCode")
            protected String supplierCode;
            @XmlElement(name = "Supplier")
            protected String supplier;
            @XmlElement(name = "Unit")
            protected String unit;
            @XmlElement(name = "TaxName")
            protected String taxName;
            @XmlElement(name = "Cost")
            protected Double cost;
            @XmlElement(name = "SellPrice")
            protected Double sellPrice;
            @XmlElement(name = "Qty")
            protected Double qty;

            /**
             * Gets the value of the catalogueNo property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCatalogueNo() {
                return catalogueNo;
            }

            /**
             * Sets the value of the catalogueNo property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCatalogueNo(String value) {
                this.catalogueNo = value;
            }

            /**
             * Gets the value of the reference property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getReference() {
                return reference;
            }

            /**
             * Sets the value of the reference property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setReference(String value) {
                this.reference = value;
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
             * Gets the value of the supplierCode property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getSupplierCode() {
                return supplierCode;
            }

            /**
             * Sets the value of the supplierCode property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setSupplierCode(String value) {
                this.supplierCode = value;
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
             * Gets the value of the cost property.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public Double getCost() {
                return cost;
            }

            /**
             * Sets the value of the cost property.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setCost(Double value) {
                this.cost = value;
            }

            /**
             * Gets the value of the sellPrice property.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public Double getSellPrice() {
                return sellPrice;
            }

            /**
             * Sets the value of the sellPrice property.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setSellPrice(Double value) {
                this.sellPrice = value;
            }

            /**
             * Gets the value of the qty property.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public Double getQty() {
                return qty;
            }

            /**
             * Sets the value of the qty property.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setQty(Double value) {
                this.qty = value;
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
     *         &lt;element name="ClientCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ClientName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ContactName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="Add1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
        "clientCode",
        "clientName",
        "contactName",
        "add1"
    })
    public static class Client {

        @XmlElement(name = "ClientCode")
        protected String clientCode;
        @XmlElement(name = "ClientName")
        protected String clientName;
        @XmlElement(name = "ContactName")
        protected String contactName;
        @XmlElement(name = "Add1")
        protected String add1;

        /**
         * Gets the value of the clientCode property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getClientCode() {
            return clientCode;
        }

        /**
         * Sets the value of the clientCode property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setClientCode(String value) {
            this.clientCode = value;
        }

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
         * Gets the value of the add1 property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAdd1() {
            return add1;
        }

        /**
         * Sets the value of the add1 property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAdd1(String value) {
            this.add1 = value;
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
     *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="OrderNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="Note" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
        "orderNumber",
        "note"
    })
    public static class Header {

        @XmlElement(name = "Name")
        protected String name;
        @XmlElement(name = "OrderNumber")
        protected String orderNumber;
        @XmlElement(name = "Note")
        protected String note;

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

        /**
         * Gets the value of the note property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNote() {
            return note;
        }

        /**
         * Sets the value of the note property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNote(String value) {
            this.note = value;
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
     *         &lt;element name="ProjectNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ProjectName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ProjectAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="QuoteNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ReferenceNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="DateRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="TotalCost" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
     *         &lt;element name="TotalPrice" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
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
        "projectAddress",
        "quoteNo",
        "referenceNo",
        "dateRequired",
        "totalCost",
        "totalPrice"
    })
    public static class Project {

        @XmlElement(name = "ProjectNo")
        protected String projectNo;
        @XmlElement(name = "ProjectName")
        protected String projectName;
        @XmlElement(name = "ProjectAddress")
        protected String projectAddress;
        @XmlElement(name = "QuoteNo")
        protected String quoteNo;
        @XmlElement(name = "ReferenceNo")
        protected String referenceNo;
        @XmlElement(name = "DateRequired")
        protected String dateRequired;
        @XmlElement(name = "TotalCost")
        protected Double totalCost;
        @XmlElement(name = "TotalPrice")
        protected Double totalPrice;

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
         * Gets the value of the projectAddress property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getProjectAddress() {
            return projectAddress;
        }

        /**
         * Sets the value of the projectAddress property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setProjectAddress(String value) {
            this.projectAddress = value;
        }

        /**
         * Gets the value of the quoteNo property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getQuoteNo() {
            return quoteNo;
        }

        /**
         * Sets the value of the quoteNo property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setQuoteNo(String value) {
            this.quoteNo = value;
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

        /**
         * Gets the value of the dateRequired property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDateRequired() {
            return dateRequired;
        }

        /**
         * Sets the value of the dateRequired property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDateRequired(String value) {
            this.dateRequired = value;
        }

        /**
         * Gets the value of the totalCost property.
         * 
         * @return
         *     possible object is
         *     {@link Double }
         *     
         */
        public Double getTotalCost() {
            return totalCost;
        }

        /**
         * Sets the value of the totalCost property.
         * 
         * @param value
         *     allowed object is
         *     {@link Double }
         *     
         */
        public void setTotalCost(Double value) {
            this.totalCost = value;
        }

        /**
         * Gets the value of the totalPrice property.
         * 
         * @return
         *     possible object is
         *     {@link Double }
         *     
         */
        public Double getTotalPrice() {
            return totalPrice;
        }

        /**
         * Sets the value of the totalPrice property.
         * 
         * @param value
         *     allowed object is
         *     {@link Double }
         *     
         */
        public void setTotalPrice(Double value) {
            this.totalPrice = value;
        }

    }

}
