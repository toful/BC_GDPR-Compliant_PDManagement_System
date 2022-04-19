/**Personal Data Access Control System
 * ProcessingConsentManagerTest class
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

public class ProcessingConsentManagerTest {

    private static ProcessingConsentManagerTest managePurpose = null;
    private Web3j web3j;
    private DefaultGasProvider gasProvider;

    //Consent Address => Processor Address => Purpose SC
    private Hashtable< String , Hashtable< String , String > > purposeContractList;
    Scanner sn;
    Random rand;

    public  static ProcessingConsentManagerTest getPurposeManager(Web3j web3j, DefaultGasProvider gasProvider ) {
        if (managePurpose==null) {
            managePurpose = new ProcessingConsentManagerTest( web3j, gasProvider );
        }
        return managePurpose;
    }

    private ProcessingConsentManagerTest(Web3j web3j, DefaultGasProvider gasProvider ){
        this.web3j = web3j;
        this.gasProvider = gasProvider;

        this.purposeContractList = new Hashtable<>();
        sn = new Scanner(System.in);
        rand = new Random();
    }

    public void newPurpose(CollectionConsent consent, String processor, BigInteger purpose ){
        BigInteger duration = new BigInteger("36000");
        BigInteger data = new BigInteger( "4294967295");
        String processingConsentContractAddress;
        try{
            TransactionReceipt receipt = consent.newPurpose( processor, purpose, data, duration).send();

            if( !purposeContractList.containsKey( consent.getContractAddress() ) )
                purposeContractList.put( consent.getContractAddress(), new Hashtable<>() );

            if( !purposeContractList.get( consent.getContractAddress() ).containsKey( processor ) ) {
                processingConsentContractAddress = consent.getProcessingConsentSC( processor ).send();
                purposeContractList.get( consent.getContractAddress() ).put( processor, processingConsentContractAddress );

                System.out.println( "Purpose Contract Created"
                        + "\n\tProcessing Purpose: " + CollectionConsentManagerTest.PURPOSE.values()[ purpose.intValue() ]
                        + "\n\tContract address: " + processingConsentContractAddress
                        + "\n\tTransaction Hash: " + receipt.getTransactionHash()
                        + "\n\tConsent SC from which it was created: " + consent.getContractAddress() );
            }
            else{
                processingConsentContractAddress = purposeContractList.get( consent.getContractAddress() ).get( processor );
                System.out.println( "New Processing Purpose added to "
                        + processingConsentContractAddress + " contract:"
                        + "\n\tProcessing Purpose: " + CollectionConsentManagerTest.PURPOSE.values()[ purpose.intValue() ]
                        + "\n\tTransaction Hash: " + receipt.getTransactionHash()
                        + "\n\tConsent SC from which it was added: " + consent.getContractAddress() );
            }

            //Add Transaction Gas to total Gas Used
            CollectionConsentManagerTest.getConsentManager(web3j, gasProvider).gasUsed += receipt.getGasUsed().intValue();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public String newPurposeR(CollectionConsent consent, String processor, BigInteger purpose ){
        BigInteger duration = new BigInteger("360000");
        BigInteger data = new BigInteger( "4294967295");
        try{
            TransactionReceipt receipt = consent.newPurpose( processor, purpose, data, duration ).send();
            if( !purposeContractList.containsKey( consent.getContractAddress() ) )
                purposeContractList.put( consent.getContractAddress(), new Hashtable<>() );

            if( !purposeContractList.get( consent.getContractAddress() ).containsKey( processor ) ) {
                String processingConsentContractAddress = consent.getProcessingConsentSC( processor ).send();
                purposeContractList.get( consent.getContractAddress() ).put( processor, processingConsentContractAddress );
            }
            //Add Transaction Gas to total Gas Used
            CollectionConsentManagerTest.getConsentManager(web3j, gasProvider).gasUsed += receipt.getGasUsed().intValue();
            return purposeContractList.get( consent.getContractAddress() ).get( processor );
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public void grantConsent( ProcessingConsent purpose, BigInteger processingPurpose ){
        try{
            TransactionReceipt receipt = purpose.grantConsent( processingPurpose ).send();
            //Add Transaction Gas to total Gas Used
            CollectionConsentManagerTest.getConsentManager(web3j, gasProvider).gasUsed += receipt.getGasUsed().intValue();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void revokeConsent( ProcessingConsent purpose, BigInteger processingPurpose ){
        try{
            TransactionReceipt receipt = purpose.revokeConsent( processingPurpose ).send();
            //Add Transaction Gas to total Gas Used
            CollectionConsentManagerTest.getConsentManager(web3j, gasProvider).gasUsed += receipt.getGasUsed().intValue();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void modifyData( ProcessingConsent contract, BigInteger purpose, BigInteger data ){
        try {
            TransactionReceipt receipt = contract.modifyData( purpose, data ).send();
            //Add Transaction Gas to total Gas Used
            CollectionConsentManagerTest.getConsentManager(web3j, gasProvider).gasUsed += receipt.getGasUsed().intValue();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public ProcessingConsent selectPurposeContract( String consentContract, ClientTransactionManager transactionManager ){
        if( purposeContractList.get( consentContract ).isEmpty() ) return null;
        while (true) {
            System.out.println("Select a Processors purpose Smart Contract:\n" + purposeContractList.get( consentContract ).keys().toString() );
            try {
                return ProcessingConsent.load( purposeContractList.get( consentContract ).get( sn.next() ),
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
                result += CollectionConsentManagerTest.PURPOSE.values()[ processingPurpose.intValue() ] + "\n";
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
                    result += CollectionConsentManagerTest.PURPOSE.values()[ processingPurpose.intValue() ] + "\n";
            }
            return result;
        }
        catch (Exception e){
            System.out.println( "Error:\n");
            e.printStackTrace();
            return "";
        }
    }
}
