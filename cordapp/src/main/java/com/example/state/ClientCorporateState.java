package com.example.state;

import com.example.schema.ClientCorporateSchema;
import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.LinearState;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.PersistentState;
import net.corda.core.schemas.QueryableState;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ClientCorporateState implements LinearState, QueryableState {
    private final String corporate_id;
    private final String corporate_name;
    private final Party lender;


    private final UniqueIdentifier linearId;



    /**
     * @param value the value of the state.
     * @param lender the party issuing the client.
     * @param borrower the party receiving and approving the client.
     */
    public ClientCorporateState(Party lender,
                    String  corporate_id,
                    UniqueIdentifier linearId,
                    String corporate_name)
    {
        this.corporate_id = corporate_id;
        this.corporate_name = corporate_name;

        this.lender = lender;
        this.linearId = linearId;

    }

    public String getCorporate_id() {
        return corporate_id;
    }

    public String getCorporate_name() {
        return corporate_name;
    }

    public Party getLender() {
        return lender;
    }


    @Override public UniqueIdentifier getLinearId() { return linearId; }
    @Override public List<AbstractParty> getParticipants() {
        return Arrays.asList(lender);
    }

    @Override public PersistentState generateMappedObject(MappedSchema schema) {
        if (schema instanceof ClientCorporateSchema) {
            return new ClientCorporateSchema.PersistentClientCorporate(
                    this.lender.getName().toString(),
                    this.corporate_id,
                    this.linearId.getId(),
                    this.corporate_name.toString()
            );
        } else {
            throw new IllegalArgumentException("Unrecognised schema $schema");
        }
    }

    @Override public Iterable<MappedSchema> supportedSchemas() {
        return ImmutableList.of(new ClientCorporateSchema());
    }

    @Override
    public String toString() {
        return String.format("ClientCorporateState(corporate_id=%s, lender=%s, linearId=%s)", corporate_id, lender, linearId);
    }
}