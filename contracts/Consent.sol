pragma solidity >=0.4.22 <0.7.0;

import 'Purpose.sol';

/** 
 * @title Consent
 * @dev Holds the explicit consent given to a collector to get personal data from a user
 * 
 */
contract Consent {
   
    address private dataSubject;
    address private controller;
    address[] private recipients;

    //Storing all processors and purposes for which they process the data.   
    struct ProcessorPurposeStruct{
        bool exists;
        uint[] processingPurposes;
        mapping( uint => address ) purposesList;
    }
    mapping( address => ProcessorPurposeStruct ) private purposes;
    
    address[] private processors;
    uint[] private processingPurposes;
    
    
    uint256 beginningDate;
    uint256 expirationDate;
    
    uint8 private valid;
    uint8 private erasure;

    //Purpose.PURPOSE[] private defaultPurposes;
    uint[] private defaultPurposes;

    //aux value to return the last purpose contract address created
    address private lastPurposeAddress;
    
    //Needs to be redefined
    string private data;

    /** 
     * @dev Create a new consent contract between a Data Subject and a Controller.
     * @param _dataSubject data Subject address
     * @param _recipients list of the recipients that will hold the personal data of the Subject
     * @param duration validity expiration time of the contract (in seconds)
     */
    constructor( address _dataSubject, address[] memory _recipients, uint duration, uint[] memory _defaultPurposes ) public {
        controller = msg.sender;
        dataSubject = _dataSubject;
        recipients = _recipients;
        beginningDate = block.timestamp;
        expirationDate = beginningDate + duration;
        //expirationDate = beginningDate + (duration * 1 days); //Same in days

        defaultPurposes = _defaultPurposes;
        
        valid = 0; //Privacy by default
        erasure = 0;

        lastPurposeAddress = address(this);
    }
    
    
    //NEEDS A REFACTOR TO BECOME FOR EFFICIENT
    /** 
     * @dev Create a new processing purpose for processor using dataSubject data and supervised by controller.
     * @param processor data processor address
     * @param processingPurpose processing purpose
     * @param duration validity expiration time of the contract (in seconds)
     */
    function newPurpose( address  processor, uint processingPurpose, uint duration ) external contractValidity onlyController returns(address){
        //cheking that a purpose does not exists for a certain processor and processing purpose  //&& 
        //require( !( purposes[processor].exists ) );
        require( existsPurpose( purposes[processor].processingPurposes, processingPurpose) == -1, "Processor has already a contract with this processing purpose." );
        
        Purpose newPurposeContract;
        if( existsPurpose( defaultPurposes, processingPurpose ) > -1 )
            newPurposeContract = new Purpose( controller, dataSubject, processor, processingPurpose, duration, 1 );
        else
            newPurposeContract = new Purpose( controller, dataSubject, processor, processingPurpose, duration, 0 );

        
        if( !purposes[processor].exists ){
            uint[] memory aux;
            purposes[ processor ] = ProcessorPurposeStruct( true, aux );
        }
        purposes[ processor ].processingPurposes.push( processingPurpose );
        purposes[ processor ].purposesList[ processingPurpose ] = address( newPurposeContract );
        
        if( existsProcessor( processors, processor ) == -1 ) 
            processors.push( processor );
        if( existsPurpose( processingPurposes, processingPurpose ) == -1 ) 
            processingPurposes.push( processingPurpose );

        lastPurposeAddress = address(newPurposeContract); 
        return address(newPurposeContract);
    }


    /** 
     * @dev Modifies which data can be processed.
     * @param _data that can be processed by the data processor
     **/
    function modifyData(string calldata _data) external{
        //needs to be defined
    }

    
    /** 
     * @dev Forces the controller to remove all Data subject collected personal data .
     **/
    function eraseData() external onlyDataSubject{
        erasure = 1;
    }


    /**
     * @dev Consent contract enabled by the Data Subject. 
     */
    function grantConsent() external onlyDataSubject{
        valid = 1;
    }
    
    /**
     * @dev Consent contract revoked by the Data Subject. 
     */
    function revokeConsent() external onlyDataSubject{
        valid = 0;
    }


    /**
     * @dev Returns the current state of this consent contract 
     */
    function verify() external view returns(bool) {
        uint256 timestamp = block.timestamp;
        bool isValid = valid == 1 && timestamp >= beginningDate && timestamp <= expirationDate;
        return isValid;
    }


    //More methods
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * @dev Returns Purpose SC's address related to certain processor and purpose.
     */
    function getPurposeSC( address processor, uint purpose ) external view returns( address ){
        require( purposes[ processor ].exists, "Processor has not requested to process DS's PD." );
        return purposes[ processor ].purposesList[ purpose ];
    }


    /**
     * @dev Returns all processors with permits to process DS's personal data.
     */
    function getAllProcessors() external view returns(address[] memory){
        return processors;
    }


    /**
     * @dev Returns all purposes for which DS's personal data is processed.
     */
    function getAllPurposes() external view returns(uint[] memory){
        return processingPurposes;
    }
    

    /**
     * @dev Returns all processing purposes for a specific processor.
     * @param processor data processor address
     */
    function getAllPurposesProcessor( address processor ) external view returns(uint[] memory){
        return  purposes[ processor ].processingPurposes;
    }


    /** 
     * @dev Auxiliar function that returns the last purpose SC generated address
     */
    function getLastPurpose() external view returns(address){
        return lastPurposeAddress;
    }

    

    //COMPLEX REVOKE CONSENT - EVALUATE THE PROCESSING COST
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * @dev Revoke consent for all Purpose SC for a specific processing purpose. 
     */
    function revokeConsentPurpose( uint purpose ) external onlyDataSubject{
        require( existsPurpose( processingPurposes, purpose) > -1, "No processor is processing DS's personal data for this purpose." );
        for( uint i=0; i < processors.length; i++ ){
            if( existsPurpose( purposes[ processors[i] ].processingPurposes, purpose) > -1 ){
                Purpose( purposes[ processors[i] ].purposesList[ purpose ] ).revokeConsent();
                //Remove value from: purposes[ processors[i] ].processingPurposes ?
            }
        }
        //Remove element from the default purposes array
        int index = existsPurpose( defaultPurposes, purpose);
        if(  index > -1 ){
            defaultPurposes[ uint(index) ] = defaultPurposes[ defaultPurposes.length - 1 ];
            delete defaultPurposes[ defaultPurposes.length - 1 ];
            defaultPurposes.length--;
        }
        //Remove element from the prcessing purposes array?
        //processingPurposes
    }

    /**
     * @dev Revoke consent for all Purpose SC for a specific processor. 
     */
    function revokeConsentProcessor( address processor ) external onlyDataSubject{
        require( existsProcessor( processors, processor) > -1, "Processor is not processing DS's personal data for any purpose." );
        for( uint i=0; i < purposes[ processor ].processingPurposes.length; i++ ){
                Purpose( purposes[ processor ].purposesList[ purposes[ processor ].processingPurposes[i] ] ).revokeConsent();
                //Remove value from: purposes[ processors[i] ].processingPurposes ?
        }
        //Remove element for the processors array?
        //processors
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    
    
    modifier onlyDataSubject(){
        require( msg.sender == dataSubject, 'Only the data Subject is allowed to do this action.' );
        _;
    }
    
    modifier onlyController(){
        require( msg.sender == controller, 'Only the data Controller is allowed to do this action.' );
        _;
    }
    
    modifier contractValidity(){
        uint256 timestamp = block.timestamp;
        require( valid == 1 && timestamp >= beginningDate && timestamp <= expirationDate, 'Consent constract is not valid.' );
        _;
    }


    //Not efficient, we should find an alternative
    function existsPurpose( uint[] memory array, uint value ) internal pure returns (int){
      for( int i=0; uint(i) < array.length; i++ ){
          if( array[ uint(i) ] == value )
          return i;
      }
      return -1;
    }

    function existsProcessor( address[] memory array, address value ) internal pure returns (int){
      for( int i=0; uint(i) < array.length; i++ ){
          if( array[ uint(i) ] == value )
          return i;
      }
      return -1;
    }
    
   
}