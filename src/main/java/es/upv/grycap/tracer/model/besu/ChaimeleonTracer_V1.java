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
    public static final String BINARY = "60c0604052601360809081527f436861696d656c656f6e5472616365725f56310000000000000000000000000060a0526001906200003e908262000112565b503480156200004c57600080fd5b50600280546001600160a81b031916336101000260ff1916179055620001de565b634e487b7160e01b600052604160045260246000fd5b600181811c908216806200009857607f821691505b602082108103620000b957634e487b7160e01b600052602260045260246000fd5b50919050565b601f8211156200010d57600081815260208120601f850160051c81016020861015620000e85750805b601f850160051c820191505b818110156200010957828155600101620000f4565b5050505b505050565b81516001600160401b038111156200012e576200012e6200006d565b62000146816200013f845462000083565b84620000bf565b602080601f8311600181146200017e5760008415620001655750858301515b600019600386901b1c1916600185901b17855562000109565b600085815260208120601f198616915b82811015620001af578886015182559484019460019091019084016200018e565b5085821015620001ce5787850151600019600388901b60f8161c191681555b5050505050600190811b01905550565b61176b80620001ee6000396000f3fe608060405234801561001057600080fd5b506004361061009e5760003560e01c80638fcce19f116100665780638fcce19f146101775780639cd1e220146101a45780639ed8177b146101c6578063b134a51e146101d9578063f5f5ba72146101ec57600080fd5b806308fcd674146100a35780633a2fa807146100cd57806350ca5428146100f55780637ddf390014610117578063893d20e814610149575b600080fd5b6100b66100b136600461107e565b610203565b6040516100c4929190611154565b60405180910390f35b6100e66000805460408051602081019091528281529092565b6040516100c493929190611174565b6101086101033660046111a3565b61030d565b6040516100c4939291906111f0565b6100b6610125366004611274565b6002805460ff19169115159190911790556040805160208101909152600080825291565b60025460408051602081018252600080825291516100c49361010090046001600160a01b031692919061129d565b6101956002546040805160208101909152600080825260ff90921692565b6040516100c4939291906112b6565b6101b76101b23660046111a3565b6104eb565b6040516100c4939291906112c8565b6101086101d4366004611383565b610723565b6101b76101e7366004611383565b61090d565b6101f4610b44565b6040516100c4939291906113a5565b604080516080810182524281526001600160401b038085166020830190815233938301938452606083810186815260008054600181018255818052865160039091027f290decd9548b62a8d60345a988386fc84ba6bc95484008f6362f93160ef3e563810191825594517f290decd9548b62a8d60345a988386fc84ba6bc95484008f6362f93160ef3e5648601805499516001600160a01b0316600160401b026001600160e01b0319909a1691909716179790971790945551929490939290917f290decd9548b62a8d60345a988386fc84ba6bc95484008f6362f93160ef3e56501906102f09082611460565b505060408051602081019091526000808252969095509350505050565b6060600060606000806000806103248a8a8a610bf5565b929650909450925090506000826001811115610342576103426110d6565b036104a8576000836001600160401b0381111561036157610361610fdc565b60405190808252806020026020018201604052801561039457816020015b606081526020019060019003908161037f5790505b50905060005b848110156104985760008682815181106103b6576103b661151f565b6020026020010151815481106103ce576103ce61151f565b906000526020600020906003020160020180546103ea906113d7565b80601f0160208091040260200160405190810160405280929190818152602001828054610416906113d7565b80156104635780601f1061043857610100808354040283529160200191610463565b820191906000526020600020905b81548152906001019060200180831161044657829003601f168201915b505050505082828151811061047a5761047a61151f565b602002602001018190525080806104909061154b565b91505061039a565b50965090945092506104e2915050565b6040805160008082526020820190925290610498565b60608152602001906001900390816104be57905050965090945092506104e2915050565b93509350939050565b6060600060606000806000806105028a8a8a610bf5565b929650909450925090506000826001811115610520576105206110d6565b036104a8576000836001600160401b0381111561053f5761053f610fdc565b60405190808252806020026020018201604052801561059057816020015b604080516080810182526000808252602080830182905292820152606080820152825260001990920191018161055d5790505b50905060005b848110156104985760008682815181106105b2576105b261151f565b6020026020010151815481106105ca576105ca61151f565b6000918252602091829020604080516080810182526003939093029091018054835260018101546001600160401b03811694840194909452600160401b9093046001600160a01b03169082015260028201805491929160608401919061062f906113d7565b80601f016020809104026020016040519081016040528092919081815260200182805461065b906113d7565b80156106a85780601f1061067d576101008083540402835291602001916106a8565b820191906000526020600020905b81548152906001019060200180831161068b57829003601f168201915b5050505050815250508282815181106106c3576106c361151f565b602002602001018190525080806106d99061154b565b915050610596565b60408051608081018252600080825260208083018290529282015260608082015282526000199092019101816106e157905050965090945092506104e2915050565b60008054606091908290808610156108b75784816107418289611564565b1115610754576107518783611577565b90505b6000816001600160401b0381111561076e5761076e610fdc565b6040519080825280602002602001820160405280156107a157816020015b606081526020019060019003908161078c5790505b50905060005b828110156108955760006107bb828b611564565b815481106107cb576107cb61151f565b906000526020600020906003020160020180546107e7906113d7565b80601f0160208091040260200160405190810160405280929190818152602001828054610813906113d7565b80156108605780601f1061083557610100808354040283529160200191610860565b820191906000526020600020905b81548152906001019060200180831161084357829003601f168201915b50505050508282815181106108775761087761151f565b6020026020010181905250808061088d9061154b565b9150506107a7565b5080600060405180602001604052806000815250955095509550505050610906565b60408051600080825260208201909252906108e2565b60608152602001906001900390816108cd5790505b5060016040518060600160405280603781526020016116a960379139935093509350505b9250925092565b60008054606091908290808610156108b757848161092b8289611564565b111561093e5761093b8783611577565b90505b6000816001600160401b0381111561095857610958610fdc565b6040519080825280602002602001820160405280156109a957816020015b60408051608081018252600080825260208083018290529282015260608082015282526000199092019101816109765790505b50905060005b828110156108955760006109c3828b611564565b815481106109d3576109d361151f565b6000918252602091829020604080516080810182526003939093029091018054835260018101546001600160401b03811694840194909452600160401b9093046001600160a01b031690820152600282018054919291606084019190610a38906113d7565b80601f0160208091040260200160405190810160405280929190818152602001828054610a64906113d7565b8015610ab15780601f10610a8657610100808354040283529160200191610ab1565b820191906000526020600020905b815481529060010190602001808311610a9457829003601f168201915b505050505081525050828281518110610acc57610acc61151f565b60200260200101819052508080610ae29061154b565b9150506109af565b6040805160808101825260008082526020808301829052928201526060808201528252600019909201910181610aea5790505060016040518060600160405280603781526020016116a96037913993509350935050610906565b60606000606060016000818054610b5a906113d7565b80601f0160208091040260200160405190810160405280929190818152602001828054610b86906113d7565b8015610bd35780601f10610ba857610100808354040283529160200191610bd3565b820191906000526020600020905b815481529060010190602001808311610bb657829003601f168201915b5050505050915060405180602001604052806000815250925092509250909192565b60008054606091908190839085871115610c4057604080516000808252608082018352602860208301818152929391926001929091906116e090860139945094509450945050610e96565b808610610c7e57604080516000808252608082018352602e602083018181529293919260019290919061170890860139945094509450945050610e96565b6000610c8a8888611577565b610c95906001611564565b90506000610cca8a60408051808201825260008082526020918201528151808301909252825182529182019181019190915290565b90506000826001600160401b03811115610ce657610ce6610fdc565b604051908082528060200260200182016040528015610d0f578160200160208202803683370190505b50905060008a5b84811015610e51576000808281548110610d3257610d3261151f565b90600052602060002090600302016002018054610d4e906113d7565b80601f0160208091040260200160405190810160405280929190818152602001828054610d7a906113d7565b8015610dc75780601f10610d9c57610100808354040283529160200191610dc7565b820191906000526020600020905b815481529060010190602001808311610daa57829003601f168201915b505050505090506000610e0a610e048360408051808201825260008082526020918201528151808301909252825182529182019181019190915290565b87610e9f565b90508015610e3c5782858581518110610e2557610e2561151f565b6020908102919091010152610e398461154b565b93505b50508080610e499061154b565b915050610d16565b5060025460ff1615610e7757815115610e77576000610e708286611577565b8351038352505b6040805160208101909152600080825292995090975090955093505050505b93509350935093565b6020808301518351835192840151600093610ebd9291849190610ec8565b141590505b92915050565b600080858411610fcf5760208411610f7b5760008415610f13576001610eef866020611577565b610efa90600861158a565b610f05906002611685565b610f0f9190611577565b1990505b8351811685610f228989611564565b610f2c9190611577565b805190935082165b818114610f6657878411610f4e5787945050505050610fd4565b83610f5881611691565b945050828451169050610f34565b610f708785611564565b945050505050610fd4565b838320610f888588611577565b610f929087611564565b91505b858210610fcd57848220808203610fba57610fb08684611564565b9350505050610fd4565b610fc5600184611577565b925050610f95565b505b849150505b949350505050565b634e487b7160e01b600052604160045260246000fd5b600082601f83011261100357600080fd5b81356001600160401b038082111561101d5761101d610fdc565b604051601f8301601f19908116603f0116810190828211818310171561104557611045610fdc565b8160405283815286602085880101111561105e57600080fd5b836020870160208301376000602085830101528094505050505092915050565b6000806040838503121561109157600080fd5b82356001600160401b0380821682146110a957600080fd5b909250602084013590808211156110bf57600080fd5b506110cc85828601610ff2565b9150509250929050565b634e487b7160e01b600052602160045260246000fd5b6002811061110a57634e487b7160e01b600052602160045260246000fd5b9052565b6000815180845260005b8181101561113457602081850181015186830182015201611118565b506000602082860101526020601f19601f83011685010191505092915050565b61115e81846110ec565b604060208201526000610fd4604083018461110e565b83815261118460208201846110ec565b60606040820152600061119a606083018461110e565b95945050505050565b6000806000606084860312156111b857600080fd5b83356001600160401b038111156111ce57600080fd5b6111da86828701610ff2565b9660208601359650604090950135949350505050565b6000606082016060835280865180835260808501915060808160051b8601019250602080890160005b8381101561124757607f1988870301855261123586835161110e565b95509382019390820190600101611219565b5050611255818701896110ec565b505050828103604084015261126a818561110e565b9695505050505050565b60006020828403121561128657600080fd5b8135801515811461129657600080fd5b9392505050565b6001600160a01b038416815261118460208201846110ec565b831515815261118460208201846110ec565b60006060808301818452808751808352608092508286019150828160051b8701016020808b0160005b8481101561135457898403607f19018652815180518552838101516001600160401b0316848601526040808201516001600160a01b0316908601528801518885018890526113418886018261110e565b96840196945050908201906001016112f1565b50506113628189018b6110ec565b508681036040880152611375818961110e565b9a9950505050505050505050565b6000806040838503121561139657600080fd5b50508035926020909101359150565b6060815260006113b8606083018661110e565b6113c560208401866110ec565b828103604084015261126a818561110e565b600181811c908216806113eb57607f821691505b60208210810361140b57634e487b7160e01b600052602260045260246000fd5b50919050565b601f82111561145b57600081815260208120601f850160051c810160208610156114385750805b601f850160051c820191505b8181101561145757828155600101611444565b5050505b505050565b81516001600160401b0381111561147957611479610fdc565b61148d8161148784546113d7565b84611411565b602080601f8311600181146114c257600084156114aa5750858301515b600019600386901b1c1916600185901b178555611457565b600085815260208120601f198616915b828110156114f1578886015182559484019460019091019084016114d2565b508582101561150f5787850151600019600388901b60f8161c191681555b5050505050600190811b01905550565b634e487b7160e01b600052603260045260246000fd5b634e487b7160e01b600052601160045260246000fd5b60006001820161155d5761155d611535565b5060010190565b80820180821115610ec257610ec2611535565b81810381811115610ec257610ec2611535565b8082028115828204841417610ec257610ec2611535565b600181815b808511156115dc5781600019048211156115c2576115c2611535565b808516156115cf57918102915b93841c93908002906115a6565b509250929050565b6000826115f357506001610ec2565b8161160057506000610ec2565b816001811461161657600281146116205761163c565b6001915050610ec2565b60ff84111561163157611631611535565b50506001821b610ec2565b5060208310610133831016604e8410600b841016171561165f575081810a610ec2565b61166983836115a1565b806000190482111561167d5761167d611535565b029392505050565b600061129683836115e4565b6000816116a0576116a0611535565b50600019019056fe737461727420706f736974696f6e2067726561746572206f7220657175616c20746f20746865206e756d626572206f6620747261636573737461727420706f736974696f6e2067726561746572207468616e20656e6420706f736974696f6e656e6420706f736974696f6e206869676865722f657175616c207468616e2f746f2074726163657320636f756e74a264697066735822122000306876a58a667f1e01d634f80d953e05c330a4d8412972836fcc499c4777bc64736f6c63430008110033";

    public static final String FUNC_ADDTRACE = "addTrace";

    public static final String FUNC_GETCONTRACTNAME = "getContractName";

    public static final String FUNC_GETFULLTRACESBYVALUE = "getFullTracesByValue";

    public static final String FUNC_GETFULLTRACESSUBARRAY = "getFullTracesSubarray";

    public static final String FUNC_GETOWNER = "getOwner";

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

    public RemoteFunctionCall<TransactionReceipt> addTrace(BigInteger eTime, String trace) {
        final Function function = new Function(
                FUNC_ADDTRACE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint64(eTime), 
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

//    public RemoteFunctionCall<Tuple3<List<TraceEntry>, BigInteger, String>> getFullTracesByValue(String value, BigInteger sP, BigInteger eP) {
//        final Function function = new Function(FUNC_GETFULLTRACESBYVALUE, 
//                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(value), 
//                new org.web3j.abi.datatypes.generated.Uint256(sP), 
//                new org.web3j.abi.datatypes.generated.Uint256(eP)), 
//                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<TraceEntry>>() {}, new TypeReference<Uint8>() {}, new TypeReference<Utf8String>() {}));
//        return new RemoteFunctionCall<Tuple3<List<TraceEntry>, BigInteger, String>>(function,
//                new Callable<Tuple3<List<TraceEntry>, BigInteger, String>>() {
//                    @Override
//                    public Tuple3<List<TraceEntry>, BigInteger, String> call() throws Exception {
//                        List<Type> results = executeCallMultipleValueReturn(function);
//                        return new Tuple3<List<TraceEntry>, BigInteger, String>(
//                                convertToNative((List<TraceEntry>) results.get(0).getValue()), 
//                                (BigInteger) results.get(1).getValue(), 
//                                (String) results.get(2).getValue());
//                    }
//                });
//    }
//
//    public RemoteFunctionCall<Tuple3<List<TraceEntry>, BigInteger, String>> getFullTracesSubarray(BigInteger startPos, BigInteger maxNumElems) {
//        final Function function = new Function(FUNC_GETFULLTRACESSUBARRAY, 
//                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(startPos), 
//                new org.web3j.abi.datatypes.generated.Uint256(maxNumElems)), 
//                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<TraceEntry>>() {}, new TypeReference<Uint8>() {}, new TypeReference<Utf8String>() {}));
//        return new RemoteFunctionCall<Tuple3<List<TraceEntry>, BigInteger, String>>(function,
//                new Callable<Tuple3<List<TraceEntry>, BigInteger, String>>() {
//                    @Override
//                    public Tuple3<List<TraceEntry>, BigInteger, String> call() throws Exception {
//                        List<Type> results = executeCallMultipleValueReturn(function);
//                        return new Tuple3<List<TraceEntry>, BigInteger, String>(
//                                convertToNative((List<TraceEntry>) results.get(0).getValue()), 
//                                (BigInteger) results.get(1).getValue(), 
//                                (String) results.get(2).getValue());
//                    }
//                });
//    }

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

    public RemoteFunctionCall<Tuple3<List<String>, BigInteger, String>> getTracesByValue(String value, BigInteger sP, BigInteger eP) {
        final Function function = new Function(FUNC_GETTRACESBYVALUE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(value), 
                new org.web3j.abi.datatypes.generated.Uint256(sP), 
                new org.web3j.abi.datatypes.generated.Uint256(eP)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Utf8String>>() {}, new TypeReference<Uint8>() {}, new TypeReference<Utf8String>() {}));
        return new RemoteFunctionCall<Tuple3<List<String>, BigInteger, String>>(function,
                new Callable<Tuple3<List<String>, BigInteger, String>>() {
                    @Override
                    public Tuple3<List<String>, BigInteger, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<List<String>, BigInteger, String>(
                                convertToNative((List<Utf8String>) results.get(0).getValue()), 
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

    public RemoteFunctionCall<Tuple3<List<String>, BigInteger, String>> getTracesSubarray(BigInteger startPos, BigInteger maxNumElems) {
        final Function function = new Function(FUNC_GETTRACESSUBARRAY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(startPos), 
                new org.web3j.abi.datatypes.generated.Uint256(maxNumElems)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Utf8String>>() {}, new TypeReference<Uint8>() {}, new TypeReference<Utf8String>() {}));
        return new RemoteFunctionCall<Tuple3<List<String>, BigInteger, String>>(function,
                new Callable<Tuple3<List<String>, BigInteger, String>>() {
                    @Override
                    public Tuple3<List<String>, BigInteger, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<List<String>, BigInteger, String>(
                                convertToNative((List<Utf8String>) results.get(0).getValue()), 
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
}
