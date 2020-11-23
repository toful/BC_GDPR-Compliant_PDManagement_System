# BC_GDPR-Compliant_PDManagement_System

[![License](https://img.shields.io/github/license/toful/BC_GDPR-Compliant_PDManagement_System?style=plastic)](https://github.com/toful/BC_GDPR-Compliant_PDManagement_System)

Lightweight blockchain-based GDPR-compliant personal data management system, which provides public access immutable evidences showing the agreements between a Data Subject and a Service Provider about DS’s personal data.

System has been tested on a Ganache local blockchain.

We still need to manage how the "data" concept is managed. This value holds which data can be collected/processed, not the data itself, only the data categories.

Current project is currently being developed on the [official repository](https://github.com/toful/PD_AccessControlSystem), which will remain private ultil the final release.

## Pre-requisites

Project consists of the GDPR-Compliant PDA SCs and the Java code that implements some methods to operate over them. To complie these SCs we use Truffle and Ganache is used to emulate the Blockchain where they are tested and evaluated.

* [Truffle](https://www.trufflesuite.com/truffle): A world class development environment, testing framework and asset pipeline for blockchains using the Ethereum Virtual Machine (EVM), aiming to make life as a developer easier.
* [Ganache](https://www.trufflesuite.com/ganache): Quickly fire up a personal Ethereum blockchain which you can use to run tests, execute commands, and inspect state while controlling how the chain operates.

### Install Ganache and Truffle
You can get them using apt-get or download the repositories from the [Official Website](https://www.trufflesuite.com/). 

	sudo apt-get update
	sudo apt-get install nodejs 
	sudo apt-get install npm
	sudo npm install -g ganache-cli truffle


### Get Web3j
Web3j is used to get the SC wrappers and operate with them using Java.

	sudo apt-get install curl
	curl -L get.web3j.io | sh  

## Setup
If you are creating a new project you should run the following command and perform some configuration steps:

	truffle init

This command creates the following project structure:
```
/
├─contracts/ 			Directory for Solidity contracts
├─migrations/ 			Directory for scriptable deployment files
├─test/ 				Directory for test files for testing your application and contracts
├─truffle-config.js 	Truffle configuration file
```

### Additional Setup
In the case you want to deploy the SCs using Truffle you must do some additional configuarion steps:

* Specify the network (blockchain) over which the SCs are going to be deployed, by modifying the **truffle-config.js** file, in the case of using Ganache-cli, module.exports module should look like this:
```		
module.exports = {
	networks: {
		development: {
		  host: "Localhost",
		  port: 8545,
		  network_id: '*'
		},
	},
};:
```

Optional: Create a new file under the migrations folder (migrations/2_deploy_contracts.js) specifying the SCs you want to deploy and with which arguments. Example:
```
var Consent = artifacts.require("Consent");

module.exports = function(deployer, network, accounts) {
	const controller = accounts[0];
	const dataSubject = accounts[1];
	const recipients = accounts.slice(2,5);
	const duration = 60;
	const defaultPurposes = [1, 2]
	
	deployer.deploy(Consent, dataSubject, recipients, duration, defaultPurposes, {from: controller});
}
```

## Compile Smart Contracts
To Compile the Contracts using Truffle, execute the following command inside contracts/ directory:

	truffle compile

## Deploy Smart Contracts
First you need to init the Blockchain the Contracts are going to be deployed on:

	ganache-cli

In order to operate in a comfortable way with the Smart Contracts we have designed a Java app using the web3j library. All dependencies are specified in the pom.xml file, which are imported to your java project using Maven.

Otherwise you can deploy them using truffle.

### Deploy Smart Contracts using the Java app
To Deploy the Contracts using the Java app, first you need to create the Java wrappers to operate with them (these contracts must have been compiled before obtaining the wrappers):

	web3j  truffle generate ./build/contracts/$CONTRACT_NAME$.json -o . -p src.main.java.contracts

**Run the app.**

You can also use the Makafile which compiles the Smart Contracts, creates the Java wrappers and locates them into the proper folder.

### Deploy Smart Contracts using Truffle
Can deploy the Smart Contracts specified on the migrations/ folder files using:

	truffle migrate

Or interact with them using the truffle console:

	truffle console

Example:
```
let consent = await Consent.new( methodArgs, {from: controller})
consent.mehtodName( methodArgs, {from: ACCOUNT} )
```

Can also load already deployed contracts using:

	Consent.deployed().then( instance => {consent = instance} )


## Project Structure
```
/
├─src       			Contains all Java code
│  ├─Main.java
│  ├─ConsentManager.java
│  ├─PurposeManager.java
│  ├─ActorsManager.java
│  ├─contracts
│  │    ├─Consent.java
│  │    ├─Purpose.java
├─contracts/ 			Directory for Solidity contracts
│  ├─Consent.sol 		Consent Smart Contract code
│  ├─Purpose.sol 		Purpose Smart Contract code
├─migrations/ 			Directory for scriptable deployment files
│  ├─2_deploy_contracts.js 		Deploys Consent SC
├─test/ 				Directory for test files for testing your application and contracts
├─truffle-config.js 	Truffle configuration file
├─pom.xml 				Contains Java dependencies
├─LICENSE

```

## Author

* **Cristòfol Daudén Esmel** - [toful](https://github.com/toful)
