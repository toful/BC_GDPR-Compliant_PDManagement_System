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

public class PurposeManager {

    private static PurposeManager managePurpose = null;
    private Web3j web3j;
    private DefaultGasProvider gasProvider;

    private Hashtable< String ,ArrayList<String> > purposeContractList;
    Scanner sn;
    Random rand;

    public  static PurposeManager getPurposeManager(Web3j web3j, DefaultGasProvider gasProvider ) {
        if (managePurpose==null) {
            managePurpose = new PurposeManager( web3j, gasProvider );
        }
        return managePurpose;
    }

    private PurposeManager(Web3j web3j, DefaultGasProvider gasProvider ){
        this.web3j = web3j;
        this.gasProvider = gasProvider;

        this.purposeContractList = new Hashtable<>();
        sn = new Scanner(System.in);
        rand = new Random();
    }

    public void newPurpose(Consent consent, String processor ){
        BigInteger purpose = new BigInteger( String.valueOf(rand.nextInt( ConsentManager.PURPOSE.values().length )) );
        BigInteger duration = new BigInteger("36000");
        try{
            TransactionReceipt receipt = consent.newPurpose( processor, purpose, duration).send();
            String newPurposeAddress = consent.getLastPurpose().send();
            if( !purposeContractList.containsKey( consent.getContractAddress() ) )
                purposeContractList.put( consent.getContractAddress(), new ArrayList<String>() );
            purposeContractList.get( consent.getContractAddress() ).add( newPurposeAddress );
            System.out.println( "Purpose Contract Created"
                    + "\n\tTransaction Hash: " + receipt.getTransactionHash()
                    + "\n\tContract address: " + newPurposeAddress
                    + "\n\tConsent SC from which it was created: " + consent.getContractAddress() );
            //Add Transaction Gas to total Gas Used
            ConsentManager.getConsentManager(web3j, gasProvider).gasUsed += receipt.getGasUsed().intValue();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public String newPurposeR(Consent consent, String processor, BigInteger purpose ){
        BigInteger duration = new BigInteger("36000");
        try{
            TransactionReceipt receipt = consent.newPurpose( processor, purpose, duration).send();
            String newPurposeAddress = consent.getLastPurpose().send();
            if( !purposeContractList.containsKey( consent.getContractAddress() ) )
                purposeContractList.put( consent.getContractAddress(), new ArrayList<String>() );
            purposeContractList.get( consent.getContractAddress() ).add( newPurposeAddress );
            System.out.println( "Purpose Contract Created"
                    + "\n\tTransaction Hash: " + receipt.getTransactionHash()
                    + "\n\tContract address: " + newPurposeAddress
                    + "\n\tConsent SC from which it was created: " + consent.getContractAddress() );
            //Add Transaction Gas to total Gas Used
            ConsentManager.getConsentManager(web3j, gasProvider).gasUsed += receipt.getGasUsed().intValue();
            return newPurposeAddress;
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }


    public void grantConsent( Purpose purpose ){
        try{
            TransactionReceipt receipt = purpose.grantConsent().send();
            //Add Transaction Gas to total Gas Used
            ConsentManager.getConsentManager(web3j, gasProvider).gasUsed += receipt.getGasUsed().intValue();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void revokeConsent( Purpose purpose ){
        try{
            TransactionReceipt receipt = purpose.revokeConsent().send();
            //Add Transaction Gas to total Gas Used
            ConsentManager.getConsentManager(web3j, gasProvider).gasUsed += receipt.getGasUsed().intValue();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public Purpose selectPurposeContract( String consentContract, ClientTransactionManager transactionManager ){
        if( purposeContractList.get( consentContract ).isEmpty() ) return null;
        while (true) {
            System.out.println("Select a Consent Smart Contract: 0 - " + ( purposeContractList.get( consentContract ).size() - 1));
            try {
                return Purpose.load( purposeContractList.get( consentContract ).get( sn.nextInt() ),
                        web3j, transactionManager, gasProvider);
            } catch (Exception e) {
                System.out.println("Insert a valid option");
            }
        }
    }

    public void checkValidity( Purpose purpose ){
        try{
            if( purpose.verify().send() )
                System.out.println("Valid");
            else
                System.out.println("Not Valid");
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }


    public void operateOverPurpose(ClientTransactionManager transactionManager, ActorsManager actors, String consentContract ){
        int option; String textInput;
        boolean end = false;
        Purpose purpose = selectPurposeContract( consentContract, transactionManager );
        while (!end) {
            System.out.println("1. Grant Consent");
            System.out.println("2. Revoke Consent");
            System.out.println("3. Valid?");
            System.out.println("4. Get Purpose");
            System.out.println("5. Get Controller");
            System.out.println("6. Get Data Subject");
            System.out.println("7. Get Processor");
            System.out.println("8. Back");
            System.out.println("Select an option: ");
            try {
                option = sn.nextInt();
                switch (option) {
                    case 1:
                        grantConsent( purpose );
                        break;
                    case 2:
                        revokeConsent( purpose );
                        break;
                    case 3:
                        checkValidity( purpose );
                        break;
                    case 4:
                        System.out.println( "Processing Purpose: " +
                                ConsentManager.PURPOSE.values()[ purpose.getPurpose().send().intValue() ] );
                        break;
                    case 5:
                        System.out.println( "Controller: " + purpose.getController().send() );
                        break;
                    case 6:
                        System.out.println( "Data Subject: " + purpose.getDataSubject().send() );
                        break;
                    case 7:
                        System.out.println( "Processor: " + purpose.getProcessor().send() );
                        break;
                    case 8:
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
