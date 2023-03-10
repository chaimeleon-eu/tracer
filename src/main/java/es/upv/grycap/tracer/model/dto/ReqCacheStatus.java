package es.upv.grycap.tracer.model.dto;

/**
 * The status of a request received by the server and cached until accepted in the blockchain
 * @author Andy S Alic
 *
 */
public enum ReqCacheStatus {
	
	/**
	 * Waiting to be submitted to a blockchain
	 */
	WAITING, 
	/**
	 * error before submission
	 */
	ERROR, 
	/** 
	 * Tried to contact the blockchain, but it is not available/reachable
	 */
	BLOCKCHAIN_UNAVAILABLE, 
	/** 
	 * Request submitted, waiting for blockchain response
	 */
	BLOCKCHAIN_WAITING, 
	/**
	 * Error when trying to submit transaction
	 */
	BLOCKCHAIN_SUBMISSION_ERROR,
	/**
	 * Error thrown by blockchain
	 */
	BLOCKCHAIN_ERROR,
	/**
	 * not found on the blockchain
	 */
	BLOCKCHAIN_NOT_FOUND,
	/**
	 * Error thrown by blockchain
	 */
	BLOCKCHAIN_SUCCESS

}
