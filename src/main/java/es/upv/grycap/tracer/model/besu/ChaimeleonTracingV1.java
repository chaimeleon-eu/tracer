package es.upv.grycap.tracer.model.besu;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
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
public class ChaimeleonTracingV1 extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b50600380546001600160a01b0319163317905561068a806100326000396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c80633a2fa8071461004657806384bab2f61461005c5780639ed8177b14610071575b600080fd5b6000546040519081526020015b60405180910390f35b61006f61006a36600461033b565b610091565b005b61008461007f3660046103ec565b61015e565b604051610053919061040e565b6003546001600160a01b031633146100a857600080fd5b60408051606081018252428082526020820190815291810183815260008054600181018255908052825160039091027f290decd9548b62a8d60345a988386fc84ba6bc95484008f6362f93160ef3e563810191825593517f290decd9548b62a8d60345a988386fc84ba6bc95484008f6362f93160ef3e5648501559051919290917f290decd9548b62a8d60345a988386fc84ba6bc95484008f6362f93160ef3e565909101906101589082610529565b50505050565b600054606090808410156102ef578281600161017a83886105ff565b6101849190610612565b11156101a2576101948583610612565b61019f9060016105ff565b90505b60008167ffffffffffffffff8111156101bd576101bd610325565b6040519080825280602002602001820160405280156101f057816020015b60608152602001906001900390816101db5790505b50905060005b828110156102e457600061020a82896105ff565b8154811061021a5761021a610625565b90600052602060002090600302016002018054610236906104a0565b80601f0160208091040260200160405190810160405280929190818152602001828054610262906104a0565b80156102af5780601f10610284576101008083540402835291602001916102af565b820191906000526020600020905b81548152906001019060200180831161029257829003601f168201915b50505050508282815181106102c6576102c6610625565b602002602001018190525080806102dc9061063b565b9150506101f6565b50925061031f915050565b604080516000808252602082019092529061031a565b60608152602001906001900390816103055790505b509150505b92915050565b634e487b7160e01b600052604160045260246000fd5b60006020828403121561034d57600080fd5b813567ffffffffffffffff8082111561036557600080fd5b818401915084601f83011261037957600080fd5b81358181111561038b5761038b610325565b604051601f8201601f19908116603f011681019083821181831017156103b3576103b3610325565b816040528281528760208487010111156103cc57600080fd5b826020860160208301376000928101602001929092525095945050505050565b600080604083850312156103ff57600080fd5b50508035926020909101359150565b6000602080830181845280855180835260408601915060408160051b87010192508387016000805b8381101561049257888603603f1901855282518051808852835b8181101561046b578281018a01518982018b01528901610450565b508781018901849052601f01601f1916909601870195509386019391860191600101610436565b509398975050505050505050565b600181811c908216806104b457607f821691505b6020821081036104d457634e487b7160e01b600052602260045260246000fd5b50919050565b601f82111561052457600081815260208120601f850160051c810160208610156105015750805b601f850160051c820191505b818110156105205782815560010161050d565b5050505b505050565b815167ffffffffffffffff81111561054357610543610325565b6105578161055184546104a0565b846104da565b602080601f83116001811461058c57600084156105745750858301515b600019600386901b1c1916600185901b178555610520565b600085815260208120601f198616915b828110156105bb5788860151825594840194600190910190840161059c565b50858210156105d95787850151600019600388901b60f8161c191681555b5050505050600190811b01905550565b634e487b7160e01b600052601160045260246000fd5b8082018082111561031f5761031f6105e9565b8181038181111561031f5761031f6105e9565b634e487b7160e01b600052603260045260246000fd5b60006001820161064d5761064d6105e9565b506001019056fea264697066735822122072a49c47a2aaf3c6d75d07d59e6dcbc1a90b85ba3565558bfebbc23cc23ff9be64736f6c63430008100033";

    public static final String FUNC_ADDTRACE = "addTrace";

    public static final String FUNC_GETTRACESCOUNT = "getTracesCount";

    public static final String FUNC_GETTRACESSUBARRAY = "getTracesSubarray";

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

    public RemoteFunctionCall<List> getTracesSubarray(BigInteger startPos, BigInteger maxNumElems) {
        final Function function = new Function(FUNC_GETTRACESSUBARRAY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(startPos), 
                new org.web3j.abi.datatypes.generated.Uint256(maxNumElems)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Utf8String>>() {}));
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
