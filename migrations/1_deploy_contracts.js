
var Consent = artifacts.require("Consent");

module.exports = function(deployer, network, accounts) {
	const dataController = accounts[0];
	const dataSubject = accounts[1];
	const dataRecipients = accounts.slice(2,5);
	const data = 4294967295;
	const duration = 60;
	const defaultPurposes = [1, 2]
	
	deployer.deploy(Consent, dataController, dataRecipients, data, duration, defaultPurposes, {from: dataSubject});
}
