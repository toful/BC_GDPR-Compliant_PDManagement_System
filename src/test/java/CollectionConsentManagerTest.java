/**Personal Data Access Control System
 * CollectionConsentManagerTest class
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


public class CollectionConsentManagerTest {

    private static CollectionConsentManagerTest manageConsent = null;
    private Web3j web3j;
    private DefaultGasProvider gasProvider;

    public int gasUsed;

    private ArrayList<CollectionConsent> consentContractList;
    Scanner sn;
    Random rand;

    public enum PURPOSE { ModelTraining, ModelTesting, Profiling, ImprovingService, Advertising };
    private static ProcessingConsentManagerTest purposeManager;

    public  static CollectionConsentManagerTest getConsentManager(Web3j web3j, DefaultGasProvider gasProvider ) {
        if (manageConsent==null) {
            manageConsent = new CollectionConsentManagerTest( web3j, gasProvider );
        }
        return manageConsent;
    }

    private CollectionConsentManagerTest(Web3j web3j, DefaultGasProvider gasProvider ){
        this.web3j = web3j;
        this.gasProvider = gasProvider;

        this.consentContractList = new ArrayList<>();
        sn = new Scanner(System.in);
        rand = new Random();
        gasUsed = 0;

        purposeManager = ProcessingConsentManagerTest.getPurposeManager(web3j, gasProvider);
    }

    public CollectionConsent newCollectionConsentContract(ClientTransactionManager transactionManager, String dataController, List<String> dataRecipients) {
        List<BigInteger> defaultPurposes = new ArrayList<BigInteger>();
        defaultPurposes.add(new BigInteger("0"));
        defaultPurposes.add(new BigInteger("1"));
        defaultPurposes.add(new BigInteger("2"));
        BigInteger duration = new BigInteger("1000000");
        BigInteger data = new BigInteger( "4294967295");

        try {
            CollectionConsent aux = CollectionConsent.deploy(web3j, transactionManager, gasProvider,
                    dataController, dataRecipients, data, duration, defaultPurposes ).send();
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

    public boolean checkValidity( CollectionConsent contract ){
        try{
            return contract.verify().send();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return false;
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

    public void modifyData( CollectionConsent contract, BigInteger data ){
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
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //revokeConsentPurpose - done through the Consent SC.
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

    //revokeConsentPurpose - done manually.
    public void revokeConsentPurposeV2( ClientTransactionManager transactionManager, CollectionConsent contract, BigInteger purpose ){
        try{
            List<String> processors = contract.getAllProcessors().send();
            TransactionReceipt receipt;
            ProcessingConsent purposeSC;

            for (String processor : processors) {
                purposeSC = ProcessingConsent.load( contract.getProcessingConsentSC(processor).send(),
                        web3j, transactionManager, gasProvider);
                if( purposeSC.existsPurpose( purpose ).send() ){
                    receipt = purposeSC.revokeConsent( purpose ).send();
                    gasUsed += receipt.getGasUsed().intValue();
                }
            }
        }
        catch ( Exception e){
            e.printStackTrace();
        }
    }

    //revokeConsentProcessor
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

    public void revokeConsentProcessorV2( ClientTransactionManager transactionManager, CollectionConsent contract, String processor ){
        try{
            String purposeSCAddress = contract.getProcessingConsentSC( processor ).send();
            ProcessingConsent purposeSC = ProcessingConsent.load( purposeSCAddress, web3j, transactionManager, gasProvider);
            TransactionReceipt receipt = purposeSC.revokeAllConsents().send();
            //Add Transaction Gas to total Gas Used
            gasUsed += receipt.getGasUsed().intValue();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void revokeConsentProcessorV3( ClientTransactionManager transactionManager, CollectionConsent contract, String processor ){
        try{
            String purposeSCAddress = contract.getProcessingConsentSC( processor ).send();
            ProcessingConsent purposeSC = ProcessingConsent.load( purposeSCAddress, web3j, transactionManager, gasProvider);
            List<BigInteger> purposes = purposeSC.getPurposes().send();
            for(BigInteger purpose : purposes ){
                TransactionReceipt receipt = purposeSC.revokeConsent( purpose ).send();
                //Add Transaction Gas to total Gas Used
                gasUsed += receipt.getGasUsed().intValue();
            }
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    //getters
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String getProcessingConsentSC( CollectionConsent contract, String processor ){
        try{
            return contract.getProcessingConsentSC( processor ).send();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return "";
        }
    }

    public List<String> getAllProcessors( CollectionConsent contract ){
        try{
            return contract.getAllProcessors( ).send();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    //Get All Purposes
    //getAllPurposes
    public List<BigInteger> getAllPurposes( CollectionConsent contract, ClientTransactionManager actor ){
        try{
            List<String> aux = contract.getAllProcessors( ).send();
            List<BigInteger> allProcessingPurposes = null;
            for (String processor : aux ) {
                allProcessingPurposes.addAll( ProcessingConsent.load( contract.getProcessingConsentSC( processor ).send(), web3j, actor, gasProvider ).getPurposes().send() );
            }
            allProcessingPurposes = allProcessingPurposes.stream().distinct().collect(Collectors.toList());
            return allProcessingPurposes;
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    //Get all Processors that has requested to process data for a specific processing purpose
    //getAllProcessorsPurpose
    public List<String> getAllProcessorsPurpose( CollectionConsent contract, BigInteger purpose, ClientTransactionManager actor ){
        try{
            List<String> allProcessors = contract.getAllProcessors( ).send();
            List<String> processors = null;
            for (String processor : allProcessors ) {
                if( ProcessingConsent.load( contract.getProcessingConsentSC( processor ).send(), web3j, actor, gasProvider
                ).getPurposes().send().contains( purpose ) )
                    processors.add( processor );
            }
            return processors;
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    //Get all Processing purposes that has requested a processor
    //getAllPurposesProcessor
    public List<BigInteger> getAllPurposesProcessor( CollectionConsent contract, String processor, ClientTransactionManager actor ) {
        try {
            return ProcessingConsent.load( contract.getProcessingConsentSC( processor ).send(), web3j, actor, gasProvider 
                ).getPurposes().send();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    //countAllPurposesProcessor
    public int countAllPurposesProcessor( CollectionConsent contract, String processor, ClientTransactionManager actor ) {
        int processingPurposes;
        try {
            processingPurposes =
                    (int) ProcessingConsent.load(
                            contract.getProcessingConsentSC( processor ).send(),
                            web3j, actor, gasProvider ).getPurposes().send().stream().count();
        } catch (Exception e) {
            processingPurposes = 0;
            System.out.println("Error: " + e.getMessage());
        }
        return processingPurposes;
    }

    //getAllPurposesProcessors
    public Hashtable< String, List<BigInteger> > getAllPurposesProcessors( CollectionConsent contract, ClientTransactionManager actor ){
        Hashtable< String, List<BigInteger> > purposesProcessors = new Hashtable<>();
        try{
            List<String> aux = contract.getAllProcessors( ).send();
            for (String processor : aux ) {
                purposesProcessors.put( processor, getAllPurposesProcessor( contract, processor, actor ) );
            }
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
        return purposesProcessors;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}

