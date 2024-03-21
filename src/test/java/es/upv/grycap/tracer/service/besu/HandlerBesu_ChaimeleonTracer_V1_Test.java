package es.upv.grycap.tracer.service.besu;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tx.gas.ContractGasProvider;

import es.upv.grycap.tracer.model.TraceCacheOpResult;
import es.upv.grycap.tracer.model.besu.BesuProperties;
import es.upv.grycap.tracer.model.besu.ChaimeleonTracer_V1;
import es.upv.grycap.tracer.model.besu.ChaimeleonTracer_V1.TraceEntry;
import es.upv.grycap.tracer.model.dto.BlockchainType;
import es.upv.grycap.tracer.model.dto.ReqCacheStatus;
import es.upv.grycap.tracer.model.trace.TraceBase;
import es.upv.grycap.tracer.service.TimeManager;

public class HandlerBesu_ChaimeleonTracer_V1_Test  {
    
    @Test
    public void test_add_trace() throws Exception {
//        final String tHash = "hash";
//        ChaimeleonTracer_V1 c = Mockito.mock(ChaimeleonTracer_V1.class);
//        RemoteFunctionCall<TransactionReceipt> addCall = Mockito.mock(RemoteFunctionCall.class);
//        TransactionReceipt receipt = Mockito.mock(TransactionReceipt.class);
//        when(receipt.getTransactionHash()).thenReturn(tHash);
//        when(receipt.isStatusOK()).thenReturn(true);
//        when(addCall.send()).thenReturn(receipt);
//        when(c.addTrace(any(BigInteger.class), anyString(), anyString())).thenReturn(addCall);
//        BesuProperties p = new BesuProperties();
//        p.setContracts(List.of());
//        HandlerChaimeleonTracer_V1 h = new HandlerChaimeleonTracer_V1(BlockchainType.BESU_PRIVATE, null, p, null, null) {
//            
//            @Override
//            protected ChaimeleonTracer_V1 loadContract(String address) {
//                return c;
//            }
//        };
//        TraceBase tb = Mockito.mock(TraceBase.class);
//        TraceCacheOpResult res = h.submitTrace(tb, "caller user id");
//        assertTrue(res.getStatus() == ReqCacheStatus.BLOCKCHAIN_SUCCESS);
//        assertTrue(res.getTransactionId() == tHash);
    }
    

}
