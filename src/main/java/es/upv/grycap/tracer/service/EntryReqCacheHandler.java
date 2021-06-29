package es.upv.grycap.tracer.service;

import org.springframework.stereotype.Service;

/**
 * The blockchain is async, therefore a request to create a transaction is not actually creating that transaction.
 * On the other hand, the tracer must keep all entries regardless of the status in the blockchain.
 * Furthermore the tracer must respond as soon as possible, regardless of the blockchain technology it uses behind the scene. 
 * If there's an error, and the entry is not accepted in the blockchain, manual intervention is needed.
 * This class handles the functionality needed for all entries that have been not accepted in the blockchain, or are being processed.
 * Each tracer blockchain node must have this cache to protect against data loss that can occur in case of distributed systems.
 * @author Andy S Alic (asalic)
 *
 */
@Service
public class EntryReqCacheHandler {

}
