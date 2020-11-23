pragma solidity >=0.4.22 <0.7.0;

/** 
 * @title Purpose
 * @dev Holds the explicit consent for a Processor to process the data of a Data Subject
 */
contract Purpose {
   
    address private dataSubject;
    address private controller;
    address private processor;
    
    uint256 private beginningDate;
    uint256 private expirationDate;
    
    uint8[3] private valid;
    
    //Not defined yet
        //Enum are explicitly convertible to and from all integer type. 
        //The options are represented by subsequent unsigned integer values starting from 0, in the order they are defined.
    enum PURPOSE { ModelTraining, ModelTesting, Profiling, ImprovingService, Advertising }
    PURPOSE purpose;
    
    //Needs to be redefined
    string private data;

    /** 
     * @dev Create a new processing purpose for processor using dataSubject data and supervised by controller.
     * @param _controller Controller address
     * @param _dataSubject data Subject address
     * @param _processor data processor address
     * @param _purpose processing purpose
     * @param duration validity expiration time of the contract (in seconds)
     * @param defaultTrue in the case the procesing purpose is on the DS's whitelist
     */
    constructor( address _controller, address _dataSubject, address _processor, uint _purpose, uint duration, uint defaultTrue ) public {
        controller = _controller;
        dataSubject = _dataSubject;
        processor = _processor;
        purpose = PURPOSE(_purpose);
        beginningDate = block.timestamp;
        expirationDate = beginningDate + duration;
        if( defaultTrue==1 )
            valid = [1,1,0];
        else
            valid = [1,0,0];
    }
    
    
    /**
     * @dev Returns the current state of this processing purpose 
     */
    function verify() external view returns(bool) {
        uint256 timestamp = block.timestamp;
        bool isValid = (valid[0] & valid[1] & valid[2] ) != 0 && timestamp >= beginningDate && timestamp <= expirationDate;
        return isValid;
    }


    /** 
     * @dev Modifies which data can be processed.
     * @param _data that can be processed by the data processor
     **/
    function modifyData(string calldata _data) external{
        //needs to be defined
    }


    //GETTERS
    function getPurpose() external view returns(uint){
        return uint(purpose);
    }
    function getDataSubject() external view returns(address){
        return dataSubject;
    }
    function getController() external view returns(address){
        return controller;
    }
    function getProcessor() external view returns(address){
        return processor;
    }

    /**
     * @dev Processing Consent valid. 
     */
    function grantConsent() external{
        require( tx.origin == controller ||  tx.origin == dataSubject || tx.origin == processor, 'Actor not allowed to do this action.' );
        
        if( tx.origin == controller ) valid[0] = 1;
        else if( tx.origin == dataSubject ) valid[1] = 1;
        else if( tx.origin == processor) valid[2] = 1;
    }

    
    /**
     * @dev Processing Consent revoked. 
     */
    function revokeConsent() external{
        require( tx.origin == controller ||  tx.origin == dataSubject || tx.origin == processor, 'Actor not allowed to do this action.' );
        
        if( tx.origin == controller ) valid[0] = 0;
        else if( tx.origin == dataSubject ) valid[1] = 0;
        else if( tx.origin == processor) valid[2] = 0;
    }
    
    
    
    /**
     * @dev Processing Consent valid by the Data Subject. 
     */
    function grantConsentSubject() external onlyDataSubject{
        valid[1] = 1;
    }
    
    /**
     * @dev Processing Consent revoked by the Data Subject. 
     */
    function revokeConsentSubject() external onlyDataSubject{
        valid[1] = 0;
    }
    
    /**
     * @dev Processing Consent valid by the Controller. 
     */
    function grantConsentController() external onlyController{
        valid[0] = 1;
    }
    
    /**
     * @dev Processing Consent revoked by the Controller. 
     */
    function revokeConsentController() external onlyController{
        valid[0] = 0;
    }

    /**
     * @dev Processing Consent valid by the Processor. 
     */
    function grantConsentProcessor() external onlyProcessor{
        valid[2] = 1;
    }
    
    /**
     * @dev Processing Consent revoked by the Processor. 
     */
    function revokeConsentProcessor() external onlyProcessor{
        valid[2] = 0;
    }

    
    modifier onlyDataSubject(){
        require( tx.origin == dataSubject, 'Only the data Subject is allowed to do this action.' );
        _;
    }
    
    modifier onlyController(){
        require( tx.origin == controller, 'Only the data Controller is allowed to do this action.' );
        _;
    }

    modifier onlyProcessor(){
        require( tx.origin == processor, 'Only the processor is allowed to do this action.' );
        _;
    }
    
    modifier contractValidity(){
        uint256 timestamp = block.timestamp;
        require( (valid[0] & valid[1] & valid[2] ) != 0 && timestamp >= beginningDate && timestamp <= expirationDate, 'Processing purpose not valid.' );
        _;
    }
    
   
}