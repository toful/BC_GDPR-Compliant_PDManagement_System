/**Personal Data Access Control System
 *ConsentManager class
 *
 * Implements the transactions to Consent SCs in the blockchain.
 * Also implements a menu to interact with all SC methods.
 *
 * Author: Cristòfol Daudén Esmel
 */

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import src.main.java.contracts.Consent;
import src.main.java.contracts.Purpose;

import java.math.BigInteger;
import java.util.*;


public class ConsentManager {

    private static ConsentManager manageConsent = null;
    private Web3j web3j;
    private DefaultGasProvider gasProvider;

    public int gasUsed;

    private ArrayList<Consent> consentContractList;
    Scanner sn;
    Random rand;

    enum PURPOSE { ModelTraining, ModelTesting, Profiling, ImprovingService, Advertising };
    private static PurposeManager purposeManager;

    public  static ConsentManager getConsentManager(Web3j web3j, DefaultGasProvider gasProvider ) {
        if (manageConsent==null) {
            manageConsent = new ConsentManager( web3j, gasProvider );
        }
        return manageConsent;
    }

    private ConsentManager(Web3j web3j, DefaultGasProvider gasProvider ){
        this.web3j = web3j;
        this.gasProvider = gasProvider;

        this.consentContractList = new ArrayList<>();
        sn = new Scanner(System.in);
        rand = new Random();
        gasUsed = 0;

        purposeManager = PurposeManager.getPurposeManager(web3j, gasProvider);
    }

    public Consent newConsentContract(ClientTransactionManager transactionManager, String dataSubject, List<String> dataRecipients ) {
        List<BigInteger> defaultPurposes = new ArrayList<BigInteger>();
            defaultPurposes.add(new BigInteger("0"));
            defaultPurposes.add(new BigInteger("1"));
            defaultPurposes.add(new BigInteger("2"));
        BigInteger duration = new BigInteger("100");

        try {
            Consent aux = Consent.deploy(web3j, transactionManager, gasProvider,
                    dataSubject, dataRecipients, duration, defaultPurposes ).send();
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

    public void checkValidity( Consent contract ){
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

    //Complex revoke Consent
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

    //getters
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public List<Purpose> getAllProcessorsPurposeSC( Consent contract, ClientTransactionManager transactionManager, BigInteger purpose ){
        List<Purpose> purposeCList = new ArrayList<>();
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

    public void getAllPurposesProcessor( Consent contract, String processor ){
        try{
            List<BigInteger> aux = contract.getAllPurposesProcessor( processor ).send();
            System.out.println( "Purposes for which Processor " + processor + " has permits to process DS's personal data: " );
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

    public void getAllPurposesProcessors( Consent contract ){
        try{
            List<String> aux = contract.getAllProcessors( ).send();
            System.out.println( "ALL PURPOSES FOR EACH PROCESSOR: " );
            for (String processor : aux ) {
                getAllPurposesProcessor( contract, processor);
            }
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Consent selectConsentContract( ClientTransactionManager transactionManager ){
        if( consentContractList.isEmpty() ) return null;
        while (true) {
            System.out.println("Select a Consent Smart Contract: 0 - " + (consentContractList.size() - 1));
            try {
                return Consent.load( consentContractList.get(sn.nextInt()).getContractAddress(),
                        web3j, transactionManager, gasProvider );
            } catch (Exception e) {
                System.out.println("Insert a valid option");
            }
        }
    }

    public void operateOverConsent(ClientTransactionManager transactionManager, ActorsManager actors){
        int option; String textInput;
        boolean end = false;
        Consent consentContract = selectConsentContract(transactionManager);
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
            System.out.println("12. Operate over existing Purpose Smart Contract");
            System.out.println("13. Select New Consent Smart Contract");
            System.out.println("14. Back");
            System.out.println("Select an option: ");
            try {
                option = sn.nextInt();
                switch (option) {
                    case 1:
                        grantConsent( consentContract );
                        break;
                    case 2:
                        revokeConsent( consentContract );
                        break;
                    case 3:
                        System.out.println("Specify purpose: ");
                        option = sn.nextInt();
                        revokeConsentPurpose( consentContract, BigInteger.valueOf( option ) );
                        break;
                    case 4:
                        System.out.println("Specify processor's address: ");
                        revokeConsentProcessor( consentContract, sn.next() );
                        break;
                    case 5:
                        checkValidity( consentContract );
                        break;
                    case 6:
                        purposeManager.newPurpose( consentContract, actors.processors[rand.nextInt( actors.processors.length )] );
                        break;
                    case 7:
                        getAllProcessors( consentContract );
                        break;
                    case 8:
                        getAllPurposes( consentContract );
                        break;
                    case 9:
                        System.out.println("Specify processor's address: ");
                        textInput = sn.next();
                        getAllPurposesProcessor( consentContract, textInput );
                        break;
                    case 10:
                        System.out.println("Specify purpose: ");
                        option = sn.nextInt();
                        getAllProcessorsPurpose( consentContract, BigInteger.valueOf( option ) );
                        break;
                    case 11:
                        getAllPurposesProcessors( consentContract );
                        break;
                    case 12:
                        purposeManager.operateOverPurpose( transactionManager, actors, consentContract.getContractAddress() );
                    case 13:
                        consentContract = selectConsentContract(transactionManager);
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

    public void operateOverConsentController(ClientTransactionManager transactionManager, ActorsManager actors){
        int option; String textInput;
        boolean end = false;
        while (!end) {
            System.out.println("1. Deploy new Consent Smart Contract");
            System.out.println("2. Load existing Consent Smart Contract");
            System.out.println("3. Operate over existing Consent Smart Contract");
            System.out.println("4. Back");
            System.out.println("Select an option: ");
            try {
                option = sn.nextInt();
                switch (option) {
                    case 1:
                        //Deploy a new Consent Smart Contract
                        consentContractList.add( newConsentContract(transactionManager, actors.dataSubject, actors.dataRecipients) );
                        break;
                    case 2:
                        System.out.println("Specify the contract address: ");
                        textInput = sn.next();
                        //Load an existing contract
                        consentContractList.add(Consent.load(textInput, web3j, transactionManager, gasProvider));
                        break;
                    case 3:
                        operateOverConsent(transactionManager, actors);
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
