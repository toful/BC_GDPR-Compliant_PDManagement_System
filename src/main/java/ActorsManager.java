/**Personal Data Access Control System
 * ActorsManager class
 *
 * Simple class that holds some actors to interact with SC using this system prototype
 *
 * Author: Cristòfol Daudén Esmel
 */

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ActorsManager {

    String controller;
    String dataSubject;
    List<String> dataRecipients;
    String[] processors;
    ClientTransactionManager transactionManagerController;
    ClientTransactionManager transactionManagerDataSubject;
    ClientTransactionManager[] transactionManagerProcessors;

    Web3j web3j;

    public ActorsManager(String[] accounts, Web3j web3j){
        controller = accounts[0];
        dataSubject = accounts[1];
        dataRecipients = Arrays.asList( Arrays.copyOfRange(accounts, 2, 5) );
        processors = Arrays.copyOfRange(accounts, 5, 10);

        this.web3j = web3j;
        transactionManagerController = new ClientTransactionManager( web3j, controller );
        transactionManagerDataSubject = new ClientTransactionManager( web3j, dataSubject );
        transactionManagerProcessors = Arrays.stream(processors).map( n -> new ClientTransactionManager( web3j, n ) ).toArray( ClientTransactionManager[]::new );
    }

    public void getBalance( String account) throws IOException {
        //Get balance of an account.
        EthGetBalance balanceWei = web3j.ethGetBalance( account, DefaultBlockParameterName.LATEST).send();
        System.out.println("Balance in ether: " + Convert.fromWei(balanceWei.getBalance().toString(), Convert.Unit.ETHER) );
    }
}
