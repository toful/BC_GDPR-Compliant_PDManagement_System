pragma solidity >=0.4.22 <0.7.0;

import "./ProcessingConsent.sol";

/** 
 * @title CollectionConsent
 * @dev Holds the explicit consent given to a collector to get personal data from a user
 * 
 * ISSUE: transaction function can't return a value even if declared in the definition
 * https://ethereum.stackexchange.com/questions/40730/why-is-web3j-java-not-generating-correct-return-types-for-my-contract
 */
contract CollectionConsent {
   
    //Identities of the actors
    address private dataSubject;
    address private controller;
    address[] private recipients;

    uint256 data;

    //Consent lifetime
    uint256 beginningDate;
    uint256 expirationDate;

    //Valid flag
    uint8[2] private valid;

    //Erasure flag
    bool private erasure;

    mapping( uint => bool ) private defaultPurposes;
    mapping( address => bool ) private processorsBlacklist;

    //ProcessingConsentContracts
    //Struct to store all processors that process DS's PD.   
    struct ProcessingConsentStruct{
        bool exists;
        address processingConsentContractAddress;
    }
    //mapping: processor address => processing consent contract
    mapping( address => ProcessingConsentStruct ) private processingConsentContracts;

    //processors that has requested to proces DS's personal data for any reason
    address[] private processors;
    

    /** 
     * @dev Create a new consent contract between a Data Subject and a Controller.
     * @param _dataController data controller address
     * @param _recipients list of the recipients that will hold the personal data of the Subject
     * @param duration validity expiration time of the contract (in seconds)
     * @param _defaultPurposes for which DS must do not give his explicit consent to process his PD
     */
    constructor( address _dataController, address[] memory _recipients, uint _data, uint duration, uint[] memory _defaultPurposes ) public {
        dataSubject = msg.sender;
        controller = _dataController;
        recipients = _recipients;
        data = _data;
        beginningDate = block.timestamp;
        expirationDate = beginningDate + duration;
        //expirationDate = beginningDate + (duration * 1 days); //Same in days

        for( uint i=0; i < _defaultPurposes.length; i++ ){
            defaultPurposes[ _defaultPurposes[i] ] = true;
        }
        
        valid = [1,0];
    }
    
    
    //NEEDS A REFACTOR TO BECOME FOR EFFICIENT
    /** 
     * @dev Create a new processing purpose for processor using dataSubject data and supervised by controller.
     * @param processor data processor address
     * @param processingPurpose processing purpose
     * @param duration validity expiration time of the contract (in seconds)
     */
    function newPurpose( address processor, uint processingPurpose, uint _data, uint duration ) external contractValidity onlyController {
        
        //Processor not in the processors Blacklist
        require( !processorsBlacklist[ processor ], "This processor is in the Blacklist.");
        
        //Check if exists a Processing Consent SC for this processor, if not, create a new one.
        ProcessingConsent processingConsentContract;
        if( !processingConsentContracts[processor].exists ){
            processingConsentContract = new ProcessingConsent( controller, dataSubject, processor );
            processingConsentContracts[processor] = ProcessingConsentStruct( true, address(processingConsentContract) );

            processors.push( processor );
        }
        else{
            processingConsentContract = ProcessingConsent( processingConsentContracts[processor].processingConsentContractAddress );
        }

        //check if DP has already requested consent for this processing purpose
        require( !processingConsentContract.existsPurpose( processingPurpose ), "Processor has already requested to process DS's personal data for this purpose." );

        //need to check that Processing data is not more permessive than Collection data.


        //add now processing purpose to the ProcessingConsent SC.
        //check if processing purpose is on DS's default processing purposes.
        if( defaultPurposes[ processingPurpose ] )
            processingConsentContract.newPurpose( processingPurpose, _data, duration, 1 );
        else
            processingConsentContract.newPurpose( processingPurpose, _data, duration, 0 );

    }


    
    /**
     * @dev Consent contract enabled by the Data Subject and/or Data Controller. 
     */
    function grantConsent() external {
        require( tx.origin == controller ||  tx.origin == dataSubject, 'Actor not allowed to do this action.' );
        
        if( tx.origin == dataSubject ) valid[0] = 1;
        else if( tx.origin == controller ) valid[1] = 1;
    }
    
    /**
     * @dev Consent contract revoked by the Data Subject and/or Data Controller. 
     */
    function revokeConsent() external {
        require( tx.origin == controller ||  tx.origin == dataSubject, 'Actor not allowed to do this action.' );
        
        if( tx.origin == dataSubject ) valid[0] = 0;
        else if( tx.origin == controller ) valid[1] = 0;
    }

    /**
     * @dev Returns the current state of this consent contract 
     */
    function verify() external view returns( bool ) {
        uint256 timestamp = block.timestamp;
        bool isValid = valid[0] != 0 && valid[1] != 0 && timestamp >= beginningDate && timestamp <= expirationDate;
        return isValid;
    }

    function eraseData() external onlyDataSubject{
        erasure = true;
    }


    //TO BE RE-DEFINED.
    /**
     * @dev Modifies the data field. 
     */
    function modifyData( uint _data ) external onlyDataSubject{
        data = _data;
        //shoudl be more complex, modifying the data field on all Processing SCs.
    }

    
    //COMPLEX REVOKE CONSENT - EVALUATE THE PROCESSING COST
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * @dev Revoke consent for all ProcessingConsent SC for a specific processing purpose. 
     */
     function revokeConsentPurpose( uint purpose ) external onlyDataSubject{

        address processor;
        for( uint i=0; i < processors.length; i++ ){
            processor =  processors[ i ];
            //If purpose is not valid or it not exists, there is no sense on revoking it.
            if( ProcessingConsent( processingConsentContracts[ processor ].processingConsentContractAddress ).verifyDS( purpose ) )
                ProcessingConsent( processingConsentContracts[ processor ].processingConsentContractAddress ).revokeConsent( purpose );
        }

        //Remove element from the default purposes array
        defaultPurposes[ purpose ] = false;
     }


    /**
     * @dev Revoke consent for all ProcessingConsent SC for a specific processor. 
     */
    function revokeConsentProcessor( address processor ) external onlyDataSubject{
        require( processingConsentContracts[ processor ].exists, "Processor is not processing DS's personal data for any purpose." );

        ProcessingConsent( processingConsentContracts[ processor ].processingConsentContractAddress ).revokeAllConsents();
        
        //Add element to the processors blacklist
        processorsBlacklist[ processor ] = true;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @dev Returns which data fields can be collected 
     */
    function getData() external view returns( uint256 ) {
        return data;
    }


    /**
     * @dev Returns the ProcessingConsent SC address of the specified processor, created from this contract.
     */
    function getProcessingConsentSC( address processor ) external view returns( address ){
        require( processingConsentContracts[ processor ].exists, "Processor has not requested to process DS's PD." );
        return processingConsentContracts[ processor ].processingConsentContractAddress;
    }


    /**
     * @dev Returns all processors that has requested to process DS's personal data.
     */
    function getAllProcessors() external view returns( address[] memory ){
        return processors;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////


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
        require( valid[0] != 0 && valid[1] != 0 && timestamp >= beginningDate && timestamp <= expirationDate, 'Consent constract is not valid.' );
        _;
    }
    
   
}