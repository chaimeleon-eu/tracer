package es.upv.grycap.tracer.service;

import java.util.List;
import java.io.IOException;
import java.math.BigInteger;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import es.upv.grycap.tracer.model.BesuProperties;
import es.upv.grycap.tracer.model.BlockchainProperties;
import es.upv.grycap.tracer.model.FilterParams;
import es.upv.grycap.tracer.model.TraceCacheOpResult;
import es.upv.grycap.tracer.model.dto.BlockchainType;
import es.upv.grycap.tracer.model.dto.ITransaction;
import es.upv.grycap.tracer.model.dto.ReqDTO;
import es.upv.grycap.tracer.model.trace.TraceBase;
import es.upv.grycap.tracer.model.trace.v1.Trace;

@Service
public class BesuManager implements BlockchainManager {
	
	protected final BesuProperties props;
	
	public ITransaction generateTransaction(final ReqDTO entry, String callerUserId) {
		return null;	
	}
	
	@Override
	public TraceCacheOpResult submitTransaction(final ITransaction transaction) {
//		 Web3j web3 = Web3j.build(new HttpService(props.getUrl()));
//		 EthGetTransactionCount ethGetTransactionCount = web3.ethGetTransactionCount(
//	             address, DefaultBlockParameterName.LATEST).sendAsync().get();
//
//	     BigInteger nonce = ethGetTransactionCount.getTransactionCount();
//		 Transaction transaction = Transaction.createContractTransaction(
//	              <from address>,
//	              <nonce>,
//	              BigInteger.valueOf(<gas price>),  // we use default gas limit
//	              "0x...<smart contract code to execute>"
//	      );
//
//	      org.web3j.protocol.core.methods.response.EthSendTransaction
//	              transactionResponse = parity.ethSendTransaction(ethSendTransaction)
//	              .send();
//
//	      String transactionHash = transactionResponse.getTransactionHash();
			return null;
	}
	
	@Autowired
	public BesuManager(@Autowired final BesuProperties props) {
		this.props = props;
	}


//	@Override
//	public Transaction<?, ?, ?> getTransactionById(String transactionId) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public List<TraceBase> getTraces(FilterParams filterParams) {
		return List.of();
	}

	@Override
	public BlockchainProperties getBlockchainProperties() {
		return props;
	}

	@Override
	public TraceCacheOpResult getTransactionStatusById(String transactionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BlockchainType getType() {
		return BlockchainType.BESU_PRIVATE;
	}

}
