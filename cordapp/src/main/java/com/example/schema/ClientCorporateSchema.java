package com.example.schema;


import com.google.common.collect.ImmutableList;
import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.PersistentState;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

/**
 * An IOUState schema.
 */
public class ClientCorporateSchema extends MappedSchema {
    public ClientCorporateSchema() {
        super(ClientCorporateSchema.class, 1, ImmutableList.of(PersistentClientCorporate.class));
    }

    @Entity
    @Table(name = "client_corporate_state")
    public static class PersistentClientCorporate extends PersistentState {
        @Column(name = "lender")
        private final String lender;
        @Column(name = "linear_id")
        private final UUID linearId;
        @Column(name = "corporate_id")
        private final String corporate_id;
        @Column(name = "corporate_name")
        private final String corporate_name;

        public PersistentClientCorporate(String lender, String corporate_id, UUID linearId, String corporate_name) {
            this.lender = lender;

            this.corporate_name = corporate_name;
            this.linearId = linearId;
            this.corporate_id = corporate_id;

        }

        // Default constructor required by hibernate.
        public PersistentClientCorporate() {
            this.lender = null;

            this.corporate_name = null;
            this.linearId = null;
            this.corporate_id = null;
        }

        public String getLender() {
            return lender;
        }

        public UUID getLinearId() {
            return linearId;
        }

        public String getCorporate_id() {
            return corporate_id;
        }

        public String getCorporate_name() {
            return corporate_name;
        }

        public UUID getId() {
            return linearId;
        }
    }
}