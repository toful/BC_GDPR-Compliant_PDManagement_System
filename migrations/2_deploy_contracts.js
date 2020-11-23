
var Consent = artifacts.require("Consent");

module.exports = function(deployer, network, accounts) {
	const controller = accounts[0];
	const dataSubject = accounts[1];
	const recipients = accounts.slice(2,5);
	const duration = 60;
	const defaultPurposes = [1, 2]
	
	deployer.deploy(Consent, dataSubject, recipients, duration, defaultPurposes, {from: controller});
}
