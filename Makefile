full:
	make build

PREFERENCE_ORDER=false
SECOND_TURN=false

build: 
	cd "$(shell pwd)/src"  && javac Main.java

run: 
	cd "$(shell pwd)/src" && java Main $(PREFERENCE_ORDER) $(SECOND_TURN)

clean:
	rm **/*.class