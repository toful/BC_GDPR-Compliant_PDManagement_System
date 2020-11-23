import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import src.main.java.contracts.Consent;
import src.main.java.contracts.Purpose;

import java.math.BigInteger;
import java.util.*;


public class ConsentManagerTest {

    private static ConsentManagerTest manageConsent = null;
    private Web3j web3j;
    private DefaultGasProvider gasProvider;

    public int gasUsed;

    private ArrayList<Consent> consentContractList;
    Scanner sn;
    Random rand;

    enum PURPOSE { ModelTraining, ModelTesting, Profiling, ImprovingService, Advertising };
    private static PurposeManagerTest purposeManager;

    public  static ConsentManagerTest getConsentManager(Web3j web3j, DefaultGasProvider gasProvider ) {
        if (manageConsent==null) {
            manageConsent = new ConsentManagerTest( web3j, gasProvider );
        }
        return manageConsent;
    }

    private ConsentManagerTest(Web3j web3j, DefaultGasProvider gasProvider ){
        this.web3j = web3j;
        this.gasProvider = gasProvider;

        this.consentContractList = new ArrayList<>();
        sn = new Scanner(System.in);
        rand = new Random();
        gasUsed = 0;

        purposeManager = PurposeManagerTest.getPurposeManager(web3j, gasProvider);
    }

    public Consent newConsentContract(ClientTransactionManager transactionManager, String dataSubject, List<String> dataRecipients ) {
        List<BigInteger> defaultPurposes = new ArrayList<BigInteger>();
        defaultPurposes.add(new BigInteger("0"));
        defaultPurposes.add(new BigInteger("1"));
        defaultPurposes.add(new BigInteger("2"));
        BigInteger duration = new BigInteger("1000000");

        try {
            Consent aux = Consent.deploy(web3j, transactionManager, gasProvider,
                    dataSubject, dataRecipients, duration, defaultPurposes ).send();
            //Add Transaction Gas to total Gas Used
            gasUsed += aux.getTransactionReceipt().orElse( null ).getGasUsed().intValue();
            return aux;
        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public void grantConsent( Consent contract ){
        try{
            TransactionReceipt receipt = contract.grantConsent().send();
            //Add Transaction Gas to total Gas Used
            gasUsed += receipt.getGasUsed().intValue();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void revokeConsent( Consent contract ){
        try{
            TransactionReceipt receipt = contract.revokeConsent().send();
            //Add Transaction Gas to total Gas Used
            gasUsed += receipt.getGasUsed().intValue();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public boolean checkValidity( Consent contract ){
        try{
            return contract.verify().send();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    //Complex revoke Consent
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void revokeConsentPurpose( Consent contract, BigInteger purpose ){
        try{
            TransactionReceipt receipt = contract.revokeConsentPurpose( purpose ).send();
            //Add Transaction Gas to total Gas Used
            gasUsed += receipt.getGasUsed().intValue();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    /*
    public void revokeConsentPurposeV2( ClientTransactionManager transactionManager, Consent contract, BigInteger purpose ){
        try{
            List<String > purposeSCAddressList = contract.getAllPurposeSCs().send();
            TransactionReceipt receipt;
            Purpose purposeSC;
            for( String purposeSCAddress : purposeSCAddressList ) {
                purposeSC = Purpose.load( purposeSCAddress, web3j, transactionManager, gasProvider);
                if( purposeSC.existsPurpose( purpose).send() ){
                    receipt = purposeSC.revokeConsent( purpose ).send();
                    gasUsed += receipt.getGasUsed().intValue();
                }
            }
        }
        catch ( Exception e){
            e.printStackTrace();
        }
    }
    */

    public void revokeConsentProcessor( Consent contract, String processor ){
        try{
            TransactionReceipt receipt = contract.revokeConsentProcessor( processor ).send();
            //Add Transaction Gas to total Gas Used
            gasUsed += receipt.getGasUsed().intValue();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
    /*
    public void revokeConsentProcessorV2( ClientTransactionManager transactionManager, Consent contract, String processor ){
        try{
            String purposeSCAddress = contract.getPurposeSC( processor ).send();
            Purpose purposeSC = Purpose.load( purposeSCAddress, web3j, transactionManager, gasProvider);
            TransactionReceipt receipt = purposeSC.revokeAllConsents().send();
            //Add Transaction Gas to total Gas Used
            gasUsed += receipt.getGasUsed().intValue();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void revokeConsentProcessorV3( ClientTransactionManager transactionManager, Consent contract, String processor ){
        try{
            String purposeSCAddress = contract.getPurposeSC( processor ).send();
            Purpose purposeSC = Purpose.load( purposeSCAddress, web3j, transactionManager, gasProvider);
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

    public void revokeConsentProcessorV3_DS( ClientTransactionManager transactionManager, Consent contract, String processor ){
        try{
            String purposeSCAddress = contract.getPurposeSC( processor ).send();
            Purpose purposeSC = Purpose.load( purposeSCAddress, web3j, transactionManager, gasProvider);
            List<BigInteger> purposes = purposeSC.getPurposes().send();
            for(BigInteger purpose : purposes ){
                TransactionReceipt receipt = purposeSC.revokeConsentSubject( purpose ).send();
                //Add Transaction Gas to total Gas Used
                gasUsed += receipt.getGasUsed().intValue();
            }
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
    */
    //getters
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public ArrayList<Purpose> getAllProcessorsPurposeSC( Consent contract, ClientTransactionManager transactionManager, BigInteger purpose ){
        ArrayList<Purpose> purposeCList = new ArrayList<>();
        try {
            for( String processor : (List<String>) contract.getAllProcessors().send() ){
                if( contract.getAllPurposesProcessor( processor ).send().contains( purpose ) ){
                    purposeCList.add(
                            Purpose.load( contract.getPurposeSC(processor, purpose).send(),
                                    web3j, transactionManager, gasProvider ) );
                }
            }
            return purposeCList;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void getAllProcessors( Consent contract ){
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

    public void getAllPurposes( Consent contract ){
        try{
            List<BigInteger> aux = contract.getAllPurposes( ).send();
            System.out.println( "Purposes for which DS's personal data is processed: " );
            for (BigInteger purpose : aux ) {
                System.out.println( "\t" + PURPOSE.values()[ purpose.intValue() ] );
            }
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void getAllProcessorsPurpose( Consent contract, BigInteger purpose ){
        try{
            if( !contract.getAllPurposes( ).send().contains( purpose) )
                System.out.println( "No processor is has permits to process DS's for this purpose");
            else{
                List<String> aux = new ArrayList<>();
                List<String> allProcessors = contract.getAllProcessors( ).send();
                for (String processor : allProcessors ) {
                    if( contract.getAllPurposesProcessor( processor ).send().contains(purpose) )
                        aux.add( processor );
                }
                System.out.println( "Processor that have permit to process DS's for " + PURPOSE.values()[ purpose.intValue() ] + ": " );
                for (String processor : aux ) {
                    System.out.println( "\t" + processor );
                }
            }
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public List<BigInteger> getAllPurposesProcessor( Consent contract, String processor ){
        try{
            return contract.getAllPurposesProcessor( processor ).send();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public void getAllPurposesProcessors( Consent contract ){
        try{
            List<String> aux = contract.getAllProcessors( ).send();
            System.out.println( "ALL PROCESSING PURPOSES REQUESTED FOR EACH PROCESSOR: " );
            for (String processor : aux ) {
                getAllPurposesProcessor( contract, processor);
            }
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}

