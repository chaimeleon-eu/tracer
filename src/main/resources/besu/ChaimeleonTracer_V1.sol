pragma solidity >=0.8.17;
pragma abicoder v2;

error ParamsError(string msg);

contract ChaimeleonTracer_V1 {
    
    /**
     * Maintains the codes returned by the contract.
     * Starts with 0, always leave SUCCESS as the first item
     */
    enum ReturnCode { SUCCESS, PARAMS_ERROR, DATA_EXISTS_ERROR, NOT_FOUND } 
    
    TraceEntry[] internal traces;
    mapping(string => TraceByIdPos) internal tracesById;
    
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
         * The SHA256 hash of the whole string representation of the trace
        **/
        uint256 hash;
        /**
         * The JSON string representation of the trace
         */
        string trace;
    }
    
    struct slice {
        uint _len;
        uint _ptr;
    }
    
    struct TraceByIdPos {
         uint256 pos;
         bool isValue;
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
    

    
    function addTrace(uint64 eTime, string memory traceId, string memory trace) public 
            returns (ReturnCode, string memory) {//onlyOwner{
        if (tracesById[traceId].isValue) {
            return (ReturnCode.DATA_EXISTS_ERROR, "trace already exists");
        } else {
            traces.push(TraceEntry(block.timestamp, eTime, msg.sender, uint256(sha256(abi.encode(trace))), trace));
            tracesById[traceId] = TraceByIdPos(traces.length - 1, true);
            return (ReturnCode.SUCCESS, "");
        }
        //uint256 len = traces.push();
        //traces[len-1] = t;
        //tracesCount += 1; 
    }
    
    function getTracesCount() public view 
            returns (uint256, ReturnCode, string memory) {
        return (traces.length, ReturnCode.SUCCESS, "");
    }
    
    // function getTraceById(string memory traceId) public view returns (string memory, ReturnCode, string memory) {
    //     if (tracesById[traceId].isValue) {
    //         uint256 pos = tracesById[traceId].pos;
    //         return (traces[pos].trace, ReturnCode.SUCCESS, "");
    //     } else {
    //         return ("", ReturnCode.NOT_FOUND, "trace not found");
    //     }
    // }
    
    function getTracesPossByValue(string memory value, uint64 sP, uint64 eP)  internal view 
            returns (uint256[] memory, uint256, ReturnCode, string memory) {
        uint256 len = traces.length;
        if (sP > eP) {
            //revert ParamsError("start position greater than end position");
            return (new uint256[](0), 0, ReturnCode.PARAMS_ERROR, "start position greater than end position");
        }
        if (eP >= len) {
            //revert ParamsError("end position higher/equal than/to traces count");
            return (new uint256[](0), 0, ReturnCode.PARAMS_ERROR, "end position higher/equal than/to traces count");   
        }
        uint256 n = eP - sP + 1;
        slice memory vS = toSlice(value);
        uint256[] memory ps = new uint256[](n);
        uint256 e = 0;
        for (uint256 i=sP; i<n; i++) {
            string memory trace = traces[i].trace;
            bool exists = contains(toSlice(trace), vS);
            if (exists) {
                ps[e] = i;
                ++e;
            }
        }
        if (rTPBV) {
            if (ps.length > 0) {
                uint256 redSize = n - e;
                assembly { mstore(ps, sub(mload(ps),  redSize)) }
            }
        }
        return (ps, e, ReturnCode.SUCCESS, "");//FoundTracesPoss(ps, e);
    }
    
    // function getTracesByValue(string memory value, uint sP, uint eP)  public view 
    //         returns (string[] memory, ReturnCode, string memory) {        
    //     //FoundTracesPoss fTP
    //     (uint64[] memory poss, uint64 rc, ReturnCode code, string memory message) = getTracesPossByValue(value, sP, eP);
    //     if (code == ReturnCode.SUCCESS) {  
    //         string[] memory rs = new string[](rc);
    //         for (uint64 i=0; i<rc; i++) {
    //             rs[i] = traces[poss[i]].trace;
    //         }
    //         return (rs, code, message);        
    //     } else {
    //         return (new string[](0), code, message);
    //     }
    // }

    // function getTracesSubarray(uint startPos, uint maxNumElems) public view 
    //         returns (string[] memory, ReturnCode, string memory) {       
    //     uint64 len = traces.length;
    //     if (startPos < len) {
    //         uint64 numElems = maxNumElems;
    //         if (startPos + maxNumElems > len) {
    //             numElems = len - startPos;
    //         }
    //         //TraceEntry[] memory r = new TraceEntry[](numElems);
    //         string[] memory r = new string[](numElems);
    //         for (uint64 i=0; i<numElems; i++) {
    //             r[i] = traces[startPos + i].trace;
    //         }
    //         return (r, ReturnCode.SUCCESS, "");
    //     } else {
    //         //return new TraceEntry[](0);
    //         return (new string[](0), ReturnCode.PARAMS_ERROR, "start position greater or equal to the number of traces");
    //     }
    // }
    
    function getTraceById(string memory traceId) public view returns (TraceEntry memory, ReturnCode, string memory) {
        if (tracesById[traceId].isValue) {
            uint256 pos = tracesById[traceId].pos;
            return (traces[pos], ReturnCode.SUCCESS, "");
        } else {
            return (TraceEntry(0, 0, msg.sender, 0, ""), ReturnCode.NOT_FOUND, "trace not found");
        }
    }
    
    function getTracesByValue(string memory value, uint64 sP, uint64 eP)  public view 
            returns (TraceEntry[] memory, ReturnCode, string memory) {        
        //FoundTracesPoss fTP 
        (uint256[] memory poss, uint256 rc, ReturnCode code, string memory message) = getTracesPossByValue(value, sP, eP);   
        if (code == ReturnCode.SUCCESS) {       
            TraceEntry[] memory rs = new TraceEntry[](rc);
            for (uint256 i=0; i<rc; i++) {
                rs[i] = traces[poss[i]];//.trace;
            }
            return (rs, code, message);
            
        } else {
            return (new TraceEntry[](0), code, message);
        }
    }
    
    function getTracesSubarray(uint64 startPos, uint64 maxNumElems) public view 
            returns (TraceEntry[] memory, ReturnCode, string memory) {       
        uint256 len = traces.length;
        if (startPos < len) {
            uint256 numElems = maxNumElems;
            if (startPos + maxNumElems > len) {
                numElems = len - startPos;
            }
            //TraceEntry[] memory r = new TraceEntry[](numElems);
            TraceEntry[] memory r = new TraceEntry[](numElems);
            for (uint256 i=0; i<numElems; i++) {
                r[i] = traces[startPos + i];//.trace;
            }
            return (r, ReturnCode.SUCCESS, "");
        } else {
            //return new TraceEntry[](0);
            return (new TraceEntry[](0), ReturnCode.PARAMS_ERROR, "start position greater or equal to the number of traces");
        }
        
    }
    
    function getContractName() public view 
            returns(string memory, ReturnCode, string memory){
         return (name, ReturnCode.SUCCESS, "");
    }
    
    function getOwner() public view
            returns(address, ReturnCode, string memory){
         return (owner, ReturnCode.SUCCESS, "");
    }
    
    function getTracesPossByValueResize() public view 
            returns(bool, ReturnCode, string memory){
        return (rTPBV, ReturnCode.SUCCESS, "");
    }
    
    function setTracesPossByValueResize(bool newVal) public 
            returns (ReturnCode, string memory) {
        rTPBV = newVal;
        return (ReturnCode.SUCCESS, "");
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