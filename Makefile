######################################################################
#                           PD_ACS
#                        Cristòfol Daudén Esmel
#                            Makefile
######################################################################

# General defines

JSON = build/contracts
JAVA = src/main/java/contracts
BIN = target/

######################################################################
all : 	
	cd contracts/;	truffle compile; \
	cd ..; mkdir $(JAVA); \
	#/home/crises/.web3j/web3j  truffle generate ./build/contracts/ProcessingConsent.json -o . -p src.main.java.contracts
	#/home/crises/.web3j/web3j  truffle generate ./build/contracts/CollectionConsent.json -o . -p src.main.java.contracts
	/home/crises/.web3j/web3j generate truffle --truffle-json ./build/contracts/ProcessingConsent.json --outputDir . -p src.main.java.contracts
	/home/crises/.web3j/web3j generate truffle --truffle-json ./build/contracts/CollectionConsent.json --outputDir . -p src.main.java.contracts
	

clean :
	rm -rf $(BIN) $(JSON) $(JAVA)


run : clean all
