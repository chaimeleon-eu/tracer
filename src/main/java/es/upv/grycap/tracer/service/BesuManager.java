package es.upv.grycap.tracer.service;

import java.util.List;
import java.io.IOException;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import es.upv.grycap.tracer.model.BesuProperties;
import es.upv.grycap.tracer.model.BlockchainProperties;
import es.upv.grycap.tracer.model.FilterParams;
import es.upv.grycap.tracer.model.dto.ReqDTO;
import es.upv.grycap.tracer.model.dto.bigchaindb.Transaction;
import es.upv.grycap.tracer.model.trace.TraceBase;
import es.upv.grycap.tracer.model.trace.v1.Trace;

@Service
public class BesuManager implements BlockchainManager {
	
	protected final BesuProperties props;
	
	@Autowired
	public BesuManager(@Autowired final BesuProperties props) {
		this.props = props;
	}

	@Override
	public void addTrace(ReqDTO entry, String callerUserId) {
		 Web3j web3 = Web3j.build(new HttpService(props.getUrl()));
		
	}

	@Override
	public Transaction<?, ?, ?> getTransactionById(String transactionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TraceBase> getTraces(FilterParams filterParams) {
		return List.of();
	}

	@Override
	public BlockchainProperties getBlockchainProperties() {
		return props;
	}

}
