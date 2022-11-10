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
    public static final String BINARY = "60c0604052601360809081527f436861696d656c656f6e5472616365725f56310000000000000000000000000060a0526002906200003e908262000112565b503480156200004c57600080fd5b50600380546001600160a81b031916336101000260ff1916179055620001de565b634e487b7160e01b600052604160045260246000fd5b600181811c908216806200009857607f821691505b602082108103620000b957634e487b7160e01b600052602260045260246000fd5b50919050565b601f8211156200010d57600081815260208120601f850160051c81016020861015620000e85750805b601f850160051c820191505b818110156200010957828155600101620000f4565b5050505b505050565b81516001600160401b038111156200012e576200012e6200006d565b62000146816200013f845462000083565b84620000bf565b602080601f8311600181146200017e5760008415620001655750858301515b600019600386901b1c1916600185901b17855562000109565b600085815260208120601f198616915b82811015620001af578886015182559484019460019091019084016200018e565b5085821015620001ce5787850151600019600388901b60f8161c191681555b5050505050600190811b01905550565b6117e980620001ee6000396000f3fe608060405234801561001057600080fd5b50600436106100935760003560e01c80637ddf3900116100665780637ddf39001461012e578063893d20e8146101605780638fcce19f1461018e578063f5f5ba72146101bb578063f6e5a9d9146101d257600080fd5b80632d057423146100985780633a2fa807146100c357806355538507146100eb5780637dcbddbb1461010c575b600080fd5b6100ab6100a636600461104d565b6101e5565b6040516100ba93929190611180565b60405180910390f35b6100dc6000805460408051602081019091528281529092565b6040516100ba93929190611204565b6100fe6100f9366004611233565b610426565b6040516100ba9291906112a6565b61011f61011a3660046112c6565b61062e565b6040516100ba939291906112fa565b6100fe61013c36600461132c565b6003805460ff19169115159190911790556040805160208101909152600080825291565b60035460408051602081018252600080825291516100ba9361010090046001600160a01b0316929190611355565b6101ac6003546040805160208101909152600080825260ff90921692565b6040516100ba9392919061136e565b6101c3610815565b6040516100ba93929190611380565b6100ab6101e0366004611393565b6108c6565b6060600060606000806000806101fc8a8a8a610b40565b92965090945092509050600082600381111561021a5761021a611148565b036103dd576000836001600160401b0381111561023957610239610f8f565b60405190808252806020026020018201604052801561027257816020015b61025f610f4e565b8152602001906001900390816102575790505b50905060005b848110156103cd576000868281518110610294576102946113c6565b6020026020010151815481106102ac576102ac6113c6565b60009182526020918290206040805160a0810182526004939093029091018054835260018101546001600160401b03811694840194909452600160401b9093046001600160a01b0316908201526002820154606082015260038201805491929160808401919061031b906113dc565b80601f0160208091040260200160405190810160405280929190818152602001828054610347906113dc565b80156103945780601f1061036957610100808354040283529160200191610394565b820191906000526020600020905b81548152906001019060200180831161037757829003601f168201915b5050505050815250508282815181106103af576103af6113c6565b602002602001018190525080806103c59061142c565b915050610278565b509650909450925061041d915050565b60408051600080825260208201909252906103cd565b6103fb610f4e565b8152602001906001900390816103f3579050509650909450925061041d915050565b93509350939050565b6000606060018460405161043a9190611445565b9081526040519081900360200190206001015460ff161561048a575050604080518082019091526014815273747261636520616c72656164792065786973747360601b6020820152600290610626565b60006040518060a00160405280428152602001876001600160401b03168152602001336001600160a01b031681526020016002866040516020016104ce9190611461565b60408051601f19818403018152908290526104e891611445565b602060405180830381855afa158015610505573d6000803e3d6000fd5b5050506040513d601f19601f820116820180604052508101906105289190611474565b81526020908101869052825460018181018555600094855293829020835160049092020190815590820151928101805460408401516001600160a01b0316600160401b026001600160e01b03199091166001600160401b03909516949094179390931790925560608101516002830155608081015190919060038201906105af90826114dc565b505050604051806040016040528060016000805490506105cf919061159b565b8152600160209091018190526040516105e9908790611445565b908152604080516020928190038301812084518155938301516001909401805460ff19169415159490941790935590820190526000808252925090505b935093915050565b610636610f4e565b6000606060018460405161064a9190611445565b9081526040519081900360200190206001015460ff16156107ae5760006001856040516106779190611445565b90815260200160405180910390206000015490506000818154811061069e5761069e6113c6565b600091825260208083206040805160a0810182526004949094029091018054845260018101546001600160401b03811693850193909352600160401b9092046001600160a01b03169083015260028101546060830152600381018054919392918491608084019161070e906113dc565b80601f016020809104026020016040519081016040528092919081815260200182805461073a906113dc565b80156107875780601f1061075c57610100808354040283529160200191610787565b820191906000526020600020905b81548152906001019060200180831161076a57829003601f168201915b5050505050815250509150604051806020016040528060008152509350935093505061080e565b50506040805160a081018252600080825260208083018290523383850152606083018290528351808201855291825260808301919091528251808401909352600f83526e1d1c9858d9481b9bdd08199bdd5b99608a1b9083015291506003905b9193909250565b6060600060606002600081805461082b906113dc565b80601f0160208091040260200160405190810160405280929190818152602001828054610857906113dc565b80156108a45780601f10610879576101008083540402835291602001916108a4565b820191906000526020600020905b81548152906001019060200180831161088757829003601f168201915b5050505050915060405180602001604052806000815250925092509250909192565b600080546060919082906001600160401b038616811115610ae4576001600160401b038516816108f687896115ae565b6001600160401b0316111561091b576109186001600160401b0388168361159b565b90505b6000816001600160401b0381111561093557610935610f8f565b60405190808252806020026020018201604052801561096e57816020015b61095b610f4e565b8152602001906001900390816109535790505b50905060005b82811015610ac2576000610991826001600160401b038c166115d5565b815481106109a1576109a16113c6565b60009182526020918290206040805160a0810182526004939093029091018054835260018101546001600160401b03811694840194909452600160401b9093046001600160a01b03169082015260028201546060820152600382018054919291608084019190610a10906113dc565b80601f0160208091040260200160405190810160405280929190818152602001828054610a3c906113dc565b8015610a895780601f10610a5e57610100808354040283529160200191610a89565b820191906000526020600020905b815481529060010190602001808311610a6c57829003601f168201915b505050505081525050828281518110610aa457610aa46113c6565b60200260200101819052508080610aba9061142c565b915050610974565b5080600060405180602001604052806000815250955095509550505050610b39565b6040805160008082526020820190925290610b15565b610b02610f4e565b815260200190600190039081610afa5790505b50600160405180606001604052806037815260200161172760379139935093509350505b9250925092565b6000805460609190819083906001600160401b038087169088161115610b97576040805160008082526080820183526028602083018181529293919260019290919061175e90860139945094509450945050610e08565b80866001600160401b031610610bde57604080516000808252608082018352602e602083018181529293919260019290919061178690860139945094509450945050610e08565b6000610bea88886115e8565b610bf59060016115ae565b6001600160401b031690506000610c338a60408051808201825260008082526020918201528151808301909252825182529182019181019190915290565b90506000826001600160401b03811115610c4f57610c4f610f8f565b604051908082528060200260200182016040528015610c78578160200160208202803683370190505b50905060006001600160401b038b165b84811015610dc3576000808281548110610ca457610ca46113c6565b90600052602060002090600402016003018054610cc0906113dc565b80601f0160208091040260200160405190810160405280929190818152602001828054610cec906113dc565b8015610d395780601f10610d0e57610100808354040283529160200191610d39565b820191906000526020600020905b815481529060010190602001808311610d1c57829003601f168201915b505050505090506000610d7c610d768360408051808201825260008082526020918201528151808301909252825182529182019181019190915290565b87610e11565b90508015610dae5782858581518110610d9757610d976113c6565b6020908102919091010152610dab8461142c565b93505b50508080610dbb9061142c565b915050610c88565b5060035460ff1615610de957815115610de9576000610de2828661159b565b8351038352505b6040805160208101909152600080825292995090975090955093505050505b93509350935093565b6020808301518351835192840151600093610e2f9291849190610e3a565b141590505b92915050565b600080858411610f415760208411610eed5760008415610e85576001610e6186602061159b565b610e6c906008611608565b610e77906002611703565b610e81919061159b565b1990505b8351811685610e9489896115d5565b610e9e919061159b565b805190935082165b818114610ed857878411610ec05787945050505050610f46565b83610eca8161170f565b945050828451169050610ea6565b610ee287856115d5565b945050505050610f46565b838320610efa858861159b565b610f0490876115d5565b91505b858210610f3f57848220808203610f2c57610f2286846115d5565b9350505050610f46565b610f3760018461159b565b925050610f07565b505b849150505b949350505050565b6040518060a001604052806000815260200160006001600160401b0316815260200160006001600160a01b0316815260200160008152602001606081525090565b634e487b7160e01b600052604160045260246000fd5b600082601f830112610fb657600080fd5b81356001600160401b0380821115610fd057610fd0610f8f565b604051601f8301601f19908116603f01168101908282118183101715610ff857610ff8610f8f565b8160405283815286602085880101111561101157600080fd5b836020870160208301376000602085830101528094505050505092915050565b80356001600160401b038116811461104857600080fd5b919050565b60008060006060848603121561106257600080fd5b83356001600160401b0381111561107857600080fd5b61108486828701610fa5565b93505061109360208501611031565b91506110a160408501611031565b90509250925092565b60005b838110156110c55781810151838201526020016110ad565b50506000910152565b600081518084526110e68160208601602086016110aa565b601f01601f19169290920160200192915050565b805182526001600160401b03602082015116602083015260018060a01b036040820151166040830152606081015160608301526000608082015160a06080850152610f4660a08501826110ce565b634e487b7160e01b600052602160045260246000fd5b6004811061117c57634e487b7160e01b600052602160045260246000fd5b9052565b6000606082016060835280865180835260808501915060808160051b8601019250602080890160005b838110156111d757607f198887030185526111c58683516110fa565b955093820193908201906001016111a9565b50506111e58187018961115e565b50505082810360408401526111fa81856110ce565b9695505050505050565b838152611214602082018461115e565b60606040820152600061122a60608301846110ce565b95945050505050565b60008060006060848603121561124857600080fd5b61125184611031565b925060208401356001600160401b038082111561126d57600080fd5b61127987838801610fa5565b9350604086013591508082111561128f57600080fd5b5061129c86828701610fa5565b9150509250925092565b6112b0818461115e565b604060208201526000610f4660408301846110ce565b6000602082840312156112d857600080fd5b81356001600160401b038111156112ee57600080fd5b610f4684828501610fa5565b60608152600061130d60608301866110fa565b61131a602084018661115e565b82810360408401526111fa81856110ce565b60006020828403121561133e57600080fd5b8135801515811461134e57600080fd5b9392505050565b6001600160a01b0384168152611214602082018461115e565b8315158152611214602082018461115e565b60608152600061130d60608301866110ce565b600080604083850312156113a657600080fd5b6113af83611031565b91506113bd60208401611031565b90509250929050565b634e487b7160e01b600052603260045260246000fd5b600181811c908216806113f057607f821691505b60208210810361141057634e487b7160e01b600052602260045260246000fd5b50919050565b634e487b7160e01b600052601160045260246000fd5b60006001820161143e5761143e611416565b5060010190565b600082516114578184602087016110aa565b9190910192915050565b60208152600061134e60208301846110ce565b60006020828403121561148657600080fd5b5051919050565b601f8211156114d757600081815260208120601f850160051c810160208610156114b45750805b601f850160051c820191505b818110156114d3578281556001016114c0565b5050505b505050565b81516001600160401b038111156114f5576114f5610f8f565b6115098161150384546113dc565b8461148d565b602080601f83116001811461153e57600084156115265750858301515b600019600386901b1c1916600185901b1785556114d3565b600085815260208120601f198616915b8281101561156d5788860151825594840194600190910190840161154e565b508582101561158b5787850151600019600388901b60f8161c191681555b5050505050600190811b01905550565b81810381811115610e3457610e34611416565b6001600160401b038181168382160190808211156115ce576115ce611416565b5092915050565b80820180821115610e3457610e34611416565b6001600160401b038281168282160390808211156115ce576115ce611416565b8082028115828204841417610e3457610e34611416565b600181815b8085111561165a57816000190482111561164057611640611416565b8085161561164d57918102915b93841c9390800290611624565b509250929050565b60008261167157506001610e34565b8161167e57506000610e34565b8160018114611694576002811461169e576116ba565b6001915050610e34565b60ff8411156116af576116af611416565b50506001821b610e34565b5060208310610133831016604e8410600b84101617156116dd575081810a610e34565b6116e7838361161f565b80600019048211156116fb576116fb611416565b029392505050565b600061134e8383611662565b60008161171e5761171e611416565b50600019019056fe737461727420706f736974696f6e2067726561746572206f7220657175616c20746f20746865206e756d626572206f6620747261636573737461727420706f736974696f6e2067726561746572207468616e20656e6420706f736974696f6e656e6420706f736974696f6e206869676865722f657175616c207468616e2f746f2074726163657320636f756e74a2646970667358221220e78c99dfe8354b4e6fb14c7adbfaaa2a6f4b86cedfac8799b89a7429f6544b7564736f6c63430008110033";

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
                new org.web3j.abi.datatypes.generated.Uint64(sP), 
                new org.web3j.abi.datatypes.generated.Uint64(eP)), 
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
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint64(startPos), 
                new org.web3j.abi.datatypes.generated.Uint64(maxNumElems)), 
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
