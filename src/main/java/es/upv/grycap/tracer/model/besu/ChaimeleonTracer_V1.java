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
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint64;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple3;
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
    public static final String BINARY = "60c0604052601360809081527f436861696d656c656f6e5472616365725f56310000000000000000000000000060a0526002906200003e908262000112565b503480156200004c57600080fd5b50600380546001600160a81b031916336101000260ff1916179055620001de565b634e487b7160e01b600052604160045260246000fd5b600181811c908216806200009857607f821691505b602082108103620000b957634e487b7160e01b600052602260045260246000fd5b50919050565b601f8211156200010d57600081815260208120601f850160051c81016020861015620000e85750805b601f850160051c820191505b818110156200010957828155600101620000f4565b5050505b505050565b81516001600160401b038111156200012e576200012e6200006d565b62000146816200013f845462000083565b84620000bf565b602080601f8311600181146200017e5760008415620001655750858301515b600019600386901b1c1916600185901b17855562000109565b600085815260208120601f198616915b82811015620001af578886015182559484019460019091019084016200018e565b5085821015620001ce5787850151600019600388901b60f8161c191681555b5050505050600190811b01905550565b61171a80620001ee6000396000f3fe608060405234801561001057600080fd5b50600436106100935760003560e01c80637ddf3900116100665780637ddf39001461012e578063893d20e8146101605780638fcce19f1461018e5780639ed8177b146101bb578063f5f5ba72146101ce57600080fd5b80633a2fa8071461009857806350ca5428146100c957806355538507146100eb5780637dcbddbb1461010c575b600080fd5b6100b16000805460408051602081019091528281529092565b6040516100c093929190610fc3565b60405180910390f35b6100dc6100d7366004611094565b6101e5565b6040516100c09392919061112f565b6100fe6100f93660046111b3565b610426565b6040516100c092919061122f565b61011f61011a36600461124f565b61062e565b6040516100c093929190611283565b6100fe61013c3660046112b5565b6003805460ff19169115159190911790556040805160208101909152600080825291565b60035460408051602081018252600080825291516100c09361010090046001600160a01b03169291906112de565b6101ac6003546040805160208101909152600080825260ff90921692565b6040516100c0939291906112f7565b6100dc6101c9366004611309565b610815565b6101d6610a62565b6040516100c09392919061132b565b6060600060606000806000806101fc8a8a8a610b13565b92965090945092509050600082600381111561021a5761021a610f3b565b036103dd576000836001600160401b0381111561023957610239610ff2565b60405190808252806020026020018201604052801561027257816020015b61025f610efa565b8152602001906001900390816102575790505b50905060005b848110156103cd5760008682815181106102945761029461133e565b6020026020010151815481106102ac576102ac61133e565b60009182526020918290206040805160a0810182526004939093029091018054835260018101546001600160401b03811694840194909452600160401b9093046001600160a01b0316908201526002820154606082015260038201805491929160808401919061031b90611354565b80601f016020809104026020016040519081016040528092919081815260200182805461034790611354565b80156103945780601f1061036957610100808354040283529160200191610394565b820191906000526020600020905b81548152906001019060200180831161037757829003601f168201915b5050505050815250508282815181106103af576103af61133e565b602002602001018190525080806103c5906113a4565b915050610278565b509650909450925061041d915050565b60408051600080825260208201909252906103cd565b6103fb610efa565b8152602001906001900390816103f3579050509650909450925061041d915050565b93509350939050565b6000606060018460405161043a91906113bd565b9081526040519081900360200190206001015460ff161561048a575050604080518082019091526014815273747261636520616c72656164792065786973747360601b6020820152600290610626565b60006040518060a00160405280428152602001876001600160401b03168152602001336001600160a01b031681526020016002866040516020016104ce91906113d9565b60408051601f19818403018152908290526104e8916113bd565b602060405180830381855afa158015610505573d6000803e3d6000fd5b5050506040513d601f19601f8201168201806040525081019061052891906113ec565b81526020908101869052825460018181018555600094855293829020835160049092020190815590820151928101805460408401516001600160a01b0316600160401b026001600160e01b03199091166001600160401b03909516949094179390931790925560608101516002830155608081015190919060038201906105af9082611454565b505050604051806040016040528060016000805490506105cf9190611513565b8152600160209091018190526040516105e99087906113bd565b908152604080516020928190038301812084518155938301516001909401805460ff19169415159490941790935590820190526000808252925090505b935093915050565b610636610efa565b6000606060018460405161064a91906113bd565b9081526040519081900360200190206001015460ff16156107ae57600060018560405161067791906113bd565b90815260200160405180910390206000015490506000818154811061069e5761069e61133e565b600091825260208083206040805160a0810182526004949094029091018054845260018101546001600160401b03811693850193909352600160401b9092046001600160a01b03169083015260028101546060830152600381018054919392918491608084019161070e90611354565b80601f016020809104026020016040519081016040528092919081815260200182805461073a90611354565b80156107875780601f1061075c57610100808354040283529160200191610787565b820191906000526020600020905b81548152906001019060200180831161076a57829003601f168201915b5050505050815250509150604051806020016040528060008152509350935093505061080e565b50506040805160a081018252600080825260208083018290523383850152606083018290528351808201855291825260808301919091528251808401909352600f83526e1d1c9858d9481b9bdd08199bdd5b99608a1b9083015291506003905b9193909250565b6000805460609190829080861015610a065784816108338289611526565b1115610846576108438783611513565b90505b6000816001600160401b0381111561086057610860610ff2565b60405190808252806020026020018201604052801561089957816020015b610886610efa565b81526020019060019003908161087e5790505b50905060005b828110156109e45760006108b3828b611526565b815481106108c3576108c361133e565b60009182526020918290206040805160a0810182526004939093029091018054835260018101546001600160401b03811694840194909452600160401b9093046001600160a01b0316908201526002820154606082015260038201805491929160808401919061093290611354565b80601f016020809104026020016040519081016040528092919081815260200182805461095e90611354565b80156109ab5780601f10610980576101008083540402835291602001916109ab565b820191906000526020600020905b81548152906001019060200180831161098e57829003601f168201915b5050505050815250508282815181106109c6576109c661133e565b602002602001018190525080806109dc906113a4565b91505061089f565b5080600060405180602001604052806000815250955095509550505050610a5b565b6040805160008082526020820190925290610a37565b610a24610efa565b815260200190600190039081610a1c5790505b50600160405180606001604052806037815260200161165860379139935093509350505b9250925092565b60606000606060026000818054610a7890611354565b80601f0160208091040260200160405190810160405280929190818152602001828054610aa490611354565b8015610af15780601f10610ac657610100808354040283529160200191610af1565b820191906000526020600020905b815481529060010190602001808311610ad457829003601f168201915b5050505050915060405180602001604052806000815250925092509250909192565b60008054606091908190839085871115610b5e576040805160008082526080820183526028602083018181529293919260019290919061168f90860139945094509450945050610db4565b808610610b9c57604080516000808252608082018352602e60208301818152929391926001929091906116b790860139945094509450945050610db4565b6000610ba88888611513565b610bb3906001611526565b90506000610be88a60408051808201825260008082526020918201528151808301909252825182529182019181019190915290565b90506000826001600160401b03811115610c0457610c04610ff2565b604051908082528060200260200182016040528015610c2d578160200160208202803683370190505b50905060008a5b84811015610d6f576000808281548110610c5057610c5061133e565b90600052602060002090600402016003018054610c6c90611354565b80601f0160208091040260200160405190810160405280929190818152602001828054610c9890611354565b8015610ce55780601f10610cba57610100808354040283529160200191610ce5565b820191906000526020600020905b815481529060010190602001808311610cc857829003601f168201915b505050505090506000610d28610d228360408051808201825260008082526020918201528151808301909252825182529182019181019190915290565b87610dbd565b90508015610d5a5782858581518110610d4357610d4361133e565b6020908102919091010152610d57846113a4565b93505b50508080610d67906113a4565b915050610c34565b5060035460ff1615610d9557815115610d95576000610d8e8286611513565b8351038352505b6040805160208101909152600080825292995090975090955093505050505b93509350935093565b6020808301518351835192840151600093610ddb9291849190610de6565b141590505b92915050565b600080858411610eed5760208411610e995760008415610e31576001610e0d866020611513565b610e18906008611539565b610e23906002611634565b610e2d9190611513565b1990505b8351811685610e408989611526565b610e4a9190611513565b805190935082165b818114610e8457878411610e6c5787945050505050610ef2565b83610e7681611640565b945050828451169050610e52565b610e8e8785611526565b945050505050610ef2565b838320610ea68588611513565b610eb09087611526565b91505b858210610eeb57848220808203610ed857610ece8684611526565b9350505050610ef2565b610ee3600184611513565b925050610eb3565b505b849150505b949350505050565b6040518060a001604052806000815260200160006001600160401b0316815260200160006001600160a01b0316815260200160008152602001606081525090565b634e487b7160e01b600052602160045260246000fd5b60048110610f6f57634e487b7160e01b600052602160045260246000fd5b9052565b60005b83811015610f8e578181015183820152602001610f76565b50506000910152565b60008151808452610faf816020860160208601610f73565b601f01601f19169290920160200192915050565b838152610fd36020820184610f51565b606060408201526000610fe96060830184610f97565b95945050505050565b634e487b7160e01b600052604160045260246000fd5b600082601f83011261101957600080fd5b81356001600160401b038082111561103357611033610ff2565b604051601f8301601f19908116603f0116810190828211818310171561105b5761105b610ff2565b8160405283815286602085880101111561107457600080fd5b836020870160208301376000602085830101528094505050505092915050565b6000806000606084860312156110a957600080fd5b83356001600160401b038111156110bf57600080fd5b6110cb86828701611008565b9660208601359650604090950135949350505050565b805182526001600160401b03602082015116602083015260018060a01b036040820151166040830152606081015160608301526000608082015160a06080850152610ef260a0850182610f97565b6000606082016060835280865180835260808501915060808160051b8601019250602080890160005b8381101561118657607f198887030185526111748683516110e1565b95509382019390820190600101611158565b505061119481870189610f51565b50505082810360408401526111a98185610f97565b9695505050505050565b6000806000606084860312156111c857600080fd5b83356001600160401b0380821682146111e057600080fd5b909350602085013590808211156111f657600080fd5b61120287838801611008565b9350604086013591508082111561121857600080fd5b5061122586828701611008565b9150509250925092565b6112398184610f51565b604060208201526000610ef26040830184610f97565b60006020828403121561126157600080fd5b81356001600160401b0381111561127757600080fd5b610ef284828501611008565b60608152600061129660608301866110e1565b6112a36020840186610f51565b82810360408401526111a98185610f97565b6000602082840312156112c757600080fd5b813580151581146112d757600080fd5b9392505050565b6001600160a01b0384168152610fd36020820184610f51565b8315158152610fd36020820184610f51565b6000806040838503121561131c57600080fd5b50508035926020909101359150565b6060815260006112966060830186610f97565b634e487b7160e01b600052603260045260246000fd5b600181811c9082168061136857607f821691505b60208210810361138857634e487b7160e01b600052602260045260246000fd5b50919050565b634e487b7160e01b600052601160045260246000fd5b6000600182016113b6576113b661138e565b5060010190565b600082516113cf818460208701610f73565b9190910192915050565b6020815260006112d76020830184610f97565b6000602082840312156113fe57600080fd5b5051919050565b601f82111561144f57600081815260208120601f850160051c8101602086101561142c5750805b601f850160051c820191505b8181101561144b57828155600101611438565b5050505b505050565b81516001600160401b0381111561146d5761146d610ff2565b6114818161147b8454611354565b84611405565b602080601f8311600181146114b6576000841561149e5750858301515b600019600386901b1c1916600185901b17855561144b565b600085815260208120601f198616915b828110156114e5578886015182559484019460019091019084016114c6565b50858210156115035787850151600019600388901b60f8161c191681555b5050505050600190811b01905550565b81810381811115610de057610de061138e565b80820180821115610de057610de061138e565b8082028115828204841417610de057610de061138e565b600181815b8085111561158b5781600019048211156115715761157161138e565b8085161561157e57918102915b93841c9390800290611555565b509250929050565b6000826115a257506001610de0565b816115af57506000610de0565b81600181146115c557600281146115cf576115eb565b6001915050610de0565b60ff8411156115e0576115e061138e565b50506001821b610de0565b5060208310610133831016604e8410600b841016171561160e575081810a610de0565b6116188383611550565b806000190482111561162c5761162c61138e565b029392505050565b60006112d78383611593565b60008161164f5761164f61138e565b50600019019056fe737461727420706f736974696f6e2067726561746572206f7220657175616c20746f20746865206e756d626572206f6620747261636573737461727420706f736974696f6e2067726561746572207468616e20656e6420706f736974696f6e656e6420706f736974696f6e206869676865722f657175616c207468616e2f746f2074726163657320636f756e74a2646970667358221220f83ad808129bdc8ba73d917fa9d663017f45ebc5520d62f50d8dba0af31c452164736f6c63430008110033";

    public static final String FUNC_ADDTRACE = "addTrace";

    public static final String FUNC_GETCONTRACTNAME = "getContractName";

    public static final String FUNC_GETOWNER = "getOwner";

    public static final String FUNC_GETTRACEBYID = "getTraceById";

    public static final String FUNC_GETTRACESBYVALUE = "getTracesByValue";

    public static final String FUNC_GETTRACESCOUNT = "getTracesCount";

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

    public RemoteFunctionCall<TransactionReceipt> addTrace(BigInteger eTime, String traceId, String trace) {
        final Function function = new Function(
                FUNC_ADDTRACE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint64(eTime), 
                new org.web3j.abi.datatypes.Utf8String(traceId), 
                new org.web3j.abi.datatypes.Utf8String(trace)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple3<String, BigInteger, String>> getContractName() {
        final Function function = new Function(FUNC_GETCONTRACTNAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Uint8>() {}, new TypeReference<Utf8String>() {}));
        return new RemoteFunctionCall<Tuple3<String, BigInteger, String>>(function,
                new Callable<Tuple3<String, BigInteger, String>>() {
                    @Override
                    public Tuple3<String, BigInteger, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<String, BigInteger, String>(
                                (String) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (String) results.get(2).getValue());
                    }
                });
    }

    public RemoteFunctionCall<Tuple3<String, BigInteger, String>> getOwner() {
        final Function function = new Function(FUNC_GETOWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint8>() {}, new TypeReference<Utf8String>() {}));
        return new RemoteFunctionCall<Tuple3<String, BigInteger, String>>(function,
                new Callable<Tuple3<String, BigInteger, String>>() {
                    @Override
                    public Tuple3<String, BigInteger, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<String, BigInteger, String>(
                                (String) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (String) results.get(2).getValue());
                    }
                });
    }

    public RemoteFunctionCall<Tuple3<TraceEntry, BigInteger, String>> getTraceById(String traceId) {
        final Function function = new Function(FUNC_GETTRACEBYID, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(traceId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<TraceEntry>() {}, new TypeReference<Uint8>() {}, new TypeReference<Utf8String>() {}));
        return new RemoteFunctionCall<Tuple3<TraceEntry, BigInteger, String>>(function,
                new Callable<Tuple3<TraceEntry, BigInteger, String>>() {
                    @Override
                    public Tuple3<TraceEntry, BigInteger, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<TraceEntry, BigInteger, String>(
                                (TraceEntry) results.get(0), 
                                (BigInteger) results.get(1).getValue(), 
                                (String) results.get(2).getValue());
                    }
                });
    }

    public RemoteFunctionCall<Tuple3<List<TraceEntry>, BigInteger, String>> getTracesByValue(String value, BigInteger sP, BigInteger eP) {
        final Function function = new Function(FUNC_GETTRACESBYVALUE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(value), 
                new org.web3j.abi.datatypes.generated.Uint256(sP), 
                new org.web3j.abi.datatypes.generated.Uint256(eP)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<TraceEntry>>() {}, new TypeReference<Uint8>() {}, new TypeReference<Utf8String>() {}));
        return new RemoteFunctionCall<Tuple3<List<TraceEntry>, BigInteger, String>>(function,
                new Callable<Tuple3<List<TraceEntry>, BigInteger, String>>() {
                    @Override
                    public Tuple3<List<TraceEntry>, BigInteger, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<List<TraceEntry>, BigInteger, String>(
                                convertToNative((List<TraceEntry>) results.get(0).getValue()), 
                                (BigInteger) results.get(1).getValue(), 
                                (String) results.get(2).getValue());
                    }
                });
    }

    public RemoteFunctionCall<Tuple3<BigInteger, BigInteger, String>> getTracesCount() {
        final Function function = new Function(FUNC_GETTRACESCOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint8>() {}, new TypeReference<Utf8String>() {}));
        return new RemoteFunctionCall<Tuple3<BigInteger, BigInteger, String>>(function,
                new Callable<Tuple3<BigInteger, BigInteger, String>>() {
                    @Override
                    public Tuple3<BigInteger, BigInteger, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<BigInteger, BigInteger, String>(
                                (BigInteger) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (String) results.get(2).getValue());
                    }
                });
    }

    public RemoteFunctionCall<Tuple3<Boolean, BigInteger, String>> getTracesPossByValueResize() {
        final Function function = new Function(FUNC_GETTRACESPOSSBYVALUERESIZE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}, new TypeReference<Uint8>() {}, new TypeReference<Utf8String>() {}));
        return new RemoteFunctionCall<Tuple3<Boolean, BigInteger, String>>(function,
                new Callable<Tuple3<Boolean, BigInteger, String>>() {
                    @Override
                    public Tuple3<Boolean, BigInteger, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<Boolean, BigInteger, String>(
                                (Boolean) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (String) results.get(2).getValue());
                    }
                });
    }

    public RemoteFunctionCall<Tuple3<List<TraceEntry>, BigInteger, String>> getTracesSubarray(BigInteger startPos, BigInteger maxNumElems) {
        final Function function = new Function(FUNC_GETTRACESSUBARRAY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(startPos), 
                new org.web3j.abi.datatypes.generated.Uint256(maxNumElems)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<TraceEntry>>() {}, new TypeReference<Uint8>() {}, new TypeReference<Utf8String>() {}));
        return new RemoteFunctionCall<Tuple3<List<TraceEntry>, BigInteger, String>>(function,
                new Callable<Tuple3<List<TraceEntry>, BigInteger, String>>() {
                    @Override
                    public Tuple3<List<TraceEntry>, BigInteger, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<List<TraceEntry>, BigInteger, String>(
                                convertToNative((List<TraceEntry>) results.get(0).getValue()), 
                                (BigInteger) results.get(1).getValue(), 
                                (String) results.get(2).getValue());
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

    public static class TraceEntry extends DynamicStruct {
        public BigInteger bTime;

        public BigInteger eTime;

        public String sender;

        public BigInteger hash;

        public String trace;

        public TraceEntry(BigInteger bTime, BigInteger eTime, String sender, BigInteger hash, String trace) {
            super(new org.web3j.abi.datatypes.generated.Uint256(bTime),new org.web3j.abi.datatypes.generated.Uint64(eTime),new org.web3j.abi.datatypes.Address(sender),new org.web3j.abi.datatypes.generated.Uint256(hash),new org.web3j.abi.datatypes.Utf8String(trace));
            this.bTime = bTime;
            this.eTime = eTime;
            this.sender = sender;
            this.hash = hash;
            this.trace = trace;
        }

        public TraceEntry(Uint256 bTime, Uint64 eTime, Address sender, Uint256 hash, Utf8String trace) {
            super(bTime,eTime,sender,hash,trace);
            this.bTime = bTime.getValue();
            this.eTime = eTime.getValue();
            this.sender = sender.getValue();
            this.hash = hash.getValue();
            this.trace = trace.getValue();
        }
    }
}
