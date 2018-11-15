package com.example.flow;


import co.paralleluniverse.fibers.Suspendable;
import com.example.contract.ClientCorporateContract;
import com.example.contract.IOUContract;

import com.example.state.ClientCorporateState;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import kotlin.Unit;
import net.corda.confidential.IdentitySyncFlow;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.ContractState;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;
import net.corda.core.utilities.ProgressTracker.Step;

import static com.example.contract.ClientCorporateContract.ClientCorporate_CONTRACT_ID;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.contract.IOUContract.IOU_CONTRACT_ID;
import static net.corda.core.contracts.ContractsDSL.requireThat;

/**
 * This flow allows two parties (the [Initiator] and the [Acceptor]) to come to an agreement about the IOU encapsulated
 * within an [IOUState].
 * <p>
 * In our simple example, the [Acceptor] always accepts a valid IOU.
 * <p>
 * These flows have deliberately been implemented by using only the call() method for ease of understanding. In
 * practice we would recommend splitting up the various stages of the flow into sub-routines.
 * <p>
 * All methods called within the [FlowLogic] sub-class need to be annotated with the @Suspendable annotation.
 */
public class ClientCorporateFlows {
    @InitiatingFlow
    @StartableByRPC
    public static class ClientInitiator extends FlowLogic<SignedTransaction> {

        private final String corporate_id;
        private final String corporate_name;


        private final Step SYNCING = new Step("Syncing identities.") {
            @Override
            public ProgressTracker childProgressTracker() {
                return IdentitySyncFlow.Send.Companion.tracker();
            }
        };
        private final Step COLLECTING = new Step("Collecting counterparty signature.") {
            @Override
            public ProgressTracker childProgressTracker() {
                return CollectSignaturesFlow.Companion.tracker();
            }
        };


        private final Step FINALISING = new Step("Finalising transaction.") {
            @Override
            public ProgressTracker childProgressTracker() {
                return FinalityFlow.Companion.tracker();
            }
        };

        private final Step GENERATING_TRANSACTION = new Step("Generating transaction based on new ClientCorporate.");
        private final Step VERIFYING_TRANSACTION = new Step("Verifying contract constraints.");
        private final Step SIGNING_TRANSACTION = new Step("Signing transaction with our private key.");
        private final Step GATHERING_SIGS = new Step("Gathering the counterparty's signature.") {
            @Override
            public ProgressTracker childProgressTracker() {
                return CollectSignaturesFlow.Companion.tracker();
            }
        };


        // The progress tracker checkpoints each stage of the flow and outputs the specified messages when each
        // checkpoint is reached in the code. See the 'progressTracker.currentStep' expressions within the call()
        // function.
        private final ProgressTracker progressTracker = new ProgressTracker(
                GENERATING_TRANSACTION,
                VERIFYING_TRANSACTION,
                SIGNING_TRANSACTION,
                GATHERING_SIGS

        );

        public ClientInitiator(String corporate_name, String corporate_id) {
            this.corporate_name = corporate_name;
            this.corporate_id = corporate_id;

        }

        @Override

        public ProgressTracker getProgressTracker() {
            return progressTracker;
        }

        /**
         * The flow logic is encapsulated within the call() method.
         */
        @Suspendable
        @Override
        public SignedTransaction call() throws FlowException {
            // Obtain a reference to the notary we want to use.

            // io.ipfs.multihash.Multihash filePointer = io.ipfs.multihash.Multihash.fromBase58(attachmentHashValue);
            final Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);

            // Stage 1.
            progressTracker.setCurrentStep(GENERATING_TRANSACTION);
            // Generate an unsigned transaction.
            Party me = getServiceHub().getMyInfo().getLegalIdentities().get(0);

            ClientCorporateState clientCorporateState=new ClientCorporateState(me,corporate_id, new UniqueIdentifier(),corporate_name);

            final Command<ClientCorporateContract.Commands.Create> txCommand = new Command<>(
                    new ClientCorporateContract.Commands.Create(),
                    ImmutableList.of(clientCorporateState.getLender().getOwningKey()));
            final TransactionBuilder txBuilder = new TransactionBuilder(notary)
                    .addOutputState(clientCorporateState,ClientCorporate_CONTRACT_ID)
                    .addCommand(txCommand);

            // Stage 2.
            // progressTracker.setCurrentStep(VERIFYING_TRANSACTION);
            // Verify that the transaction is valid.
            txBuilder.verify(getServiceHub());

            // Stage 3.
            // progressTracker.setCurrentStep(SIGNING_TRANSACTION);
            // Sign the transaction.
            final SignedTransaction partSignedTx = getServiceHub().signInitialTransaction(txBuilder);

            // Stage 4.
            //  progressTracker.setCurrentStep(GATHERING_SIGS);
            // Send the state to the counterparty, and receive it back with their signature.

            //Set<FlowSession> sessions = new HashSet<FlowSession>();
            //Set<Party> parties = ImmutableSet.of(otherParty, otherParty2);

            //for (Party party : parties) {
            //  sessions.add(initiateFlow(party));
            //}
            //subFlow(new IdentitySyncFlow.Send(sessions, partSignedTx.getTx(), SYNCING.childProgressTracker()));
            // Stage 9. Collect signatures from the borrower and the new lender.
            // progressTracker.setCurrentStep(COLLECTIN);
            //final SignedTransaction stx = subFlow(new CollectSignaturesFlow(
            //  partSignedTx,
            //sessions,
            //ImmutableList.of(iouState.getLender().getOwningKey()),
            //COLLECTING.childProgressTracker()));

            // Stage 10. Notarise and record, the transaction in our vaults. Send a copy to me as well.
            //   progressTracker.setCurrentStep(FINALISING);
            return subFlow(new FinalityFlow(partSignedTx));
            // FlowSession otherPartySession = initiateFlow(otherParty);
            // final SignedTransaction fullySignedTx = subFlow(
            //   new CollectSignaturesFlow(partSignedTx, sessions, CollectSignaturesFlow.Companion.tracker()));

            // Stage 5.
            //progressTracker.setCurrentStep(FINALISING_TRANSACTION);
            // Notarise and record the transaction in both parties' vaults.
            //return subFlow(new FinalityFlow(fullySignedTx));
        }
    }

    @InitiatedBy(ClientInitiator.class)
    public static class Responder extends FlowLogic<SignedTransaction> {
        private final FlowSession otherFlow;

        public Responder(FlowSession otherFlow) {
            this.otherFlow = otherFlow;
        }

        @Suspendable
        @Override
        public SignedTransaction call() throws FlowException {
            subFlow(new IdentitySyncFlow.Receive(otherFlow));
            SignedTransaction stx = subFlow(new SignTxFlowNoChecking(otherFlow, SignTransactionFlow.Companion.tracker()));
            return waitForLedgerCommit(stx.getId());
        }
    }

    static class SignTxFlowNoChecking extends SignTransactionFlow {
        SignTxFlowNoChecking(FlowSession otherFlow, ProgressTracker progressTracker) {
            super(otherFlow, progressTracker);
        }

        @Override
        protected void checkTransaction(SignedTransaction tx) {
            // TODO: Add checking here.
        }
    }
}

