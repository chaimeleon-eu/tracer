pragma solidity *0.8;

contract ChaimeleonTracingV1 {
    TraceEntry[] traces;
    mapping(string => uint128[]) tracesIdsByDatasetId;
    mapping(string => uint128) traceIdByUserId;
    
    uint128 tracesCount;
    
    address owner;
    
    modifier onlyOwner() {
        require(msg.sender == owner);
        _;
    }
    
    struct TraceEntry {
        uint256 _id;
        uint256 _timestamp;
        string trace;
    }
    
    function constructor() public {
        owner = msg.sender;
    }
    
    function addTrace(string memory trace) public onlyOwner{
        traces.push(trace);
        tracesCount += 1; 
    }
    
    function getTracesCount() public (return uint256) {
        return tracesCount;
    }
    

}