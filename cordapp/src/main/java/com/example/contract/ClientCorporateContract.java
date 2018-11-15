package com.example.contract;

import com.example.state.ClientCorporateState;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.CommandWithParties;
import net.corda.core.contracts.Contract;
import net.corda.core.identity.AbstractParty;
import net.corda.core.transactions.LedgerTransaction;

import java.util.stream.Collectors;

import static net.corda.core.contracts.ContractsDSL.requireSingleCommand;
import static net.corda.core.contracts.ContractsDSL.requireThat;

/**
 * A implementation of a basic smart contract in Corda.
 *
 * This contract enforces rules regarding the creation of a valid [ClientCorporateState], which in turn encapsulates an [ClientCorporate].
 *
 * For a new [ClientCorporate] to be issued onto the ledger, a transaction is required which takes:
 * - Zero input states.
 * - One output state: the new [ClientCorporate].
 * - An Create() command with the public keys of both the lender and the borrower.
 *
 * All contracts must sub-class the [Contract] interface.
 */
public class ClientCorporateContract implements Contract {
    public static final String ClientCorporate_CONTRACT_ID = "com.example.contract.ClientCorporateContract";

    /**
     * The verify() function of all the states' contracts must not throw an exception for a transaction to be
     * considered valid.
     */
    @Override
    public void verify(LedgerTransaction tx) {
        final CommandWithParties<Commands.Create> command = requireSingleCommand(tx.getCommands(), Commands.Create.class);
        requireThat(require -> {
            // Generic constraints around the ClientCorporate transaction.
            require.using("No inputs should be consumed when issuing an ClientCorporate.",

                    tx.getInputs().isEmpty());
            require.using("Only one output state should be created.",
                    tx.getOutputs().size() == 1);
            final ClientCorporateState out = tx.outputsOfType(ClientCorporateState.class).get(0);

         //   require.using("All of the participants must be signers.",
           //         command.getSigners().containsAll(out.getParticipants().stream().map(AbstractParty::getOwningKey).collect(Collectors.toList())));

            // ClientCorporate-specific constraints.
            require.using("The ClientCorporate's value must be non-null.",
                    out.getCorporate_id()!=null);

            return null;
        });
    }

    /**
     * This contract only implements one command, Create.
     */
    public interface Commands extends CommandData {
        class Create implements Commands {}
    }
}