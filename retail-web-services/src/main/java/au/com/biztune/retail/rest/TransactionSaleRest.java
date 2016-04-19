// Sydney Trains 2015

package au.com.biztune.retail.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

/**
 * Created by arash on 10/08/2015.
 */

@Component
@Path("transactionSale")
public class TransactionSaleRest {
    private final Logger logger = LoggerFactory.getLogger(TransactionSaleRest.class);
    @Context
    private UriInfo uriInfo;

    @Context
    private Request request;
}
