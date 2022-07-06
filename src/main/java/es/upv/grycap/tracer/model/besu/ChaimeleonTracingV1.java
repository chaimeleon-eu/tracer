package es.upv.grycap.tracer.model.besu;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.TypeReference;
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
 * <p>Generated with web3j version 4.8.7.
 */
@SuppressWarnings("rawtypes")
public class ChaimeleonTracingV1 extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b50600380546001600160a01b031916331790556102ac806100326000396000f3fe608060405234801561001057600080fd5b50600436106100365760003560e01c80633a2fa8071461003b57806384bab2f614610055575b600080fd5b6100436100fd565b60408051918252519081900360200190f35b6100fb6004803603602081101561006b57600080fd5b81019060208101813564010000000081111561008657600080fd5b82018360208201111561009857600080fd5b803590602001918460018302840111640100000000831117156100ba57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610103945050505050565b005b60005490565b6003546001600160a01b0316331461011a57600080fd5b6040805160608101825242808252602080830191825292820184815260008054600181018255908052835160039091027f290decd9548b62a8d60345a988386fc84ba6bc95484008f6362f93160ef3e563810191825592517f290decd9548b62a8d60345a988386fc84ba6bc95484008f6362f93160ef3e56484015590518051939491936101cf937f290decd9548b62a8d60345a988386fc84ba6bc95484008f6362f93160ef3e565019291909101906101d5565b50505050565b828054600181600116156101000203166002900490600052602060002090601f01602090048101928261020b5760008555610251565b82601f1061022457805160ff1916838001178555610251565b82800160010185558215610251579182015b82811115610251578251825591602001919060010190610236565b5061025d929150610261565b5090565b5b8082111561025d576000815560010161026256fea2646970667358221220303829d793cdd32fe378c700eda3bc6acd13d642ff6cd6c81e89ccb3d604083164736f6c63430007060033";

    public static final String FUNC_ADDTRACE = "addTrace";

    public static final String FUNC_GETTRACESCOUNT = "getTracesCount";

    @Deprecated
    protected ChaimeleonTracingV1(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ChaimeleonTracingV1(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected ChaimeleonTracingV1(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ChaimeleonTracingV1(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> addTrace(String trace) {
        final Function function = new Function(
                FUNC_ADDTRACE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(trace)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> getTracesCount() {
        final Function function = new Function(FUNC_GETTRACESCOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    @Deprecated
    public static ChaimeleonTracingV1 load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ChaimeleonTracingV1(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static ChaimeleonTracingV1 load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ChaimeleonTracingV1(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static ChaimeleonTracingV1 load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new ChaimeleonTracingV1(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ChaimeleonTracingV1 load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ChaimeleonTracingV1(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ChaimeleonTracingV1> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ChaimeleonTracingV1.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<ChaimeleonTracingV1> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ChaimeleonTracingV1.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<ChaimeleonTracingV1> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ChaimeleonTracingV1.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<ChaimeleonTracingV1> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ChaimeleonTracingV1.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }
}
