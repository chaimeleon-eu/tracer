pragma solidity >=0.8.15;
pragma abicoder v2;

error ParamsError(string msg);

contract ChaimeleonTracer_V1 {
    
    TraceEntry[] internal traces;
    
    string internal name = "ChaimeleonTracer_V1";
    
    /**
     * Resize the array returned by getTracesPossByValue
     */
    bool internal rTPBV;
    
   // mapping(string => uint128[]) private tracesPosByDatasetId;
    //mapping(string => uint128[]) private tracesPosByUserId;
    
   // uint128 tracesCount;
    
    address internal owner;
    
    modifier onlyOwner() {
        require(msg.sender == owner);
        _;
    }
    
    struct FoundTracesPoss {
        
        uint[] poss;
        
        uint rc;
        
    }
    
    struct TraceEntry {
        /**
         * Internal time of the block where this trace entry has been stored in, represented in seconds since the epoch of 1970-01-01T00:00:00Z 
         */
        uint256 bTime;
        /**
         * Time received with the add trace call as the number of milliseconds from the epoch of 1970-01-01T00:00:00Z. 
         */
        uint64 eTime;
        /**
         * The address of the external entity that requested the storing of this trace
         */
        address sender;
        /**
         * The JSON string representation of the trace
         */
        string trace;
    }
    
    struct slice {
        uint _len;
        uint _ptr;
    }
    
    constructor() {
        owner = msg.sender;
        rTPBV = false;
    }
    
//     Returns the memory address of the first byte after the last occurrence of
//     `needle` in `self`, or the address of `self` if not found.
    function rfindPtr(uint selflen, uint selfptr, uint needlelen, uint needleptr) private pure returns (uint) {
        uint ptr;

        if (needlelen <= selflen) {
            if (needlelen <= 32) {
                bytes32 mask;
                if (needlelen > 0) {
                    mask = bytes32(~(2 ** (8 * (32 - needlelen)) - 1));
                }

                bytes32 needledata;
                assembly { needledata := and(mload(needleptr), mask) }

                ptr = selfptr + selflen - needlelen;
                bytes32 ptrdata;
                assembly { ptrdata := and(mload(ptr), mask) }

                while (ptrdata != needledata) {
                    if (ptr <= selfptr)
                        return selfptr;
                    ptr--;
                    assembly { ptrdata := and(mload(ptr), mask) }
                }
                return ptr + needlelen;
            } else {
                // For long needles, use hashing
                bytes32 hash;
                assembly { hash := keccak256(needleptr, needlelen) }
                ptr = selfptr + (selflen - needlelen);
                while (ptr >= selfptr) {
                    bytes32 testHash;
                    assembly { testHash := keccak256(ptr, needlelen) }
                    if (hash == testHash)
                        return ptr + needlelen;
                    ptr -= 1;
                }
            }
        }
        return selfptr;
    }
    
    /*
     * @dev Returns True if `self` contains `needle`.
     * @param self The slice to search.
     * @param needle The text to search for in `self`.
     * @return True if `needle` is found in `self`, false otherwise.
     */
    function contains(slice memory self, slice memory needle) internal pure returns (bool) {
        return rfindPtr(self._len, self._ptr, needle._len, needle._ptr) != self._ptr;
    }
    
        /*
     * @dev Returns a slice containing the entire string.
     * @param self The string to make a slice from.
     * @return A newly allocated slice containing the entire string.
     */
    function toSlice(string memory self) internal pure returns (slice memory) {
        uint ptr;
        assembly {
            ptr := add(self, 0x20)
        }
        return slice(bytes(self).length, ptr);
    }
    

    
    function addTrace(uint64 eTime, string memory trace) public {//onlyOwner{
        traces.push(TraceEntry(block.timestamp, eTime, msg.sender, trace));
        //uint256 len = traces.push();
        //traces[len-1] = t;
        //tracesCount += 1; 
    }
    
    function getTracesCount() public view returns (uint256) {
        return traces.length;
    }
    
    function getTracesPossByValue(string memory value, uint sP, uint eP)  public view returns (uint[] memory, uint) {
        uint len = traces.length;
        if (sP > eP) {
            revert ParamsError("start position greater than end position");
        }
        if (eP >= len) {
            revert ParamsError("end position higher/equal than/to traces count");            
        }
        uint n = eP - sP + 1;
        slice memory vS = toSlice(value);
        uint[] memory ps = new uint[](n);
        uint e = 0;
        for (uint i=sP; i<n; i++) {
            string memory trace = traces[i].trace;
            bool exists = contains(toSlice(trace), vS);
            if (exists) {
                ps[e] = i;
                ++e;
            }
        }
        if (rTPBV) {
            if (ps.length > 0) {
                uint redSize = n - e;
                assembly { mstore(ps, sub(mload(ps),  redSize)) }
            }
        }
        return (ps, e);//FoundTracesPoss(ps, e);
    }
    
    function getTracesByValue(string memory value, uint sP, uint eP)  public view returns (string[] memory) {        
        //FoundTracesPoss fTP
        uint[] memory poss;
        uint rc;
        (poss, rc) = getTracesPossByValue(value, sP, eP);        
        string[] memory rs = new string[](rc);
        for (uint i=0; i<rc; i++) {
            rs[i] = traces[poss[i]].trace;
        }
        return rs;
    }
    
    function getFullTracesByValue(string memory value, uint sP, uint eP)  public view returns (TraceEntry[] memory) {        
        //FoundTracesPoss fTP 
        uint[] memory poss;
        uint rc;
        (poss, rc) = getTracesPossByValue(value, sP, eP);        
        TraceEntry[] memory rs = new TraceEntry[](rc);
        for (uint i=0; i<rc; i++) {
            rs[i] = traces[poss[i]];//.trace;
        }
        return rs;
    }
    
    function getTracesSubarray(uint startPos, uint maxNumElems) public view returns (string[] memory) {       
        uint len = traces.length;
        if (startPos < len) {
            uint numElems = maxNumElems;
            if ((startPos + maxNumElems - 1) > len) {
                numElems = len - startPos + 1;
            }
            //TraceEntry[] memory r = new TraceEntry[](numElems);
            string[] memory r = new string[](numElems);
            for (uint i=0; i<numElems; i++) {
                r[i] = traces[startPos + i].trace;
            }
            return r;
        } else {
            //return new TraceEntry[](0);
            return new string[](0);
        }
    }
    
    function getContractName() public view returns(string memory){
         return name;
    }
    
    function getOwner() public view returns(address){
         return owner;
    }
    
    function getTracesPossByValueResize() public view returns(bool){
        return rTPBV;
    }
    
    function setTracesPossByValueResize(bool newVal) public {
        rTPBV = newVal;
    }
    
//    function getTracesByDatasetId(string memory datasetId) public view returns (string[] memory) {
//        uint128[] memory poss = tracesPosByDatasetId[datasetId];
//        string[] memory result = new string[](poss.length);
//        for (uint128 i=0; i<poss.length; i++) {
//            result[i] = traces[poss[i]].trace;
//        }
//        return result;
//    }
    

}