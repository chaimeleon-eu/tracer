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
    public static final String BINARY = "60c0604052601360809081527f436861696d656c656f6e5472616365725f56310000000000000000000000000060a0526001906200003e908262000112565b503480156200004c57600080fd5b50600280546001600160a81b031916336101000260ff1916179055620001de565b634e487b7160e01b600052604160045260246000fd5b600181811c908216806200009857607f821691505b602082108103620000b957634e487b7160e01b600052602260045260246000fd5b50919050565b601f8211156200010d57600081815260208120601f850160051c81016020861015620000e85750805b601f850160051c820191505b818110156200010957828155600101620000f4565b5050505b505050565b81516001600160401b038111156200012e576200012e6200006d565b62000146816200013f845462000083565b84620000bf565b602080601f8311600181146200017e5760008415620001655750858301515b600019600386901b1c1916600185901b17855562000109565b600085815260208120601f198616915b82811015620001af578886015182559484019460019091019084016200018e565b5085821015620001ce5787850151600019600388901b60f8161c191681555b5050505050600190811b01905550565b6117a180620001ee6000396000f3fe608060405234801561001057600080fd5b506004361061009e5760003560e01c80638fcce19f116100665780638fcce19f146101775780639cd1e220146101a45780639ed8177b146101c6578063b134a51e146101d9578063f5f5ba72146101ec57600080fd5b806308fcd674146100a35780633a2fa807146100cd57806350ca5428146100f55780637ddf390014610117578063893d20e814610149575b600080fd5b6100b66100b13660046110ac565b610203565b6040516100c4929190611182565b60405180910390f35b6100e66000805460408051602081019091528281529092565b6040516100c4939291906111a2565b6101086101033660046111d1565b61030d565b6040516100c49392919061121e565b6100b66101253660046112a2565b6002805460ff19169115159190911790556040805160208101909152600080825291565b60025460408051602081018252600080825291516100c49361010090046001600160a01b03169291906112cb565b6101956002546040805160208101909152600080825260ff90921692565b6040516100c4939291906112e4565b6101b76101b23660046111d1565b6104eb565b6040516100c4939291906112f6565b6101086101d43660046113b1565b610723565b6101b76101e73660046113b1565b610924565b6101f4610b72565b6040516100c4939291906113d3565b604080516080810182524281526001600160401b038085166020830190815233938301938452606083810186815260008054600181018255818052865160039091027f290decd9548b62a8d60345a988386fc84ba6bc95484008f6362f93160ef3e563810191825594517f290decd9548b62a8d60345a988386fc84ba6bc95484008f6362f93160ef3e5648601805499516001600160a01b0316600160401b026001600160e01b0319909a1691909716179790971790945551929490939290917f290decd9548b62a8d60345a988386fc84ba6bc95484008f6362f93160ef3e56501906102f0908261148e565b505060408051602081019091526000808252969095509350505050565b6060600060606000806000806103248a8a8a610c23565b92965090945092509050600082600181111561034257610342611104565b036104a8576000836001600160401b038111156103615761036161100a565b60405190808252806020026020018201604052801561039457816020015b606081526020019060019003908161037f5790505b50905060005b848110156104985760008682815181106103b6576103b661154d565b6020026020010151815481106103ce576103ce61154d565b906000526020600020906003020160020180546103ea90611405565b80601f016020809104026020016040519081016040528092919081815260200182805461041690611405565b80156104635780601f1061043857610100808354040283529160200191610463565b820191906000526020600020905b81548152906001019060200180831161044657829003601f168201915b505050505082828151811061047a5761047a61154d565b6020026020010181905250808061049090611579565b91505061039a565b50965090945092506104e2915050565b6040805160008082526020820190925290610498565b60608152602001906001900390816104be57905050965090945092506104e2915050565b93509350939050565b6060600060606000806000806105028a8a8a610c23565b92965090945092509050600082600181111561052057610520611104565b036104a8576000836001600160401b0381111561053f5761053f61100a565b60405190808252806020026020018201604052801561059057816020015b604080516080810182526000808252602080830182905292820152606080820152825260001990920191018161055d5790505b50905060005b848110156104985760008682815181106105b2576105b261154d565b6020026020010151815481106105ca576105ca61154d565b6000918252602091829020604080516080810182526003939093029091018054835260018101546001600160401b03811694840194909452600160401b9093046001600160a01b03169082015260028201805491929160608401919061062f90611405565b80601f016020809104026020016040519081016040528092919081815260200182805461065b90611405565b80156106a85780601f1061067d576101008083540402835291602001916106a8565b820191906000526020600020905b81548152906001019060200180831161068b57829003601f168201915b5050505050815250508282815181106106c3576106c361154d565b602002602001018190525080806106d990611579565b915050610596565b60408051608081018252600080825260208083018290529282015260608082015282526000199092019101816106e157905050965090945092506104e2915050565b60008054606091908290808610156108ce5784816001610743838a611592565b61074d91906115a5565b111561076b5761075d87836115a5565b610768906001611592565b90505b6000816001600160401b038111156107855761078561100a565b6040519080825280602002602001820160405280156107b857816020015b60608152602001906001900390816107a35790505b50905060005b828110156108ac5760006107d2828b611592565b815481106107e2576107e261154d565b906000526020600020906003020160020180546107fe90611405565b80601f016020809104026020016040519081016040528092919081815260200182805461082a90611405565b80156108775780601f1061084c57610100808354040283529160200191610877565b820191906000526020600020905b81548152906001019060200180831161085a57829003601f168201915b505050505082828151811061088e5761088e61154d565b602002602001018190525080806108a490611579565b9150506107be565b508060006040518060200160405280600081525095509550955050505061091d565b60408051600080825260208201909252906108f9565b60608152602001906001900390816108e45790505b5060016040518060600160405280603781526020016116df60379139935093509350505b9250925092565b60008054606091908290808610156108ce5784816001610944838a611592565b61094e91906115a5565b111561096c5761095e87836115a5565b610969906001611592565b90505b6000816001600160401b038111156109865761098661100a565b6040519080825280602002602001820160405280156109d757816020015b60408051608081018252600080825260208083018290529282015260608082015282526000199092019101816109a45790505b50905060005b828110156108ac5760006109f1828b611592565b81548110610a0157610a0161154d565b6000918252602091829020604080516080810182526003939093029091018054835260018101546001600160401b03811694840194909452600160401b9093046001600160a01b031690820152600282018054919291606084019190610a6690611405565b80601f0160208091040260200160405190810160405280929190818152602001828054610a9290611405565b8015610adf5780601f10610ab457610100808354040283529160200191610adf565b820191906000526020600020905b815481529060010190602001808311610ac257829003601f168201915b505050505081525050828281518110610afa57610afa61154d565b60200260200101819052508080610b1090611579565b9150506109dd565b6040805160808101825260008082526020808301829052928201526060808201528252600019909201910181610b185790505060016040518060600160405280603781526020016116df603791399350935093505061091d565b60606000606060016000818054610b8890611405565b80601f0160208091040260200160405190810160405280929190818152602001828054610bb490611405565b8015610c015780601f10610bd657610100808354040283529160200191610c01565b820191906000526020600020905b815481529060010190602001808311610be457829003601f168201915b5050505050915060405180602001604052806000815250925092509250909192565b60008054606091908190839085871115610c6e576040805160008082526080820183526028602083018181529293919260019290919061171690860139945094509450945050610ec4565b808610610cac57604080516000808252608082018352602e602083018181529293919260019290919061173e90860139945094509450945050610ec4565b6000610cb888886115a5565b610cc3906001611592565b90506000610cf88a60408051808201825260008082526020918201528151808301909252825182529182019181019190915290565b90506000826001600160401b03811115610d1457610d1461100a565b604051908082528060200260200182016040528015610d3d578160200160208202803683370190505b50905060008a5b84811015610e7f576000808281548110610d6057610d6061154d565b90600052602060002090600302016002018054610d7c90611405565b80601f0160208091040260200160405190810160405280929190818152602001828054610da890611405565b8015610df55780601f10610dca57610100808354040283529160200191610df5565b820191906000526020600020905b815481529060010190602001808311610dd857829003601f168201915b505050505090506000610e38610e328360408051808201825260008082526020918201528151808301909252825182529182019181019190915290565b87610ecd565b90508015610e6a5782858581518110610e5357610e5361154d565b6020908102919091010152610e6784611579565b93505b50508080610e7790611579565b915050610d44565b5060025460ff1615610ea557815115610ea5576000610e9e82866115a5565b8351038352505b6040805160208101909152600080825292995090975090955093505050505b93509350935093565b6020808301518351835192840151600093610eeb9291849190610ef6565b141590505b92915050565b600080858411610ffd5760208411610fa95760008415610f41576001610f1d8660206115a5565b610f289060086115b8565b610f339060026116bb565b610f3d91906115a5565b1990505b8351811685610f508989611592565b610f5a91906115a5565b805190935082165b818114610f9457878411610f7c5787945050505050611002565b83610f86816116c7565b945050828451169050610f62565b610f9e8785611592565b945050505050611002565b838320610fb685886115a5565b610fc09087611592565b91505b858210610ffb57848220808203610fe857610fde8684611592565b9350505050611002565b610ff36001846115a5565b925050610fc3565b505b849150505b949350505050565b634e487b7160e01b600052604160045260246000fd5b600082601f83011261103157600080fd5b81356001600160401b038082111561104b5761104b61100a565b604051601f8301601f19908116603f011681019082821181831017156110735761107361100a565b8160405283815286602085880101111561108c57600080fd5b836020870160208301376000602085830101528094505050505092915050565b600080604083850312156110bf57600080fd5b82356001600160401b0380821682146110d757600080fd5b909250602084013590808211156110ed57600080fd5b506110fa85828601611020565b9150509250929050565b634e487b7160e01b600052602160045260246000fd5b6002811061113857634e487b7160e01b600052602160045260246000fd5b9052565b6000815180845260005b8181101561116257602081850181015186830182015201611146565b506000602082860101526020601f19601f83011685010191505092915050565b61118c818461111a565b604060208201526000611002604083018461113c565b8381526111b2602082018461111a565b6060604082015260006111c8606083018461113c565b95945050505050565b6000806000606084860312156111e657600080fd5b83356001600160401b038111156111fc57600080fd5b61120886828701611020565b9660208601359650604090950135949350505050565b6000606082016060835280865180835260808501915060808160051b8601019250602080890160005b8381101561127557607f1988870301855261126386835161113c565b95509382019390820190600101611247565b50506112838187018961111a565b5050508281036040840152611298818561113c565b9695505050505050565b6000602082840312156112b457600080fd5b813580151581146112c457600080fd5b9392505050565b6001600160a01b03841681526111b2602082018461111a565b83151581526111b2602082018461111a565b60006060808301818452808751808352608092508286019150828160051b8701016020808b0160005b8481101561138257898403607f19018652815180518552838101516001600160401b0316848601526040808201516001600160a01b03169086015288015188850188905261136f8886018261113c565b968401969450509082019060010161131f565b50506113908189018b61111a565b5086810360408801526113a3818961113c565b9a9950505050505050505050565b600080604083850312156113c457600080fd5b50508035926020909101359150565b6060815260006113e6606083018661113c565b6113f3602084018661111a565b8281036040840152611298818561113c565b600181811c9082168061141957607f821691505b60208210810361143957634e487b7160e01b600052602260045260246000fd5b50919050565b601f82111561148957600081815260208120601f850160051c810160208610156114665750805b601f850160051c820191505b8181101561148557828155600101611472565b5050505b505050565b81516001600160401b038111156114a7576114a761100a565b6114bb816114b58454611405565b8461143f565b602080601f8311600181146114f057600084156114d85750858301515b600019600386901b1c1916600185901b178555611485565b600085815260208120601f198616915b8281101561151f57888601518255948401946001909101908401611500565b508582101561153d5787850151600019600388901b60f8161c191681555b5050505050600190811b01905550565b634e487b7160e01b600052603260045260246000fd5b634e487b7160e01b600052601160045260246000fd5b60006001820161158b5761158b611563565b5060010190565b80820180821115610ef057610ef0611563565b81810381811115610ef057610ef0611563565b60008160001904831182151516156115d2576115d2611563565b500290565b600181815b808511156116125781600019048211156115f8576115f8611563565b8085161561160557918102915b93841c93908002906115dc565b509250929050565b60008261162957506001610ef0565b8161163657506000610ef0565b816001811461164c576002811461165657611672565b6001915050610ef0565b60ff84111561166757611667611563565b50506001821b610ef0565b5060208310610133831016604e8410600b8410161715611695575081810a610ef0565b61169f83836115d7565b80600019048211156116b3576116b3611563565b029392505050565b60006112c4838361161a565b6000816116d6576116d6611563565b50600019019056fe737461727420706f736974696f6e2067726561746572206f7220657175616c20746f20746865206e756d626572206f6620747261636573737461727420706f736974696f6e2067726561746572207468616e20656e6420706f736974696f6e656e6420706f736974696f6e206869676865722f657175616c207468616e2f746f2074726163657320636f756e74a2646970667358221220c5b7965e7360fdf0c1d377f63019bcb5776997838daf95a957f94195ff41a3da64736f6c63430008100033";

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
