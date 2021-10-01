build:
	docker build -t chip16-pom .

release: build
	docker run --rm -v $(shell pwd):/home/app chip16-pom mvn clean package

run: release
	java -jar target/gs-maven-0.1.0.jar # it was tested on openjdk8


