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
    public static final String BINARY = "608060405234801561001057600080fd5b50600380546001600160a01b03191633179055610670806100326000396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c80633a2fa8071461004657806384bab2f61461005c5780639ed8177b14610071575b600080fd5b6000546040519081526020015b60405180910390f35b61006f61006a366004610314565b610091565b005b61008461007f3660046103c5565b61015e565b60405161005391906103e7565b6003546001600160a01b031633146100a857600080fd5b60408051606081018252428082526020820190815291810183815260008054600181018255908052825160039091027f290decd9548b62a8d60345a988386fc84ba6bc95484008f6362f93160ef3e563810191825593517f290decd9548b62a8d60345a988386fc84ba6bc95484008f6362f93160ef3e5648501559051919290917f290decd9548b62a8d60345a988386fc84ba6bc95484008f6362f93160ef3e565909101906101589082610502565b50505050565b600054606090828161017082876105d8565b106101825761017f85836105eb565b90505b60008167ffffffffffffffff81111561019d5761019d6102fe565b6040519080825280602002602001820160405280156101d057816020015b60608152602001906001900390816101bb5790505b5090506101de6001846105eb565b8610156102f35760005b82816001600160801b031610156102f157600061020e6001600160801b038316896105d8565b8154811061021e5761021e6105fe565b9060005260206000209060030201600201805461023a90610479565b80601f016020809104026020016040519081016040528092919081815260200182805461026690610479565b80156102b35780601f10610288576101008083540402835291602001916102b3565b820191906000526020600020905b81548152906001019060200180831161029657829003601f168201915b505050505082826001600160801b0316815181106102d3576102d36105fe565b602002602001018190525080806102e990610614565b9150506101e8565b505b925050505b92915050565b634e487b7160e01b600052604160045260246000fd5b60006020828403121561032657600080fd5b813567ffffffffffffffff8082111561033e57600080fd5b818401915084601f83011261035257600080fd5b813581811115610364576103646102fe565b604051601f8201601f19908116603f0116810190838211818310171561038c5761038c6102fe565b816040528281528760208487010111156103a557600080fd5b826020860160208301376000928101602001929092525095945050505050565b600080604083850312156103d857600080fd5b50508035926020909101359150565b6000602080830181845280855180835260408601915060408160051b87010192508387016000805b8381101561046b57888603603f1901855282518051808852835b81811015610444578281018a01518982018b01528901610429565b508781018901849052601f01601f191690960187019550938601939186019160010161040f565b509398975050505050505050565b600181811c9082168061048d57607f821691505b6020821081036104ad57634e487b7160e01b600052602260045260246000fd5b50919050565b601f8211156104fd57600081815260208120601f850160051c810160208610156104da5750805b601f850160051c820191505b818110156104f9578281556001016104e6565b5050505b505050565b815167ffffffffffffffff81111561051c5761051c6102fe565b6105308161052a8454610479565b846104b3565b602080601f831160018114610565576000841561054d5750858301515b600019600386901b1c1916600185901b1785556104f9565b600085815260208120601f198616915b8281101561059457888601518255948401946001909101908401610575565b50858210156105b25787850151600019600388901b60f8161c191681555b5050505050600190811b01905550565b634e487b7160e01b600052601160045260246000fd5b808201808211156102f8576102f86105c2565b818103818111156102f8576102f86105c2565b634e487b7160e01b600052603260045260246000fd5b60006001600160801b03808316818103610630576106306105c2565b600101939250505056fea2646970667358221220caf0dc68b9515d10593102b4baeec676337d8434aa09de63fdeea39ca6af15cf64736f6c63430008100033";

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
