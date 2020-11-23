/**Personal Data Access Control System
 *PurposeManager class
 *
 * Implements the transactions to Purpose SCs in the blockchain.
 * Also implements a menu to interact with all SC methods.
 *
 * Author: Cristòfol Daudén Esmel
 */

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import src.main.java.contracts.Purpose;
import src.main.java.contracts.Consent;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class PurposeManagerTest {

    private static PurposeManagerTest managePurpose = null;
    private Web3j web3j;
    private DefaultGasProvider gasProvider;

    //Consent Address => Processor Address => Purpose SC
    private Hashtable< String , Hashtable< String , String > > purposeContractList;
    Scanner sn;
    Random rand;

    public  static PurposeManagerTest getPurposeManager(Web3j web3j, DefaultGasProvider gasProvider ) {
        if (managePurpose==null) {
            managePurpose = new PurposeManagerTest( web3j, gasProvider );
        }
        return managePurpose;
    }

    private PurposeManagerTest(Web3j web3j, DefaultGasProvider gasProvider ){
        this.web3j = web3j;
        this.gasProvider = gasProvider;

        this.purposeContractList = new Hashtable<>();
        sn = new Scanner(System.in);
        rand = new Random();
    }

    public void newPurpose(Consent consent, String processor, BigInteger purpose ){
        BigInteger duration = new BigInteger("36000");
        try{
            TransactionReceipt receipt = consent.newPurpose( processor, purpose, duration).send();

            if( !purposeContractList.containsKey( consent.getContractAddress() ) )
                purposeContractList.put( consent.getContractAddress(), new Hashtable<>() );

            if( !purposeContractList.get( consent.getContractAddress() ).containsKey( processor ) ) {
                String newPurposeAddress = consent.getLastPurpose().send();
                purposeContractList.get( consent.getContractAddress() ).put( processor, newPurposeAddress );

                System.out.println( "Purpose Contract Created"
                        + "\n\tProcessing Purpose: " + ConsentManager.PURPOSE.values()[ purpose.intValue() ]
                        + "\n\tContract address: " + newPurposeAddress
                        + "\n\tTransaction Hash: " + receipt.getTransactionHash()
                        + "\n\tConsent SC from which it was created: " + consent.getContractAddress() );
            }
            else{
                System.out.println( "New Processing Purpose added to " +
                        purposeContractList.get( consent.getContractAddress() ).get( processor ) + " contract:"
                        + "\n\tProcessing Purpose: " + ConsentManager.PURPOSE.values()[ purpose.intValue() ]
                        + "\n\tTransaction Hash: " + receipt.getTransactionHash()
                        + "\n\tConsent SC from which it was added: " + consent.getContractAddress() );
            }

            //Add Transaction Gas to total Gas Used
            ConsentManager.getConsentManager(web3j, gasProvider).gasUsed += receipt.getGasUsed().intValue();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public String newPurposeR(Consent consent, String processor, BigInteger purpose ){
        BigInteger duration = new BigInteger("360000");
        try{
            TransactionReceipt receipt = consent.newPurpose( processor, purpose, duration).send();
            if( !purposeContractList.containsKey( consent.getContractAddress() ) )
                purposeContractList.put( consent.getContractAddress(), new Hashtable<>() );

            if( !purposeContractList.get( consent.getContractAddress() ).containsKey( processor ) ) {
                String newPurposeAddress = consent.getLastPurpose().send();
                purposeContractList.get(consent.getContractAddress()).put(processor, newPurposeAddress);
            }
            //Add Transaction Gas to total Gas Used
            ConsentManagerTest.getConsentManager(web3j, gasProvider).gasUsed += receipt.getGasUsed().intValue();
            return purposeContractList.get( consent.getContractAddress() ).get( processor );
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public void grantConsent( Purpose purpose ){
        try{
            TransactionReceipt receipt = purpose.grantConsent( ).send();
            //Add Transaction Gas to total Gas Used
            ConsentManagerTest.getConsentManager(web3j, gasProvider).gasUsed += receipt.getGasUsed().intValue();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void revokeConsent( Purpose purpose ){
        try{
            TransactionReceipt receipt = purpose.revokeConsent( ).send();
            //Add Transaction Gas to total Gas Used
            ConsentManagerTest.getConsentManager(web3j, gasProvider).gasUsed += receipt.getGasUsed().intValue();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void grantConsentDS( Purpose purpose ){
        try{
            TransactionReceipt receipt = purpose.grantConsentSubject( ).send();
            //Add Transaction Gas to total Gas Used
            ConsentManagerTest.getConsentManager(web3j, gasProvider).gasUsed += receipt.getGasUsed().intValue();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void revokeConsentDS( Purpose purpose ){
        try{
            TransactionReceipt receipt = purpose.revokeConsentSubject( ).send();
            //Add Transaction Gas to total Gas Used
            ConsentManagerTest.getConsentManager(web3j, gasProvider).gasUsed += receipt.getGasUsed().intValue();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void grantConsentP( Purpose purpose ){
        try{
            TransactionReceipt receipt = purpose.grantConsentProcessor( ).send();
            //Add Transaction Gas to total Gas Used
            ConsentManagerTest.getConsentManager(web3j, gasProvider).gasUsed += receipt.getGasUsed().intValue();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void revokeConsentP( Purpose purpose ){
        try{
            TransactionReceipt receipt = purpose.revokeConsentProcessor( ).send();
            //Add Transaction Gas to total Gas Used
            ConsentManagerTest.getConsentManager(web3j, gasProvider).gasUsed += receipt.getGasUsed().intValue();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public Purpose selectPurposeContract( String consentContract, ClientTransactionManager transactionManager ){
        if( purposeContractList.get( consentContract ).isEmpty() ) return null;
        while (true) {
            System.out.println("Select a Processors purpose Smart Contract:\n" + purposeContractList.get( consentContract ).keys().toString() );
            try {
                return Purpose.load( purposeContractList.get( consentContract ).get( sn.next() ),
                        web3j, transactionManager, gasProvider);
            } catch (Exception e) {
                System.out.println("Insert a valid option");
            }
        }
    }

    public boolean checkValidity( Purpose purpose, BigInteger processingPurpose ){
        try {
            if ( purpose.verify().send() ){
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

    /*
    public String getRequestedProcessingPurposes( Purpose purpose ){
        try{
            String result = "";
            for( BigInteger processingPurpose : (List<BigInteger>) purpose.getPurposes().send() ){
                result += ConsentManager.PURPOSE.values()[ processingPurpose.intValue() ] + "\n";
            }
            return result;
        }
        catch (Exception e){
            System.out.println( "Error:\n");
            e.printStackTrace();
            return "";
        }
    }

    public String getProcessingPurposes( Purpose purpose ){
        try{
            String result = "";
            for( BigInteger processingPurpose : (List<BigInteger>) purpose.getPurposes().send() ){
                if( purpose.verify( processingPurpose ).send() )
                    result += ConsentManager.PURPOSE.values()[ processingPurpose.intValue() ] + "\n";
            }
            return result;
        }
        catch (Exception e){
            System.out.println( "Error:\n");
            e.printStackTrace();
            return "";
        }
    }
    */
}
