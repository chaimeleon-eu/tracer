package es.upv.grycap.tracer.service;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.upv.grycap.tracer.model.dto.BlockchainType;
import es.upv.grycap.tracer.model.dto.ReqDTO;

public class TraceSubmitter implements Runnable {
	
	protected final Map<BlockchainType, BlockchainManager> managers;
	protected  ReqDTO request;
	protected String requestBody;
	
	public TraceSubmitter(final Map<BlockchainType, BlockchainManager> managers, String requestBody) {
		this.managers = managers;
		request = null;
		this.requestBody = requestBody;
	}
	
	public TraceSubmitter(final Map<BlockchainType, BlockchainManager> managers, ReqDTO request) {
		this.managers = managers;
		this.request = request;
		this.requestBody = null;
	}

	@Override
	public void run() {
		if (request == null) {
			ObjectMapper om = new ObjectMapper();
		}
		if (request.getBlockchains() == null) {
			managers.values().forEach(mgr -> mgr.addTrace(request, callerUserId));
		} else {
			request.getBlockchains().forEach(bc -> managers.get(bc).addTrace(request, callerUserId));
		}
		
	}

}
