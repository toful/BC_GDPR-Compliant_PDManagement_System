/**Personal Data Access Control System
 *MainClass
 *
 * Author: Cristòfol Daudén Esmel
 */

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import src.main.java.contracts.Consent;
import src.main.java.contracts.Purpose;

import java.io.FileWriter;
import java.math.BigInteger;
import java.util.*;


public class Test {

    private static Web3j web3j;
    private static ClientTransactionManager transactionManager = null;
    private static DefaultGasProvider gasProvider;
    private static ConsentManagerTest consentManager;
    private static PurposeManagerTest purposeManager;
    private static ActorsManager actors;
    private static FileWriter csvWriter;

    public static void main(String args[])  throws Exception {

        csvWriter = new FileWriter("results/results.csv", true);
        csvWriter.append( "SCsVersion,Method,Iterations,TotalGasUsed,AverageGasUsed\n");

        //create a Web3j instance to our local Ethereum node (Ganache)
        web3j = Web3j.build( new HttpService("http://localhost:8545") );
        gasProvider = new DefaultGasProvider();
        consentManager = ConsentManagerTest.getConsentManager(web3j, gasProvider);
        purposeManager = PurposeManagerTest.getPurposeManager(web3j, gasProvider);

        //Getting the accounts generated by Ganache and Setting up controller and data subject actors
        actors = new ActorsManager( web3j.ethAccounts().send().getAccounts().toArray(new String[0]), web3j );

        evaluateConsentContract( 10 );
        evaluatePurposeContract( 10 );
        evaluateRevokeConsentPurpose();
        evaluateRevokeConsentProcessor();

        csvWriter.flush();
        csvWriter.close();
        web3j.shutdown();
    }

    public static void evaluateConsentContract(int numIterations) throws Exception{
        Consent consent;
        List<Consent> consentDSlist = new ArrayList<>();
        //New consent
        System.out.println( "Creating new Consent SC");
        consentManager.gasUsed = 0;
        for( int i = 0; i < numIterations; i++ ){
            consent = consentManager.newConsentContract( actors.transactionManagerController, actors.dataSubject, actors.dataRecipients );
            consentDSlist.add( Consent.load( consent.getContractAddress(),
                    web3j, actors.transactionManagerDataSubject,gasProvider ) );
        }
        csvWriter.append( "1SCProcessorPurpose,NewConsentSC," + numIterations + "," +
                consentManager.gasUsed + "," + consentManager.gasUsed/numIterations + "\n" );
        //Grant Consent
        System.out.println( "Granting consent on SC");
        consentManager.gasUsed = 0;
        for( Consent consentDS: consentDSlist ){
            consentManager.grantConsent( consentDS );
        }
        csvWriter.append( "1SCProcessorPurpose,GrantConsent_ConsentSC," + numIterations + "," +
                consentManager.gasUsed + "," + consentManager.gasUsed/numIterations + "\n" );
        //Revoke Consent
        System.out.println( "Revoking consent on SC");
        consentManager.gasUsed = 0;
        for( Consent consentDS: consentDSlist ){
            consentManager.revokeConsent( consentDS );
        }
        csvWriter.append( "1SCProcessorPurpose,RevokeConsent_ConsentSC," + numIterations + "," +
                consentManager.gasUsed + "," + consentManager.gasUsed/numIterations + "\n" );
    }


    public static void evaluatePurposeContract(int numIterations) throws Exception{
        Consent consent;
        List<Consent> consentClist = new ArrayList<>();
        List<Consent> consentDSlist = new ArrayList<>();
        List<Purpose> purposeDSlist = new ArrayList<>();
        List<Purpose> purposePlist = new ArrayList<>();
        String aux;
        //New consent
        System.out.println("EVALUATING PURPOSE SMART CONTRACT");
        System.out.println( "Creating new Consent SC");
        //consentManager.gasUsed = 0;
        for( int i = 0; i < numIterations; i++ ){
            consent = consentManager.newConsentContract( actors.transactionManagerController, actors.dataSubject, actors.dataRecipients );
            consentClist.add( consent );
            consentDSlist.add( Consent.load( consent.getContractAddress(),
                    web3j, actors.transactionManagerDataSubject,gasProvider ) );
        }
        //csvWriter.append( "1SCProcessorPurpose,NewConsentSC," + numIterations + "," + consentManager.gasUsed + "," + consentManager.gasUsed/numIterations + "\n" );
        //Grant Consent
        System.out.println( "Granting consent on SC");
        //consentManager.gasUsed = 0;
        for( Consent consentDS: consentDSlist ){
            consentManager.grantConsent( consentDS );
        }

        //New Purposes
        List<BigInteger> purposes = new ArrayList<BigInteger>();
        purposes.add(new BigInteger("0"));
        purposes.add(new BigInteger("1"));
        purposes.add(new BigInteger("2"));
        purposes.add(new BigInteger("3"));
        System.out.println( "Add " + purposes.size() + " more Purposes for each processor on each Consent SC. " +
                "\n\tNum Processors: " + actors.processors.length  + " Num Consent SC: " + consentClist.size() );
        consentManager.gasUsed = 0;
        for( int i=0; i < actors.processors.length; i++ ){
            for (Consent consentC : consentClist) {
                for( BigInteger purpose : purposes ){
                    aux = purposeManager.newPurposeR( consentC, actors.processors[i], purpose );
                    purposeDSlist.add( Purpose.load( aux,
                            web3j, actors.transactionManagerDataSubject, gasProvider ) );
                    purposePlist.add( Purpose.load( aux,
                            web3j, actors.transactionManagerProcessors[i], gasProvider ) );
                }
            }
        }
        csvWriter.append( "1SCProcessorPurpose,NewPurposeProcessor," +
                actors.processors.length * consentClist.size() * purposes.size() + "," +
                consentManager.gasUsed + "," +
                consentManager.gasUsed/(actors.processors.length * consentClist.size() * purposes.size()) + "\n" );

        /////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////
        //DS Grants Consent Purpose
        System.out.println( "DS Grants consent on all Purpose SC");
        consentManager.gasUsed = 0;
        for( Purpose purposeDS : purposeDSlist ) {
                purposeManager.grantConsent( purposeDS );
        }
        csvWriter.append( "1SCProcessorPurpose,DS_GrantConsent_PurposeSC," +
                ( purposeDSlist.size() ) + "," +
                consentManager.gasUsed + "," +
                consentManager.gasUsed/( purposeDSlist.size() ) + "\n" );

        //Processor Grants Consent Purpose
        System.out.println( "Processor Grants consent on all Purpose SC");
        consentManager.gasUsed = 0;
        for( Purpose purposeP : purposePlist ) {
                purposeManager.grantConsent( purposeP );
        }
        csvWriter.append( "1SCProcessorPurpose,P_GrantConsent_PurposeSC," +
                ( purposeDSlist.size() ) + "," +
                consentManager.gasUsed + "," +
                consentManager.gasUsed/( purposePlist.size() ) + "\n" );
        /////////////////////////////////////////////////////////////////////////////////////////////
        //DS Revokes Consent Purpose
        System.out.println( "DS Revokes consent on all Purpose SC");
        consentManager.gasUsed = 0;
        for( Purpose purposeDS : purposeDSlist ) {
                purposeManager.revokeConsent( purposeDS );
        }
        csvWriter.append( "1SCProcessorPurpose,DS_RevokeConsent_PurposeSC," +
                ( purposeDSlist.size() ) + "," +
                consentManager.gasUsed + "," +
                consentManager.gasUsed/( purposeDSlist.size() ) + "\n" );

        //Processor Revokes Consent Purpose
        System.out.println( "Processor Revokes consent on all Purpose SC");
        consentManager.gasUsed = 0;
        for( Purpose purposeP : purposePlist ) {
                purposeManager.revokeConsent( purposeP );
        }
        csvWriter.append( "1SCProcessorPurpose,P_RevokeConsent_PurposeSC," +
                ( purposeDSlist.size() ) + "," +
                consentManager.gasUsed + "," +
                consentManager.gasUsed/( purposePlist.size() ) + "\n" );
        /////////////////////////////////////////////////////////////////////////////////////////////
        //Specific grant and revoke consent
        /////////////////////////////////////////////////////////////////////////////////////////////
        //DS Grants Consent Purpose
        System.out.println( "DS Grants consent on all Purpose SC");
        consentManager.gasUsed = 0;
        for( Purpose purposeDS : purposeDSlist ) {
                purposeManager.grantConsentDS( purposeDS );
        }
        csvWriter.append( "1SCProcessorPurpose,Specific_DS_GrantConsent_PurposeSC," +
                ( purposeDSlist.size() ) + "," +
                consentManager.gasUsed + "," +
                consentManager.gasUsed/( purposeDSlist.size() ) + "\n" );

        //Processor Grants Consent Purpose
        System.out.println( "Processor Grants consent on all Purpose SC");
        consentManager.gasUsed = 0;
        for( Purpose purposeP : purposePlist ) {
                purposeManager.grantConsentP( purposeP );
        }
        csvWriter.append( "1SCProcessorPurpose,Specific_P_GrantConsent_PurposeSC," +
                ( purposeDSlist.size() ) + "," +
                consentManager.gasUsed + "," +
                consentManager.gasUsed/( purposePlist.size() ) + "\n" );
        /////////////////////////////////////////////////////////////////////////////////////////////
        //DS Revokes Consent Purpose
        System.out.println( "DS Revokes consent on all Purpose SC");
        consentManager.gasUsed = 0;
        for( Purpose purposeDS : purposeDSlist ) {
                purposeManager.revokeConsentDS( purposeDS );
        }
        csvWriter.append( "1SCProcessorPurpose,Specific_DS_RevokeConsent_PurposeSC," +
                ( purposeDSlist.size() ) + "," +
                consentManager.gasUsed + "," +
                consentManager.gasUsed/( purposeDSlist.size() ) + "\n" );

        //Processor Revokes Consent Purpose
        System.out.println( "Processor Revokes consent on all Purpose SC");
        consentManager.gasUsed = 0;
        for( Purpose purposeP : purposePlist ) {
                purposeManager.revokeConsentP( purposeP );
        }
        csvWriter.append( "1SCProcessorPurpose,Specific_P_RevokeConsent_PurposeSC," +
                ( purposeDSlist.size() ) + "," +
                consentManager.gasUsed + "," +
                consentManager.gasUsed/( purposePlist.size() ) + "\n" );

    }


    public static void evaluateRevokeConsentPurpose() throws Exception{
        //Test Revoke Consent Purpose
        String purpose;
        ArrayList<Purpose> purposesListDS = new ArrayList<>();
        ArrayList<Purpose> purposesListP = new ArrayList<>();
        ArrayList<Purpose> revokingPurposesListDS = new ArrayList<>();
        ArrayList<Purpose> revokingPurposesListP = new ArrayList<>();
        List<BigInteger> processingPurposes = new ArrayList<BigInteger>();
        processingPurposes.add(new BigInteger("1"));
        processingPurposes.add(new BigInteger("2"));
        processingPurposes.add(new BigInteger("3"));
        BigInteger revokingPurpose = new BigInteger("0");

        System.out.println("\nEVALUATING Revoke Purpose");

        //New consent
        Consent consent = consentManager.newConsentContract( actors.transactionManagerController, actors.dataSubject, actors.dataRecipients );

        //DS grants consent
        Consent consentDS = Consent.load( consent.getContractAddress(), web3j, actors.transactionManagerDataSubject, gasProvider);
        consentManager.grantConsent( consentDS );

        //New Purposes and Processors and DS grant consent

        for( int i=0; i < actors.processors.length; i++ ){
            purpose = purposeManager.newPurposeR( consent, actors.processors[i], revokingPurpose );
            revokingPurposesListDS.add( Purpose.load( purpose, web3j, actors.transactionManagerDataSubject, gasProvider) );
            revokingPurposesListP.add( Purpose.load( purpose, web3j, actors.transactionManagerProcessors[i], gasProvider) );
            purposeManager.grantConsent( revokingPurposesListDS.get( revokingPurposesListDS.size()-1 ) );
            purposeManager.grantConsent( revokingPurposesListP.get( revokingPurposesListP.size()-1 ) );
            for( BigInteger processingPurpose : processingPurposes ){
                purpose = purposeManager.newPurposeR( consent, actors.processors[i], processingPurpose );
                purposesListDS.add( Purpose.load( purpose, web3j, actors.transactionManagerDataSubject, gasProvider) );
                purposesListP.add( Purpose.load( purpose, web3j, actors.transactionManagerProcessors[i], gasProvider) );
                purposeManager.grantConsent( purposesListDS.get( purposesListDS.size()-1 ) );
                purposeManager.grantConsent( purposesListP.get( purposesListP.size()-1 ) );
            }
        }

        //Revoke consent for all purposes SC with specific purpose
        System.out.println( "Revoking consent of purpose: " +
                ConsentManager.PURPOSE.values()[ revokingPurpose.intValue() ] );
        consentManager.gasUsed = 0;
        consentManager.revokeConsentPurpose( consentDS, revokingPurpose );
        csvWriter.append( "1SCProcessorPurpose,RevokeConsentPurpose," +
                consentDS.getAllProcessors().send().size() + "," +
                consentManager.gasUsed + "," +
                consentManager.gasUsed/( consentDS.getAllProcessors().send().size() ) + "\n" );

        /*
        //We can also get the required purpose SCs as
        revokingPurposesListDS = consentManager.getAllProcessorsPurposeSC(
                consentDS, actors.transactionManagerDataSubject, revokingPurpose );
        */
        //Revoke consent for all purposes SC with specific purpose V2
        for( Purpose purposeDS : revokingPurposesListDS ){
            purposeManager.grantConsent( purposeDS );
        }
        System.out.println( "Revoking consent of purpose: " +
                ConsentManager.PURPOSE.values()[ revokingPurpose.intValue() ] );
        consentManager.gasUsed = 0;
        for( Purpose purposeDS : revokingPurposesListDS ){
            purposeManager.revokeConsent( purposeDS );
        }
        csvWriter.append( "1SCProcessorPurpose,RevokeConsentPurpose_locally," +
                revokingPurposesListDS.size() + "," +
                consentManager.gasUsed + "," +
                consentManager.gasUsed/( revokingPurposesListDS.size() ) + "\n" );


        //Revoke consent for all purposes SC with specific purpose V2
        for( Purpose purposeDS : revokingPurposesListDS ){
            purposeManager.grantConsent( purposeDS );
        }
        System.out.println( "Revoking consent of purpose: " +
                ConsentManager.PURPOSE.values()[ revokingPurpose.intValue() ] );
        consentManager.gasUsed = 0;
        for( Purpose purposeDS : revokingPurposesListDS ){
            purposeManager.revokeConsentDS( purposeDS );
        }
        csvWriter.append( "1SCProcessorPurpose,RevokeConsentPurpose_locally_specific," +
                revokingPurposesListDS.size() + "," +
                consentManager.gasUsed + "," +
                consentManager.gasUsed/( revokingPurposesListDS.size() ) + "\n" );

    }


    public static void evaluateRevokeConsentProcessor() throws Exception{
        //Test Revoke Consent Purpose
        String purpose;
        ArrayList<Purpose> purposesListDS = new ArrayList<>();
        ArrayList<Purpose> purposesListP = new ArrayList<>();
        ArrayList<Purpose> revokingProcessorsListDS = new ArrayList<>();
        ArrayList<Purpose> revokingProcessorsListP = new ArrayList<>();
        List<BigInteger> processingPurposes = new ArrayList<BigInteger>();
        processingPurposes.add(new BigInteger("0"));
        processingPurposes.add(new BigInteger("1"));
        processingPurposes.add(new BigInteger("2"));
        processingPurposes.add(new BigInteger("3"));

        System.out.println("EVALUATING Revoke Processor");

        //New consent
        Consent consent = consentManager.newConsentContract( actors.transactionManagerController, actors.dataSubject, actors.dataRecipients );

        //DS grants consent
        Consent consentDS = Consent.load( consent.getContractAddress(), web3j, actors.transactionManagerDataSubject, gasProvider);
        consentManager.grantConsent( consentDS );

        //New Purposes and Processors and DS grant consent
        for( BigInteger processingPurpose : processingPurposes ){
            purpose = purposeManager.newPurposeR( consent, actors.processors[0], processingPurpose );
            revokingProcessorsListDS.add( Purpose.load( purpose, web3j, actors.transactionManagerDataSubject, gasProvider) );
            revokingProcessorsListP.add( Purpose.load( purpose, web3j, actors.transactionManagerProcessors[0], gasProvider) );
            purposeManager.grantConsent( revokingProcessorsListDS.get( revokingProcessorsListDS.size()-1 ) );
            purposeManager.grantConsent( revokingProcessorsListDS.get( revokingProcessorsListP.size()-1 ) );
        }
        for( int i=1; i < actors.processors.length; i++ ){
            for( BigInteger processingPurpose : processingPurposes ){
                purpose = purposeManager.newPurposeR( consent, actors.processors[i], processingPurpose );
                purposesListDS.add( Purpose.load( purpose, web3j, actors.transactionManagerDataSubject, gasProvider) );
                purposesListP.add( Purpose.load( purpose, web3j, actors.transactionManagerProcessors[i], gasProvider) );
                purposeManager.grantConsent( purposesListDS.get( purposesListDS.size()-1 ) );
                purposeManager.grantConsent( purposesListP.get( purposesListP.size()-1 ) );
            }
        }


        //Revoke consent for all purposes SC to a specific processor
        System.out.println( "Revoking consent to a processor: " + actors.processors[0] );
        consentManager.gasUsed = 0;
        consentManager.revokeConsentProcessor( consentDS, actors.processors[0] );
        csvWriter.append( "1SCProcessorPurpose,RevokeConsentProcessor," +
                consentManager.getAllPurposesProcessor( consentDS, actors.processors[0] ).size() + "," +
                consentManager.gasUsed + "," +
                consentManager.gasUsed / ( consentManager.getAllPurposesProcessor( consentDS, actors.processors[0] ).size() ) + "\n" );

        //Revoke consent for all purposes SC to a specific processor V2
        for( Purpose processingPurpose : revokingProcessorsListDS ){
            purposeManager.grantConsent( processingPurpose );
        }
        System.out.println( "Revoking consent to a processor: " + actors.processors[0] );
        consentManager.gasUsed = 0;
        for( Purpose processingPurpose : revokingProcessorsListDS ){
            purposeManager.revokeConsent( processingPurpose );
        }
        csvWriter.append( "1SCProcessorPurpose,RevokeConsentProcessor_locally," +
                revokingProcessorsListDS.size() + "," +
                consentManager.gasUsed + "," +
                consentManager.gasUsed / revokingProcessorsListDS.size() + "\n" );

        //Revoke consent for all purposes SC to a specific processor V3
        for( Purpose processingPurpose : revokingProcessorsListDS ){
            purposeManager.grantConsent( processingPurpose );
        }
        System.out.println( "Revoking consent to a processor: " + actors.processors[0] );
        consentManager.gasUsed = 0;
        for( Purpose processingPurpose : revokingProcessorsListDS ){
            purposeManager.revokeConsentDS( processingPurpose );
        }
        csvWriter.append( "1SCProcessorPurpose,RevokeConsentProcessor_locally_specific," +
                revokingProcessorsListDS.size() + "," +
                consentManager.gasUsed + "," +
                consentManager.gasUsed / revokingProcessorsListDS.size() + "\n" );

    }


}
