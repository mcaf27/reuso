full:
	make build
	make run

build: 
	cd "$(shell pwd)/src"  && javac Main.java

run: 
	cd "$(shell pwd)/src" && java Main

clean:
	rm **/*.class