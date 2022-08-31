package es.upv.grycap.tracer.model.besu;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
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
import org.web3j.tuples.generated.Tuple2;
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
public class ChaimeleonTracer_V1 extends Contract {
    public static final String BINARY = "60c0604052601360809081527f436861696d656c656f6e5472616365725f56310000000000000000000000000060a0526001906200003e908262000112565b503480156200004c57600080fd5b50600280546001600160a81b031916336101000260ff1916179055620001de565b634e487b7160e01b600052604160045260246000fd5b600181811c908216806200009857607f821691505b602082108103620000b957634e487b7160e01b600052602260045260246000fd5b50919050565b601f8211156200010d57600081815260208120601f850160051c81016020861015620000e85750805b601f850160051c820191505b818110156200010957828155600101620000f4565b5050505b505050565b81516001600160401b038111156200012e576200012e6200006d565b62000146816200013f845462000083565b84620000bf565b602080601f8311600181146200017e5760008415620001655750858301515b600019600386901b1c1916600185901b17855562000109565b600085815260208120601f198616915b82811015620001af578886015182559484019460019091019084016200018e565b5085821015620001ce5787850151600019600388901b60f8161c191681555b5050505050600190811b01905550565b6112ae80620001ee6000396000f3fe608060405234801561001057600080fd5b506004361061009e5760003560e01c8063893d20e811610066578063893d20e8146101305780638fcce19f146101595780639cd1e2201461016f5780639ed8177b1461018f578063f5f5ba72146101a257600080fd5b806308fcd674146100a35780633a2fa807146100b857806350ca5428146100ce57806374d0a7bb146100ee5780637ddf39001461010f575b600080fd5b6100b66100b1366004610d0b565b6101b7565b005b6000546040519081526020015b60405180910390f35b6100e16100dc366004610d64565b6102aa565b6040516100c59190610df8565b6101016100fc366004610d64565b61041c565b6040516100c5929190610e5a565b6100b661011d366004610ea2565b6002805460ff1916911515919091179055565b60025461010090046001600160a01b03166040516001600160a01b0390911681526020016100c5565b60025460ff1660405190151581526020016100c5565b61018261017d366004610d64565b610701565b6040516100c59190610ecb565b6100e161019d366004610f69565b6108d4565b6101aa610a9b565b6040516100c59190610f8b565b6040805160808101825242815267ffffffffffffffff80851660208301908152339383019384526060830185815260008054600181018255908052845160039091027f290decd9548b62a8d60345a988386fc84ba6bc95484008f6362f93160ef3e563810191825592517f290decd9548b62a8d60345a988386fc84ba6bc95484008f6362f93160ef3e5648401805497516001600160a01b0316600160401b026001600160e01b031990981691909516179590951790925590519192917f290decd9548b62a8d60345a988386fc84ba6bc95484008f6362f93160ef3e565909101906102a39082611027565b5050505050565b60608060006102ba86868661041c565b909250905060008167ffffffffffffffff8111156102da576102da610c68565b60405190808252806020026020018201604052801561030d57816020015b60608152602001906001900390816102f85790505b50905060005b8281101561041157600084828151811061032f5761032f6110e7565b602002602001015181548110610347576103476110e7565b9060005260206000209060030201600201805461036390610f9e565b80601f016020809104026020016040519081016040528092919081815260200182805461038f90610f9e565b80156103dc5780601f106103b1576101008083540402835291602001916103dc565b820191906000526020600020905b8154815290600101906020018083116103bf57829003601f168201915b50505050508282815181106103f3576103f36110e7565b6020026020010181905250808061040990611113565b915050610313565b509695505050505050565b60008054606091908385111561048e5760405160016201f41360e51b0319815260206004820152602860248201527f737461727420706f736974696f6e2067726561746572207468616e20656e64206044820152673837b9b4ba34b7b760c11b60648201526084015b60405180910390fd5b8084106104f85760405160016201f41360e51b0319815260206004820152602e60248201527f656e6420706f736974696f6e206869676865722f657175616c207468616e2f7460448201526d1bc81d1c9858d95cc818dbdd5b9d60921b6064820152608401610485565b6000610504868661112c565b61050f90600161113f565b905060006105448860408051808201825260008082526020918201528151808301909252825182529182019181019190915290565b905060008267ffffffffffffffff81111561056157610561610c68565b60405190808252806020026020018201604052801561058a578160200160208202803683370190505b5090506000885b848110156106cc5760008082815481106105ad576105ad6110e7565b906000526020600020906003020160020180546105c990610f9e565b80601f01602080910402602001604051908101604052809291908181526020018280546105f590610f9e565b80156106425780601f1061061757610100808354040283529160200191610642565b820191906000526020600020905b81548152906001019060200180831161062557829003601f168201915b50505050509050600061068561067f8360408051808201825260008082526020918201528151808301909252825182529182019181019190915290565b87610b2d565b905080156106b757828585815181106106a0576106a06110e7565b60209081029190910101526106b484611113565b93505b505080806106c490611113565b915050610591565b5060025460ff16156106f2578151156106f25760006106eb828661112c565b8351038352505b90999098509650505050505050565b606080600061071186868661041c565b909250905060008167ffffffffffffffff81111561073157610731610c68565b60405190808252806020026020018201604052801561078257816020015b604080516080810182526000808252602080830182905292820152606080820152825260001990920191018161074f5790505b50905060005b828110156104115760008482815181106107a4576107a46110e7565b6020026020010151815481106107bc576107bc6110e7565b60009182526020918290206040805160808101825260039390930290910180548352600181015467ffffffffffffffff811694840194909452600160401b9093046001600160a01b03169082015260028201805491929160608401919061082290610f9e565b80601f016020809104026020016040519081016040528092919081815260200182805461084e90610f9e565b801561089b5780601f106108705761010080835404028352916020019161089b565b820191906000526020600020905b81548152906001019060200180831161087e57829003601f168201915b5050505050815250508282815181106108b6576108b66110e7565b602002602001018190525080806108cc90611113565b915050610788565b60005460609080841015610a6557828160016108f0838861113f565b6108fa919061112c565b11156109185761090a858361112c565b61091590600161113f565b90505b60008167ffffffffffffffff81111561093357610933610c68565b60405190808252806020026020018201604052801561096657816020015b60608152602001906001900390816109515790505b50905060005b82811015610a5a576000610980828961113f565b81548110610990576109906110e7565b906000526020600020906003020160020180546109ac90610f9e565b80601f01602080910402602001604051908101604052809291908181526020018280546109d890610f9e565b8015610a255780601f106109fa57610100808354040283529160200191610a25565b820191906000526020600020905b815481529060010190602001808311610a0857829003601f168201915b5050505050828281518110610a3c57610a3c6110e7565b60200260200101819052508080610a5290611113565b91505061096c565b509250610a95915050565b6040805160008082526020820190925290610a90565b6060815260200190600190039081610a7b5790505b509150505b92915050565b606060018054610aaa90610f9e565b80601f0160208091040260200160405190810160405280929190818152602001828054610ad690610f9e565b8015610b235780601f10610af857610100808354040283529160200191610b23565b820191906000526020600020905b815481529060010190602001808311610b0657829003601f168201915b5050505050905090565b6020808301518351835192840151600093610b4b9291849190610b54565b14159392505050565b600080858411610c5b5760208411610c075760008415610b9f576001610b7b86602061112c565b610b86906008611152565b610b91906002611255565b610b9b919061112c565b1990505b8351811685610bae898961113f565b610bb8919061112c565b805190935082165b818114610bf257878411610bda5787945050505050610c60565b83610be481611261565b945050828451169050610bc0565b610bfc878561113f565b945050505050610c60565b838320610c14858861112c565b610c1e908761113f565b91505b858210610c5957848220808203610c4657610c3c868461113f565b9350505050610c60565b610c5160018461112c565b925050610c21565b505b849150505b949350505050565b634e487b7160e01b600052604160045260246000fd5b600082601f830112610c8f57600080fd5b813567ffffffffffffffff80821115610caa57610caa610c68565b604051601f8301601f19908116603f01168101908282118183101715610cd257610cd2610c68565b81604052838152866020858801011115610ceb57600080fd5b836020870160208301376000602085830101528094505050505092915050565b60008060408385031215610d1e57600080fd5b823567ffffffffffffffff8082168214610d3757600080fd5b90925060208401359080821115610d4d57600080fd5b50610d5a85828601610c7e565b9150509250929050565b600080600060608486031215610d7957600080fd5b833567ffffffffffffffff811115610d9057600080fd5b610d9c86828701610c7e565b9660208601359650604090950135949350505050565b6000815180845260005b81811015610dd857602081850181015186830182015201610dbc565b506000602082860101526020601f19601f83011685010191505092915050565b6000602080830181845280855180835260408601915060408160051b870101925083870160005b82811015610e4d57603f19888603018452610e3b858351610db2565b94509285019290850190600101610e1f565b5092979650505050505050565b604080825283519082018190526000906020906060840190828701845b82811015610e9357815184529284019290840190600101610e77565b50505092019290925292915050565b600060208284031215610eb457600080fd5b81358015158114610ec457600080fd5b9392505050565b60006020808301818452808551808352604092508286019150828160051b87010184880160005b83811015610f5b57888303603f190185528151805184528781015167ffffffffffffffff1688850152868101516001600160a01b031687850152606090810151608091850182905290610f4781860183610db2565b968901969450505090860190600101610ef2565b509098975050505050505050565b60008060408385031215610f7c57600080fd5b50508035926020909101359150565b602081526000610ec46020830184610db2565b600181811c90821680610fb257607f821691505b602082108103610fd257634e487b7160e01b600052602260045260246000fd5b50919050565b601f82111561102257600081815260208120601f850160051c81016020861015610fff5750805b601f850160051c820191505b8181101561101e5782815560010161100b565b5050505b505050565b815167ffffffffffffffff81111561104157611041610c68565b6110558161104f8454610f9e565b84610fd8565b602080601f83116001811461108a57600084156110725750858301515b600019600386901b1c1916600185901b17855561101e565b600085815260208120601f198616915b828110156110b95788860151825594840194600190910190840161109a565b50858210156110d75787850151600019600388901b60f8161c191681555b5050505050600190811b01905550565b634e487b7160e01b600052603260045260246000fd5b634e487b7160e01b600052601160045260246000fd5b600060018201611125576111256110fd565b5060010190565b81810381811115610a9557610a956110fd565b80820180821115610a9557610a956110fd565b600081600019048311821515161561116c5761116c6110fd565b500290565b600181815b808511156111ac578160001904821115611192576111926110fd565b8085161561119f57918102915b93841c9390800290611176565b509250929050565b6000826111c357506001610a95565b816111d057506000610a95565b81600181146111e657600281146111f05761120c565b6001915050610a95565b60ff841115611201576112016110fd565b50506001821b610a95565b5060208310610133831016604e8410600b841016171561122f575081810a610a95565b6112398383611171565b806000190482111561124d5761124d6110fd565b029392505050565b6000610ec483836111b4565b600081611270576112706110fd565b50600019019056fea264697066735822122031ecc4351882ddc4f5dbf3fb077b49f396ff234753d1d87fa84ad85abdf3c45c64736f6c63430008100033";

    public static final String FUNC_ADDTRACE = "addTrace";

    public static final String FUNC_GETCONTRACTNAME = "getContractName";

    public static final String FUNC_GETFULLTRACESBYVALUE = "getFullTracesByValue";

    public static final String FUNC_GETOWNER = "getOwner";

    public static final String FUNC_GETTRACESBYVALUE = "getTracesByValue";

    public static final String FUNC_GETTRACESCOUNT = "getTracesCount";

    public static final String FUNC_GETTRACESPOSSBYVALUE = "getTracesPossByValue";

    public static final String FUNC_GETTRACESPOSSBYVALUERESIZE = "getTracesPossByValueResize";

    public static final String FUNC_GETTRACESSUBARRAY = "getTracesSubarray";

    public static final String FUNC_SETTRACESPOSSBYVALUERESIZE = "setTracesPossByValueResize";

    @Deprecated
    protected ChaimeleonTracer_V1(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ChaimeleonTracer_V1(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected ChaimeleonTracer_V1(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ChaimeleonTracer_V1(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> addTrace(BigInteger eTime, String trace) {
        final Function function = new Function(
                FUNC_ADDTRACE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint64(eTime), 
                new org.web3j.abi.datatypes.Utf8String(trace)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> getContractName() {
        final Function function = new Function(FUNC_GETCONTRACTNAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

//    public RemoteFunctionCall<List> getFullTracesByValue(String value, BigInteger sP, BigInteger eP) {
//        final Function function = new Function(FUNC_GETFULLTRACESBYVALUE, 
//                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(value), 
//                new org.web3j.abi.datatypes.generated.Uint256(sP), 
//                new org.web3j.abi.datatypes.generated.Uint256(eP)), 
//                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<TraceEntry>>() {}));
//        return new RemoteFunctionCall<List>(function,
//                new Callable<List>() {
//                    @Override
//                    @SuppressWarnings("unchecked")
//                    public List call() throws Exception {
//                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
//                        return convertToNative(result);
//                    }
//                });
//    }

    public RemoteFunctionCall<String> getOwner() {
        final Function function = new Function(FUNC_GETOWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<List> getTracesByValue(String value, BigInteger sP, BigInteger eP) {
        final Function function = new Function(FUNC_GETTRACESBYVALUE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(value), 
                new org.web3j.abi.datatypes.generated.Uint256(sP), 
                new org.web3j.abi.datatypes.generated.Uint256(eP)), 
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

    public RemoteFunctionCall<Tuple2<List<BigInteger>, BigInteger>> getTracesPossByValue(String value, BigInteger sP, BigInteger eP) {
        final Function function = new Function(FUNC_GETTRACESPOSSBYVALUE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(value), 
                new org.web3j.abi.datatypes.generated.Uint256(sP), 
                new org.web3j.abi.datatypes.generated.Uint256(eP)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple2<List<BigInteger>, BigInteger>>(function,
                new Callable<Tuple2<List<BigInteger>, BigInteger>>() {
                    @Override
                    public Tuple2<List<BigInteger>, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<List<BigInteger>, BigInteger>(
                                convertToNative((List<Uint256>) results.get(0).getValue()), 
                                (BigInteger) results.get(1).getValue());
                    }
                });
    }

    public RemoteFunctionCall<Boolean> getTracesPossByValueResize() {
        final Function function = new Function(FUNC_GETTRACESPOSSBYVALUERESIZE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
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

    public RemoteFunctionCall<TransactionReceipt> setTracesPossByValueResize(Boolean newVal) {
        final Function function = new Function(
                FUNC_SETTRACESPOSSBYVALUERESIZE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Bool(newVal)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static ChaimeleonTracer_V1 load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ChaimeleonTracer_V1(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static ChaimeleonTracer_V1 load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ChaimeleonTracer_V1(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static ChaimeleonTracer_V1 load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new ChaimeleonTracer_V1(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ChaimeleonTracer_V1 load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ChaimeleonTracer_V1(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ChaimeleonTracer_V1> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ChaimeleonTracer_V1.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<ChaimeleonTracer_V1> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ChaimeleonTracer_V1.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<ChaimeleonTracer_V1> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ChaimeleonTracer_V1.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<ChaimeleonTracer_V1> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ChaimeleonTracer_V1.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }
}
