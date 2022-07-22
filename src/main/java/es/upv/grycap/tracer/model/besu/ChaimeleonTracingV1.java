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
 * <p>Generated with web3j version 4.8.7.
 */
@SuppressWarnings("rawtypes")
public class ChaimeleonTracingV1 extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b50600380546001600160a01b0319163317905561058f806100326000396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c8063083f3939146100465780633a2fa8071461006f57806384bab2f614610084575b600080fd5b6100596100543660046103fb565b610099565b60405161006691906104ac565b60405180910390f35b610077610282565b6040516100669190610524565b6100976100923660046103fb565b610288565b005b606060006001836040516100ad9190610490565b908152604080519182900360209081018320805480830285018301909352828452919083018282801561013157602002820191906000526020600020906000905b82829054906101000a90046001600160801b03166001600160801b031681526020019060100190602082600f010492830192600103820291508084116100ee5790505b505050505090506000815167ffffffffffffffff8111801561015257600080fd5b5060405190808252806020026020018201604052801561018657816020015b60608152602001906001900390816101715790505b50905060005b825181101561027a5760008382815181106101a357fe5b60200260200101516001600160801b0316815481106101be57fe5b600091825260209182902060026003909202018101805460408051601f6000196101006001861615020190931694909404918201859004850284018501905280835291929091908301828280156102565780601f1061022b57610100808354040283529160200191610256565b820191906000526020600020905b81548152906001019060200180831161023957829003601f168201915b505050505082828151811061026757fe5b602090810291909101015260010161018c565b509392505050565b60005490565b6003546001600160a01b0316331461029f57600080fd5b6040805160608101825242808252602080830191825292820184815260008054600181018255908052835160039091027f290decd9548b62a8d60345a988386fc84ba6bc95484008f6362f93160ef3e563810191825592517f290decd9548b62a8d60345a988386fc84ba6bc95484008f6362f93160ef3e5648401559051805193949193610354937f290decd9548b62a8d60345a988386fc84ba6bc95484008f6362f93160ef3e5650192919091019061035a565b50505050565b828054600181600116156101000203166002900490600052602060002090601f01602090048101928261039057600085556103d6565b82601f106103a957805160ff19168380011785556103d6565b828001600101855582156103d6579182015b828111156103d65782518255916020019190600101906103bb565b506103e29291506103e6565b5090565b5b808211156103e257600081556001016103e7565b6000602080838503121561040d578182fd5b823567ffffffffffffffff80821115610424578384fd5b818501915085601f830112610437578384fd5b81358181111561044357fe5b604051601f8201601f191681018501838111828210171561046057fe5b6040528181528382018501881015610476578586fd5b818585018683013790810190930193909352509392505050565b600082516104a281846020870161052d565b9190910192915050565b6000602080830181845280855180835260408601915060408482028701019250838701855b8281101561051757878503603f19018452815180518087526104f8818989018a850161052d565b601f01601f1916959095018601945092850192908501906001016104d1565b5092979650505050505050565b90815260200190565b60005b83811015610548578181015183820152602001610530565b83811115610354575050600091015256fea26469706673582212203c01ff2db69dff86dc74bc4d0c13c9e41141e4a9886a4c9be37fbee48a1b05a164736f6c63430007060033";

    public static final String FUNC_ADDTRACE = "addTrace";

    public static final String FUNC_GETTRACESBYDATASETID = "getTracesByDatasetId";

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

    public RemoteFunctionCall<List> getTracesByDatasetId(String datasetId) {
        final Function function = new Function(FUNC_GETTRACESBYDATASETID, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(datasetId)), 
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
