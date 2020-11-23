package src.main.java.contracts;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.5.16.
 */
@SuppressWarnings("rawtypes")
public class Purpose extends Contract {
    public static final String BINARY = "0x60806040523480156200001157600080fd5b50604051620012f8380380620012f8833981810160405260c08110156200003757600080fd5b8101908080519060200190929190805190602001909291908051906020019092919080519060200190929190805190602001909291908051906020019092919050505085600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550846000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555083600260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508260048111156200014957fe5b600660006101000a81548160ff021916908360048111156200016757fe5b02179055504260038190555081600354016004819055506001811415620001c8576040518060600160405280600160ff168152602001600160ff168152602001600060ff168152506005906003620001c19291906200020f565b5062000203565b6040518060600160405280600160ff168152602001600060ff168152602001600060ff168152506005906003620002019291906200020f565b505b505050505050620002e3565b826003601f016020900481019282156200029d5791602002820160005b838211156200026c57835183826101000a81548160ff021916908360ff16021790555092602001926001016020816000010492830192600103026200022c565b80156200029b5782816101000a81549060ff02191690556001016020816000010492830192600103026200026c565b505b509050620002ac9190620002b0565b5090565b620002e091905b80821115620002dc57600081816101000a81549060ff021916905550600101620002b7565b5090565b90565b61100580620002f36000396000f3fe608060405234801561001057600080fd5b50600436106100ea5760003560e01c8063845606251161008c578063e854066511610066578063e854066514610296578063ed831c70146102a0578063fc735e99146102aa578063ff4c4d54146102cc576100ea565b806384560625146102645780639b3ee54714610282578063e62eba451461028c576100ea565b80633018205f116100c85780633018205f1461014d5780634265c707146101975780634fca47af146101e15780636fac0fe91461025a576100ea565b80630205ad2f146100ef5780630561bac3146100f9578063214d4f7414610143575b600080fd5b6100f76102d6565b005b6101016103ae565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b61014b6103d7565b005b6101556104ae565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b61019f6104d8565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b610258600480360360208110156101f757600080fd5b810190808035906020019064010000000081111561021457600080fd5b82018360208201111561022657600080fd5b8035906020019184600183028401116401000000008311171561024857600080fd5b9091929391929390505050610502565b005b610262610506565b005b61026c6107fb565b6040518082815260200191505060405180910390f35b61028a61081d565b005b6102946108f5565b005b61029e6109cc565b005b6102a8610aa4565b005b6102b2610b7c565b604051808215151515815260200191505060405180910390f35b6102d4610c1e565b005b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff161461037c576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526036815260200180610f446036913960400191505060405180910390fd5b6000600560006003811061038c57fe5b602091828204019190066101000a81548160ff021916908360ff160217905550565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff161461047c576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526033815260200180610f7a6033913960400191505060405180910390fd5b6000600560016003811061048c57fe5b602091828204019190066101000a81548160ff021916908360ff160217905550565b6000600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b6000600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b5050565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff1614806105ae57506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff16145b806106065750600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff16145b61065b576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526024815260200180610fad6024913960400191505060405180910390fd5b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff1614156106e657600160056000600381106106c157fe5b602091828204019190066101000a81548160ff021916908360ff1602179055506107f9565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff161415610770576001600560016003811061074b57fe5b602091828204019190066101000a81548160ff021916908360ff1602179055506107f8565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff1614156107f757600160056002600381106107d657fe5b602091828204019190066101000a81548160ff021916908360ff1602179055505b5b5b565b6000600660009054906101000a900460ff16600481111561081857fe5b905090565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff16146108c3576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526030815260200180610f146030913960400191505060405180910390fd5b600060056002600381106108d357fe5b602091828204019190066101000a81548160ff021916908360ff160217905550565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff161461099a576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526033815260200180610f7a6033913960400191505060405180910390fd5b600160056001600381106109aa57fe5b602091828204019190066101000a81548160ff021916908360ff160217905550565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff1614610a72576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526036815260200180610f446036913960400191505060405180910390fd5b60016005600060038110610a8257fe5b602091828204019190066101000a81548160ff021916908360ff160217905550565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff1614610b4a576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526030815260200180610f146030913960400191505060405180910390fd5b60016005600260038110610b5a57fe5b602091828204019190066101000a81548160ff021916908360ff160217905550565b6000804290506000806005600260038110610b9357fe5b602091828204019190069054906101000a900460ff166005600160038110610bb757fe5b602091828204019190069054906101000a900460ff166005600060038110610bdb57fe5b602091828204019190069054906101000a900460ff16161660ff1614158015610c0657506003548210155b8015610c1457506004548211155b9050809250505090565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff161480610cc657506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff16145b80610d1e5750600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff16145b610d73576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526024815260200180610fad6024913960400191505060405180910390fd5b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff161415610dfe5760006005600060038110610dd957fe5b602091828204019190066101000a81548160ff021916908360ff160217905550610f11565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff161415610e885760006005600160038110610e6357fe5b602091828204019190066101000a81548160ff021916908360ff160217905550610f10565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff161415610f0f5760006005600260038110610eee57fe5b602091828204019190066101000a81548160ff021916908360ff1602179055505b5b5b56fe4f6e6c79207468652070726f636573736f7220697320616c6c6f77656420746f20646f207468697320616374696f6e2e4f6e6c7920746865206461746120436f6e74726f6c6c657220697320616c6c6f77656420746f20646f207468697320616374696f6e2e4f6e6c79207468652064617461205375626a65637420697320616c6c6f77656420746f20646f207468697320616374696f6e2e4163746f72206e6f7420616c6c6f77656420746f20646f207468697320616374696f6e2ea265627a7a723158202fbe79a3214d81b1f848185a710f4567c356f9f4fcff429faef2c9f710a5cdda64736f6c63430005100032";

    public static final String FUNC_VERIFY = "verify";

    public static final String FUNC_MODIFYDATA = "modifyData";

    public static final String FUNC_GETPURPOSE = "getPurpose";

    public static final String FUNC_GETDATASUBJECT = "getDataSubject";

    public static final String FUNC_GETCONTROLLER = "getController";

    public static final String FUNC_GETPROCESSOR = "getProcessor";

    public static final String FUNC_GRANTCONSENT = "grantConsent";

    public static final String FUNC_REVOKECONSENT = "revokeConsent";

    public static final String FUNC_GRANTCONSENTSUBJECT = "grantConsentSubject";

    public static final String FUNC_REVOKECONSENTSUBJECT = "revokeConsentSubject";

    public static final String FUNC_GRANTCONSENTCONTROLLER = "grantConsentController";

    public static final String FUNC_REVOKECONSENTCONTROLLER = "revokeConsentController";

    public static final String FUNC_GRANTCONSENTPROCESSOR = "grantConsentProcessor";

    public static final String FUNC_REVOKECONSENTPROCESSOR = "revokeConsentProcessor";

    protected static final HashMap<String, String> _addresses;

    static {
        _addresses = new HashMap<String, String>();
    }

    @Deprecated
    protected Purpose(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Purpose(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Purpose(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Purpose(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<Boolean> verify() {
        final Function function = new Function(FUNC_VERIFY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> modifyData(String _data) {
        final Function function = new Function(
                FUNC_MODIFYDATA, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_data)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> getPurpose() {
        final Function function = new Function(FUNC_GETPURPOSE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> getDataSubject() {
        final Function function = new Function(FUNC_GETDATASUBJECT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> getController() {
        final Function function = new Function(FUNC_GETCONTROLLER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> getProcessor() {
        final Function function = new Function(FUNC_GETPROCESSOR, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> grantConsent() {
        final Function function = new Function(
                FUNC_GRANTCONSENT, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> revokeConsent() {
        final Function function = new Function(
                FUNC_REVOKECONSENT, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> grantConsentSubject() {
        final Function function = new Function(
                FUNC_GRANTCONSENTSUBJECT, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> revokeConsentSubject() {
        final Function function = new Function(
                FUNC_REVOKECONSENTSUBJECT, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> grantConsentController() {
        final Function function = new Function(
                FUNC_GRANTCONSENTCONTROLLER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> revokeConsentController() {
        final Function function = new Function(
                FUNC_REVOKECONSENTCONTROLLER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> grantConsentProcessor() {
        final Function function = new Function(
                FUNC_GRANTCONSENTPROCESSOR, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> revokeConsentProcessor() {
        final Function function = new Function(
                FUNC_REVOKECONSENTPROCESSOR, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static Purpose load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Purpose(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Purpose load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Purpose(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Purpose load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Purpose(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Purpose load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Purpose(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Purpose> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _controller, String _dataSubject, String _processor, BigInteger _purpose, BigInteger duration, BigInteger defaultTrue) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_controller), 
                new org.web3j.abi.datatypes.Address(_dataSubject), 
                new org.web3j.abi.datatypes.Address(_processor), 
                new org.web3j.abi.datatypes.generated.Uint256(_purpose), 
                new org.web3j.abi.datatypes.generated.Uint256(duration), 
                new org.web3j.abi.datatypes.generated.Uint256(defaultTrue)));
        return deployRemoteCall(Purpose.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<Purpose> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _controller, String _dataSubject, String _processor, BigInteger _purpose, BigInteger duration, BigInteger defaultTrue) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_controller), 
                new org.web3j.abi.datatypes.Address(_dataSubject), 
                new org.web3j.abi.datatypes.Address(_processor), 
                new org.web3j.abi.datatypes.generated.Uint256(_purpose), 
                new org.web3j.abi.datatypes.generated.Uint256(duration), 
                new org.web3j.abi.datatypes.generated.Uint256(defaultTrue)));
        return deployRemoteCall(Purpose.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Purpose> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _controller, String _dataSubject, String _processor, BigInteger _purpose, BigInteger duration, BigInteger defaultTrue) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_controller), 
                new org.web3j.abi.datatypes.Address(_dataSubject), 
                new org.web3j.abi.datatypes.Address(_processor), 
                new org.web3j.abi.datatypes.generated.Uint256(_purpose), 
                new org.web3j.abi.datatypes.generated.Uint256(duration), 
                new org.web3j.abi.datatypes.generated.Uint256(defaultTrue)));
        return deployRemoteCall(Purpose.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Purpose> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _controller, String _dataSubject, String _processor, BigInteger _purpose, BigInteger duration, BigInteger defaultTrue) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_controller), 
                new org.web3j.abi.datatypes.Address(_dataSubject), 
                new org.web3j.abi.datatypes.Address(_processor), 
                new org.web3j.abi.datatypes.generated.Uint256(_purpose), 
                new org.web3j.abi.datatypes.generated.Uint256(duration), 
                new org.web3j.abi.datatypes.generated.Uint256(defaultTrue)));
        return deployRemoteCall(Purpose.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }
}
