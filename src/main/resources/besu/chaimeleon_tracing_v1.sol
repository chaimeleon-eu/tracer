pragma solidity >=0.7.0 <0.8.0;

contract ChaimeleonTracingV1 {
    TraceEntry[] private traces;
    
    mapping(string => uint128[]) private tracesPosByDatasetId;
    mapping(string => uint128[]) private tracesPosByUserId;
    
   // uint128 tracesCount;
    
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
    
    constructor() {
        owner = msg.sender;
    }
    
    function addTrace(string memory trace) public onlyOwner{
        traces.push(TraceEntry(block.timestamp, block.timestamp, trace));
        //uint256 len = traces.push();
        //traces[len-1] = t;
        //tracesCount += 1; 
    }
    
    function getTracesCount() public view returns (uint256) {
        return traces.length;
    }
    
    function getTracesByDatasetId(string memory datasetId) public view returns (string[]) {
        uint128[] poss = tracesPosByDatasetId[datasetId];
        string[] memory result = new string[]();
        for (uint i=0; i<poss.length; i++) {
            result.push(traces[poss[i]].trace);
        }
        return result;
    }
    

}