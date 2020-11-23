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
	/home/toful/.web3j/web3j  truffle generate ./build/contracts/Purpose.json -o . -p src.main.java.contracts
	/home/toful/.web3j/web3j  truffle generate ./build/contracts/Consent.json -o . -p src.main.java.contracts	
	

clean :
	rm -rf $(BIN) $(JSON) $(JAVA)


run : clean all
