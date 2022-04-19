/**Personal Data Access Control System
 * ProcessingConsentManager class
 *
 * Implements the transactions needed to interact with the Processing Consent SCs deployed in the blockchain.
 * Also implements a menu to use all SC methods.
 *
 * Author: Cristòfol Daudén Esmel
 */

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import src.main.java.contracts.ProcessingConsent;
import src.main.java.contracts.CollectionConsent;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class ProcessingConsentManager {

    private static ProcessingConsentManager managePurpose = null;
    private Web3j web3j;
    private DefaultGasProvider gasProvider;

    //Consent Address => Processor Address => Purpose SC
    private Hashtable< String , Hashtable< String , String > > processingConsentContractList;
    Scanner sn;
    Random rand;

    public  static ProcessingConsentManager getPurposeManager(Web3j web3j, DefaultGasProvider gasProvider ) {
        if (managePurpose==null) {
            managePurpose = new ProcessingConsentManager( web3j, gasProvider );
        }
        return managePurpose;
    }

    private ProcessingConsentManager(Web3j web3j, DefaultGasProvider gasProvider ){
        this.web3j = web3j;
        this.gasProvider = gasProvider;

        this.processingConsentContractList = new Hashtable<>();
        sn = new Scanner(System.in);
        rand = new Random();
    }

    public String newPurpose( CollectionConsent consent, String processor, BigInteger purpose ){
        BigInteger duration = new BigInteger("36000");
        BigInteger data = new BigInteger( "4294967295");
        String processingConsentContractAddress = "";
        try{
            TransactionReceipt receipt = consent.newPurpose( processor, purpose, data, duration).send();

            if( !processingConsentContractList.containsKey( consent.getContractAddress() ) )
                processingConsentContractList.put( consent.getContractAddress(), new Hashtable<>() );

            //If does not exist a Processing Consent for this Processor
            if( !processingConsentContractList.get( consent.getContractAddress() ).containsKey( processor ) ) {
                //processingConsentContractAddress = consent.getLastPurpose().send();
                processingConsentContractAddress = consent.getProcessingConsentSC( processor ).send();
                processingConsentContractList.get( consent.getContractAddress() ).put( processor, processingConsentContractAddress );

                System.out.println( "Purpose Contract Created"
                        + "\n\tProcessing Purpose: " + CollectionConsentManager.PURPOSE.values()[ purpose.intValue() ]
                        + "\n\tContract address: " + processingConsentContractAddress
                        + "\n\tTransaction Hash: " + receipt.getTransactionHash()
                        + "\n\tConsent SC from which it was created: " + consent.getContractAddress() );
            }
            else{
                processingConsentContractAddress = processingConsentContractList.get( consent.getContractAddress() ).get( processor );
                System.out.println( "New Processing Purpose added to " + processingConsentContractAddress + " contract:"
                        + "\n\tProcessing Purpose: " + CollectionConsentManager.PURPOSE.values()[ purpose.intValue() ]
                        + "\n\tTransaction Hash: " + receipt.getTransactionHash()
                        + "\n\tConsent SC from which it was added: " + consent.getContractAddress() );
            }

            //Add Transaction Gas to total Gas Used
            CollectionConsentManager.getConsentManager(web3j, gasProvider).gasUsed += receipt.getGasUsed().intValue();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
        return processingConsentContractAddress;
    }

    public void grantConsent( ProcessingConsent purpose, BigInteger processingPurpose ){
        try{
            TransactionReceipt receipt = purpose.grantConsent( processingPurpose ).send();
            //Add Transaction Gas to total Gas Used
            CollectionConsentManager.getConsentManager(web3j, gasProvider).gasUsed += receipt.getGasUsed().intValue();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void revokeConsent( ProcessingConsent purpose, BigInteger processingPurpose ){
        try{
            TransactionReceipt receipt = purpose.revokeConsent( processingPurpose ).send();
            //Add Transaction Gas to total Gas Used
            CollectionConsentManager.getConsentManager(web3j, gasProvider).gasUsed += receipt.getGasUsed().intValue();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void modifyData( ProcessingConsent contract, BigInteger purpose, BigInteger data ){
        try {
            TransactionReceipt receipt = contract.modifyData( purpose, data ).send();
            //Add Transaction Gas to total Gas Used
            CollectionConsentManager.getConsentManager(web3j, gasProvider).gasUsed += receipt.getGasUsed().intValue();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public ProcessingConsent selectPurposeContract( String consentContract, ClientTransactionManager transactionManager ){
        if( processingConsentContractList.get( consentContract ).isEmpty() ) return null;
        while (true) {
            System.out.println("Select a Processors purpose Smart Contract:\n" + processingConsentContractList.get( consentContract ).keys().toString() );
            try {
                return ProcessingConsent.load( processingConsentContractList.get( consentContract ).get( sn.next() ),
                        web3j, transactionManager, gasProvider);
            } catch (Exception e) {
                System.out.println("Insert a valid option");
            }
        }
    }

    public boolean checkValidity( ProcessingConsent purpose, BigInteger processingPurpose ){
        try {
            if ( purpose.verify(processingPurpose).send() ){
                System.out.println("Valid");
                return true;
            }
            else{
                System.out.println("Not Valid");
                return false;
            }
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public String getRequestedProcessingPurposes( ProcessingConsent purpose ){
        try{
            String result = "";
            for( BigInteger processingPurpose : (List<BigInteger>) purpose.getPurposes().send() ){
                result += CollectionConsentManager.PURPOSE.values()[ processingPurpose.intValue() ] + "\n";
            }
            return result;
        }
        catch (Exception e){
            System.out.println( "Error:\n");
            e.printStackTrace();
            return "";
        }
    }

    public String getProcessingPurposes( ProcessingConsent purpose ){
        try{
            String result = "";
            for( BigInteger processingPurpose : (List<BigInteger>) purpose.getPurposes().send() ){
                if( purpose.verify( processingPurpose ).send() )
                    result += CollectionConsentManager.PURPOSE.values()[ processingPurpose.intValue() ] + "\n";
            }
            return result;
        }
        catch (Exception e){
            System.out.println( "Error:\n");
            e.printStackTrace();
            return "";
        }
    }

    public void operateOverProcessingConsentSC(ClientTransactionManager transactionManager, ActorsManager actors, String consentContract ){
        int option; String textInput;
        boolean end = false;
        ProcessingConsent purpose = selectPurposeContract( consentContract, transactionManager );
        while (!end) {
            System.out.println("1. Grant Consent");
            System.out.println("2. Revoke Consent");
            System.out.println("3. Valid?");
            System.out.println("4. Get Requested Purposes");
            System.out.println("5. Get Enabled Purposes");
            System.out.println("6. Get Controller");
            System.out.println("7. Get Data Subject");
            System.out.println("8. Get Processor");
            System.out.println("9. Back");
            System.out.println("Select an option: ");
            try {
                option = sn.nextInt();
                switch (option) {
                    case 1:
                        System.out.println("Specify purpose: ");
                        grantConsent( purpose, BigInteger.valueOf( sn.nextInt() ) );
                        break;
                    case 2:
                        System.out.println("Specify purpose: ");
                        revokeConsent( purpose, BigInteger.valueOf( sn.nextInt() ) );
                        break;
                    case 3:
                        System.out.println("Specify purpose: ");
                        checkValidity( purpose, BigInteger.valueOf( sn.nextInt() ) );
                        break;
                    case 4:
                        System.out.println( "Requested Processing Purposes: " +
                            purpose.getPurposes().send().stream().map(
                                    n -> CollectionConsentManager.PURPOSE.values()[ ((BigInteger) n).intValue() ].toString() ).collect( Collectors.toList() ).toString() );
                        System.out.println( "Requested Processing Purposes: " + getRequestedProcessingPurposes( purpose ) );
                        break;
                    case 5:
                        System.out.println( "Processing Purposes: " + getProcessingPurposes( purpose ) );
                        break;
                    case 6:
                        System.out.println( "Controller: " + purpose.getController().send() );
                        break;
                    case 7:
                        System.out.println( "Data Subject: " + purpose.getDataSubject().send() );
                        break;
                    case 8:
                        System.out.println( "Processor: " + purpose.getProcessor().send() );
                        break;
                    case 9:
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

}
