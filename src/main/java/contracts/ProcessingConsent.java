package src.main.java.contracts;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
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
 * <p>Generated with web3j version 1.4.1.
 */
@SuppressWarnings("rawtypes")
public class ProcessingConsent extends Contract {
    public static final String BINARY = "0x608060405234801561001057600080fd5b50604051611a61380380611a618339818101604052606081101561003357600080fd5b810190808051906020019092919080519060200190929190805190602001909291905050508273ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff16146100dc576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526036815260200180611a2b6036913960400191505060405180910390fd5b336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555082600260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555081600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555080600360006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050505061183a806101f16000396000f3fe608060405234801561001057600080fd5b50600436106100cf5760003560e01c80633018205f1161008c5780634265c707116100665780634265c7071461032f5780638753367f14610379578063b6f96af2146103bf578063b7a7af99146103c9576100cf565b80633018205f1461024e578063373eed8d146102985780633a728745146102d0576100cf565b80630561bac3146100d457806316a0042c1461011e5780632178543c1461014c5780632a22288e146101985780632a26de85146101de5780632e6473701461020c575b600080fd5b6100dc61040f565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b61014a6004803603602081101561013457600080fd5b8101908080359060200190929190505050610439565b005b6101966004803603608081101561016257600080fd5b81019080803590602001909291908035906020019092919080359060200190929190803590602001909291905050506107e6565b005b6101c4600480360360208110156101ae57600080fd5b8101908080359060200190929190505050610adf565b604051808215151515815260200191505060405180910390f35b61020a600480360360208110156101f457600080fd5b8101908080359060200190929190505050610b1d565b005b6102386004803603602081101561022257600080fd5b8101908080359060200190929190505050610eca565b6040518082815260200191505060405180910390f35b610256610eea565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6102ce600480360360408110156102ae57600080fd5b810190808035906020019092919080359060200190929190505050610f14565b005b6102d8610fd9565b6040518080602001828103825283818151815260200191508051906020019060200280838360005b8381101561031b578082015181840152602081019050610300565b505050509050019250505060405180910390f35b610337611031565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6103a56004803603602081101561038f57600080fd5b810190808035906020019092919050505061105b565b604051808215151515815260200191505060405180910390f35b6103c76111dd565b005b6103f5600480360360208110156103df57600080fd5b81019080803590602001909291905050506114c0565b604051808215151515815260200191505060405180910390f35b6000600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff1614806104e25750600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff16145b8061053a5750600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff16145b61058f576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260248152602001806117e26024913960400191505060405180910390fd5b6004600082815260200190815260200160002060000160009054906101000a900460ff16610608576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602381526020018061173a6023913960400191505060405180910390fd5b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff1614156106a75760006004600083815260200190815260200160002060040160006003811061068257fe5b602091828204019190066101000a81548160ff021916908360ff1602179055506107e3565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff1614156107465760006004600083815260200190815260200160002060040160016003811061072157fe5b602091828204019190066101000a81548160ff021916908360ff1602179055506107e2565b600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff1614156107e1576000600460008381526020019081526020016000206004016002600381106107c057fe5b602091828204019190066101000a81548160ff021916908360ff1602179055505b5b5b50565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161461088b576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252606381526020018061177f6063913960800191505060405180910390fd5b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff1614610931576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252603b8152602001806116aa603b913960400191505060405180910390fd5b6004600085815260200190815260200160002060000160009054906101000a900460ff16156109ab576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602281526020018061175d6022913960400191505060405180910390fd5b6109b36115bd565b60018214156109ea576040518060600160405280600160ff168152602001600160ff168152602001600060ff168152509050610a14565b6040518060600160405280600160ff168152602001600060ff168152602001600060ff1681525090505b6040518060a001604052806001151581526020018581526020014281526020018442018152602001828152506004600087815260200190815260200160002060008201518160000160006101000a81548160ff021916908315150217905550602082015181600101556040820151816002015560608201518160030155608082015181600401906003610aa89291906115df565b5090505060058590806001815401808255809150509060018203906000526020600020016000909192909190915055505050505050565b60006004600083815260200190815260200160002060000160009054906101000a900460ff1615610b135760019050610b18565b600090505b919050565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff161480610bc65750600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff16145b80610c1e5750600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff16145b610c73576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260248152602001806117e26024913960400191505060405180910390fd5b6004600082815260200190815260200160002060000160009054906101000a900460ff16610cec576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602381526020018061173a6023913960400191505060405180910390fd5b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff161415610d8b57600160046000838152602001908152602001600020600401600060038110610d6657fe5b602091828204019190066101000a81548160ff021916908360ff160217905550610ec7565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff161415610e2a57600160046000838152602001908152602001600020600401600160038110610e0557fe5b602091828204019190066101000a81548160ff021916908360ff160217905550610ec6565b600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff161415610ec557600160046000838152602001908152602001600020600401600260038110610ea457fe5b602091828204019190066101000a81548160ff021916908360ff1602179055505b5b5b50565b600060046000838152602001908152602001600020600101549050919050565b6000600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff1614610fba576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260338152602001806116e56033913960400191505060405180910390fd5b8060046000848152602001908152602001600020600101819055505050565b6060600580548060200260200160405190810160405280929190818152602001828054801561102757602002820191906000526020600020905b815481526020019060010190808311611013575b5050505050905090565b6000600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b60006004600083815260200190815260200160002060000160009054906101000a900460ff166110d6576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602381526020018061173a6023913960400191505060405180910390fd5b60004290506000806004600086815260200190815260200160002060040160026003811061110057fe5b602091828204019190069054906101000a900460ff166004600087815260200190815260200160002060040160016003811061113857fe5b602091828204019190069054906101000a900460ff166004600088815260200190815260200160002060040160006003811061117057fe5b602091828204019190069054906101000a900460ff16161660ff16141580156111af575060046000858152602001908152602001600020600201548210155b80156111d1575060046000858152602001908152602001600020600301548211155b90508092505050919050565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff1614806112865750600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff16145b806112de5750600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff16145b611333576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260248152602001806117e26024913960400191505060405180910390fd5b600060058054905011611391576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260228152602001806117186022913960400191505060405180910390fd5b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff1614156113f6576113f1600061153f565b6114be565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff16141561145b57611456600161153f565b6114bd565b600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff1614156114bc576114bb600261153f565b5b5b5b565b6000806004600084815260200190815260200160002060000160009054906101000a900460ff166114f45760009050611536565b60006004600085815260200190815260200160002060040160016003811061151857fe5b602091828204019190069054906101000a900460ff1660ff16141590505b80915050919050565b60008090505b6005805490508110156115b9576000600460006005848154811061156557fe5b90600052602060002001548152602001908152602001600020600401836003811061158c57fe5b602091828204019190066101000a81548160ff021916908360ff1602179055508080600101915050611545565b5050565b6040518060600160405280600390602082028038833980820191505090505090565b826003601f016020900481019282156116685791602002820160005b8382111561163957835183826101000a81548160ff021916908360ff16021790555092602001926001016020816000010492830192600103026115fb565b80156116665782816101000a81549060ff0219169055600101602081600001049283019260010302611639565b505b5090506116759190611679565b5090565b6116a691905b808211156116a257600081816101000a81549060ff02191690555060010161167f565b5090565b9056fe4f6e6c7920636f6e74726f6c6c65722063616e206164642061206e65772050726f63657373696e6720707572706f736520746f20746869732053434f6e6c79207468652064617461205375626a65637420697320616c6c6f77656420746f20646f207468697320616374696f6e2e4e6f2050726f63657373696e6720707572706f736573206f6e20746869732053432e50726f63657373696e6720707572706f736520646f6573206e6f74206578697374732e50726f63657373696e6720707572706f736520616c7265616479206578697374732e4e65772050726f63657373696e6720707572706f73652063616e206f6e6c792062652061646465642066726f6d2074686520436f6e73656e742053432066726f6d207768696368207468697320507572706f73652053432077617320637265617465644163746f72206e6f7420616c6c6f77656420746f20646f207468697320616374696f6e2ea265627a7a723158204a9db163860c526e6c372d728010ac74249128acb1aa04cbfc627abe6e13ada064736f6c634300051000325472616e73616374696f6e2073656e64657220646f6573206e6f74206d617463687420776974682074686520436f6e74726f6c6c6572";

    public static final String FUNC_NEWPURPOSE = "newPurpose";

    public static final String FUNC_MODIFYDATA = "modifyData";

    public static final String FUNC_VERIFY = "verify";

    public static final String FUNC_VERIFYDS = "verifyDS";

    public static final String FUNC_EXISTSPURPOSE = "existsPurpose";

    public static final String FUNC_GETPURPOSES = "getPurposes";

    public static final String FUNC_GETDATASUBJECT = "getDataSubject";

    public static final String FUNC_GETCONTROLLER = "getController";

    public static final String FUNC_GETPROCESSOR = "getProcessor";

    public static final String FUNC_GETDATAPURPOSE = "getDataPurpose";

    public static final String FUNC_GRANTCONSENT = "grantConsent";

    public static final String FUNC_REVOKECONSENT = "revokeConsent";

    public static final String FUNC_REVOKEALLCONSENTS = "revokeAllConsents";

    protected static final HashMap<String, String> _addresses;

    static {
        _addresses = new HashMap<String, String>();
    }

    @Deprecated
    protected ProcessingConsent(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ProcessingConsent(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected ProcessingConsent(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ProcessingConsent(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> newPurpose(BigInteger _purpose, BigInteger data, BigInteger duration, BigInteger defaultTrue) {
        final Function function = new Function(
                FUNC_NEWPURPOSE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_purpose), 
                new org.web3j.abi.datatypes.generated.Uint256(data), 
                new org.web3j.abi.datatypes.generated.Uint256(duration), 
                new org.web3j.abi.datatypes.generated.Uint256(defaultTrue)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> modifyData(BigInteger _purpose, BigInteger _data) {
        final Function function = new Function(
                FUNC_MODIFYDATA, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_purpose), 
                new org.web3j.abi.datatypes.generated.Uint256(_data)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> verify(BigInteger _purpose) {
        final Function function = new Function(FUNC_VERIFY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_purpose)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> verifyDS(BigInteger _purpose) {
        final Function function = new Function(FUNC_VERIFYDS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_purpose)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> existsPurpose(BigInteger _purpose) {
        final Function function = new Function(FUNC_EXISTSPURPOSE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_purpose)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<List> getPurposes() {
        final Function function = new Function(FUNC_GETPURPOSES, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
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

    public RemoteFunctionCall<BigInteger> getDataPurpose(BigInteger _purpose) {
        final Function function = new Function(FUNC_GETDATAPURPOSE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_purpose)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> grantConsent(BigInteger _purpose) {
        final Function function = new Function(
                FUNC_GRANTCONSENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_purpose)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> revokeConsent(BigInteger _purpose) {
        final Function function = new Function(
                FUNC_REVOKECONSENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_purpose)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> revokeAllConsents() {
        final Function function = new Function(
                FUNC_REVOKEALLCONSENTS, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static ProcessingConsent load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ProcessingConsent(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static ProcessingConsent load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ProcessingConsent(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static ProcessingConsent load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new ProcessingConsent(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ProcessingConsent load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ProcessingConsent(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ProcessingConsent> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _controller, String _dataSubject, String _processor) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_controller), 
                new org.web3j.abi.datatypes.Address(_dataSubject), 
                new org.web3j.abi.datatypes.Address(_processor)));
        return deployRemoteCall(ProcessingConsent.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<ProcessingConsent> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _controller, String _dataSubject, String _processor) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_controller), 
                new org.web3j.abi.datatypes.Address(_dataSubject), 
                new org.web3j.abi.datatypes.Address(_processor)));
        return deployRemoteCall(ProcessingConsent.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<ProcessingConsent> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _controller, String _dataSubject, String _processor) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_controller), 
                new org.web3j.abi.datatypes.Address(_dataSubject), 
                new org.web3j.abi.datatypes.Address(_processor)));
        return deployRemoteCall(ProcessingConsent.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<ProcessingConsent> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _controller, String _dataSubject, String _processor) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_controller), 
                new org.web3j.abi.datatypes.Address(_dataSubject), 
                new org.web3j.abi.datatypes.Address(_processor)));
        return deployRemoteCall(ProcessingConsent.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }
}
