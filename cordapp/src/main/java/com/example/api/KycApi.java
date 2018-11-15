package com.example.api;


import java.util.Date;

import com.example.flow.ClientCorporateFlows;
import  com.example.model.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.identity.Party;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.node.NodeInfo;
import net.corda.core.node.services.Vault;
import net.corda.core.node.services.vault.*;
import net.corda.core.transactions.SignedTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static java.util.stream.Collectors.toList;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.OK;



@Path("kyc")
public class KycApi {

    private final CordaRPCOps rpcOps;
    private final CordaX500Name myLegalName;

    private final List<String> serviceNames = ImmutableList.of("Notary");

    static private final Logger logger = LoggerFactory.getLogger(KycApi.class);

    public KycApi(CordaRPCOps rpcOps) {
        this.rpcOps = rpcOps;
        this.myLegalName = rpcOps.nodeInfo().getLegalIdentities().get(0).getName();
    }
    @POST
    @Path("createCorporateTranscationii")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createEmployee(Employee employee) {
        System.out.println(employee);
        return Response.status(OK).entity(employee).build();
    }

    @POST
    @Path("createCorporateTranscation")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCordaTranscation(ClientCorporate clientCorporate) {
        if (clientCorporate ==null) {
            return Response.status(BAD_REQUEST).entity("Query parameter 'clientCorporate' must be null.\n").build();
        }
        if (clientCorporate.getCorporate_id()  == null) {
            return Response.status(BAD_REQUEST).entity("Query parameter 'clientCorporate.getCorporate_id' missing or has wrong format.\n").build();
        }
        if (clientCorporate.getCorporate_name()  == null) {
            return Response.status(BAD_REQUEST).entity("Query parameter 'clientCorporate.getCorporate_name' missing or has wrong format.\n").build();
        }

        try {
            final SignedTransaction signedTx = rpcOps
                    .startTrackedFlowDynamic(ClientCorporateFlows.ClientInitiator.class, clientCorporate.getCorporate_name(), clientCorporate.getCorporate_id() )
                    .getReturnValue()
                    .get();

            final String msg = String.format("Transaction id %s committed to ledger.\n", signedTx.getId());
            return Response.status(CREATED).entity(msg).build();

        } catch (Throwable ex) {
            final String msg = ex.getMessage();
            logger.error(ex.getMessage(), ex);
            return Response.status(BAD_REQUEST).entity(msg).build();
        }


    }


}