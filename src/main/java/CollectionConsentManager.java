/**Personal Data Access Control System
 * CollectionConsentManager class
 *
 * Implements the transactions needed to interact with the Collection Consent SCs deployed in the blockchain.
 * Also implements a menu to use all SC methods.
 *
 * Author: Cristòfol Daudén Esmel
 */

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import src.main.java.contracts.CollectionConsent;
import src.main.java.contracts.ProcessingConsent;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;


public class CollectionConsentManager {

    private static CollectionConsentManager manageConsent = null;
    private Web3j web3j;
    private DefaultGasProvider gasProvider;

    public int gasUsed;

    private ArrayList<CollectionConsent> collectionConsentContractList;
    Scanner sn;
    Random rand;

    enum PURPOSE { ModelTraining, ModelTesting, Profiling, ImprovingService, Advertising };
    private static ProcessingConsentManager purposeManager;

    public  static CollectionConsentManager getConsentManager(Web3j web3j, DefaultGasProvider gasProvider ) {
        if (manageConsent==null) {
            manageConsent = new CollectionConsentManager( web3j, gasProvider );
        }
        return manageConsent;
    }

    private CollectionConsentManager(Web3j web3j, DefaultGasProvider gasProvider ){
        this.web3j = web3j;
        this.gasProvider = gasProvider;

        this.collectionConsentContractList = new ArrayList<>();
        sn = new Scanner(System.in);
        rand = new Random();
        gasUsed = 0;

        purposeManager = ProcessingConsentManager.getPurposeManager(web3j, gasProvider);
    }

    public CollectionConsent newConsentContract(ClientTransactionManager transactionManager, String dataController, List<String> dataRecipients) {
        List<BigInteger> defaultPurposes = new ArrayList<BigInteger>();
            defaultPurposes.add(new BigInteger("0"));
            defaultPurposes.add(new BigInteger("1"));
            defaultPurposes.add(new BigInteger("2"));
        BigInteger duration = new BigInteger("1000000");
        BigInteger data = new BigInteger( "4294967295");

        try {
            CollectionConsent aux = CollectionConsent.deploy(web3j, transactionManager, gasProvider,
                    dataController, dataRecipients, data, duration, defaultPurposes ).send();
            System.out.println( "Consent Contract Created"
                    + "\n\tTransaction Hash: " + aux.getTransactionReceipt().orElse(null).getTransactionHash()
                    + "\n\tContract address: " + aux.getContractAddress() );
            //Add Transaction Gas to total Gas Used
            gasUsed += aux.getTransactionReceipt().orElse( null ).getGasUsed().intValue();
            return aux;
        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public void grantConsent( CollectionConsent contract ){
        try{
            TransactionReceipt receipt = contract.grantConsent().send();
            //Add Transaction Gas to total Gas Used
            gasUsed += receipt.getGasUsed().intValue();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void revokeConsent( CollectionConsent contract ){
        try{
            TransactionReceipt receipt = contract.revokeConsent().send();
            //Add Transaction Gas to total Gas Used
            gasUsed += receipt.getGasUsed().intValue();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void checkValidity( CollectionConsent contract ){
        try{
            if( contract.verify().send() )
                System.out.println("Valid");
            else
                System.out.println("Not Valid");
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void eraseData( CollectionConsent contract ){
        try{
            TransactionReceipt receipt = contract.eraseData().send();
            //Add Transaction Gas to total Gas Used
            gasUsed += receipt.getGasUsed().intValue();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void modifyData( CollectionConsent contract ){
        BigInteger data = new BigInteger( "0094967295");
        try {
            TransactionReceipt receipt = contract.modifyData( data ).send();
            //Add Transaction Gas to total Gas Used
            gasUsed += receipt.getGasUsed().intValue();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    //Complex revoke Consent
    public void revokeConsentPurpose( CollectionConsent contract, BigInteger purpose ){
        try{
            TransactionReceipt receipt = contract.revokeConsentPurpose( purpose ).send();
            //Add Transaction Gas to total Gas Used
            gasUsed += receipt.getGasUsed().intValue();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void revokeConsentProcessor( CollectionConsent contract, String processor ){
        try{
            TransactionReceipt receipt = contract.revokeConsentProcessor( processor ).send();
            //Add Transaction Gas to total Gas Used
            gasUsed += receipt.getGasUsed().intValue();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    //getters
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Get all Processing Consent SCs
    public String getProcessingConsentSC( CollectionConsent contract, String processor ){
        try{
            return contract.getProcessingConsentSC( processor ).send();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return "";
        }
    }

    public void getAllProcessors( CollectionConsent contract ){
        try{
            List<String> aux = contract.getAllProcessors( ).send();
            System.out.println( "Processors that operate over this SC: " );
            for (String processor : aux ) {
                System.out.println( "\t" + processor );
            }
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    //Get All Purposes
    //getAllPurposes
    public void getAllPurposes( CollectionConsent contract, ClientTransactionManager actor ){
        try{
            List<String> aux = contract.getAllProcessors( ).send();
            List<BigInteger> allProcessingPurposes = null;
            for (String processor : aux ) {
                allProcessingPurposes.addAll( ProcessingConsent.load( contract.getProcessingConsentSC( processor ).send(), web3j, actor, gasProvider ).getPurposes().send() );
            }
            allProcessingPurposes = allProcessingPurposes.stream().distinct().collect(Collectors.toList());
            System.out.println( "Purposes for which DS's personal data is processed: " );
            for (BigInteger purpose : allProcessingPurposes ) {
                System.out.println( "\t" + PURPOSE.values()[ purpose.intValue() ] );
            }
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    //Get all Processors that has requested to process data for a specific processing purpose
    //getAllProcessorsPurpose
    public void getAllProcessorsPurpose( CollectionConsent contract, BigInteger purpose, ClientTransactionManager actor ){
        try{
            List<String> allProcessors = contract.getAllProcessors( ).send();
            List<String> processors = null;
            for (String processor : allProcessors ) {
                if( ProcessingConsent.load( contract.getProcessingConsentSC( processor ).send(), web3j, actor, gasProvider
                        ).getPurposes().send().contains( purpose ) )
                    processors.add( processor );
            }
            if( processors.isEmpty() )
                System.out.println( "No processor is has permits to process DS's for this purpose");
            else{
                System.out.println( "Processor that have permit to process DS's for " + PURPOSE.values()[ purpose.intValue() ] + ": " );
                for (String processor : processors ) {
                    System.out.println( "\t" + processor );
                }
            }
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    //Get all Processing purposes that has requested a processor
    //getAllPurposesProcessor
    public void getAllPurposesProcessor( CollectionConsent contract, String processor, ClientTransactionManager actor ){
        try{
            List<BigInteger> aux = ProcessingConsent.load( contract.getProcessingConsentSC( processor ).send(), web3j, actor, gasProvider
            ).getPurposes().send();
            System.out.println( "Purposes for which Processor " + processor + " has requested to process DS's personal data: " );
            for (BigInteger purpose : aux ) {
                System.out.println( "\t" + PURPOSE.values()[ purpose.intValue() ] );
            }
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    //For all processors, get all requested processing purposes.
    //getAllPurposesProcessors
    public void getAllPurposesProcessors( CollectionConsent contract, ClientTransactionManager actor ){
        try{
            List<String> aux = contract.getAllProcessors( ).send();
            System.out.println( "ALL PROCESSING PURPOSES REQUESTED FOR EACH PROCESSOR: " );
            for (String processor : aux ) {
                getAllPurposesProcessor( contract, processor, actor);
            }
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public CollectionConsent selectConsentContract( ClientTransactionManager transactionManager ){
        if( collectionConsentContractList.isEmpty() ) return null;
        while (true) {
            System.out.println("Select a Consent Smart Contract: 0 - " + (collectionConsentContractList.size() - 1));
            try {
                return CollectionConsent.load( collectionConsentContractList.get(sn.nextInt()).getContractAddress(),
                        web3j, transactionManager, gasProvider );
            } catch (Exception e) {
                System.out.println("Insert a valid option");
            }
        }
    }

    public void operateOverCollectionConsent(ClientTransactionManager transactionManager, ActorsManager actors){
        int option; String textInput;
        boolean end = false;
        CollectionConsent collectionConsentContract = selectConsentContract(transactionManager);
        while (!end) {
            System.out.println("1. Grant Consent");
            System.out.println("2. Revoke Consent");
            System.out.println("3. Revoke Consent for a Purpose");
            System.out.println("4. Revoke Consent for a Processor");
            System.out.println("5. Valid?");
            System.out.println("6. New Purpose");
            System.out.println("7. Get All Processors");
            System.out.println("8. Get All Purposes");
            System.out.println("9. Get All Purposes from a Processor");
            System.out.println("10. Get All Processors that have permits over specific purpose");
            System.out.println("11. Get All Purposes and Processors");
            System.out.println("12. Operate over existing Processing Consent Smart Contract");
            System.out.println("13. Select New Consent Smart Contract");
            System.out.println("14. Back");
            System.out.println("Select an option: ");
            try {
                option = sn.nextInt();
                switch (option) {
                    case 1:
                        grantConsent( collectionConsentContract );
                        break;
                    case 2:
                        revokeConsent( collectionConsentContract );
                        break;
                    case 3:
                        System.out.println("Specify purpose: ");
                        option = sn.nextInt();
                        revokeConsentPurpose( collectionConsentContract, BigInteger.valueOf( option ) );
                        break;
                    case 4:
                        System.out.println("Specify processor's address: ");
                        revokeConsentProcessor( collectionConsentContract, sn.next() );
                        break;
                    case 5:
                        checkValidity( collectionConsentContract );
                        break;
                    case 6:
                        purposeManager.newPurpose( collectionConsentContract,
                                actors.processors[rand.nextInt( actors.processors.length )],
                                new BigInteger( String.valueOf(rand.nextInt( CollectionConsentManager.PURPOSE.values().length )) ) );
                        break;
                    case 7:
                        getAllProcessors( collectionConsentContract );
                        break;
                    case 8:
                        getAllPurposes( collectionConsentContract, transactionManager );
                        break;
                    case 9:
                        System.out.println("Specify processor's address: ");
                        textInput = sn.next();
                        getAllPurposesProcessor( collectionConsentContract, textInput, transactionManager );
                        break;
                    case 10:
                        System.out.println("Specify purpose: ");
                        option = sn.nextInt();
                        getAllProcessorsPurpose( collectionConsentContract, BigInteger.valueOf( option ), transactionManager );
                        break;
                    case 11:
                        getAllPurposesProcessors( collectionConsentContract, transactionManager );
                        break;
                    case 12:
                        purposeManager.operateOverProcessingConsentSC( transactionManager, actors, collectionConsentContract.getContractAddress() );
                    case 13:
                        collectionConsentContract = selectConsentContract(transactionManager);
                        break;
                    case 14:
                        end = true;
                        break;
                    default:
                        System.out.println("Not a valid option");
                }
            } catch (InputMismatchException e) {
                System.out.println("Insert a valid option");
                sn.next();
            } catch (NullPointerException e) {
                System.out.println("There is no Consent contract deployed yet!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void operateOverCollectionConsentDS(ClientTransactionManager transactionManager, ActorsManager actors){
        int option; String textInput;
        boolean end = false;
        while (!end) {
            System.out.println("1. Deploy new Collection Consent Smart Contract");
            System.out.println("2. Load existing Collection Consent Smart Contract");
            System.out.println("3. Operate over existing Consent Smart Contract");
            System.out.println("4. Back");
            System.out.println("Select an option: ");
            try {
                option = sn.nextInt();
                switch (option) {
                    case 1:
                        //Deploy a new Consent Smart Contract
                        collectionConsentContractList.add( newConsentContract( transactionManager, actors.controller, actors.dataRecipients) );
                        break;
                    case 2:
                        System.out.println("Specify the contract address: ");
                        textInput = sn.next();
                        //Load an existing contract
                        collectionConsentContractList.add( CollectionConsent.load( textInput, web3j, transactionManager, gasProvider) );
                        break;
                    case 3:
                        operateOverCollectionConsent(transactionManager, actors);
                        end = true;
                        break;
                    case 4:
                        end = true;
                        break;
                    default:
                        System.out.println("Not a valid option");
                }
            } catch (InputMismatchException e) {
                System.out.println("Insert a valid option");
                sn.next();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
