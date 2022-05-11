pragma solidity >=0.4.22 <0.7.0;

/** 
 * @title ProcessingConsent
 * @dev Holds the explicit consent for a Processor to process the data of a Data Subject
 */
contract ProcessingConsent {
   
    address private collectionConsentSC;
    address private dataSubject;
    address private controller;
    address private processor;

    //Storing all processors and purposes for which they process the data.   
    struct ProcessingPurposeStruct{
        bool exists;
        uint256 data;
        uint256 beginningDate;
        uint256 expirationDate;
        uint8[3] valid;     //DC - DS - DP
    }
    mapping( uint => ProcessingPurposeStruct ) private purposes;
    
    uint[] private processingPurposes; 

    //Enum are explicitly convertible to and from all integer type. 
    //The options are represented by subsequent unsigned integer values starting from 0, in the order they are defined.
    enum PURPOSE { ModelTraining, ModelTesting, Profiling, ImprovingService, Advertising }
    

    /** 
     * @dev Create a new purpose Smart Contract for processor using dataSubject's data and supervised by controller.
     * @param _controller Controller address
     * @param _dataSubject data Subject address
     * @param _processor data processor address
     */
    constructor( address _controller, address _dataSubject, address _processor ) public {
        require( tx.origin == _controller, 
            "Transaction sender does not matcht with the Controller");
        collectionConsentSC = msg.sender;
        controller = _controller;
        dataSubject = _dataSubject;
        processor = _processor;
    }


    /** 
     * @dev Add new prcessing purpose.
     * @param _purpose processing purpose
     * @param duration validity expiration time of the contract (in seconds)
     * @param defaultTrue in the case the procesing purpose is on the DS's whitelist
     */
    function newPurpose( uint _purpose, uint data, uint duration, uint defaultTrue ) external{
        //Can only be crated from the Consent SC adn by the Controller
        require( msg.sender == collectionConsentSC, 
            "New Processing purpose can only be added from the Consent SC from which this Purpose SC was created" );
        require( tx.origin == controller,
            "Only controller can add a new Processing purpose to this SC" );
        //chech if this purpose does not already exists
        require( !purposes[ _purpose ].exists, "Processing purpose already exists." );

        uint8[3] memory valid;
        if( defaultTrue==1 )
            valid = [1,1,0];
        else
            valid = [1,0,0];

        purposes[ _purpose ] = ProcessingPurposeStruct( 
            true, 
            data,
            block.timestamp,
            block.timestamp + duration,
            valid );

        processingPurposes.push( _purpose );
    }
    
    
    /**
     * @dev Modifies whcih data can be processed for certain processing purpose
     * @param _purpose processing purpose
     * @param _data data that can be processed
     */
    function modifyData( uint _purpose, uint _data ) external onlyDataSubject{
        purposes[ _purpose ].data = _data;
    }

    
    /**
     * @dev Returns the current state of this processing purpose
     * @param _purpose processing purpose 
     */
    function verify( uint _purpose ) external view returns(bool) {
        require( purposes[ _purpose ].exists, "Processing purpose does not exists." );

        uint256 timestamp = block.timestamp;
        bool isValid = (purposes[ _purpose ].valid[0] & 
                        purposes[ _purpose ].valid[1] & 
                        purposes[ _purpose ].valid[2] ) != 0 && 
                        timestamp >= purposes[ _purpose ].beginningDate && 
                        timestamp <= purposes[ _purpose ].expirationDate;
        return isValid;
    }

    /**
     * @dev Returns the current state of this processing purpose
     * @param _purpose processing purpose 
     */
    function verifyDS( uint _purpose ) external view returns(bool) {
        bool exists;

        if( !purposes[ _purpose ].exists )
            exists = false;
        else 
            exists = purposes[ _purpose ].valid[1] != 0;
        return exists;
    }


    /**
     * @dev Returns if processor has already requested to process DS's personal data for a certain purpose.
     * @param _purpose processing purpose 
     */
    function existsPurpose( uint _purpose ) external view returns(bool) {
        if( purposes[ _purpose ].exists )
            return true;
        else
            return false;
    }



    //GETTERS
    function getPurposes() external view returns( uint[] memory ){
        return processingPurposes;
    }
    function getDataSubject() external view returns( address ){
        return dataSubject;
    }
    function getController() external view returns( address ){
        return controller;
    }
    function getProcessor() external view returns( address ){
        return processor;
    }
    function getDataPurpose( uint _purpose ) external view returns( uint256 ){
        return purposes[ _purpose].data;
    }

    /**
     * @dev Processing Consent valid. 
     */
    function grantConsent( uint _purpose ) external{
        require( tx.origin == controller ||  tx.origin == dataSubject || tx.origin == processor, 'Actor not allowed to do this action.' );
        require( purposes[ _purpose ].exists, "Processing purpose does not exists." );
        
        if( tx.origin == controller ) purposes[ _purpose ].valid[0] = 1;
        else if( tx.origin == dataSubject ) purposes[ _purpose ].valid[1] = 1;
        else if( tx.origin == processor) purposes[ _purpose ].valid[2] = 1;
    }

    
    /**
     * @dev Processing Consent revoked. 
     */
    function revokeConsent( uint _purpose ) external{
        require( tx.origin == controller ||  tx.origin == dataSubject || tx.origin == processor, 'Actor not allowed to do this action.' );
        require( purposes[ _purpose ].exists, "Processing purpose does not exists." );
        
        if( tx.origin == controller ) purposes[ _purpose ].valid[0] = 0;
        else if( tx.origin == dataSubject ) purposes[ _purpose ].valid[1] = 0;
        else if( tx.origin == processor) purposes[ _purpose ].valid[2] = 0;
    }


    /**
     * @dev Processing Consent revoked. 
     */
    function revokeAllConsents() external{
        require( tx.origin == controller ||  tx.origin == dataSubject || tx.origin == processor, 'Actor not allowed to do this action.' );
        require( processingPurposes.length > 0, "No Processing purposes on this SC." );
        
        if( tx.origin == controller ) revokeAllConsentsAux(0);
        else if( tx.origin == dataSubject ) revokeAllConsentsAux(1);
        else if( tx.origin == processor) revokeAllConsentsAux(2);
    }


    function revokeAllConsentsAux( uint p ) private{
        for( uint i=0; i < processingPurposes.length; i++){
            purposes[ processingPurposes[i] ].valid[p] = 0;
        }
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
    
   
}